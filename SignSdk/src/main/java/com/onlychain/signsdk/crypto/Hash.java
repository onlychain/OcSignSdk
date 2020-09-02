package com.onlychain.signsdk.crypto;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    private Hash() { }

    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Couldn't find a SHA-256 provider", e);
        }
    }
}
