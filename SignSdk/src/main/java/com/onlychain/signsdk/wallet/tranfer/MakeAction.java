package com.onlychain.signsdk.wallet.tranfer;


import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.bean.BaseActionBean;
import com.onlychain.signsdk.bean.HeadBean;
import com.onlychain.signsdk.bean.OutBean;
import com.onlychain.signsdk.bean.PurseBean;
import com.onlychain.signsdk.crypto.Hash;
import com.onlychain.signsdk.secp256k1.Secp256k1;
import com.onlychain.signsdk.utils.Leb128Utils;
import com.onlychain.signsdk.utils.OcMath;
import com.onlychain.signsdk.utils.WalletUtils;

import java.math.BigInteger;
import java.util.List;

public class MakeAction {
    AccountBean mAccountBean;
    List<PurseBean.RecordBean> coinList;
    List<OutBean> outList;
    int actionType=1;
    long lockHeight=0;

    public MakeAction(AccountBean mAccountBean,int actionType,List<PurseBean.RecordBean> coinList, List<OutBean> outList) {
        this.mAccountBean = mAccountBean;
        this.actionType=actionType;
        this.coinList = coinList;
        this.outList=outList;
        this.lockHeight=-1;
    }

    public MakeAction(AccountBean mAccountBean,int actionType,List<PurseBean.RecordBean> coinList, List<OutBean> outList,long lockHeight) {
        this.mAccountBean = mAccountBean;
        this.actionType=actionType;
        this.coinList = coinList;
        this.outList=outList;
        this.lockHeight=lockHeight;
    }


    /**
     * 普通交易
     * @return
     */
    public BaseActionBean createAction(String height){
        HeadBean head=new HeadBean(actionType,setVInBody()+setVOutBody());
        HeadBean.EndBean mEndBean = new HeadBean.EndBean(height,mAccountBean.getPublicKey(),head.toString());
        if(actionType==4)
            mEndBean.setLockTimeAdd1Year(height);

        if(actionType==1)
            if (lockHeight>0)
                mEndBean.setLockTimeForCoin(lockHeight);

        String result=mEndBean.getResult();
        String sigStr = OcMath.toHexStringNoPrefix(Secp256k1.sign(mAccountBean.getPrivateKeyBin(), WalletUtils.getTxIdBin(result)).serialize());
        BaseActionBean mBaseActionBean=new BaseActionBean();
        mBaseActionBean.setMessage(result);
        mBaseActionBean.setSignStr(sigStr);
//        mBaseActionBean.setTxid(WalletUtils.getTxId(result));
        return mBaseActionBean;
    }


      private String setVInBody(){
        StringBuffer result=new StringBuffer();
        //LEB128 输入笔数
        String number= Leb128Utils.encodeUleb128(coinList.size());
        for(PurseBean.RecordBean coin:coinList)
        {
//            String txN=Leb128Utils.encodeUleb128(coin.getN());
            String txN=OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(coin.getN())),2);
            String scriptContent = coin.getTxId()+txN+Script.getLockScript(mAccountBean.getAddressNoPrefix());
//            System.out.println("text--------------"+scriptContent);
            scriptContent= Script.vInScript(mAccountBean, Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(scriptContent))));
            result = result.append(coin.getTxId()).append(txN).append(scriptContent);
        }
        return number+result.toString();
    }


    private String setVOutBody(){
        StringBuffer result=new StringBuffer();
        //LEB128 输出笔数
        String number= Leb128Utils.encodeUleb128(outList.size());
        for(OutBean out:outList)
        {
            result = result.append(OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(out.getValue())),16)).append(Script.vOutScript(out.getAddress()));
        }
        return number+result.toString();
    }



}
