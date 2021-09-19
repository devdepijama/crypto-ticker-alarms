package br.com.devdepijama.cryptoticker.resources.alarm.service;

import br.com.devdepijama.cryptoticker.resources.alarm.Alarm;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AlarmServiceImpl implements AlarmService {

    private Map<String, AssetWatcher> watcherByMarket;

    public AlarmServiceImpl() {
        this.watcherByMarket = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Alarm> getAll(String market) {
        return Optional.ofNullable(watcherByMarket.get(market))
                        .map(AssetWatcher::getAll)
                        .orElse(Collections.emptySet());
    }

    @Override
    public Optional<Alarm> getById(String market, String alarmId) {
        return getAll(market).stream()
                             .filter(alarm -> alarmId.equals(alarm.getId()))
                             .findFirst();
    }

    @Override
    public void add(String market, Alarm alarm) {
        final AssetWatcher watcher = Optional.ofNullable(watcherByMarket.get(market))
                                             .orElseGet(() -> {
                                                 watcherByMarket.put(market, new AssetWatcherImpl(market));
                                                 return watcherByMarket.get(market);
                                             });
        watcher.addAlarm(alarm.getId(), alarm);
    }

    @Override
    public void deleteAll(String market) {
        Optional.ofNullable(watcherByMarket.get(market))
                .ifPresent(watcher -> {
                    try {
                        watcher.close();
                        watcherByMarket.remove(market);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void delete(String market, String id) {
        Optional.ofNullable(watcherByMarket.get(market))
                .ifPresent(watcher -> {
                    watcher.deleteAlarm(id);
                });
    }
}
