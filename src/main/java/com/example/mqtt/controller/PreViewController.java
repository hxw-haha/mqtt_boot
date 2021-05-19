package com.example.mqtt.controller;

import com.example.mqtt.utils.CloseIo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/preview")
public class PreViewController {

    @GetMapping("/image")
    public void image(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        File file = new File("D:\\image\\视频上传.png");

        //String urlStr = "http://10.20.157.142:9080/SunECMDM/servlet/getFile?s/lV3bcibQh7NosWJDsR0DtQugCulWrduUHQ40e+m721Sv3wMWq8xTuIOivrt5ok8pDCOwGeUvhNPOpcomYqsyO1giyGkIW71W1zjgCtL45qz2G993acJN5jcDqhS9xC0iksSMw8NhuSIU6Ga8tHztz8OqD9Ktgg9zME2Y/uCVdpmHjKPu5igW2Wnk9ielEWU7y9MdjrGN2dVkXqm19pWecbB/ZgMnwbE6qksRi+o6v8ejcQJ7GScMs4ockjT55eEq4OUU0V/8+2EdflwWL0cvMBG2rynA7L3ixrtGHI41DXoA3V4v0W2JlLiFuz+Dah/YEi5EY3A+s=";

        BufferedInputStream bis = null;
        OutputStream sos = null;
        try {
            response.setHeader("Content-Type", "image/jpg");
            sos = response.getOutputStream();
//            URL url = new URL(urlStr);
//            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
//            httpUrl.setRequestMethod("GET");
//            httpUrl.setConnectTimeout(5 * 1000);
//            httpUrl.connect();
//            bis = new BufferedInputStream(httpUrl.getInputStream());

            InputStream inputStream = new FileInputStream(file);
            bis = new BufferedInputStream(inputStream);

            byte[] buf = new byte[1024];
            int len;
            while ((len = bis.read(buf)) != -1) {
                sos.write(buf, 0, len);
            }
            sos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseIo.close(sos);
            CloseIo.close(bis);
        }
    }

    @GetMapping(value = "/video")
    public void video(String taskId, String batchNo, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("taskId:" + taskId + ",batchNo:" + batchNo);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "inline; filename=video.mp4");
        response.setContentType("video/mp4");

//        File file = new File("D:\\image\\VID_2021_040505_0628305.mp4");
        String urlStr = "https://1252005255.vod2.myqcloud.com/8b973c82vodtransgzp1252005255/5aacb5c35285890806462520152/v.f30.mp4?t=608ab2ee&rlimit=50&us=N0AYIYiKj1&sign=0fb0b957e99b0c15a0d4fda5f9465fee";

        OutputStream os = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            response.setContentType("video/mp4");

            os = response.getOutputStream();
            bos = new BufferedOutputStream(os);
            URL url = new URL(urlStr);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestMethod("GET");
            httpUrl.setConnectTimeout(10 * 1000);
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());

//            InputStream inputStream = new FileInputStream(file);
//            bis = new BufferedInputStream(inputStream);

            byte[] buf = new byte[1024];
            int len;
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseIo.close(bis);
            CloseIo.close(bos);
            CloseIo.close(os);
        }
    }

}
