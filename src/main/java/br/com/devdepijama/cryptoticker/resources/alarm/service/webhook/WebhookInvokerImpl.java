package br.com.devdepijama.cryptoticker.resources.alarm.service.webhook;

import br.com.devdepijama.cryptoticker.resources.alarm.Alarm;
import br.com.devdepijama.cryptoticker.resources.alarm.AlarmTrigger;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class WebhookInvokerImpl implements WebhookInvoker {

    private final RestTemplate restTemplate;

    public WebhookInvokerImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void notify(Alarm alarm, BigDecimal price) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<WebhookPayload> request = new HttpEntity<>(
            WebhookPayload.from(alarm, price)
        );

        restTemplate.exchange(alarm.getWebhook().toString(), HttpMethod.POST, request, Void.class);
    }

    @Data
    private static class WebhookPayload {

        @NonNull
        private String id;

        @NonNull
        private String name;

        @NonNull
        private String strategyId;

        @NonNull
        private String coinLeft;

        @NonNull
        private String coinRight;

        @NonNull
        private AlarmTrigger edge;

        @NonNull
        private BigDecimal price;

        public static WebhookPayload from(Alarm alarm, BigDecimal price) {
            return new WebhookPayload(
                alarm.getId(),
                alarm.getName(),
                alarm.getStrategyId(),
                alarm.getCoinLeft(),
                alarm.getCoinRight(),
                alarm.getTriggerOn(),
                price
            );
        }
    }
}
