package com.wcan.market.api.enums;

/**
 * contentType
 *
 * @author majunjie
 * @date 2019/4/17 11:27
 */
public enum ContentTypeEnum {

    X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", null),
    TEXT_PLAIN("text/plain", null),
    APPLICATION_JSON("application/json", null),
    ;

    /**
     * code
     */
    private String code;
    /**
     * api描述
     */
    private String description;

    ContentTypeEnum(String code, String description) {
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
