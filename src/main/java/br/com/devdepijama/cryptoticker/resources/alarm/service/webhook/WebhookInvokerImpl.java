package br.com.devdepijama.cryptoticker.resources.alarm.service.webhook;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class WebhookInvokerImpl implements WebhookInvoker {

    private final RestTemplate restTemplate;

    public WebhookInvokerImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void notify(String url, String id, String coinLeft, String coinRight, String price) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<WebhookPayload> request = new HttpEntity<>(
            new WebhookPayload(
                id,
                coinLeft, coinRight,
                price
            )
        );

        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
    }

    private static class WebhookPayload {
        private String id;
        private String coinLeft;
        private String coinRight;
        private String price;

        public WebhookPayload() {
        }

        public WebhookPayload(String id, String coinLeft, String coinRight, String price) {
            this.id = id;
            this.coinLeft = coinLeft;
            this.coinRight = coinRight;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoinLeft() {
            return coinLeft;
        }

        public void setCoinLeft(String coinLeft) {
            this.coinLeft = coinLeft;
        }

        public String getCoinRight() {
            return coinRight;
        }

        public void setCoinRight(String coinRight) {
            this.coinRight = coinRight;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
