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
public  class StartTranferLocal {
   private String ActionMsg;
   private String Json;
    private AccountBean mAccountBean;
    private List<OutBean> outList;
    private List<PurseBean.RecordBean> coinLis;

    public StartTranferLocal(AccountBean mAccountBean) {
        this.mAccountBean=mAccountBean;
    }


   public StartTranferLocal inputCoin(List<PurseBean.RecordBean> coinLis){
        this.coinLis=coinLis;
       return this;
   }

    public StartTranferLocal inputCoin(PurseBean.RecordBean coin){
       List<PurseBean.RecordBean> coinLis=new ArrayList<>();
        coinLis.add(coin);
        this.coinLis=coinLis;
        return this;
    }


    public StartTranferLocal inputAddress(List<OutBean> outList){
        this.outList=outList;
        return this;
    }

    public StartTranferLocal inputAddressList(List<OutBean> outoutList){
        //最多不要超过50
        if (outoutList.size()>50)
            return null;
        this.outList=outoutList;
        return this;
    }


    public StartTranferLocal inputAddress(OutBean out){
        this.outList=new ArrayList<>();
        this.outList.add(out);
        return this;
    }

    public StartTranferLocal openInterest(PurseBean.RecordBean coin){
        List<PurseBean.RecordBean> coinLis=new ArrayList<>();
        coinLis.add(coin);
        this.coinLis=coinLis;
//        commit(TYPE_4_FOR_INTEREST);
        return this;
    }

/*
    public void commit(){
        commit(TYPE_1_FOR_TRANSFER);
    }*/

    /**
     * 获取质押裸交易数据
     * @return
     */
    public BaseActionBean getPledgeSignData(long Height){
        final long pledgeCoin=Long.valueOf(String.valueOf(sumCoinList(this.coinLis)));
        this.outList=new ArrayList<>();
        this.outList.add(new OutBean(pledgeCoin,mAccountBean.getAddressNoPrefix()));
        //根据coin数量计算锁定高度
        long lockHeight=(30*(int)Math.floor(pledgeCoin/Long.valueOf(BASE_NUMBER)))+Long.valueOf(String.valueOf(Height));
        MakeAction mMakeAction = new MakeAction(mAccountBean,TYPE_1_FOR_TRANSFER,coinLis,outList,lockHeight);
        return mMakeAction.createAction(String.valueOf(Height));
    }


    /**
     * 合并零钱或拆额零钱
     * @param excreteCoin
     * @return
     */
    public StartTranferLocal inputExcreteCoins(long excreteCoin){
        this.outList=new ArrayList<>();
        this.outList.add(new OutBean(excreteCoin,mAccountBean.getAddressNoPrefix()));
        return this;
    }

    /**
     * 获取签名数据
     * @param actionType
     * @param height
     */
    public BaseActionBean getAction( int actionType,long height){
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
                return null;
            }
        }else {
            System.out.println("完全没有余额");
            return null;
        }

        MakeAction mMakeAction = new MakeAction(mAccountBean,actionType,coinLis,outList);
        return mMakeAction.createAction(String.valueOf(height));
    }


    public void commit(int actionType,long height){
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

        doCommit(actionType, height);
   }




    /**
     * 做最后整合提交
     */
    public BaseActionBean doCommit( int actionType,long height){
        //先从节点获取最新高度信息
        MakeAction mMakeAction = new MakeAction(mAccountBean,actionType,coinLis,outList);
        return mMakeAction.createAction(String.valueOf(height));
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
