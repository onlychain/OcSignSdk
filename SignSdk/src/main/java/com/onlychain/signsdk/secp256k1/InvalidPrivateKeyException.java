package com.onlychain.signsdk.secp256k1;

@SuppressWarnings("serial")
public final class InvalidPrivateKeyException extends RuntimeException {
	public InvalidPrivateKeyException() {
		super("无效的私钥");
	}

	public InvalidPrivateKeyException(String message) {
		super(message);
	}
}
