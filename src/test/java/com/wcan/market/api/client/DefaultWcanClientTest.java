package com.wcan.market.api.client;

import com.alibaba.fastjson.JSON;
import com.wcan.market.api.request.MarketEntryRequest;
import com.wcan.market.api.request.MarketGoodsListRequest;
import com.wcan.market.api.request.MarketGoodsTypeListRequest;
import com.wcan.market.api.request.MarketModuleListRequest;
import com.wcan.market.api.response.MarketGoodsListResponse;
import com.wcan.market.api.response.MarketGoodsTypeListResponse;
import com.wcan.market.api.response.MarketModuleListResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Slf4j
public class DefaultWcanClientTest {

    private DefaultWcanClient wcanClient;

    @Before
    public void init() {
        wcanClient = new DefaultWcanClient("https://testwx.jifenfu.net", "suixi", "qwerasdf");
    }

    @Test
    public void packRedirectUrlTest() {
        String extUserId = "3";
        MarketEntryRequest request = new MarketEntryRequest();
        request.setExtUserId(extUserId);
        request.setPosition("home");
//        request.setModuleCode("");
//        request.setInfoKey("");
        request.setMarketHomeType(2);
        request.setApplySceneCode("b556dceda66c4a58");
//        request.setExtendParam("");
        String redirectUrl = wcanClient.packRedirectUrl(request);
        log.info("跳转地址：" + redirectUrl);
    }

    @Test
    public void marketModuleListTest() {
        MarketModuleListRequest request = new MarketModuleListRequest();
        MarketModuleListResponse response = wcanClient.execute(request);
        List<MarketModuleListResponse.ModuleVO> data = response.getData();
        log.info("响应数据：" + JSON.toJSONString(response));
    }

    @Test
    public void marketTypeListTest() {
        MarketGoodsTypeListRequest request = new MarketGoodsTypeListRequest();
        request.setModuleCode("wn_card");
        MarketGoodsTypeListResponse response = wcanClient.execute(request);
        List<MarketGoodsTypeListResponse.ChannelGoodsTypeVO> data = response.getData();
        log.info("响应数据：" + JSON.toJSONString(response));
    }

    @Test
    public void marketGoodsListTest() {
        MarketGoodsListRequest request = new MarketGoodsListRequest();
        request.setModuleCode("wn_card");
        MarketGoodsListResponse response = wcanClient.execute(request);
        List<MarketGoodsListResponse.GoodsSpuVO> data = response.getData();
        log.info("响应数据：" + JSON.toJSONString(response));
    }

}