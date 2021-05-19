package com.example.mqtt.wx.response;

public class GetTokenResponse {

    /**
     * {
     * "errcode": 0,
     * "errmsg": "ok",
     * "access_token": "accesstoken000001",
     * "expires_in": 7200
     * }
     */

    public String access_token;
    public int errcode;
    public String errmsg;
    public String expires_in;
}
