package com.onlychain.signsdk.bean;


import com.onlychain.signsdk.crypto.Hash;
import com.onlychain.signsdk.secp256k1.PublicKey;
import com.onlychain.signsdk.secp256k1.Secp256k1;
import com.onlychain.signsdk.secp256k1.Signature;
import com.onlychain.signsdk.utils.OcMath;
import com.onlychain.signsdk.utils.WalletUtils;

public class BaseActionBean {
    private String message;
    private String sigStr;
    private String txid;
    private String commitData;

    /**
     * s使用公钥验证消息的真实性
     * @param publicKeyBin
     * @return
     */
    public boolean checkSig(byte[] publicKeyBin){
        if (Secp256k1.verify(PublicKey.parse(publicKeyBin), Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(message))), Signature.parse(OcMath.hexStringToByteArray(sigStr))))
            return true;
        else
            return false;
    }

    /**
     * 验证自定义消息
     * @param publicKeyBin
     * @param message
     * @param sigStr
     * @return
     */
    /*public boolean checkApiSign(byte[] publicKeyBin,String message,String sigStr){
        if (Secp256k1.verify(PublicKey.parse(publicKeyBin), Hash.sha256(Hash.sha256(message.getBytes())), Signature.parse(OcMath.hexStringToByteArray(sigStr))))
            return true;
        else
            return false;
    }*/

    public boolean checkSign(byte[] publicKeyBin,String message,String sigStr){
        if (Secp256k1.verify(PublicKey.parse(publicKeyBin), Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(message))), Signature.parse(OcMath.hexStringToByteArray(sigStr))))
            return true;
        else
            return false;
    }


    /**
     * 使用私钥将对应公钥与自定义消息进行加签
     * @param privateKeyBin
     * @param message
     * @return
     */
    public String makeSign(byte[] privateKeyBin,String message){
        return OcMath.toHexStringNoPrefix(Secp256k1.sign(privateKeyBin,Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(message)))).serialize());
    }

    public String getCommitData() {
        return message+sigStr;
    }

    public void setCommitData(String commitData) {
        this.commitData = commitData;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }


    public String getTxid() {
        return WalletUtils.getTxId(getCommitData());
    }

    public byte[] getTxidBin() {
        return WalletUtils.getTxIdBin(getCommitData());
    }

    public String getSignStr() {
        return sigStr;
    }

    public void setSignStr(String sigStr) {
        this.sigStr = sigStr;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }


}

