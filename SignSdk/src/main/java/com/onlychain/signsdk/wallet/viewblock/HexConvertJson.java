package com.onlychain.signsdk.wallet.viewblock;



import com.alibaba.fastjson.JSON;
import com.onlychain.signsdk.bean.AnalysisBean;
import com.onlychain.signsdk.utils.Leb128Utils;
import com.onlychain.signsdk.utils.OcMath;
import com.onlychain.signsdk.utils.WalletUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 16进制转Json
 */
public class HexConvertJson {
    private String actionStr;
    private AnalysisBean actionBaseBean;
    private String headStr;
    public HexConvertJson(String actionStr) {
        this.actionStr = actionStr;
        actionBaseBean=new AnalysisBean();

        setHead();
    }


    private void setHead(){
        headStr=actionStr.substring(0,6);
        actionBaseBean.setVersion(OcMath.toBigIntNoPrefix(headStr.substring(0,2)).toString());
        actionBaseBean.setActionType(OcMath.toBigIntNoPrefix(headStr.substring(2,4)).toString());
        actionBaseBean.setTxId(OcMath.toHexStringNoPrefix(WalletUtils.getTxIdBin(actionStr)));

        if(isTypes())
        {
            setBody();
        }else {
//            System.out.println("只支持actionType为1或4的交易类型解析");
        }
    }

    /**
     * actionType类型过滤
     * @return
     */
    private boolean isTypes(){
        if(actionBaseBean.getActionType().equals("1")||actionBaseBean.getActionType().equals("4"))
            return true;
        else return false;
    }

    private void setBody(){
        int bodyStartIndex=headStr.length()+getLebNum(headStr).length();
        AnalysisBean.TradingBean mTradingBean=new AnalysisBean.TradingBean();
        List<AnalysisBean.TradingBean.VinBean> vinList=new ArrayList<>();
        List<AnalysisBean.TradingBean.VoutBean> voutList=new ArrayList<>();
        for (int i = 0; i< Leb128Utils.decodeUnsigned(getLebNum(headStr)); i++)
        {
            AnalysisBean.TradingBean.VinBean vinObj=new AnalysisBean.TradingBean.VinBean();
            bodyStartIndex=bodyStartIndex+64;
            vinObj.setTxId(actionStr.substring(bodyStartIndex-64,bodyStartIndex));
            String lebStrN=actionStr.substring(bodyStartIndex,bodyStartIndex+2);
            vinObj.setN(String.valueOf(OcMath.toBigIntNoPrefix(lebStrN)));
            bodyStartIndex=bodyStartIndex+lebStrN.length();

//            System.out.println(vinObj.getN());
            int scriptLength=Integer.valueOf(OcMath.toBigIntNoPrefix(actionStr.substring(bodyStartIndex,bodyStartIndex+4)).toString())*2;
            bodyStartIndex=bodyStartIndex+4;
            vinObj.setScriptSig(actionStr.substring(bodyStartIndex,bodyStartIndex+scriptLength));
            bodyStartIndex=bodyStartIndex+scriptLength;

            vinList.add(vinObj);
        }

        String outSizeLeb=getLebNum(actionStr.substring(0,bodyStartIndex));
        bodyStartIndex=bodyStartIndex+outSizeLeb.length();
        for (int i=0;i<Leb128Utils.decodeUnsigned(outSizeLeb);i++)
        {

            AnalysisBean.TradingBean.VoutBean voutObj=new AnalysisBean.TradingBean.VoutBean();
            String valueHex=actionStr.substring(bodyStartIndex,bodyStartIndex+16);
            voutObj.setValue(OcMath.toBigIntNoPrefix(valueHex).toString());
            bodyStartIndex=bodyStartIndex+valueHex.length();
            String addressStr=actionStr.substring(bodyStartIndex,bodyStartIndex+54);
            voutObj.setAddress(addressStr.split("001976a914")[1].split("88ac")[0]);
            bodyStartIndex=bodyStartIndex+addressStr.length();

            voutList.add(voutObj);
        }
        String lockTimeStr=getLebNum(actionStr.substring(0,bodyStartIndex));
        mTradingBean.setLockTime(String.valueOf(Leb128Utils.decodeUnsigned(lockTimeStr)));
        bodyStartIndex=bodyStartIndex+lockTimeStr.length();
        mTradingBean.setCost(OcMath.toBigIntNoPrefix(actionStr.substring(bodyStartIndex,bodyStartIndex+16)).toString());
        bodyStartIndex=bodyStartIndex+16+2;

        mTradingBean.setVin(vinList);
        mTradingBean.setVout(voutList);
        actionBaseBean.setTrading(mTradingBean);

        String timeStr=getLebNum(actionStr.substring(0,bodyStartIndex));
        actionBaseBean.setTime(String.valueOf(Leb128Utils.decodeUnsigned(timeStr)));
        bodyStartIndex=bodyStartIndex+timeStr.length();
        String heightStr=getLebNum(actionStr.substring(0,bodyStartIndex));
        actionBaseBean.setCreatedBlock(String.valueOf(Leb128Utils.decodeUnsigned(heightStr)));
        bodyStartIndex=bodyStartIndex+heightStr.length();
        actionBaseBean.setOriginator(actionStr.substring(bodyStartIndex,bodyStartIndex+66));
        bodyStartIndex=bodyStartIndex+66+2;
        int insSize=Integer.valueOf(OcMath.toBigIntNoPrefix(actionStr.substring(bodyStartIndex-2,bodyStartIndex)).toString());
        actionBaseBean.setIns(actionStr.substring(bodyStartIndex,bodyStartIndex+insSize*2));
        bodyStartIndex=bodyStartIndex+insSize*2;
        actionBaseBean.setActionSign(actionStr.substring(bodyStartIndex));
    }



public String getJson(){
    if(isTypes())
        return  JSON.toJSONString(actionBaseBean);
    else
        return null;
}

    private String getLebNum(String startStr){
        int startIndex=startStr.length();
        int endIndex;
        while (true)
        {
            endIndex=startIndex+2;
            if (Leb128Utils.isLebEnd(actionStr.substring(startIndex,endIndex)))
            {
                break;
            }
            else
            startIndex=startIndex+2;
        }
        return actionStr.substring(startStr.length(),endIndex);
    }

}
