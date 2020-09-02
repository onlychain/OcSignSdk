package com.onlychain.signsdk.wallet.tranfer;


import com.alibaba.fastjson.JSON;
import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.bean.BaseActionBean;
import com.onlychain.signsdk.bean.GetSystemInfoBean;
import com.onlychain.signsdk.bean.OutBean;
import com.onlychain.signsdk.bean.PurseBean;
import com.onlychain.signsdk.net.Request;
import com.onlychain.signsdk.wallet.base.ApiConfig;
import com.onlychain.signsdk.wallet.iface.ImpGetAction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.onlychain.signsdk.wallet.tranfer.GetVinCoin.BASE_NUMBER;
import static com.onlychain.signsdk.wallet.tranfer.GetVinCoin.TYPE_1_FOR_TRANSFER;
import static com.onlychain.signsdk.wallet.tranfer.GetVinCoin.TYPE_4_FOR_INTEREST;

/**
 * 各类交易类型封装在此
 */
public abstract class StartTranfer {
    public abstract void receiveAction(BaseActionBean localCommitBean, StringBuffer json);
    public abstract void receiveFail(Exception e);
   private String ActionMsg;
   private String Json;
    private AccountBean mAccountBean;
    private List<OutBean> outList;
    private List<PurseBean.RecordBean> coinLis;

    public StartTranfer(AccountBean mAccountBean) {
        this.mAccountBean=mAccountBean;
    }


   public StartTranfer inputCoin(List<PurseBean.RecordBean> coinLis){
        this.coinLis=coinLis;
       return this;
   }

    public StartTranfer inputCoin(PurseBean.RecordBean coin){
       List<PurseBean.RecordBean> coinLis=new ArrayList<>();
        coinLis.add(coin);
        this.coinLis=coinLis;
        return this;
    }


    public StartTranfer inputAddress(List<OutBean> outList){
        this.outList=outList;
        return this;
    }

    public StartTranfer inputAddressList(List<OutBean> outoutList){
        //最多不要超过50
        if (outoutList.size()>50)
            return null;
        this.outList=outoutList;
        return this;
    }


    public StartTranfer inputAddress(OutBean out){
        this.outList=new ArrayList<>();
        this.outList.add(out);
        return this;
    }

    public StartTranfer openInterest(PurseBean.RecordBean coin){
        List<PurseBean.RecordBean> coinLis=new ArrayList<>();
        coinLis.add(coin);
        this.coinLis=coinLis;
//        commit(TYPE_4_FOR_INTEREST);
        return this;
    }


    public void commit(){
        commit(TYPE_1_FOR_TRANSFER);
    }

    /**
     * 获取质押裸交易数据
     * @return
     */
    public void getPledgeSignData(final ImpGetAction mImpGetAction){
        final long pledgeCoin=Long.valueOf(String.valueOf(sumCoinList(this.coinLis)));
        this.outList=new ArrayList<>();
        this.outList.add(new OutBean(pledgeCoin,mAccountBean.getAddressNoPrefix()));

        new Request(ApiConfig.API_getSystemInfo) {
            @Override
            public void success(StringBuffer json) {
                GetSystemInfoBean.RecordBean mGetSystemInfoBean= JSON.parseObject(json.toString(), GetSystemInfoBean.class).getRecord();
                //根据coin数量计算锁定高度
                long lockHeight=(30*(int)Math.floor(pledgeCoin/Long.valueOf(BASE_NUMBER)))+Long.valueOf(String.valueOf(mGetSystemInfoBean.getBlockHeight()));
                MakeAction mMakeAction = new MakeAction(mAccountBean,TYPE_1_FOR_TRANSFER,coinLis,outList,lockHeight);
                final BaseActionBean localCommitBean=mMakeAction.createAction(String.valueOf(mGetSystemInfoBean.getBlockHeight()));
                mImpGetAction.receive(localCommitBean.getCommitData());
            }
            @Override
            public void fail(Exception e) {
                receiveFail(new Exception("获取最新高度失败,请检查节点是否正常"));
            }
        };
    }


    /**
     * 合并零钱或拆额零钱
     * @param excreteCoin
     * @return
     */
    public StartTranfer inputExcreteCoins(long excreteCoin){
        this.outList=new ArrayList<>();
        this.outList.add(new OutBean(excreteCoin,mAccountBean.getAddressNoPrefix()));
        return this;
    }

