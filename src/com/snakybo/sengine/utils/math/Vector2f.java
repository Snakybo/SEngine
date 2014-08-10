package com.snakybo.sengine.utils.math;

public class Vector2f {
	public float x;
	public float y;
	
	public Vector2f() {
		this(0.0f);
	}
	
	public Vector2f(float x) {
		this(x, 0.0f);
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f r) {
		set(r);
	}
	
	public float length() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public float max() {
		return Math.max(x, y);
	}
	
	public float dot(Vector2f r) {
		return x * r.x + y * r.y;
	}
	
	public Vector2f normalized() {
		float length = length();
		
		return new Vector2f(x / length, y / length);
	}
	
	public float cross(Vector2f r) {
		return x * r.y - y * r.x;
	}
	
	public Vector2f lerp(Vector2f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}
	
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f((float)(x * cos - y * sin), (float)(x * sin + y * cos));
	}
	
	public Vector2f add(Vector2f r) {
		return new Vector2f(x + r.x, y + r.y);
	}
	
	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}
	
	public Vector2f sub(Vector2f r) {
		return new Vector2f(x - r.x, y - r.y);
	}
	
	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}
	
	public Vector2f mul(Vector2f r) {
		return new Vector2f(x * r.x, y * r.y);
	}
	
	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}
	
	public Vector2f div(Vector2f r) {
		return new Vector2f(x / r.x, y / r.y);
	}
	
	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}
	
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	public String toString() {
		return "(" + x + " " + y + ")";
	}
	
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2f set(Vector2f r) {
		set(r.x, r.y);
		return this;
	}
	
	public boolean equals(Vector2f r) {
		return x == r.x && y == r.y;
	}
}