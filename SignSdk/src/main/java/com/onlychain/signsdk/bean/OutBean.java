package com.onlychain.signsdk.bean;

public class OutBean {
    private long value;
    private String address;

    public OutBean(long value, String address) {
        this.value = value;
        this.address = address;
    }
    public OutBean() {

    }
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getAddress() {
        if(address.length()==42)
            address=address.substring(2);
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
