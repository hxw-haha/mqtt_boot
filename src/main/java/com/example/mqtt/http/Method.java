package com.example.mqtt.http;

public enum Method {

	GET("GET"), POST("POST");

	private String value;

	Method(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	public boolean isOut() {
		switch (this) {
		case POST:
			return true;
		default:
			return false;
		}
	}

}

