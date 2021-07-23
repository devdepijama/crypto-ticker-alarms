package br.com.devdepijama.cryptoticker;

import br.com.devdepijama.cryptoticker.resources.alarm.service.AlarmService;
import br.com.devdepijama.cryptoticker.resources.alarm.service.AlarmServiceImpl;
import com.binance.api.client.BinanceApiClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContext {

    @Bean
    public AlarmService getAlarmService() {
        return new AlarmServiceImpl();
    }
}
