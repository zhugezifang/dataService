package com.vince.xq.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vince.xq.common.annotation.Excel;
import com.vince.xq.common.annotation.Excel.ColumnType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 岗位表 sys_post
 * 
 * @author ruoyi
 */
public class ApiConfig implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id", cellType = ColumnType.NUMERIC)
    private Long id;

    /** dbConfigId */
    @Excel(name = "dbConfigId")
    private Long dbConfigId;

    /** apiName */
    @Excel(name = "apiName")
    private String apiName;

    /** apiPath */
    @Excel(name = "apiPath")
    private String apiPath;

    /** requestType */
    @Excel(name = "requestType")
    private String requestType;

    /** requestMode */
    @Excel(name = "requestMode")
    private String requestMode;

    /** sql */
    @Excel(name = "apiSql")
    private String apiSql;

    /** params */
    @Excel(name = "params")
    private String params;

    private List<ApiParam> param;

    /** result */
    @Excel(name = "apiResult")
    private String apiResult;

    /** result */
    @Excel(name = "online")
    private int online;

    /** qps */
    @Excel(name = "qps")
    private Long qps;

    /** timeOut */
    @Excel(name = "timeOut")
    private Long timeOut;

    /** result */
    @Excel(name = "apiToken")
    private String apiToken;

    /** createBy */
    @Excel(name = "createBy")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getDbConfigId() {
        return dbConfigId;
    }

    public void setDbConfigId(Long dbConfigId) {
        this.dbConfigId = dbConfigId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestMode() {
        return requestMode;
    }

    public void setRequestMode(String requestMode) {
        this.requestMode = requestMode;
    }

    public String getApiSql() {
        return apiSql;
    }

    public void setApiSql(String apiSql) {
        this.apiSql = apiSql;
    }

    public String getApiResult() {
        return apiResult;
    }

    public void setApiResult(String apiResult) {
        this.apiResult = apiResult;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public Long getQps() {
        return qps;
    }

    public void setQps(Long qps) {
        this.qps = qps;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public List<ApiParam> getParam() {
        return param;
    }

    public void setParam(List<ApiParam> param) {
        this.param = param;
    }
}
