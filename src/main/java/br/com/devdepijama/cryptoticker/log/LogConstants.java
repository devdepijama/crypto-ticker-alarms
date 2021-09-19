package br.com.devdepijama.cryptoticker.log;

public enum LogConstants {
    CID("cid");

    private final String value;
    LogConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
