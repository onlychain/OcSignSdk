package com.onlychain.signsdk.bean;



import com.onlychain.signsdk.utils.Leb128Utils;
import com.onlychain.signsdk.utils.OcMath;

import java.math.BigInteger;
import java.sql.Time;

public class EndBean {
    private String lockTime="";
    private String coat="";
    private String isAcitonLabel="";
    private String time="";
    private String height="";
    private String insSize="";
    private String ins="";
    private String publicKey="";
    private String sig="";

    public EndBean(String height, String publicKey, String sig) {
        this.lockTime = Leb128Utils.encodeUleb128(0);
        this.coat =  OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(0)),16);
        this.isAcitonLabel =  OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(0)),2);;
        this.time = Leb128Utils.encodeUleb128(System.currentTimeMillis());;
        this.height = Leb128Utils.encodeUleb128(height);
        this.ins = "00";
        this.insSize = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(OcMath.hexStringToByteArray(this.ins).length)),2);
        this.publicKey = publicKey;
        this.sig = sig;
    }


    @Override
    public String toString() {
        StringBuffer result=new StringBuffer();
        return result.append(lockTime).append(coat).append(isAcitonLabel).append(time).append(height).append(publicKey).append(ins).append(insSize).append(sig).toString();
    }
}
