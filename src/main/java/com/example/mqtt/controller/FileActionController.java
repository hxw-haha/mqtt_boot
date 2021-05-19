package com.example.mqtt.controller;

import com.example.mqtt.controller.response.AllFilesResponse;
import com.example.mqtt.controller.response.UploadResponse;
import com.example.mqtt.utils.FileUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fileAction")
public class FileActionController {

    private static final String ROOT_FILE_PATH = "D:\\file_action";

    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam("files") List<MultipartFile> files) {
        final UploadResponse response = new UploadResponse();
        final List<String> succeedFileNames = new ArrayList<>();
        if (files != null && files.size() > 0) {
            for (MultipartFile file : files) {
                try {
                    final String originalFilename = file.getOriginalFilename();
                    file.transferTo(new File(ROOT_FILE_PATH + File.separator + originalFilename));
                    succeedFileNames.add(originalFilename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        response.rootFilePath = ROOT_FILE_PATH;
        response.succeedFileNames = succeedFileNames;
        response.code = succeedFileNames.size() > 0 ? AllFilesResponse.SUCCEED : AllFilesResponse.FAILED;
        response.msg = "返回上传成功文件";
        return response;
    }

    @GetMapping("/getAllFiles")
    public AllFilesResponse getAllFiles() {
        final AllFilesResponse response = new AllFilesResponse();
        final List<String> childFileNames = new ArrayList<>();
        final File rootFile = new File(ROOT_FILE_PATH);
        if (rootFile.exists() && rootFile.isDirectory()) {
            final File[] childFiles = rootFile.listFiles();
            if (childFiles != null && childFiles.length > 0) {
                for (File childFile : childFiles) {
                    if (childFile.exists() && childFile.isFile()
                            && FileUtil.getFileBSize(childFile.getAbsolutePath()) != 0) {
                        childFileNames.add(childFile.getName());
                    }
                }
            }
        }
        response.rootFilePath = ROOT_FILE_PATH;
        response.childFileNames = childFileNames;
        response.code = childFileNames.size() > 0 ? AllFilesResponse.SUCCEED : AllFilesResponse.FAILED;
        response.msg = "返回全部可下载文件";
        return response;
    }
}
