package com.wcan.market.api;

import lombok.Data;

/**
 * 微能商城响应对象
 *
 * @author majunjie
 * @date 2019/4/17 10:54
 */
@Data
public class WcanResponse<T> {

    public static final String SUCCESS = "0";

    private boolean success;
    /**
     * 返回码
     */
    private String code;
    /**
     * 描述
     */
    private String message;
    /**
     * 数据
     */
    private T data;

    public boolean isSuccess() {
        return SUCCESS.equals(code);
    }

}
