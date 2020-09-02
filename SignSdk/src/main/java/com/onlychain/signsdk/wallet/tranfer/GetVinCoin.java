package com.onlychain.signsdk.wallet.tranfer;

import com.alibaba.fastjson.JSON;
import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.bean.PurseBean;
import com.onlychain.signsdk.net.Request;
import com.onlychain.signsdk.utils.CheckUtils;
import com.onlychain.signsdk.wallet.base.ApiConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class GetVinCoin {

public abstract void walletCoinList(StringBuffer json);
public abstract void getCoinFail(Exception e);
    //代表被开通权益的资产
    public static final int TYPE_4_FOR_INTEREST=4;
    //代表可用于普通转账的资产
    public static final int TYPE_1_FOR_TRANSFER=1;
    //计算单位基数
    public static final String BASE_NUMBER="100000000";

    private StringBuffer jsons;
    public GetVinCoin(AccountBean mAccountBean) {
        if (CheckUtils.checkAddress(mAccountBean.getAddress()))
        {
            new Request(ApiConfig.API_selectPurse, ApiConfig.selectPurse(mAccountBean.getAddressNoPrefix())) {
                @Override
                public void success(StringBuffer json) {
                    jsons=json;
                    walletCoinList(json);
                }

                @Override
                public void fail(Exception e) {
                    getCoinFail(e);
                }
            };
        }else {
            getCoinFail(new Exception("请输入正确的地址 = oc+40位16进制chars"));
        }
    }
    public GetVinCoin(String address) {
        if (CheckUtils.checkAddress(address))
        {
            new Request(ApiConfig.API_selectPurse, ApiConfig.selectPurse(address.split("oc")[1])) {
                @Override
                public void success(StringBuffer json) {
                    jsons=json;
                    walletCoinList(json);
                }

                @Override
                public void fail(Exception e) {
                    getCoinFail(e);
                }
            };
        }else {
            getCoinFail(new Exception("请输入正确的地址 = oc+40位16进制chars"));
        }
    }

    /**
     * 获取所有零钱列表
     * @return
     */
    public List<PurseBean.RecordBean> getCoinList(){
        return JSON.parseObject(jsons.toString(), PurseBean.class).getRecord();
    }
    public List<PurseBean.RecordBean> getCoinList(StringBuffer jsons){
        return JSON.parseObject(jsons.toString(), PurseBean.class).getRecord();
    }
    /**
     * 根据类型获取零钱列表
     * @param type
     * @return
     */
    public List<PurseBean.RecordBean> getCoinList(int type){
        List<PurseBean.RecordBean> coinList=new ArrayList<>();
        for (PurseBean.RecordBean coin: getCoinList())
            if (coin.getActionType()==type)
              coinList.add(coin);
        return coinList;
    }
    public List<PurseBean.RecordBean> getCoinList(List<PurseBean.RecordBean> coinsList,int type){
        List<PurseBean.RecordBean> coinList=new ArrayList<>();
        for (PurseBean.RecordBean coin: coinsList)
            if (coin.getActionType()==type)
                coinList.add(coin);
        return coinList;
    }
    public List<PurseBean.RecordBean> getCoinList(StringBuffer jsons,int type){
        List<PurseBean.RecordBean> coinList=new ArrayList<>();
        for (PurseBean.RecordBean coin: getCoinList(jsons))
            if (coin.getActionType()==type)
                coinList.add(coin);
        return coinList;
    }
    /**
     * 根据是否小于指定高度获取指定零钱,比如获取已解锁零钱
     * @param height
     * @return
     */
    public List<PurseBean.RecordBean> getCoinListForHeight(long height){
        List<PurseBean.RecordBean> coinList=new ArrayList<>();
        for (PurseBean.RecordBean coin: getCoinList(TYPE_1_FOR_TRANSFER))
            if (coin.getLockTime() < height)
                coinList.add(coin);
        return coinList;
    }
    public List<PurseBean.RecordBean> getCoinListForHeight(StringBuffer json,long height){
        List<PurseBean.RecordBean> coinList=new ArrayList<>();
        for (PurseBean.RecordBean coin: getCoinList(json,TYPE_1_FOR_TRANSFER))
            if (coin.getLockTime() < height)
                coinList.add(coin);
        return coinList;
    }

    public List<PurseBean.RecordBean> getCoinListForLock(long height){
        List<PurseBean.RecordBean> coinList=new ArrayList<>();
        for (PurseBean.RecordBean coin: getCoinList(TYPE_1_FOR_TRANSFER))
            if (coin.getLockTime() >= height)
                coinList.add(coin);
        return coinList;
    }
    /**
     * 获取指定金额范围内的可用零钱列表
     * @param startValue
     * @param endValue
     * @return
     */
    public List<PurseBean.RecordBean> getCoinListByRange(String startValue,String endValue){
        List<PurseBean.RecordBean> coinList=new ArrayList<>();

        BigDecimal startDecimal=new BigDecimal(startValue);
        BigDecimal endDecimal=new BigDecimal(endValue);
        startDecimal=startDecimal.multiply(new BigDecimal(BASE_NUMBER));
        endDecimal=endDecimal.multiply(new BigDecimal(BASE_NUMBER));
        for (PurseBean.RecordBean coin : getCoinList(TYPE_1_FOR_TRANSFER))
        {
            BigDecimal valueDecimal= new BigDecimal(coin.getValue());
            int startFlag =valueDecimal.compareTo(startDecimal);
            int endFlag = valueDecimal.compareTo(endDecimal);
            if (startFlag>-1 && endFlag<1)
                coinList.add(coin);
        }

        return coinList;
    }

    /**
     * 获取最适合开通权益的零钱
     * @return
     */
    public PurseBean.RecordBean getCoinForInterest(){
        return getCoinForMin("100","120");
    }

    /**
     * 自定义获取指定范围内的一笔最小零钱
      * @param startValue
     * @param endValue
     * @return
     */
    public PurseBean.RecordBean getCoinForMin(String startValue,String endValue){
        List<PurseBean.RecordBean> getCoinListByRange = getCoinListByRange(startValue,endValue);
        List bigDecimal = new ArrayList();
        for (PurseBean.RecordBean coin : getCoinListByRange)
            bigDecimal.add(new BigDecimal(coin.getValue()));
        for (PurseBean.RecordBean coin : getCoinListByRange)
            if (coin.getValue() == Float.valueOf(Collections.min(bigDecimal).toString()))
                return coin;
        return new PurseBean.RecordBean();
    }

    /**
     * 自定义获取指定范围内的一笔最大零钱
      * @param startValue
     * @param endValue
     * @return
     */
    public PurseBean.RecordBean getCoinForMax(String startValue,String endValue){
        List<PurseBean.RecordBean> getCoinListByRange = getCoinListByRange(startValue,endValue);
        List bigDecimal = new ArrayList();
        for (PurseBean.RecordBean coin : getCoinListByRange)
            bigDecimal.add(new BigDecimal(coin.getValue()));
        for (PurseBean.RecordBean coin : getCoinListByRange)
            if (coin.getValue() == Float.valueOf(Collections.max(bigDecimal).toString()))
                return coin;
        return new PurseBean.RecordBean();
    }

    /**
     * 获取余额,包含可用和不可用
     * @return
     */
    public BigDecimal getBalance(){
        BigDecimal sum=new BigDecimal("0");
        for (PurseBean.RecordBean coin : getCoinList())
            sum=sum.add(new BigDecimal(coin.getValue()));
        return sum.divide(new BigDecimal(BASE_NUMBER));
    }

    /**
     * 根据类型获取余额
     * @param type
     * @return
     */
    public BigDecimal getBalance(int type){
        BigDecimal sum=new BigDecimal("0");
        for (PurseBean.RecordBean coin : getCoinList(type))
             sum=sum.add(new BigDecimal(coin.getValue()));
        return sum.divide(new BigDecimal(BASE_NUMBER));
    }

    public BigDecimal getBalance(List<PurseBean.RecordBean>  coins){
        BigDecimal sum=new BigDecimal("0");
        for (PurseBean.RecordBean coin : coins)
            sum=sum.add(new BigDecimal(coin.getValue()));
        return sum.divide(new BigDecimal(BASE_NUMBER));
    }


    public BigDecimal getBalanceLong(List<PurseBean.RecordBean>  coins){
        BigDecimal sum=new BigDecimal("0");
        for (PurseBean.RecordBean coin : coins)
            sum=sum.add(new BigDecimal(coin.getValue()));
        return sum;
    }
}
