package com.example.mqtt.wx.response;

public class GetUserInfoResponse {

    /**
     * {
     * "errcode": 0,
     * "errmsg": "ok",
     * "UserId":"USERID",
     * "DeviceId":"DEVICEID"
     * }
     */
    public int errcode;
    public String errmsg;
    public String UserId;
    public String DeviceId;

}
