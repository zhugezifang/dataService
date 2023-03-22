
package com.vince.xq.common.datasource.dao.entity;


import lombok.Data;

/**
* @description 数据源 entity
* @author Raod
* @date 2021-03-18 12:09:57.728203200
**/

@Data
public class DataSource  {
    private String sourceCode;
    private String sourceName;
    private String sourceDesc;
    private String sourceType;
    private String sourceConfig;
    private Integer enableFlag;
    private Integer deleteFlag;
}
