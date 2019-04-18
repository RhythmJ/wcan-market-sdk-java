package com.wcan.market.api;

import com.wcan.market.api.enums.ContentTypeEnum;
import com.wcan.market.api.enums.RequestTypeEnum;
import lombok.Data;

import java.util.Map;

/**
 * 微能商城请求对象
 *
 * @author majunjie
 * @date 2019/4/17 10:54
 */
@Data
public abstract class WcanRequest<T extends WcanResponse> {

    /**
     * 渠道号
     */
    protected String channelCode;
    /**
     * 请求时间戳
     */
    protected Long timestamps;
    /**
     * 签名
     */
    protected String sign;

    /**
     * 获取请求api路径
     *
     * @return
     */
    public abstract String getApiPath();

    /**
     * 请求类型
     *
     * @return
     */
    public abstract RequestTypeEnum requestType();

    /**
     * 请求header
     *
     * @return
     */
    public abstract ContentTypeEnum contentType();

    /**
     * 获取响应实体class
     *
     * @return
     */
    public abstract Class<T> getResponseClass();

    /**
     * 获取参与签名的参数
     *
     * @return
     */
    public abstract Map<String, String> getSignMap();

    /**
     * 获取
     *
     * @return
     */
    public Map<String, String> getRqMap() {
        Map<String, String> signMap = getSignMap();
        signMap.put("sign", sign);
        return signMap;
    }

}
