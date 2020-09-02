package com.onlychain.signsdk.bean;

import java.util.List;

public class PurseBean {

    /**
     * code : 200
     * record : [{"txId":"2cdbff7cd8cf9e2df0ba114aa5b40ace73166899f63b1df1ff0355e119bfb030","n":0,"value":29089999999,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":1173178,"lockTime":1173178,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":1},{"txId":"2ffb849b6fbefe254a0af0d30ca3a8d411b4823b33c1dc2dafbc2d4bccda045d","n":8,"value":124806000000,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":1545253,"lockTime":1545253,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":1},{"txId":"39320abc22cf631b5dae8c79cd3d9c2f76932ba569da1a182969ecc22f308ae5","n":11,"value":47058000000,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":1089849,"lockTime":1089849,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":1},{"txId":"44811a3b160ab2488dbaf62188ab47d39a0f76a5dcdaf2c873ae8e5408b35171","n":0,"value":10000000000,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":710684,"lockTime":16478684,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":4},{"txId":"56f2e628a63ba91089313d443bcd76852f0cc7653613d81ccaeec08d99207cf6","n":0,"value":18972000000,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":1175829,"lockTime":1175829,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":1},{"txId":"6413cfc451092a9497f6b743595831d5cd6387056226af5a99e0d781b3c9184a","n":13,"value":45542100000,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":1025399,"lockTime":1025399,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":1},{"txId":"6a1ea1b8cadfd83a48b8b768502c4317bf00247058863aa7db49a81c2b5063cf","n":11,"value":214923000000,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":1324315,"lockTime":1324315,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":1},{"txId":"9bde6e43707a6ed960894f0194dd835a74de401cff64c2aeaf3bf6a55b487c4f","n":5,"value":121179000000,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":1056809,"lockTime":1056809,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":1},{"txId":"ab0c8039141e6ea86c8ca4b444b7913a322c2e3d2f6c253b701552f0c1f1ea99","n":6,"value":28737000000,"reqSigs":"76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac","createdBlock":1056790,"lockTime":1056790,"address":"e18da4602d2615ce76b45f6ae9cd3df041e2debc","actionType":1}]
     */

    private int code;
    private List<RecordBean> record;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<RecordBean> getRecord() {
        return record;
    }

    public void setRecord(List<RecordBean> record) {
        this.record = record;
    }

    public static class RecordBean {
        /**
         * txId : 2cdbff7cd8cf9e2df0ba114aa5b40ace73166899f63b1df1ff0355e119bfb030
         * n : 0
         * value : 29089999999
         * reqSigs : 76a914e18da4602d2615ce76b45f6ae9cd3df041e2debc88ac
         * createdBlock : 1173178
         * lockTime : 1173178
         * address : e18da4602d2615ce76b45f6ae9cd3df041e2debc
         * actionType : 1
         */

        private String txId;
        private int n;
        private long value;
        private String reqSigs;
        private long createdBlock;
        private long lockTime;
        private String address;
        private int actionType;

        public String getTxId() {
            return txId;
        }

        public void setTxId(String txId) {
            this.txId = txId;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
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

        public long getCreatedBlock() {
            return createdBlock;
        }

        public void setCreatedBlock(long createdBlock) {
            this.createdBlock = createdBlock;
        }

        public long getLockTime() {
            return lockTime;
        }

        public void setLockTime(long lockTime) {
            this.lockTime = lockTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getActionType() {
            return actionType;
        }

        public void setActionType(int actionType) {
            this.actionType = actionType;
        }
    }
}
