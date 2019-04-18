package com.wcan.market.api.enums;

/**
 * 请求方式
 *
 * @author majunjie
 * @date 2019/4/17 11:27
 */
public enum RequestTypeEnum {

    GET("GET", null),
    POST("POST", null),
    ;

    /**
     * code
     */
    private String code;
    /**
     * api描述
     */
    private String description;

    RequestTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
