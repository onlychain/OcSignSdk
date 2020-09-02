package com.onlychain.signsdk.secp256k1;

import java.math.BigInteger;

public class ModN {
	private ModN() {
	}

	public final static BigInteger N = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
	
	public static BigInteger byteArrayToU256(byte[] bytes) {
		if (bytes.length != 32) {
			throw new ArithmeticException("�ֽ����������32�ֽ�");
		}
		BigInteger x = new BigInteger(1, bytes);
		if (x.compareTo(N) >= 0) {
			x = x.subtract(N);
		}
		return x;
	}
	
	public static BigInteger add(BigInteger a, BigInteger b) {
		BigInteger c = a.add(b);
		if (c.compareTo(N) >= 0) {
			c = c.subtract(N);
		}
		return c;
	}

	public static BigInteger sub(BigInteger a, BigInteger b) {
		if (a.compareTo(b) < 0) {
			return a.add(N).subtract(b);
		} else {
			return a.subtract(b);
		}
	}
	
	public static BigInteger mul(BigInteger a, BigInteger b) {
		return a.multiply(b).mod(N);
	}
	
	public static BigInteger div(BigInteger a, BigInteger b) {
		return mul(a, inv(b));
	}
	
	public static BigInteger inv(BigInteger a) {
		return a.modInverse(N);
	}
}
