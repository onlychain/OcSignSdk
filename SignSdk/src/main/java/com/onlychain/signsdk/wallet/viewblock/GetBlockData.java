package com.onlychain.signsdk.wallet.viewblock;


import com.alibaba.fastjson.JSON;
import com.onlychain.signsdk.bean.BlockBean;
import com.onlychain.signsdk.net.Request;
import com.onlychain.signsdk.wallet.base.ApiConfig;

import java.util.List;

public abstract class GetBlockData {
    private String height;
    public abstract void getSuccess(StringBuffer json,List<String> tradList);


    public GetBlockData(String height) {
        this.height = height;
        queryBlock();
    }


    private void queryBlock(){
        new Request(ApiConfig.API_queryBlock,ApiConfig.queryBlock(height)) {
            @Override
            public void success(StringBuffer json) {

                BlockBean.RecordBean mRecord= JSON.parseObject(json.toString(), BlockBean.class).getRecord();
                getSuccess(json,mRecord.getTradingInfo());
            }
            @Override
            public void fail(Exception e) {

            }
        };

    }

}
