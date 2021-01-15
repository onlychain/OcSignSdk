package com.onlychain.ocsignsdk;


import com.onlychain.signsdk.bean.AccountBean;
import com.onlychain.signsdk.bean.BaseActionBean;
import com.onlychain.signsdk.utils.OcMath;
import com.onlychain.signsdk.utils.WalletUtils;

/**
 * 对自定义消息进行加签和解签
 * 使用场景：只允许来自本人的私钥（公钥、地址）身份进行访问自己数据，避免有人通过接口可以查看他人数据
 */
public class ApiSignTools {
    public static void main(String[] args) {
        String pri="c7c0e51106b3a63a9ccf547c1f391493e13a12950c7d25b6ecbcc8bff112d9ab";
        AccountBean mAccountBean= WalletUtils.createAccount(OcMath.hexStringToByteArray(pri));

        //OC原生消息签名,消息仅支持16进制(考虑IOS等前端兼容问题)
        String body="0123456789abcdef";
        String  signAsStr=new BaseActionBean().makeSign(mAccountBean.getPrivateKeyBin(),body);
        boolean s=new BaseActionBean().checkSign(OcMath.hexStringToByteArray(mAccountBean.getPublicKey()),body,signAsStr);
        System.out.println(s);


        //自定义消息签名
        String body2="我是自定义消息";
        String  signAsStr2=new BaseActionBean().makeApiSign(mAccountBean.getPrivateKeyBin(),body2);
        boolean s2=new BaseActionBean().checkApiSign(OcMath.hexStringToByteArray(mAccountBean.getPublicKey()),body2,signAsStr2);
        System.out.println(s2);
    }
}
