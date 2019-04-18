package com.wcan.market.api;

/**
 * 微能商城接入客户端接口
 *
 * @author majunjie
 * @date 2019/4/17 10:54
 */
public interface WcanClient {

    /**
     * 请求
     *
     * @param request
     * @param <T>
     * @return
     */
    <T extends WcanResponse> T execute(WcanRequest<T> request);

    /**
     * 请求
     *
     * @param request
     * @param <T>
     * @param url   请求地址
     * @return
     */
    <T extends WcanResponse> T execute(String url, WcanRequest<T> request);

    /**
     * 组装跳转地址
     *
     * @param var1
     * @return 返回跳转地址
     */
    String packRedirectUrl(WcanRequest var1);

}
