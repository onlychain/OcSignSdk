package com.onlychain.signsdk.wallet.base;

import java.util.LinkedHashMap;

public class ApiConfig {
    public static String API_selectPurse;
    public  static String API_receiveAction;
    public  static String API_getSystemInfo;
    public  static String API_queryAction;
    public  static String API_queryBlock;
    public static String IPs;

    private static ApiConfig  INSTANCE = null;
    public static ApiConfig getInstance() {
        if(INSTANCE == null){
            INSTANCE = new ApiConfig();
        }
        return INSTANCE;
    }


    /**
     * 初始化
     * @param IP
     */
    public static ApiConfig init(String IP) {
        IPs=IP;
        //获取钱包资产列表
        API_selectPurse= IP+"/Trading/selectPurse";
        //提交交易
        API_receiveAction= IP+"/Action/receiveAction";
        //获取系统时间、高度、轮次、轮次时间
        API_getSystemInfo= IP+"/Node/getSystemInfo";
        //查询某一个区块
        API_queryBlock= IP+"/Block/queryBlock";
        //查询某一个Txid的Action
        API_queryAction= IP+"/Action/queryAction";
        return getInstance();
    }

    //获取钱包资产列表
    public String getSystemInfoUrl(){
        return IPs+"/Node/getSystemInfo";
    }

    //获取钱包资产列表
    public LinkedHashMap<String,Object> selectPurseUrl(String address){
        return selectPurse(address);
    }
    public static LinkedHashMap<String,Object> selectPurse(String address){
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("address",address);
        return map;
    }

    //提交交易
    public LinkedHashMap<String,Object> receiveActionUrl(String message){
        return receiveAction(message);
    }
   public static LinkedHashMap<String,Object> receiveAction(String message){
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("message",message);
        return map;
   }

    //查询某一个区块
    public LinkedHashMap<String,Object> queryBlockUrl(String height){
        return queryBlock(height);
    }
    public static LinkedHashMap<String,Object> queryBlock(String height){
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("height",Integer.valueOf(height));
        return map;
    }

    //查询某一个Txid的Action
    public LinkedHashMap<String,Object> queryActionUrl(String txId){
        return queryAction(txId);
    }
    public static LinkedHashMap<String,Object> queryAction(String txId){
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("txId",txId);
        return map;
    }


}
