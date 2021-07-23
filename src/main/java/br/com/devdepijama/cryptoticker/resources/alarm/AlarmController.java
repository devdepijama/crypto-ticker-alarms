package br.com.devdepijama.cryptoticker.resources.alarm;

import br.com.devdepijama.cryptoticker.Utils;
import br.com.devdepijama.cryptoticker.resources.alarm.service.AlarmService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "alarms")
public class AlarmController {

    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping(path = "/{coinLeft}/{coinRight}")
    public Set<String> getAllAlarms(@PathVariable String coinLeft, @PathVariable String coinRight) {
        final String market = Utils.marketFromCoins(coinLeft, coinRight);
        return alarmService.getAll(market).stream()
                .map(Alarm::getId)
                .collect(Collectors.toSet());
    }

    @PostMapping
    public void addAlarm(@RequestBody Alarm alarm) {
        alarmService.add(Utils.marketFromCoins(alarm.getCoinLeft(), alarm.getCoinRight()), alarm);
    }

    @DeleteMapping(path = "/{coinLeft}/{coinRight}")
    public void deleteAllByMarket(@PathVariable String coinLeft, @PathVariable String coinRight) {
        alarmService.deleteAll(Utils.marketFromCoins(coinLeft, coinRight));
    }

    @DeleteMapping(path = "/{coinLeft}/{coinRight}/{alarmId}")
    public void deleteAlarm(
            @PathVariable String coinLeft,
            @PathVariable String coinRight,
            @PathVariable String alarmId
    ) {
        alarmService.delete(Utils.marketFromCoins(coinLeft, coinRight), alarmId);
    }
}
