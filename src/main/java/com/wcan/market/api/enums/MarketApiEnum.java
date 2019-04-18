package com.wcan.market.api.enums;

/**
 * 商城api
 *
 * @author majunjie
 * @date 2019/4/17 11:27
 */
public enum MarketApiEnum {

    ENTRY("/order-service/market", "微能商城入口"),
    LIST_MODULE("/goods-service/channel/market/modules", "微能频道列表"),
    GOODS_TYPE("/goods-service/channel/market/goodsTypes", "微能类目列表"),
    GOODS("/goods-service/channel/market/goods", "微能商品列表"),
    ;

    /**
     * api地址
     */
    private String path;
    /**
     * api描述
     */
    private String description;

    MarketApiEnum(String path, String description) {
        this.path = path;
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
