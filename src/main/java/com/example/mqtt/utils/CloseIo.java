package com.example.mqtt.utils;

import ch.qos.logback.core.util.CloseUtil;

import java.io.Closeable;
import java.io.IOException;

public final class CloseIo {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            synchronized (CloseIo.class) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
