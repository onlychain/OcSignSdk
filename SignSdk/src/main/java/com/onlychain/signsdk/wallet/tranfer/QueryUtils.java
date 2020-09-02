package com.onlychain.signsdk.wallet.tranfer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.onlychain.signsdk.net.Request;
import com.onlychain.signsdk.wallet.base.ApiConfig;
import com.onlychain.signsdk.wallet.iface.ImpQueryAction;

public class QueryUtils {
    private ImpQueryAction impQueryAction;
    private String txId;
    private int number=0;
    private Thread runTask;
    private int howNumStop=0;


    /**
     * 轮询确认对应TXID是否成功上链
     * @param howNumStop  尝试查询次数，次数满回调为失败
     * @param txId
     * @param impQueryAction
     */
    public QueryUtils(int howNumStop,final String txId,ImpQueryAction impQueryAction) {
        this.txId=txId;
        this.impQueryAction=impQueryAction;
        this.howNumStop=howNumStop;
        runTask=new Thread(new Runnable() {
            @Override
            public void run() {
                while (runTask!=null){
                    queryAction(txId);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        runTask.start();
    }

    public QueryUtils() {

    }


    public void queryAction(String txId){
        new Request(ApiConfig.API_queryAction, ApiConfig.queryAction(txId)) {
            @Override
            public void success(StringBuffer json) {
                try{
                    JSONObject record = JSON.parseObject(json.toString());
                    number+=1;
                    if(record!=null && !record.get("record").toString().equals("[]") )
                    {
                        runTask=null;
                        impQueryAction.inChainSuceess(json);
                    }
                    else if(number>=howNumStop){
                        runTask=null;
                        impQueryAction.inChainFail();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    runTask=null;
                    impQueryAction.inChainFail();
                    System.out.println("请检查节点或网络");
                }


            }
            @Override
            public void fail(Exception e) {

            }
        };

    }


}
