package com.wcan.market.api.request;

import com.wcan.market.api.*;
import com.wcan.market.api.enums.ContentTypeEnum;
import com.wcan.market.api.enums.MarketApiEnum;
import com.wcan.market.api.enums.RequestTypeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 商城入口请求
 *
 * @author majunjie
 * @date 2019/4/17 9:58
 */
@Data
public class MarketEntryRequest extends WcanRequest<WcanResponse> {

    /**
     * 用户唯一标识（第三方确保唯一，且同一用户这个标识不会改变）
     */
    private String extUserId;
    /**
     * home（通用首页）、mine（个人中心）、order（订单列表）、card（卡券列表）、order_detail（订单详情）、goods_list（商品列表）、goods_detail（商品详情）（必填）
     */
    private String position;
    /**
     * 渠道想直接进入频道，可上传此参数跳至具体位置内，无此要求可不传。获取方式，参考api：频道列表
     */
    private String moduleCode;
    /**
     * 无此要求可不传。
     * position为order_detail时传订单号
     * position为goods_list时传类目id，获取方式，参考api：类目列表
     * position为goods_detail时传商品id，获取方式，参考api：商品列表
     */
    private String infoKey;
    /**
     * 商城首页类型（主要用于底部导航切换时，跳转指定首页）
     * 1、通用首页（不填默认为该类型）
     * 2、自定义首页
     */
    private Integer marketHomeType;
    /**
     * 场景code
     */
    private String applySceneCode;
    /**
     * 拓展参数
     */
    private String extendParam;

    @Override
    public String getApiPath() {
        return MarketApiEnum.ENTRY.getPath();
    }

    @Override
    public RequestTypeEnum requestType() {
        return null;
    }

    @Override
    public ContentTypeEnum contentType() {
        return null;
    }

    @Override
    public Class<WcanResponse> getResponseClass() {
        return null;
    }

    @Override
    public Map<String, String> getSignMap() {
        Map<String, String> params = new HashMap<>();
        params.put("channelCode", channelCode);
        params.put("timestamps", timestamps + "");
        params.put("extUserId", extUserId);
        params.put("position", position);
        params.put("moduleCode", moduleCode);
        params.put("infoKey", infoKey);
        params.put("marketHomeType", marketHomeType==null ? "":marketHomeType.toString());
        params.put("applySceneCode", applySceneCode);
        params.put("extendParam", extendParam);
        return params;
    }

}
