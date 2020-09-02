package com.onlychain.signsdk.bean;

import java.util.List;

public class BlockBean {

    private int code;
    private RecordBean record;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RecordBean getRecord() {
        return record;
    }

    public void setRecord(RecordBean record) {
        this.record = record;
    }

    public static class RecordBean {

        private String parentHash;
        private String merkleRoot;
        private int version;
        private int thisTime;
        private int height;
        private int tradingNum;
        private String signature;
        private String headHash;
        private String blockSign;
        private List<String> tradingInfo;

        public String getParentHash() {
            return parentHash;
        }

        public void setParentHash(String parentHash) {
            this.parentHash = parentHash;
        }

        public String getMerkleRoot() {
            return merkleRoot;
        }

        public void setMerkleRoot(String merkleRoot) {
            this.merkleRoot = merkleRoot;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getThisTime() {
            return thisTime;
        }

        public void setThisTime(int thisTime) {
            this.thisTime = thisTime;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getTradingNum() {
            return tradingNum;
        }

        public void setTradingNum(int tradingNum) {
            this.tradingNum = tradingNum;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getHeadHash() {
            return headHash;
        }

        public void setHeadHash(String headHash) {
            this.headHash = headHash;
        }

        public String getBlockSign() {
            return blockSign;
        }

        public void setBlockSign(String blockSign) {
            this.blockSign = blockSign;
        }

        public List<String> getTradingInfo() {
            return tradingInfo;
        }

        public void setTradingInfo(List<String> tradingInfo) {
            this.tradingInfo = tradingInfo;
        }
    }
}
