package com.onlychain.signsdk.secp256k1;

@SuppressWarnings("serial")
public final class InvalidSignatureException extends RuntimeException {
	public InvalidSignatureException(String message) {
		super(message);
	}

	public InvalidSignatureException() {
		super("无效的签名");
	}
}
