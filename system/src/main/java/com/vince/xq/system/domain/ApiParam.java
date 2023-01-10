package com.vince.xq.system.domain;

import java.io.Serializable;

public class ApiParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private String remark;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
