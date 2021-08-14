package br.com.devdepijama.cryptoticker.resources.alarm.service.webhook;

import br.com.devdepijama.cryptoticker.resources.alarm.Alarm;

import java.math.BigDecimal;

public interface WebhookInvoker {
    void notify(Alarm alarm, BigDecimal price);
}
