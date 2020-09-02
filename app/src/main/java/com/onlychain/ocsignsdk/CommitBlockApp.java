package com.onlychain.ocsignsdk;

import com.alibaba.fastjson.JSON;
import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.bean.BaseActionBean;
import com.onlychain.signsdk.bean.GetSystemInfoBean;
import com.onlychain.signsdk.bean.OutBean;
import com.onlychain.signsdk.bean.PurseBean;
import com.onlychain.signsdk.net.Request;
import com.onlychain.signsdk.utils.OcMath;
import com.onlychain.signsdk.utils.WalletUtils;
import com.onlychain.signsdk.wallet.base.ApiConfig;
import com.onlychain.signsdk.wallet.iface.ImpGetAction;
import com.onlychain.signsdk.wallet.iface.ImpQueryAction;
import com.onlychain.signsdk.wallet.tranfer.GetVinCoin;
import com.onlychain.signsdk.wallet.tranfer.QueryUtils;
import com.onlychain.signsdk.wallet.tranfer.StartTranfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CommitBlockApp
{
    static List<PurseBean.RecordBean> coinPurse;
    public static void main(String[] args )
    {
        //根据随机私钥生成账户
        AccountBean mAccountBeanRandom= WalletUtils.createAccount();
        //获取带oc前缀的地址
        System.out.println("随机版，带oc前缀的地址    "+mAccountBeanRandom.getAddress());
        //获取不带oc前缀的地址
        System.out.println("随机版，不带oc前缀的地址  "+mAccountBeanRandom.getAddressNoPrefix());
        //获取公钥
        System.out.println("随机版，公钥  "+mAccountBeanRandom.getPublicKey());

        //根据确定的私钥生成账户
        final AccountBean mAccountBean=WalletUtils.createAccount(OcMath.hexStringToByteArray("ea23e889d590a443831a785a398ce74179f09dece2fe5bfda41f795c50240c62"));
        //获取带oc前缀的地址
        System.out.println("带oc前缀的地址    "+mAccountBean.getAddress());
        //获取不带oc前缀的地址
        System.out.println("不带oc前缀的地址  "+mAccountBean.getAddressNoPrefix());
        //获取公钥
        System.out.println("公钥  "+mAccountBean.getPublicKey());

        //更换节点IP
        ApiConfig.init("http://39.98.135.66:9082");

        //获取链上指定地址的钱包零钱
        new GetVinCoin(mAccountBean) {
            @Override
            public void walletCoinList(StringBuffer json) {

                //获取钱包所有的零钱列表
                getCoinList();
                //获取钱包可流通的零钱列表
                getCoinList(TYPE_1_FOR_TRANSFER);
                //获取钱包开通权益的零钱列表
                getCoinList(TYPE_4_FOR_INTEREST);

                //获取所有类型的余额
                System.out.println("获取所有类型的余额    "+getBalance());
                //获取可流通类型的余额
                System.out.println("获取零钱类型为1的所有余额  "+getBalance(TYPE_1_FOR_TRANSFER));
                //获取被开通权益的余额
                System.out.println("获取被开通权益的余额  "+getBalance(TYPE_4_FOR_INTEREST));
                //判断权益是否开通成功
                System.out.println("是否拥有权益："+(getBalance(TYPE_4_FOR_INTEREST).compareTo(new BigDecimal("20"))>-1 ? "有":"没有"));

                //获取一笔最适合开通权益的零钱（默认在20~30之间取）
                System.out.println("获取一笔最适合开通权益的零钱   "+getCoinForInterest().getValue()/Long.valueOf(BASE_NUMBER));
                //在指定范围内，获取一笔最大的零钱
                System.out.println("在指定范围内获取一笔最大的零钱 "+getCoinForMax("0","10000").getValue()/Long.valueOf(BASE_NUMBER));
                //在指定范围内，获取一笔最小的零钱
                System.out.println("在指定范围内获取一笔最小的零钱 "+getCoinForMin("10","10000").getValue()/Long.valueOf(BASE_NUMBER));


                final StartTranfer mStartTranfer=new StartTranfer(mAccountBean) {
                    @Override
                    public void receiveAction(final BaseActionBean localCommitBean, StringBuffer json) {
                        //获取Action的签名
                        System.out.println(localCommitBean.getSignStr());
                        //获取Action的消息体
                        System.out.println(localCommitBean.getMessage());
                        //获取Action的txid
                        System.out.println(localCommitBean.getTxid());
                        //使用公钥验证消息签名的真实性
                        System.out.println(localCommitBean.checkSig(mAccountBean.getPublicKeyBin()));
                        //获取提交后的结果,提交成功不等于上链成功，需要通过查询txid为准
                        System.out.println("提交成功的返回结果=="+json);

                        //通过查询Txid来确定是否上链成功
                        new QueryUtils(4,localCommitBean.getTxid(), new ImpQueryAction() {
                            @Override
                            public void inChainSuceess(StringBuffer json) {
                                System.out.println("上链成功的交易数据："+json);
                                System.out.println("上链成功TXID："+localCommitBean.getTxid());
                            }
                            @Override
                            public void inChainFail() {
                                System.out.println("上链失败");
                            }
                        });
                    }
                    @Override
                    public void receiveFail(Exception e) {
                        System.out.println(e);
                    }
                };

                // 开始向节点提交签名后的数据
                //获取当前钱包的零钱是否存在至少一笔type为4的标准权益质押量
                if(getBalance(TYPE_4_FOR_INTEREST).compareTo(new BigDecimal("20"))>-1)
                {
                    //TODO --------------------------------以下业务不能同时运行

                    new Request(ApiConfig.API_getSystemInfo) {
                        @Override
                        public void success(StringBuffer json) {
                            //设定转账的目标地址
                            OutBean mOutBean= new OutBean();
                            mOutBean.setAddress("274579901ace0417d662a203c8c3dbbb40693d8d");
                            mOutBean.setValue(10000*Long.valueOf(BASE_NUMBER));

                            OutBean mOutBean2= new OutBean();
                            mOutBean2.setAddress("419d0c3fde261eeaecd2c47b484f20db3ef7558b");
                            mOutBean2.setValue(1*Long.valueOf(BASE_NUMBER));

                            OutBean mOutBean3= new OutBean();
                            mOutBean3.setAddress("479d0c3fde261eeaecd2c47b484f20db3ef7558b");
                            mOutBean3.setValue(1*Long.valueOf(BASE_NUMBER));

                            List<OutBean> outoutList = new ArrayList<>();
                            outoutList.add(mOutBean);
                            outoutList.add(mOutBean2);
                            outoutList.add(mOutBean3);


                            GetSystemInfoBean.RecordBean mGetSystemInfoBean= JSON.parseObject(json.toString(), GetSystemInfoBean.class).getRecord();
                            System.out.println("最新高度=="+mGetSystemInfoBean.getBlockHeight());
                           /* for (PurseBean.RecordBean pr:getCoinListForHeight(mGetSystemInfoBean.getBlockHeight()))
                                System.out.println(pr.getValue());*/

                            //获取可用零钱（不含权益零钱、锁定零钱）
                            coinPurse=getCoinListForHeight(mGetSystemInfoBean.getBlockHeight());
                            //获取可流通类型的余额
                            System.out.println("获取可流通类型的余额  "+getBalance(coinPurse));


                            //TODO 提交普通单笔转账交易
//                        mStartTranfer.inputCoin(coinPurse).inputAddress(mOutBean).commit();
                            //TODO 获取单笔转账签名
                        mStartTranfer.inputCoin(coinPurse).inputAddress(mOutBean).getAction(TYPE_1_FOR_TRANSFER, new ImpGetAction() {
                            @Override
                            public void receive(String actionStr) {
                                System.out.println(actionStr);
                            }
                        });

                            //TODO 提交普通批量转账交易
                        //mStartTranfer.inputCoin(coinPurse).inputAddressList(outoutList).commit();
                            //TODO 获取普通批量转账交易签名
                    /* mStartTranfer.inputCoin(coinPurse).inputAddressList(outoutList).getAction(TYPE_1_FOR_TRANSFER, new ImpGetAction() {
                                @Override
                                public void receive(String actionStr) {
                                    System.out.println(actionStr);
                                }
                            });*/
                            //TODO 获取质押的裸交易数据
                 /*       mStartTranfer.inputCoin(coinPurse).getPledgeSignData(new ImpGetAction() {
                        @Override
                        public void receive(String actionStr) {
                            System.out.println("得到质押裸交易签名==="+actionStr);
                            }
                        });*/

                            //TODO 对零钱进行合并或拆额，如果自己的out为1个则为合并，如果自己的out为2个则为拆额 ,取整用 X*Long.valueOf(BASE_NUMBER)
                            //TODO 选某笔零钱进行拆额
//                            mStartTranfer.inputCoin(coinPurse).inputExcreteCoins(20*Long.valueOf(BASE_NUMBER)).commit();
                            //TODO 获取选某笔零钱进行拆额签名
                       /*     mStartTranfer.inputCoin(coinPurse.get(0)).inputExcreteCoins(3900l).getAction(TYPE_1_FOR_TRANSFER, new ImpGetAction() {
                                @Override
                                public void receive(String actionStr) {
                                    System.out.println(actionStr);
                                }
                            });*/

                            //TODO 选多笔零钱进行合并
//                            mStartTranfer.inputCoin(coinPurse).inputExcreteCoins(getBalanceLong(coinPurse).longValue()).commit();
                            //TODO 获取多笔零钱进行合并的签名,输入coinPurse零钱如果只有一笔，则没有合并的意义
                        /*    mStartTranfer.inputCoin(coinPurse).inputExcreteCoins(getBalanceLong(coinPurse).longValue()).getAction(TYPE_1_FOR_TRANSFER, new ImpGetAction() {
                                @Override
                                public void receive(String actionStr) {
                                    System.out.println(actionStr);
                                }
                            });*/
                        }
                        @Override
                        public void fail(Exception e) {

                        }
                    };

                } else{
                    //TODO 开通权益
                    mStartTranfer.openInterest(getCoinForMin("20","30")).commit(TYPE_4_FOR_INTEREST);
                    //TODO 获取开通权益签名
                   /* mStartTranfer.openInterest(getCoinForMin("20","30")).getAction(TYPE_4_FOR_INTEREST, new ImpGetAction() {
                        @Override
                        public void receive(String actionStr) {
                            System.out.println(actionStr);
                        }
                    });*/
                }
            }

            @Override
            public void getCoinFail(Exception e) {
                System.out.println((e));
            }
        };

    }
}
