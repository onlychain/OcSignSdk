package com.onlychain.signsdk.bean;



import com.onlychain.signsdk.utils.Leb128Utils;
import com.onlychain.signsdk.utils.OcMath;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

public class HeadBean {
    private String version;
    private String actionType;
    private String hasTranfer;
    private String Body;

    public HeadBean(int version, int actionType, int hasTranfer,String Body) {
        this.Body=Body;
        this.version = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(version)),2);
        this.actionType = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(actionType)),2);
        this.hasTranfer = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(hasTranfer)),2);
    }

    public HeadBean(int actionType,String Body) {
        this.Body=Body;
        this.version = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(1)),2);
        this.actionType = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(actionType)),2);
        this.hasTranfer = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(1)),2);
    }

    @Override
    public String toString() {
        StringBuffer result=new StringBuffer();

        return result.append(version).append(actionType).append(hasTranfer).append(Body).toString();
    }


    public static class EndBean {

        private String lockTime = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(Leb128Utils.encodeUleb128(0)),2);
        private String coat="";
        private String isAcitonLabel="";
        private String time="";
        private String height="";
        private String insSize="";
        private String ins="";
        private String publicKey="";
        private String head="";

        public EndBean(String height, String publicKey, String head) {
            this.coat =  OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(0)),16);
            this.isAcitonLabel =  OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(0)),2);;
            this.time = Leb128Utils.encodeUleb128(String.valueOf(System.currentTimeMillis()/1000));;
            this.height = Leb128Utils.encodeUleb128(height);
            this.ins = "00";
            this.insSize = OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(OcMath.hexStringToByteArray(this.ins).length)),2);
            this.publicKey = publicKey;
            this.head = head;
        }

        public void setLockTimeAdd1Year(String height) {
            long lockHeight=Long.valueOf(height)+15768001l;
            this.lockTime = Leb128Utils.encodeUleb128(lockHeight);
        }

        public void setLockTimeForCoin(long lockHeight) {
            this.lockTime = Leb128Utils.encodeUleb128(lockHeight);
        }


        public String getResult() {
            StringBuffer result=new StringBuffer();


            return head+result.append(lockTime).append(coat).append(isAcitonLabel).append(time).append(height).append(publicKey).append(insSize).append(ins).toString();
        }
    }

}
