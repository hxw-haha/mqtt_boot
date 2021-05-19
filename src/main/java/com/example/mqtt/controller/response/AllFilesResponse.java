package com.example.mqtt.controller.response;

import java.util.List;

public class AllFilesResponse extends BaseResponse {
    public String rootFilePath;
    public List<String> childFileNames;
}
