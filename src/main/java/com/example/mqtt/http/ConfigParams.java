package com.example.mqtt.http;

import java.util.ArrayList;
import java.util.List;

public class ConfigParams {

	private int connectTimeout = 15000;
	private int readTimeout = 15000;
	private String contentType = "application/x-www-form-urlencoded";
	private Method method = Method.GET;
	private List<KeyValue> requestParams;
	private String charset = "UTF-8";
	private String httpUrl;

	public ConfigParams(String httpUrl) {
		this.httpUrl = httpUrl;
		requestParams = new ArrayList<KeyValue>();
	}

	public ConfigParams addParams(String key, String value) {
		requestParams.add(new KeyValue(key, value));
		return this;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public List<KeyValue> getRequestParams() {
		return requestParams;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public ConfigParams setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public ConfigParams setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	public ConfigParams setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public ConfigParams setMethod(Method method) {
		this.method = method;
		return this;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public String getContentType() {
		return contentType;
	}

	public Method getMethod() {
		return method;
	}

}
