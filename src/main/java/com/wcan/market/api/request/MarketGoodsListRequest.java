package com.wcan.market.api.request;

import com.wcan.market.api.WcanRequest;
import com.wcan.market.api.enums.ContentTypeEnum;
import com.wcan.market.api.enums.MarketApiEnum;
import com.wcan.market.api.enums.RequestTypeEnum;
import com.wcan.market.api.response.MarketGoodsListResponse;
import com.wcan.market.api.response.MarketModuleListResponse;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 商城频道列表查询
 *
 * @author majunjie
 * @date 2019/4/17 9:58
 */
@Data
public class MarketGoodsListRequest extends WcanRequest<MarketGoodsListResponse> {

    /**
     * 频道Code
     */
    protected String moduleCode;
    /**
     * 类目id
     */
    protected Long typeId;
    /**
     * 页码
     */
    protected Integer current;
    /**
     * 条数
     */
    protected Integer size;


    @Override
    public String getApiPath() {
        return MarketApiEnum.GOODS.getPath();
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
    public Class<MarketGoodsListResponse> getResponseClass() {
        return MarketGoodsListResponse.class;
    }

    @Override
    public Map<String, String> getSignMap() {
        Map<String, String> params = new HashMap<>();
        params.put("channelCode", channelCode);
        params.put("timestamps", timestamps + "");
        params.put("moduleCode", moduleCode);
        params.put("typeId", typeId==null ? "":typeId.toString());
        params.put("current", current==null ? "1":current.toString());
        params.put("size", size==null ? "10":size.toString());
        return params;
    }

}
