package com.onlychain.signsdk.bean;

import java.util.List;

public class AnalysisBean {

    private String version;
    private String actionType;
    private TradingBean trading;
    private String time;
    private String createdBlock;
    private String originator;
    private String ins;
    private String actionSign;
    private String txId;
    private List<?> action;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public TradingBean getTrading() {
        return trading;
    }

    public void setTrading(TradingBean trading) {
        this.trading = trading;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatedBlock() {
        return createdBlock;
    }

    public void setCreatedBlock(String createdBlock) {
        this.createdBlock = createdBlock;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getIns() {
        return ins;
    }

    public void setIns(String ins) {
        this.ins = ins;
    }

    public String getActionSign() {
        return actionSign;
    }

    public void setActionSign(String actionSign) {
        this.actionSign = actionSign;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public List<?> getAction() {
        return action;
    }

    public void setAction(List<?> action) {
        this.action = action;
    }

    public static class TradingBean {

        private String lockTime;
        private String cost;
        private List<VinBean> vin;
        private List<VoutBean> vout;

        public String getLockTime() {
            return lockTime;
        }

        public void setLockTime(String lockTime) {
            this.lockTime = lockTime;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public List<VinBean> getVin() {
            return vin;
        }

        public void setVin(List<VinBean> vin) {
            this.vin = vin;
        }

        public List<VoutBean> getVout() {
            return vout;
        }

        public void setVout(List<VoutBean> vout) {
            this.vout = vout;
        }

        public static class VinBean {


            private String txId;
            private String n;
            private String scriptSig;

            public String getTxId() {
                return txId;
            }

            public void setTxId(String txId) {
                this.txId = txId;
            }

            public String getN() {
                return n;
            }

            public void setN(String n) {
                this.n = n;
            }

            public String getScriptSig() {
                return scriptSig;
            }

            public void setScriptSig(String scriptSig) {
                this.scriptSig = scriptSig;
            }
        }

        public static class VoutBean {

            private String value;
            private String n;
            private String reqSigs;
            private String address;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getN() {
                return n;
            }

            public void setN(String n) {
                this.n = n;
            }

            public String getReqSigs() {
                return reqSigs;
            }

            public void setReqSigs(String reqSigs) {
                this.reqSigs = reqSigs;
            }

            public String getAddress() {
                return "oc"+address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
