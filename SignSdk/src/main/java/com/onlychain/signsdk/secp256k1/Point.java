package com.onlychain.signsdk.secp256k1;

public final class Point {
	public static final Point ZERO = new Point();

	private final Fraction x, y;

	public Fraction getX() {
		return x;
	}

	public Fraction getY() {
		return y;
	}

	private Point() {
		x = null;
		y = null;
	}

	public Point(Fraction x, Fraction y) {
		if (x == null || y == null) throw new NullPointerException();
		this.x = x;
		this.y = y;
	}

	public boolean isZero() {
		return x == null;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", ModP.toString(ModP.toU256(x)), ModP.toString(ModP.toU256(y)));
	}
}
