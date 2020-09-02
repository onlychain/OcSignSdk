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
        //根据确定的私钥生成账户
        final AccountBean mAccountBean= WalletUtils.createAccount(OcMath.hexStringToByteArray("1a23e889d590a443831a785a398ce74179f09dece2fe5bfda41f795c50240c62"));
        //对消息进行加签和验签
        BaseActionBean mBaseActionBean=new BaseActionBean();

        //移动端:对带有公钥的json数据加签,将签名结果加入请求header中
        String jsonMsgX="{\\\"msg\\\":{\\\"publicKey\\\":\\\"0355c48844bb6392dbd251167c92904eabbbc1982b39dc7ebcb2141063f0281e1f\\\",\\\"title\\\":\\\"下方标题\\\",\\\"content\\\":\\\"这是内容\\\"}}";
        String jsonMsg="ismessage";
        System.out.println(mAccountBean.getPrivateKey());
        System.out.println(mBaseActionBean.makeSign(mAccountBean.getPrivateKeyBin(),jsonMsg));



        //服务端:提取json中公钥、整json内容以及header中的sign对其验签
        System.out.println(mBaseActionBean.checkApiSign(mAccountBean.getPublicKeyBin(),jsonMsg,"30450221008fe7f0f7bb55da842522ba91565065eae37c0bf3b06b402b129c34b288d8084402202247716b7cb03545eb455ef36c7c4070db905250615a9a4b975d49f5dadc2441"));
    }
}
