package com.wcan.market.api.client;

import com.alibaba.fastjson.JSON;
import com.wcan.market.api.*;
import com.wcan.market.api.enums.ContentTypeEnum;
import com.wcan.market.api.enums.RequestTypeEnum;
import com.wcan.market.api.util.MD5Util;
import com.wcan.market.api.util.MapUtil;
import com.wcan.market.api.util.WebUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * 默认客户端实现
 *
 * @author majunjie
 * @date 2019/4/17 11:19
 */
@Slf4j
public class DefaultWcanClient implements WcanClient {

    /**
     * 微能服务器地址
     */
    private String serverUrl;
    /**
     * 渠道号（微能提供给第三方）
     */
    private String channelCode;
    /**
     * 密钥（微能提供）
     */
    private String password;
    /**
     * 渠道分配给微能的账号
     */
    private String appKey;
    /**
     * 渠道分配给微能的密钥
     */
    private String appSecret;

    /**
     * 按微能标准文档实现
     *
     * @param serverUrl
     * @param channelCode
     * @param password
     * @param appKey
     * @param appSecret
     */
    public DefaultWcanClient(String serverUrl, String channelCode, String password, String appKey, String appSecret) {
        this.serverUrl = serverUrl;
        this.channelCode = channelCode;
        this.password = password;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    /**
     * 双方各自提供文档
     *
     * @param serverUrl
     * @param channelCode
     * @param password
     */
    public DefaultWcanClient(String serverUrl, String channelCode, String password) {
        this.serverUrl = serverUrl;
        this.channelCode = channelCode;
        this.password = password;
    }

    @Override
    public <T extends WcanResponse> T execute(WcanRequest<T> request) {
        String requestUrl = serverUrl + request.getApiPath();
        return _execute(requestUrl, request);
    }

    @Override
    public <T extends WcanResponse> T execute(String requestUrl, WcanRequest<T> request) {
        return _execute(requestUrl, request);
    }

    private <T extends WcanResponse> T _execute(String requestUrl, WcanRequest<T> request) {
        setSign(request);
        Map<String, String> rqMap = request.getRqMap();
        log.info("请求地址：{}，请求数据：{}", requestUrl, JSON.toJSONString(rqMap));
        String rsp;
        try {
            if (RequestTypeEnum.GET.equals(request.requestType())) {
                rsp = WebUtils.doGet(requestUrl, rqMap);
            } else if (ContentTypeEnum.X_WWW_FORM_URLENCODED.equals(request.contentType())) {
                rsp = WebUtils.doPost(requestUrl, rqMap, WebUtils.DEFAULT_CHARSET, 10000, 10000);
            } else {
                rsp = WebUtils.doPost(requestUrl, JSON.toJSONString(rqMap), request.contentType().getCode(), WebUtils.DEFAULT_CHARSET, 10, 10);
            }
            log.info("请求数据：{}，响应数据：{}", JSON.toJSONString(rqMap), rsp);
            return JSON.parseObject(rsp, request.getResponseClass());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String packRedirectUrl(WcanRequest request) {
        setSign(request);
        return serverUrl + request.getApiPath() + "?" + MapUtil.map2QueryString(request.getRqMap());
    }

    private void setSign(WcanRequest request) {
        request.setChannelCode(channelCode);
        request.setTimestamps(System.currentTimeMillis());
        try {
            Map signMap = request.getSignMap();
            String signData = MapUtil.map2SignString(signMap);
            String sign = MD5Util.getMD5(password + signData + password);
            request.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
