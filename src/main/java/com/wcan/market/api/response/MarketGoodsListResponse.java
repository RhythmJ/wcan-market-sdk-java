package com.wcan.market.api.response;

import com.wcan.market.api.WcanResponse;
import lombok.Data;

import java.util.List;

/**
 * 商城商品列表响应
 *
 * @author majunjie
 * @date 2019/4/17 9:58
 */
@Data
public class MarketGoodsListResponse extends WcanResponse<List<MarketGoodsListResponse.GoodsSpuVO>> {

    @Data
    public class GoodsSpuVO {

        /**
         * id
         */
        private Long id;
        /**
         * SPU商品名称
         */
        private String spuName;
        /**
         * 长方形图4:3
         */
        private String picUrl;
        /**
         * 规格数量
         */
        private Integer skuNum;

    }

}
