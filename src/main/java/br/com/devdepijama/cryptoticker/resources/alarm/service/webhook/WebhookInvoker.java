package br.com.devdepijama.cryptoticker.resources.alarm.service.webhook;

import java.net.URL;

public interface WebhookInvoker {
    void notify(String url, String id, String coinLeft, String coinRight, String price);
}
