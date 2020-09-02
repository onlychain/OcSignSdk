package com.onlychain.signsdk.secp256k1;

@SuppressWarnings("serial")
public final class InvalidMessageException extends RuntimeException {
	public InvalidMessageException() {
		super("消息长度必须是32字节");
	}
}