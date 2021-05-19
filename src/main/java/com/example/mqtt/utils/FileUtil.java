package com.example.mqtt.utils;

import org.springframework.util.StringUtils;

import java.io.File;

public class FileUtil {
    /**
     * 获取文件字节大小
     *
     * @param path 文件路径
     * @return
     */
    public static long getFileBSize(String path) {
        if (!StringUtils.hasLength(path)) {
            return 0;
        }
        final File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0;
    }
}
