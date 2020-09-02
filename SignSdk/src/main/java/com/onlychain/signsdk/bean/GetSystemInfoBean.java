package com.onlychain.signsdk.bean;

public class GetSystemInfoBean {


    /**
     * code : 200
     * record : {"rounds":34639,"sysTime":4364508,"thisTime":120,"blockHash":"47ec88cd3139b691ebef0e2e28f1692e1dccbca8acce002a7ef149251d4ff53f","blockHeight":1969474,"serverName":"f87c505b8b2ee130e38986c703b4e94cbdaadd04"}
     */

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
        /**
         * rounds : 34639
         * sysTime : 4364508
         * thisTime : 120
         * blockHash : 47ec88cd3139b691ebef0e2e28f1692e1dccbca8acce002a7ef149251d4ff53f
         * blockHeight : 1969474
         * serverName : f87c505b8b2ee130e38986c703b4e94cbdaadd04
         */

        private int rounds;
        private int sysTime;
        private int thisTime;
        private String blockHash;
        private long blockHeight;
        private String serverName;

        public int getRounds() {
            return rounds;
        }

        public void setRounds(int rounds) {
            this.rounds = rounds;
        }

        public int getSysTime() {
            return sysTime;
        }

        public void setSysTime(int sysTime) {
            this.sysTime = sysTime;
        }

        public int getThisTime() {
            return thisTime;
        }

        public void setThisTime(int thisTime) {
            this.thisTime = thisTime;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public long getBlockHeight() {
            return blockHeight;
        }

        public void setBlockHeight(long blockHeight) {
            this.blockHeight = blockHeight;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }
    }
}

