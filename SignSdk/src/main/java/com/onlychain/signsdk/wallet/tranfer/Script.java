package com.onlychain.signsdk.wallet.tranfer;




import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.secp256k1.Secp256k1;
import com.onlychain.signsdk.secp256k1.Signature;
import com.onlychain.signsdk.utils.OcMath;

import java.math.BigInteger;

public class Script {

    /**
     * 交易输入脚本
     * @param mAccountBean 账户对象
     * @param message 消息
     * @return 脚本结果
     */
    public static String vInScript(AccountBean mAccountBean, byte[]  message){
        String sigResult;
        String sigContent;
        Signature sig = Secp256k1.sign(mAccountBean.getPrivateKeyBin(), message);
//        System.out.println("sig--------------"+OcMath.toHexStringNoPrefix(sig.serialize()));
        byte[] sigByte=sig.serialize();
        String sigStr= OcMath.toHexStringNoPrefix(sigByte);
        String publicKeyStr=OcMath.toHexStringNoPrefix(mAccountBean.getPublicKeyBin());

        sigContent=OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(sigByte.length)),2)+sigStr+OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(mAccountBean.getPublicKeyBin().length)),2)+publicKeyStr;
        sigResult=OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(OcMath.hexStringToByteArray(sigContent).length)),4)+sigContent;
        return sigResult;
    }


    /**
     * 交易输出脚本
     * @param address 地址
     * @return 脚本结果
     */
    public static String vOutScript(String address){
        String scriptResult=getLockScript(address);
        scriptResult=OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(OcMath.hexStringToByteArray(scriptResult).length)),4)+scriptResult;
        return scriptResult;
    }


    /**
     * 锁定脚本
     * @param address
     * @return
     */
    public static String getLockScript(String address){
        return "76a914"+address+"88ac";
    }
}
