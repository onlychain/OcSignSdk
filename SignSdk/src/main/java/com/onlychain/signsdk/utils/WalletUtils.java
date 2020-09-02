package com.onlychain.signsdk.utils;


import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.crypto.Hash;
import com.onlychain.signsdk.crypto.RIPEMD160Digest;
import com.onlychain.signsdk.secp256k1.Secp256k1;

public class WalletUtils {

    /**
     * 根据私钥生成账户信息
     * @return
     */
    public static AccountBean createAccount(){
        return  createAccount(Secp256k1.createPrivateKey());
    }

    /**
     * 随机创建一组私钥、公钥、地址
     * @return
     */
    public static AccountBean createAccount(byte[] privateKey){
        AccountBean mAccountBean=new AccountBean();
        try {
            byte[] publicKey = Secp256k1.createPublicKey(privateKey).serialize(true);
            mAccountBean.setPrivateKey(OcMath.toHexStringNoPrefix(privateKey));
            mAccountBean.setPublicKey(OcMath.toHexStringNoPrefix(publicKey));
            mAccountBean.setAddress(createAddress(publicKey));
        }catch (Exception e){

        }
        return  mAccountBean;
    }


    /**
     * 获取整交易的TxId
     * @param input
     * @return
     */
    public static String getTxId(byte[] input){
        return OcMath.toHexStringNoPrefix(Hash.sha256(Hash.sha256(input)));
    }

    /**
     * 获取整交易的TxId
     * @param input
     * @return
     */
    public static String getTxId(String input){
        return OcMath.toHexStringNoPrefix(Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(input))));
    }

    public static byte[] getTxIdBin(String input){
        return Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(input)));
    }

    public static byte[] getTxIdBin(byte[] input){
        return Hash.sha256(Hash.sha256(input));
    }

    public static String createAddress(byte[] publicKey) {
        byte[] sha256 = Hash.sha256(publicKey);
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256, 0, sha256.length);
        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);
        return  OcMath.toHexStringNoPrefix(out).toLowerCase();
    }
}
