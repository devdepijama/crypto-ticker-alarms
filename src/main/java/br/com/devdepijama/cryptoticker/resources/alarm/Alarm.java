package br.com.devdepijama.cryptoticker.resources.alarm;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.net.URL;

@Data
public class Alarm {

    @NonNull
    private String id;

    @NonNull
    private AlarmLine line;

    @NonNull
    private AlarmTrigger triggerOn;

    @NonNull
    private String coinLeft;

    @NonNull
    private String coinRight;

    @NonNull
    private URL webhook;

    @Data
    public static class AlarmLine {
        private AlarmLineCoefficients coefficients;
    }

    @Data
    public static class AlarmLineCoefficients {
        private BigDecimal angular;
        private BigDecimal linear;
    }
}
