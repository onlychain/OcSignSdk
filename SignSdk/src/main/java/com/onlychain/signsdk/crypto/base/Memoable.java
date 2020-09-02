package com.onlychain.signsdk.crypto.base;

public interface Memoable
{

    Memoable copy();


    void reset(Memoable other);
}
