package com.onlychain.ocsignsdk;

import com.onlychain.ocsignsdk.data.JsonData;
import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.bean.BaseActionBean;
import com.onlychain.signsdk.bean.OutBean;
import com.onlychain.signsdk.bean.PurseBean;
import com.onlychain.signsdk.net.Request;
import com.onlychain.signsdk.utils.OcMath;
import com.onlychain.signsdk.utils.WalletUtils;
import com.onlychain.signsdk.wallet.base.ApiConfig;
import com.onlychain.signsdk.wallet.tranfer.GetVinCoinLocal;
import com.onlychain.signsdk.wallet.tranfer.StartTranferLocal;

import java.util.ArrayList;
import java.util.List;

import static com.onlychain.signsdk.wallet.tranfer.GetVinCoinLocal.BASE_NUMBER;
import static com.onlychain.signsdk.wallet.tranfer.GetVinCoinLocal.TYPE_1_FOR_TRANSFER;
import static com.onlychain.signsdk.wallet.tranfer.GetVinCoinLocal.TYPE_4_FOR_INTEREST;

/**
 * 关于所有的裸签名操作
 */
public class AboutSign {

    public static void main(String[] args) {

        //解析本地零钱json，或取出数据库中零钱列表对象
        //TODO 注意！！！
        //TODO 注意！！！
        //TODO 注意！！！,此处输入的JsonData.coinListJson零钱数据一定要正确，否则以下签名结果都是无法通过主网验证,此处JsonData.coinListJson零钱为模拟数据，非实际测试零钱
        GetVinCoinLocal mGetVinCoinLocal  = new GetVinCoinLocal(new StringBuffer(JsonData.coinListJson));

        //通过高度筛选出可用的零钱
        List<PurseBean.RecordBean> coinPurse= mGetVinCoinLocal.getCoinListForHeight(1570004);
        for (PurseBean.RecordBean pur:coinPurse)
            System.out.println("（可流通的零钱）"+pur.getValue()+"   TYPE:"+ pur.getActionType());

        //————————————————————————————————————————————————————————————————————————————————————————————————————————————
        //初始化IP配置
        String ip="http://39.98.135.66:9082";
        ApiConfig.init(ip);

        //模拟一个最新高度
        long height=1570004;
        //打印获取当前主网最新高度的API
        System.out.println(ApiConfig.API_getSystemInfo);
        System.out.println(ApiConfig.init(ip).getSystemInfoUrl());

        //带网络请求获取最新高度,如需请求其它功能只需替换 ApiConfig.API_getSystemInfo ，如 ApiConfig.API_selectPurse
        new Request(ApiConfig.API_getSystemInfo){
            @Override
            public void success(StringBuffer json) {
            //通过解析json取得高度，可将次高度存全局数据库进行控制零钱是否质押解锁
            }

            @Override
            public void fail(Exception e) {

            }
        };

        //导入账户
        AccountBean mAccountBean=WalletUtils.createAccount(OcMath.hexStringToByteArray("ea23e889d590a443831a785a398ce74179f09dece2fe5bfda41f795c50240c62"));
        StartTranferLocal mStartTranferLocal=  new StartTranferLocal(mAccountBean);

        //TODO 生成单笔转账交易签名
        OutBean mOutBean= new OutBean();
        //设置目标转账地址
        mOutBean.setAddress("274579901ace0417d662a203c8c3dbbb40693d8d");
        mOutBean.setValue(10000*Long.valueOf(BASE_NUMBER));
        BaseActionBean singleTrans = mStartTranferLocal.inputCoin(coinPurse).inputAddress(mOutBean).getAction(TYPE_1_FOR_TRANSFER,height);
        System.out.println("单笔转账："+singleTrans.getCommitData());

        //TODO 生成批量转账交易签名
        List<OutBean> outoutList = new ArrayList<>();
        outoutList.add(mOutBean);
        outoutList.add(new OutBean(10000*Long.valueOf(BASE_NUMBER),"374579901ace0417d662a203c8c3dbbb40693d8d"));
        BaseActionBean moreTrans = mStartTranferLocal.inputCoin(coinPurse).inputAddressList(outoutList).getAction(TYPE_1_FOR_TRANSFER,height);
        System.out.println("多笔转账："+moreTrans.getCommitData());

        //TODO 单笔或多笔零钱质押签名
        BaseActionBean mPledge = mStartTranferLocal.inputCoin(coinPurse).getPledgeSignData(height);
        System.out.println("单笔或多笔零钱质押："+mPledge.getCommitData());

        //TODO 单笔或多笔零钱拆额与合并签名
        BaseActionBean mExcreteCoins = mStartTranferLocal.inputCoin(coinPurse).inputExcreteCoins(990*Long.valueOf(BASE_NUMBER)).getAction(TYPE_1_FOR_TRANSFER,height);
        System.out.println("单笔或多笔零钱拆额与合并："+mExcreteCoins.getCommitData());

        //TODO 开通权益签名,有符合指定范围的零钱才会触发以下打印
        BaseActionBean mOpenInterest = mStartTranferLocal.openInterest(mGetVinCoinLocal.getCoinForMin("20","30")).getAction(TYPE_4_FOR_INTEREST,height);
        System.out.println("开通权益："+mOpenInterest.getCommitData());

        //TODO 获取某交易签名的订单号
        System.out.println("签名数据的订单凭据（TXID）："+mOpenInterest.getTxid());

        //TODO 获取验证构造的签名合法性
        System.out.println("验证提交签名的合法性："+mOpenInterest.checkSig(mAccountBean.getPublicKeyBin()));

        //TODO 获取验证构造的签名合法性
        System.out.println("验证指定签名的合法性："+mOpenInterest.checkSign(mAccountBean.getPublicKeyBin(),mOpenInterest.getMessage(),mOpenInterest.getSignStr()));


        //TODO 提交签名至主网,谨慎开启
/*        new Request(ApiConfig.API_receiveAction,ApiConfig.receiveAction(mOpenInterest.getCommitData())) {
            @Override
            public void success(StringBuffer json) {

            }

            @Override
            public void fail(Exception e) {

            }
        };*/
    }
}
