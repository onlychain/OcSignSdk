package com.onlychain.signsdk.secp256k1;

import java.math.BigInteger;

public final class Fraction {
	public static final Fraction ZERO = new Fraction(BigInteger.ZERO);

	private final BigInteger num, den;

	public BigInteger getNum() {
		return num;
	}

	public BigInteger getDen() {
		return den;
	}

	public Fraction(BigInteger num, BigInteger den) {
		if (den.equals(BigInteger.ZERO)) throw new ArithmeticException("分母不能是0");
		this.num = num;
		this.den = den;
	}

	public Fraction(BigInteger num) {
		this(num, BigInteger.ONE);
	}

	public boolean isZero() {
		return num.equals(BigInteger.ZERO);
	}
}
