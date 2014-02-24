package com.snakybo.engine.core;

public class Vector2f {
	public static final Vector2f UP = new Vector2f(0, 1);
	public static final Vector2f ONE = new Vector2f(1, 1);
	public static final Vector2f ZERO = new Vector2f(0, 0);
	public static final Vector2f RIGHT = new Vector2f(1, 0);
	
	private float x;
	private float y;
	
	/** Create a vector */
	public Vector2f() {
		this(0, 0);
	}
	
	/** Create a vector */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/** Normalize the vector */
	public Vector2f normalize() {
		return new Vector2f(x / length(), y / length());
	}
	
	/** Rotate the vector */
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f((float)(x * cos - y * sin),(float)(x * sin + y * cos));
	}
	
	/** Scale the vector */
	public Vector2f scale(float r) {
		return new Vector2f(x * r, y * r);
	}
	
	/** Linearly interpolate between the vectors */
	public Vector2f lerp(Vector2f r, float lerpFactor) {
		return r.sub(this).mul(lerpFactor).add(this);
	}

	/** @return The length of the vector */
	public float length() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	/** @return The dot product of two vectors */
	public float dot(Vector2f r) {
		return x * r.getX() + y * r.getY();
	}
	
	public float max() {
		return Math.max(x, y);
	}
	
	/** @return The cross product of two vectors */
	public float cross(Vector2f r) {
		return x * r.getX() - y * r.getY();
	}
	
	/** @return The angle between two vectors */
	public float angle(Vector2f r) {
		return (float)(Math.atan2(r.getY() - y, r.getX() - x) / (Math.PI / 180));
	}
	
	/** @return The distance between two vectors */
	public float distance(Vector2f r) {
		return (float)Math.sqrt((x - r.getX()) * (x - r.getX()) + (y - r.getY()) * (y - r.getY()));
	}
	
	/** @return The absolute value of the vector */
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	public Vector2f add(Vector2f r) { return new Vector2f(x + r.getX(), y + r.getY()); }
	public Vector2f add(float r) { return new Vector2f(x + r, y + r); }
	
	public Vector2f sub(Vector2f r) { return new Vector2f(x - r.getX(), y - r.getY()); }
	public Vector2f sub(float r) { return new Vector2f(x - r, y - r); }
	
	public Vector2f mul(Vector2f r) { return new Vector2f(x * r.getX(), y * r.getY()); }
	public Vector2f mul(float r) { return new Vector2f(x * r, y * r); }
	
	public Vector2f div(Vector2f r) { return new Vector2f(x / r.getX(), y / r.getY()); }
	public Vector2f div(float r) { return new Vector2f(x / r, y / r); }
	
	public String toString() { return "(" + x + " " + y + ")"; }
	public boolean equals(Vector2f r) {	return (x == r.getX()) && (y == r.getY()); }
	
	public void setX(float x) {	this.x = x; }
	public void setY(float y) {	this.y = y; }
	
	public float getX() { return x; }
	public float getY() { return y; }
	
	/** @return The smallest value of both vectors */
	public static Vector2f min(Vector2f r1, Vector2f r2) {
		Vector2f result = ZERO;
		
		result.x = (r1.getX() < r2.getX()) ? r1.getX() : r2.getX();
		result.y = (r1.getY() < r2.getY()) ? r1.getY() : r2.getY();
		
		return result;
	}
	
	/** @return The largest value of both vectors */
	public static Vector2f max(Vector2f r1, Vector2f r2) {
		Vector2f result = ZERO;
		
		result.x = (r1.getX() > r2.getX()) ? r1.getX() : r2.getX();
		result.y = (r1.getY() > r2.getY()) ? r1.getY() : r2.getY();
		
		return result;
	}
}
