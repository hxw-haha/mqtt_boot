package com.example.mqtt.http;

import com.example.mqtt.utils.CloseIo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpHelp {

    private static HttpHelp instance;

    private HttpHelp() {
    }

    public static synchronized HttpHelp getInstance() {
        if (instance == null)
            instance = new HttpHelp();
        return instance;
    }

    public String request(ConfigParams configParams) {
        OutputStream outputStream = null;
        HttpURLConnection connection = null;
        InputStream inStream = null;
        BufferedReader buReader = null;
        String result = "";// 返回结果字符串
        try {
            String httpUrl = configParams.getHttpUrl();
            if (!configParams.getMethod().isOut()) {
                if (configParams.getRequestParams().size() > 0) {
                    if (httpUrl.endsWith("?")) {
                        httpUrl = httpUrl + buildParams(configParams);
                    } else {
                        httpUrl = httpUrl + "?" + buildParams(configParams);
                    }
                }
            }

            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(configParams.getMethod().value());
            connection.setConnectTimeout(configParams.getConnectTimeout());
            connection.setReadTimeout(configParams.getReadTimeout());
            connection.setRequestProperty("Content-Type",
                    configParams.getContentType());
            connection.setDoOutput(true);

            connection.connect();
            if (configParams.getMethod().isOut()) {
                outputStream = connection.getOutputStream();
                outputStream.write(buildParams(configParams).getBytes());
            }

            if (connection.getResponseCode() == 200) {
                inStream = connection.getInputStream();
                buReader = new BufferedReader(new InputStreamReader(inStream,
                        configParams.getCharset()));
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = buReader.readLine()) != null) {
                    sbf.append(temp);
                }
                result = sbf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseIo.close(buReader);
            CloseIo.close(inStream);
            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }

        return result;
    }

    /**
     * 上送请求参数
     *
     * @param configParams
     * @return
     */
    private String buildParams(ConfigParams configParams) {
        StringBuilder builder = new StringBuilder();
        for (KeyValue keyValue : configParams.getRequestParams()) {
            Object value = keyValue.getValue();
            if (value instanceof String) {
                String key = keyValue.getKey();
                try {
                    String stringValue = URLEncoder.encode((String) value,
                            configParams.getCharset());
                    builder.append("&").append(key).append("=")
                            .append(stringValue);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

}
