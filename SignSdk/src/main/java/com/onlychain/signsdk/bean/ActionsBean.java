package com.onlychain.signsdk.bean;

import java.util.List;

public class ActionsBean {
    private String version;
    private String actionType;
    private String hasTrading;
    private String hasAction;
    private TradingBean trading;
    private ActionBean action;
    private long time;
    private long createdBlock;
    private String originator;
    private String insLength;
    private String ins;
    private String actionSign;
    private String txId;

    public String getInsLength() {
        return insLength;
    }

    public void setInsLength(String insLength) {
        this.insLength = insLength;
    }

    public String getHasAction() {
        return hasAction;
    }

    public void setHasAction(String hasAction) {
        this.hasAction = hasAction;
    }

    public String getHasTrading() {
        return hasTrading;
    }

    public void setHasTrading(String hasTrading) {
        this.hasTrading = hasTrading;
    }

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

    public ActionBean getAction() {
        return action;
    }

    public void setAction(ActionBean action) {
        this.action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getCreatedBlock() {
        return createdBlock;
    }

    public void setCreatedBlock(long createdBlock) {
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

    public static class TradingBean {

        private long lockTime;
        private long cost;
        private long vinNum;
        private long voutNum;
        private List<VinBean> vin;
        private List<VoutBean> vout;


        public long getVinNum() {
            return vinNum;
        }

        public void setVinNum(long vinNum) {
            this.vinNum = vinNum;
        }

        public long getVoutNum() {
            return voutNum;
        }

        public void setVoutNum(long voutNum) {
            this.voutNum = voutNum;
        }

        public long getLockTime() {
            return lockTime;
        }

        public void setLockTime(long lockTime) {
            this.lockTime = lockTime;
        }

        public long getCost() {
            return cost;
        }

        public void setCost(long cost) {
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
            private int scriptLength;
            private String scriptSig;

            public int getScriptLength() {
                return scriptLength;
            }

            public void setScriptLength(int scriptLength) {
                this.scriptLength = scriptLength;
            }

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

            private long value;
            private String n;
            private String reqSigs;
            private String address;

            public String getN() {
                return n;
            }

            public void setN(String n) {
                this.n = n;
            }

            public long getValue() {
                return value;
            }

            public void setValue(long value) {
                this.value = value;
            }



            public String getReqSigs() {
                return reqSigs;
            }

            public void setReqSigs(String reqSigs) {
                this.reqSigs = reqSigs;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }

    public static class ActionBean {
        private String voter;
        private int again;
        private int rounds;
        private List<String> candidate;

        public String getVoter() {
            return voter;
        }

        public void setVoter(String voter) {
            this.voter = voter;
        }

        public int getAgain() {
            return again;
        }

        public void setAgain(int again) {
            this.again = again;
        }

        public int getRounds() {
            return rounds;
        }

        public void setRounds(int rounds) {
            this.rounds = rounds;
        }

        public List<String> getCandidate() {
            return candidate;
        }

        public void setCandidate(List<String> candidate) {
            this.candidate = candidate;
        }
    }
}
