package com.example.mqtt.http;


class KeyValue {

    private String key;
    private Object value;

    KeyValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
