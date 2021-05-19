package com.example.mqtt.wx;

import com.example.mqtt.http.ConfigParams;
import com.example.mqtt.http.HttpHelp;
import com.example.mqtt.wx.response.GetTokenResponse;
import com.example.mqtt.wx.response.GetUserInfoResponse;
import com.google.gson.Gson;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
public class WXController {

    @PostMapping("/getUserId")
    public String getUserId(String code) {
        String userId = "";
        final ConfigParams getTokenParams = new ConfigParams(
                "https://qyapi.weixin.qq.com/cgi-bin/gettoken");
        getTokenParams.addParams("corpid", "wweb92cf046715a75d")
                .addParams("corpsecret", "xW1DrRFhduZIx7uDXWze3dSPdFRWqXz-eOMESNtyMns");
        final String getTokenRequest = HttpHelp.getInstance().request(getTokenParams);
        if (StringUtils.hasLength(getTokenRequest)) {
            final GetTokenResponse getTokenResponse = new Gson().fromJson(getTokenRequest, GetTokenResponse.class);
            if (getTokenResponse.errcode == 0) {
                final ConfigParams getUserInfoParams = new ConfigParams(
                        "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo");
                getUserInfoParams.addParams("access_token", getTokenResponse.access_token)
                        .addParams("code", code);
                final String getUserInfoRequest = HttpHelp.getInstance().request(getUserInfoParams);
                GetUserInfoResponse getUserInfoResponse = new Gson().fromJson(getUserInfoRequest, GetUserInfoResponse.class);
                if (getUserInfoResponse.errcode == 0) {
                    userId = getUserInfoResponse.UserId;
                }
            }
        }
        return userId;
    }
}
