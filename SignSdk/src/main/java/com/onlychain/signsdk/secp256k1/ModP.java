package com.onlychain.signsdk.secp256k1;

import java.math.BigInteger;
public final class ModP {
	private ModP() {
	}

	public final static BigInteger P = new BigInteger("fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f", 16);

	public static BigInteger byteArrayToU256(byte[] bytes) {
		if (bytes.length != 32) {
			throw new ArithmeticException("字节数组必须是32字节");
		}
		BigInteger x = new BigInteger(1, bytes);
		if (x.compareTo(P) >= 0) {
			x = x.subtract(P);
		}
		return x;
	}

	public static BigInteger add(BigInteger a, BigInteger b) {
		BigInteger c = a.add(b);
		if (c.compareTo(P) >= 0) {
			c = c.subtract(P);
		}
		return c;
	}

	public static BigInteger sub(BigInteger a, BigInteger b) {
		if (a.compareTo(b) < 0) {
			return a.add(P).subtract(b);
		} else {
			return a.subtract(b);
		}
	}

	public static BigInteger mul(BigInteger a, BigInteger b) {
		return a.multiply(b).mod(P);
	}

	public static BigInteger div(BigInteger a, BigInteger b) {
		return mul(a, inv(b));
	}

	public static BigInteger neg(BigInteger a) {
		if (a.equals(BigInteger.ZERO)) return BigInteger.ZERO;
		return P.subtract(a);
	}

	public static BigInteger square(BigInteger a) {
		return a.multiply(a).mod(P);
	}

	public static BigInteger inv(BigInteger a) {
		return a.modInverse(P);
	}

	public static BigInteger sqrt(BigInteger a) {
		if (!a.modPow(P.subtract(BigInteger.ONE).shiftRight(1), P).equals(BigInteger.ONE)) {
			throw new ArithmeticException("'" + toString(a) + "'在素域P中没有开平方。");
		}
		return fastSqrt(a);
	}

	public static BigInteger fastSqrt(BigInteger a) {
		return a.modPow(P.add(BigInteger.ONE).shiftRight(2), P);
	}

	public static BigInteger pow(BigInteger a, BigInteger b) {
		return a.modPow(b, P);
	}

	public static String toString(BigInteger a) {
		if (a.compareTo(BigInteger.ZERO) < 0 || a.compareTo(P) >= 0) {
			throw new ArithmeticException("给定的整数必须>=0且<P。");
		}

		String hex = a.toString(16);
		if (hex.length() < 64) {
			return "0000000000000000000000000000000000000000000000000000000000000000".substring(hex.length()) + hex;
		} else if (hex.length() > 64) {
			return hex.substring(hex.length() - 64);
		} else {
			return hex;
		}
	}

	/**
	 * 返回大整型的字节数组（固定32字节）
	 * @param a
	 * @return
	 */
	public static byte[] toByteArray(BigInteger a) {
		if (a.compareTo(BigInteger.ZERO) < 0 || a.compareTo(P) >= 0) {
			throw new ArithmeticException("给定的整数必须>=0且<P。");
		}

		byte[] bytes = new byte[32];
		byte[] tempBytes = a.toByteArray();
		if (tempBytes.length >= 32) {
			System.arraycopy(tempBytes, tempBytes.length - 32, bytes, 0, 32);
		} else {
			System.arraycopy(tempBytes, 0, bytes, 32 - tempBytes.length, tempBytes.length);
		}
		return bytes;
	}

	public static Fraction add(Fraction a, Fraction b) {
		if (a.getDen().equals(b.getDen())) {
			return new Fraction(add(a.getNum(), b.getNum()), a.getDen());
		} else {
			return new Fraction(add(mul(a.getNum(), b.getDen()), mul(a.getDen(), b.getNum())), mul(a.getDen(), b.getDen()));
		}
	}

	public static Fraction sub(Fraction a, Fraction b) {
		if (a.getDen().equals(b.getDen())) {
			return new Fraction(sub(a.getNum(), b.getNum()), a.getDen());
		} else {
			return new Fraction(sub(mul(a.getNum(), b.getDen()), mul(a.getDen(), b.getNum())), mul(a.getDen(), b.getDen()));
		}
	}

	public static Fraction neg(Fraction a) {
		return new Fraction(neg(a.getNum()), a.getDen());
	}

	public static Fraction mul(Fraction a, Fraction b) {
		return new Fraction(mul(a.getNum(), b.getNum()), mul(a.getDen(), b.getDen()));
	}

	public static Fraction div(Fraction a, Fraction b) {
		return new Fraction(mul(a.getNum(), b.getDen()), mul(a.getDen(), b.getNum()));
	}

	public static BigInteger toU256(Fraction a) {
		return div(a.getNum(), a.getDen());
	}

	public static boolean equal(Fraction a, Fraction b) {
		return mul(a.getNum(), b.getDen()).equals(mul(a.getDen(), b.getNum()));
	}

	static Point mul2(Point a) {
		Fraction lambda = div(mul(mul(a.getX(), a.getX()), new Fraction(BigInteger.valueOf(3))), add(a.getY(), a.getY()));
		Fraction x = sub(mul(lambda, lambda), add(a.getX(), a.getX()));
		Fraction y = sub(mul(lambda, sub(a.getX(), x)), a.getY());
		return new Point(x, y);
	}

	public static Point add(Point a, Point b) {
		if (a.isZero()) return b;
		if (b.isZero()) return a;

		if (equal(a.getX(), b.getX())) {
			if (equal(a.getY(), b.getY())) {
				return mul2(a);
			} else {
				return Point.ZERO;
			}
		} else {
			Fraction lambda = div(sub(b.getY(), a.getY()), sub(b.getX(), a.getX()));
			Fraction x = sub(mul(lambda, lambda), add(a.getX(), b.getX()));
			Fraction y = sub(mul(lambda, sub(a.getX(), x)), a.getY());
			return new Point(x, y);
		}
	}

	public static Point mul(Point p, BigInteger n) {
		if (p.isZero() || n.equals(BigInteger.ZERO)) return Point.ZERO;

		int bitLength = n.bitLength();
		Point ret = Point.ZERO, temp = p;
		for (int i = 0; i < bitLength; i++) {
			if (n.testBit(i)) {
				ret = add(ret, temp);
			}
			if (i != bitLength - 1) {
				temp = mul2(temp);
			}
		}
		return ret;
	}


	static final BigInteger G_X = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
	static final BigInteger G_Y = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
	static final Point G = new Point(new Fraction(G_X), new Fraction(G_Y));
	static final Point[] G_TABLE = new Point[256];

	static {
		G_TABLE[0] = G;
		for (int i = 1; i < 256; i++) {
			G_TABLE[i] = mul2(G_TABLE[i - 1]);
		}
	}

	public static Point mulG(BigInteger n) {
		Point r = Point.ZERO;
		for (int i = 0; i < 256; i++) {
			if (n.testBit(i)) {
				r = add(r, G_TABLE[i]);
			}
		}
		return r;
	}

}