    /**
     * 获取签名数据
     * @param actionType
     * @param mImpGetAction
     */
    public void getAction(final int actionType,final ImpGetAction mImpGetAction){
        //找零
        long findCoin=0;
        if(actionType==TYPE_1_FOR_TRANSFER){
            findCoin= Long.valueOf(sumCoinList(coinLis).subtract(sumOutList(outList)).toString());
            //如果零钱全部转出则无需给自己找零
            if (findCoin!=0)
                this.outList.add(new OutBean(findCoin,mAccountBean.getAddressNoPrefix()));
        }else if(actionType==TYPE_4_FOR_INTEREST){
            this.outList=new ArrayList<>();
            this.outList.add(new OutBean(this.coinLis.get(0).getValue(),mAccountBean.getAddressNoPrefix()));
        }


       if(coinLis!=null&&coinLis.size()>0&&Long.valueOf(sumCoinList(coinLis).toString())>0)
        {
            if (findCoin<0)
            {
                System.out.println("有余额但不够抵扣");
                return;
            }
        }else {
            System.out.println("完全没有余额");
            return;
        }
        new Request(ApiConfig.API_getSystemInfo) {
            @Override
            public void success(StringBuffer json) {

                GetSystemInfoBean.RecordBean mGetSystemInfoBean=JSON.parseObject(json.toString(), GetSystemInfoBean.class).getRecord();

                MakeAction mMakeAction = new MakeAction(mAccountBean,actionType,coinLis,outList);
                final BaseActionBean localCommitBean=mMakeAction.createAction(String.valueOf(mGetSystemInfoBean.getBlockHeight()));
                mImpGetAction.receive(localCommitBean.getCommitData());
            }
            @Override
            public void fail(Exception e) {
                receiveFail(new Exception("获取最新高度失败,请检查节点是否正常"));
            }
        };
    }


    public void commit(final int actionType){
        //找零
        long findCoin=0;
        if(actionType==TYPE_1_FOR_TRANSFER){
            findCoin= Long.valueOf(sumCoinList(coinLis).subtract(sumOutList(outList)).toString());
            //如果零钱全部转出则无需给自己找零
            if (findCoin!=0)
                this.outList.add(new OutBean(findCoin,mAccountBean.getAddressNoPrefix()));
        }else if(actionType==TYPE_4_FOR_INTEREST){
            this.outList=new ArrayList<>();
            this.outList.add(new OutBean(this.coinLis.get(0).getValue(),mAccountBean.getAddressNoPrefix()));
        }


        if(coinLis!=null&&coinLis.size()>0&&Long.valueOf(sumCoinList(coinLis).toString())>0)
        {
            if (findCoin<0)
            {
                System.out.println("有余额但不够抵扣");
                return;
            }
        }else {
            System.out.println("完全没有余额");
            return;
        }

        System.out.println(sumCoinList(coinLis));
        System.out.println(sumOutList(outList));
        System.out.println(findCoin);

        doCommit(actionType);
   }




    /**
     * 做最后整合提交
     */
    public void doCommit(final int actionType){
        //先从节点获取最新高度信息
        new Request(ApiConfig.API_getSystemInfo) {
            @Override
            public void success(StringBuffer json) {
                GetSystemInfoBean.RecordBean mGetSystemInfoBean=JSON.parseObject(json.toString(), GetSystemInfoBean.class).getRecord();

                MakeAction mMakeAction = new MakeAction(mAccountBean,actionType,coinLis,outList);
                final BaseActionBean localCommitBean=mMakeAction.createAction(String.valueOf(mGetSystemInfoBean.getBlockHeight()));

                System.out.println("提交的目标数据："+localCommitBean.getCommitData());

                //开始将Action序列化信息提交至节点
                new Request(ApiConfig.API_receiveAction,ApiConfig.receiveAction(localCommitBean.getCommitData())) {
                    @Override
                    public void success(StringBuffer json) {
                        receiveAction(localCommitBean,json);
                    }

                    @Override
                    public void fail(Exception e) {
                        receiveFail(e);
                    }
                };
            }
            @Override
            public void fail(Exception e) {
                receiveFail(new Exception("获取最新高度失败,请检查节点是否正常"));
            }
        };
    }



    public BigDecimal sumCoinList(List<PurseBean.RecordBean> coinList){
        BigDecimal sum=new BigDecimal("0");
        for (PurseBean.RecordBean coin : coinList)
            sum=sum.add(new BigDecimal(coin.getValue()));
        return sum;
    }

    public BigDecimal sumOutList(List<OutBean> outList){
        BigDecimal sum=new BigDecimal("0");
        for (OutBean out : outList)
            sum=sum.add(new BigDecimal(out.getValue()));
        return sum;
    }
}
