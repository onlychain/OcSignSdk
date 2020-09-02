package com.onlychain.signsdk.secp256k1;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class Secp256k1 {
	private Secp256k1() {
	}

	static SecureRandom RNG;

	static {
		try {
			RNG = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static byte[] createPrivateKey() {
		while (true) {
			BigInteger k = new BigInteger(256, RNG);
			if (k.equals(BigInteger.ZERO) || k.compareTo(ModN.N) >= 0) continue;
			return ModP.toByteArray(k);
		}
	}

	public static PublicKey createPublicKey(byte[] privateKey) {
		BigInteger k = ModP.byteArrayToU256(privateKey);
		if (k.equals(BigInteger.ZERO) || k.compareTo(ModN.N) >= 0) throw new InvalidPrivateKeyException();
		Point p = ModP.mul(ModP.G, k);
		return new PublicKey(ModP.toU256(p.getX()), ModP.toU256(p.getY()));
	}

	public static Signature sign(byte[] privateKey, byte[] message) {
		if (privateKey.length != 32) throw new InvalidPrivateKeyException("私钥长度必须是32字节");
		if (message.length != 32) throw new InvalidMessageException();

		BigInteger dA = ModN.byteArrayToU256(privateKey);
		BigInteger m = ModN.byteArrayToU256(message);
		byte[] tempPrivKey = createPrivateKey();
		PublicKey tempPubKey = createPublicKey(tempPrivKey);
		BigInteger k = ModN.byteArrayToU256(tempPrivKey);
		BigInteger s = ModN.div(ModN.add(m, ModN.mul(dA, tempPubKey.getX())), k);
		return new Signature(tempPubKey.getX(), s);
	}

	public static boolean verify(PublicKey publicKey, byte[] message, Signature signature) {
		if (message.length != 32) throw new InvalidMessageException();

		BigInteger m = ModN.byteArrayToU256(message);
		BigInteger s_inv = ModN.inv(signature.getS());
		BigInteger u1 = ModN.mul(s_inv, m);
		BigInteger u2 = ModN.mul(s_inv, signature.getR());
		Point p = ModP.add(ModP.mulG(u1), ModP.mul(new Point(new Fraction(publicKey.getX()), new Fraction(publicKey.getY())), u2));
		return ModP.equal(p.getX(), new Fraction(signature.getR()));
	}
}
