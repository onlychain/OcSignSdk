package com.onlychain.ocsignsdk;

import com.alibaba.fastjson.JSON;
import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.bean.GetSystemInfoBean;
import com.onlychain.signsdk.bean.PurseBean;
import com.onlychain.signsdk.net.Request;
import com.onlychain.signsdk.utils.CheckUtils;
import com.onlychain.signsdk.utils.OcMath;
import com.onlychain.signsdk.utils.WalletUtils;
import com.onlychain.signsdk.wallet.base.ApiConfig;
import com.onlychain.signsdk.wallet.tranfer.GetVinCoin;

import java.util.List;

/**
 * 关于零钱的操作
 */
public class AboutCoins {


    public static void main(String[] args) {
        //设置主网普通节点IP
        ApiConfig.init("http://39.98.135.66:9082");
        //根据确定的私钥生成账户
        final AccountBean mAccountBean= WalletUtils.createAccount(OcMath.hexStringToByteArray("ea23e889d590a443831a785a398ce74179f09dece2fe5bfda41f795c50240c62"));
        //获取带oc前缀的地址
        System.out.println("带oc前缀的地址    "+mAccountBean.getAddress());


        //使用主网API获取零钱列表json
        new GetVinCoin(mAccountBean.getAddress()){
            @Override
            public void walletCoinList(StringBuffer json) {
                System.out.println(json);
                //解析JSON成零钱列表（含权益、锁定、可流通的零钱）
                List<PurseBean.RecordBean> coinList = getCoinList(json);
                for (PurseBean.RecordBean pur:coinList)
                    System.out.println("（含权益、锁定、可流通的零钱）"+pur.getValue()+"   TYPE:"+ pur.getActionType());

                System.out.println("\n");

                List<PurseBean.RecordBean> coinListFor4 = getCoinList(coinList,TYPE_4_FOR_INTEREST);
                for (PurseBean.RecordBean pur:coinListFor4)
                    System.out.println("（含权益的零钱）"+pur.getValue()+"   TYPE:"+ pur.getActionType());

                System.out.println("\n");

                List<PurseBean.RecordBean> coinListFor1 = getCoinList(coinList,TYPE_1_FOR_TRANSFER);
                for (PurseBean.RecordBean pur:coinListFor1)
                    System.out.println("（含锁定、可流通的零钱）"+pur.getValue()+"   TYPE:"+ pur.getActionType());


                System.out.println("\n");

                //获取主网最新高度进行零钱锁定筛选可获得可流通的零钱和质押中零钱
                new Request(ApiConfig.API_getSystemInfo) {
                    @Override
                    public void success(StringBuffer json) {
                        long lastHeight=JSON.parseObject(json.toString(),GetSystemInfoBean.class).getRecord().getBlockHeight();
                        List<PurseBean.RecordBean> coinPurse=getCoinListForHeight(lastHeight);
                        for (PurseBean.RecordBean pur:coinPurse)
                            System.out.println("（可流通的零钱）"+pur.getValue()+"   TYPE:"+ pur.getActionType());

                        System.out.println("\n");


                        List<PurseBean.RecordBean> coinPurseLock=getCoinListForLock(lastHeight);
                        for (PurseBean.RecordBean pur:coinPurseLock)
                            System.out.println("（投票质押中的零钱）"+pur.getValue()+"   TYPE:"+ pur.getActionType());

                    }
                    @Override
                    public void fail(Exception e) {

                    }
                };

            }

            @Override
            public void getCoinFail(Exception e) {
                //地址错误时候抛出异常
                System.out.println(e);
            }
        };













    }



}
