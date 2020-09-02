package com.onlychain.signsdk.bean;


import com.onlychain.signsdk.utils.OcMath;

public class AccountBean {
    private String privateKey;
    private String publicKey;
    private String address;

    public String getPrivateKey() {
        return privateKey;
    }
    public byte[] getPrivateKeyBin() {
        return OcMath.hexStringToByteArray(privateKey);
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
    public byte[] getPublicKeyBin() {
        return OcMath.hexStringToByteArray(publicKey);
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAddress() {
        return address;
    }
    public String getAddressNoPrefix() {
        return address.substring(2);
    }
    public void setAddress(String address) {
        this.address = "oc"+address;
    }
}
