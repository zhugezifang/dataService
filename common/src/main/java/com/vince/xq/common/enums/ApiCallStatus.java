package com.vince.xq.common.enums;

public enum ApiCallStatus {
    SUCCESS(1, "success"),
    FAIL(2, "fail"),
    TIMEOUT(3, "timeOut");

    private Integer type;
    private String msg;

    ApiCallStatus(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
