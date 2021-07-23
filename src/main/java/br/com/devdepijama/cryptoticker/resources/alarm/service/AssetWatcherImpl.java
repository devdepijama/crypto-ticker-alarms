package br.com.devdepijama.cryptoticker.resources.alarm.service;

import br.com.devdepijama.cryptoticker.resources.alarm.Alarm;
import br.com.devdepijama.cryptoticker.resources.alarm.Trigger;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.domain.event.AggTradeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AssetWatcherImpl implements AssetWatcher, Closeable {

    private final Map<String, Alarm> alarmsById;
    private final String assetName;
    private Closeable callback;
    private BigDecimal lastPrice;
    private final BinanceApiCallback<AggTradeEvent> binanceApiCallback;

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetWatcherImpl.class);

    public AssetWatcherImpl(String assetName) {
        this.assetName = assetName;
        this.alarmsById = new HashMap<>();
        this.binanceApiCallback = new BinanceApiCallback<>() {
            @Override
            public void onResponse(AggTradeEvent response) {
                onTrade(response);
            }

            @Override
            public void onFailure(Throwable cause) {
                LOGGER.error("Failure when receiving a trade event. Details: ", cause);
            }
        };
        this.callback = BinanceApiClientFactory.newInstance()
                                               .newWebSocketClient()
                                               .onAggTradeEvent(assetName, this.binanceApiCallback);
    }

    @Override
    public Optional<Alarm> getById(String alarmId) {
        return Optional.ofNullable(alarmsById.get(alarmId));
    }

    @Override
    public Set<Alarm> getAll() {
        return new HashSet<>(alarmsById.values());
    }

    @Override
    public void addAlarm(String id, Alarm alarm) {
        alarmsById.put(id, alarm);
    }

    @Override
    public void deleteAlarm(String id) {
        alarmsById.remove(id);
    }

    public void onTrade(AggTradeEvent tradeEvent) {
        final BigDecimal price = new BigDecimal(tradeEvent.getPrice());
        final long eventTimestamp = tradeEvent.getEventTime();

        LOGGER.debug("{} -> {}", assetName, price);
        alarmsById.values().forEach(alarm -> {
            final BigDecimal targetPrice = computeTargetPrice(alarm, eventTimestamp);

            final Trigger triggerCondition = alarm.getTriggerOn();
            lastPrice = Optional.ofNullable(lastPrice)
                                .orElse(price);

            if (
                (triggerCondition.equals(Trigger.RISING) && isRising(lastPrice, targetPrice, price)) ||
                (triggerCondition.equals(Trigger.FALLING) && isFalling(lastPrice, targetPrice, price)) ||
                (triggerCondition.equals(Trigger.ANY) && isHit(lastPrice, targetPrice, price))
            ) {
                LOGGER.info("Alarm triggered! {} on {}", triggerCondition.name().toLowerCase(), price);
            }

            // Update last price for reference
            this.lastPrice = price;
        });
    }

    @Override
    public void close() throws IOException {
        callback.close();
    }

    private BigDecimal computeTargetPrice(Alarm alarm, long timestamp) {
        // Alarm coefficients are linear coefficients in the following form:
        // TargetPrice(t) = a.t + b
        // where a is the angular coefficient and b is the linear coefficient
        final BigDecimal a = alarm.getLine().getCoefficients().getAngular();
        final BigDecimal t = new BigDecimal(timestamp);
        final BigDecimal b = alarm.getLine().getCoefficients().getLinear();

        return a.multiply(t).add(b);
    }

    private boolean isRising(BigDecimal previousPrice, BigDecimal targetPrice, BigDecimal currentPrice) {
        // currentPrice > previousPrice
        if (currentPrice.compareTo(previousPrice) < 0) return false;
        return ((previousPrice.compareTo(targetPrice) <= 0) && (targetPrice.compareTo(currentPrice) <= 0));
    }

    private boolean isFalling(BigDecimal previousPrice, BigDecimal targetPrice, BigDecimal currentPrice) {
        // currentPrice < previousPrice
        if (currentPrice.compareTo(previousPrice) > 0) return false;
        return ((currentPrice.compareTo(targetPrice) <= 0) && (targetPrice.compareTo(previousPrice) <= 0));
    }

    private boolean isHit(BigDecimal previousPrice, BigDecimal targetPrice, BigDecimal currentPrice) {
        // previousPrice <= targetPrice <= currentPrice
        if ((previousPrice.compareTo(targetPrice) <= 0) && (targetPrice.compareTo(currentPrice) <= 0)) return true;
        return (currentPrice.compareTo(targetPrice) <= 0) && (targetPrice.compareTo(previousPrice) <= 0);
    }
}