package com.populstay.populife.entity;

public class GatewayInfoResp {
    private boolean success;
    private int code;
    private String msg;
    private GatewayInfo data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GatewayInfo getData() {
        return data;
    }

    public void setData(GatewayInfo data) {
        this.data = data;
    }
}
