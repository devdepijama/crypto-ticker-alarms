package br.com.devdepijama.cryptoticker.resources.alarm.service;

import br.com.devdepijama.cryptoticker.resources.alarm.Alarm;

import java.util.Optional;
import java.util.Set;

public interface AlarmService {
    Set<Alarm> getAll(String market);
    Optional<Alarm> getById(String market, String alarmId);
    void add(String market, Alarm alarm);
    void deleteAll(String market);
    void delete(String market, String id);
}
