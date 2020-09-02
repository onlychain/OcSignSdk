package com.onlychain.signsdk.secp256k1;

import java.math.BigInteger;
import java.util.Arrays;

public final class PublicKey {
	private BigInteger x, y;

	public BigInteger getX() {
		return x;
	}

	public BigInteger getY() {
		return y;
	}

	public PublicKey(BigInteger x, BigInteger y) {
		this.x = x;
		this.y = y;
	}

	static final byte EVEN_PUBLIC_KEY = 2;
	static final byte ODD_PUBLIC_KEY = 3;
	static final byte FULL_PUBLIC_KEY = 4;

	public byte[] serialize(boolean compressed) {
		int len = compressed ? 33 : 65;
		byte[] ret = new byte[len];
		byte[] x = ModP.toByteArray(this.x);
		System.arraycopy(x, 0, ret, 1, 32);
		if (compressed) {
			ret[0] = y.testBit(0) ? ODD_PUBLIC_KEY : EVEN_PUBLIC_KEY;
		} else {
			byte[] y = ModP.toByteArray(this.y);
			System.arraycopy(y, 0, ret, 33, 32);
			ret[0] = FULL_PUBLIC_KEY;
		}
		return ret;
	}

	public static PublicKey parse(byte[] publicKeyBytes) {
		if (publicKeyBytes.length < 33) throw new InvalidPublicKeyException("长度太小");
		BigInteger x = new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, 33));
		if (publicKeyBytes[0] == EVEN_PUBLIC_KEY || publicKeyBytes[0] == ODD_PUBLIC_KEY) {
			try {
				BigInteger y = ModP.sqrt(ModP.add(ModP.pow(x, BigInteger.valueOf(3)), BigInteger.valueOf(7)));
				if (y.testBit(0) != (publicKeyBytes[0] == ODD_PUBLIC_KEY)) {
					y = ModP.neg(y);
				}
				return new PublicKey(x, y);
			} catch (ArithmeticException e) {
				throw new InvalidPublicKeyException("无效的公钥");
			}
		} else if (publicKeyBytes[0] == FULL_PUBLIC_KEY) {
			if (publicKeyBytes.length < 65) throw new InvalidPublicKeyException("长度太小");
			BigInteger y = new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 33, 65));
			if (!ModP.add(ModP.pow(x, BigInteger.valueOf(3)), BigInteger.valueOf(7)).equals(ModP.square(y))) {
				throw new InvalidPublicKeyException("无效的公钥");
			}
			return new PublicKey(x, y);
		} else {
			throw new InvalidPublicKeyException("不支持的公钥类型");
		}
	}
}