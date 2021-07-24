package br.com.devdepijama.cryptoticker.resources.alarm.service;

import br.com.devdepijama.cryptoticker.resources.alarm.Alarm;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface AssetWatcher {

    Optional<Alarm> getById(String alarmId);
    Set<Alarm> getAll();
    void addAlarm(String id, Alarm alarm);
    void deleteAlarm(String id);
    void close() throws IOException;
}
