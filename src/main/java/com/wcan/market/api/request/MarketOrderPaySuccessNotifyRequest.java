package com.wcan.market.api.request;

import com.wcan.market.api.enums.ContentTypeEnum;
import com.wcan.market.api.enums.RequestTypeEnum;
import com.wcan.market.api.WcanRequest;
import com.wcan.market.api.WcanResponse;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 商城顶支付成功通知
 *
 * @author majunjie
 * @date 2019/4/17 9:58
 */
@Data
public class MarketOrderPaySuccessNotifyRequest extends WcanRequest<WcanResponse> {

    /**
     * 微能订单号
     */
    private String orderId;
    /**
     * 第三方订单号
     */
    private String apOrderId;
    /**
     * 交易总金额（单位分）
     */
    private Long price;

    @Override
    public String getApiPath() {
        return null;
    }

    @Override
    public RequestTypeEnum requestType() {
        return RequestTypeEnum.GET;
    }

    @Override
    public ContentTypeEnum contentType() {
        return ContentTypeEnum.X_WWW_FORM_URLENCODED;
    }

    @Override
    public Class<WcanResponse> getResponseClass() {
        return WcanResponse.class;
    }

    @Override
    public Map<String, String> getSignMap() {
        Map<String, String> params = new HashMap<>();
        params.put("channelCode", channelCode);
        params.put("timestamps", timestamps + "");
        params.put("orderId", orderId);
        params.put("apOrderId", apOrderId);
        params.put("price", price == null ? "" : price.toString());
        return params;
    }

}
