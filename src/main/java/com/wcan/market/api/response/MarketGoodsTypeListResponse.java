package com.wcan.market.api.response;

import com.wcan.market.api.WcanResponse;
import lombok.Data;

import java.util.List;

/**
 * 商城类目列表响应
 *
 * @author majunjie
 * @date 2019/4/17 9:58
 */
@Data
public class MarketGoodsTypeListResponse extends WcanResponse<List<MarketGoodsTypeListResponse.ChannelGoodsTypeVO>> {

    @Data
    public class ChannelGoodsTypeVO {

        /**
         * 分类名称
         */
        private String name;
        /**
         * 类目id
         */
        private Long id;

    }

}
