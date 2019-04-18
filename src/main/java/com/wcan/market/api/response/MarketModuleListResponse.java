package com.wcan.market.api.response;

import com.wcan.market.api.WcanResponse;
import lombok.Data;

import java.util.List;

/**
 * 商城频道列表响应
 *
 * @author majunjie
 * @date 2019/4/17 9:58
 */
@Data
public class MarketModuleListResponse extends WcanResponse<List<MarketModuleListResponse.ModuleVO>> {

    @Data
    public class ModuleVO {

        /**
         * 频道code
         */
        private String moduleCode;
        /**
         * 频道code
         */
        private String onePicUrl;
        /**
         * 频道名称
         */
        private String name;

    }

}
