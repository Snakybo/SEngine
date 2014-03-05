package com.snakybo.engine.core;

public class Vector3f {
	public static final Vector3f UP = new Vector3f(0, 1, 0);
	public static final Vector3f ONE = new Vector3f(1, 1, 1);
	public static final Vector3f ZERO = new Vector3f(0, 0, 0);
	public static final Vector3f RIGHT = new Vector3f(1, 0, 0);
	public static final Vector3f FORWARD = new Vector3f(0, 0, 1);
	
	private float x;
	private float y;
	private float z;
	
	public Vector3f() {
		this(0, 0, 0);
	}
	
	/** Create a vector */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/** Normalize the vector */
	public Vector3f normalize() {
		return new Vector3f(x / length(), y / length(), z / length());
	}
	
	/** Rotate the vector on the specified axis */
	public Vector3f rotate(Vector3f axis, float angle) {
		float sinAngle = (float)Math.sin(-angle);
		float cosAngle = (float)Math.cos(-angle);
		
		return cross(axis.mul(sinAngle)).add(mul(cosAngle)).add(axis.mul(dot(axis.mul(1 - cosAngle))));
	}
	
	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.mul(this).mul(conjugate);
		
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	/** Linearly interpolate between the vectors */
	public Vector3f lerp(Vector3f r, float lerpFactor) {
		return r.sub(this).mul(lerpFactor).add(this);
	}
	
	/** Scale the vector */
	public Vector3f scale(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}
	
	/** @return The length of the vector */
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	/** @return The dot product of two vectors */
	public float dot(Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}
	
	public float max() {
		return Math.max(x, Math.max(y,  z));
	}
	
	/** @return The cross product of two vectors */
	public Vector3f cross(Vector3f r) {
		float _x = y * r.getZ() - z * r.getY();
		float _y = z * r.getX() - x * r.getZ();
		float _z = x * r.getY() - y * r.getX();
		
		return new Vector3f(_x, _y, _z);
	}
	
	/** @return The angle between two vectors */
	public float angle(Vector3f r) {
		float dls = dot(r) / (length() * r.length());
		
		if (dls < -1.0f) {
			dls = -1.0f;
		} else if (dls > 1.0f) {
			dls = 1.0f;
		}
		
		return (float)Math.acos(dls);
	}
	
	/** @return The distance between two vectors */
	public float distance(Vector3f r) {
		return (float)(Math.sqrt((x - r.getX()) * (x - r.getX()) + (y - r.getY()) * (y - r.getY()) + (z - r.getZ()) * (z - r.getZ())));
	}
	
	/** @return The absolute value of the vector */
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}	
	
	public Vector3f add(Vector3f r) { return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ()); }
	public Vector3f add(float r) { return new Vector3f(x + r, y + r, z + r); }
	
	public Vector3f sub(Vector3f r) { return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ()); }
	public Vector3f sub(float r) { return new Vector3f(x - r, y - r, z - r); }
	
	public Vector3f mul(Vector3f r) { return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ()); }
	public Vector3f mul(float r) { return new Vector3f(x * r, y * r, z * r); }
	
	public Vector3f div(Vector3f r) { return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ()); }
	public Vector3f div(float r) { return new Vector3f(x / r, y / r, z / r); }
	
	public String toString() { return "(" + x + " " + y + " " + z + ")"; }
	public boolean equals(Vector3f r) { return (x == r.getX()) && (y == r.getY()) && (z == r.getZ()); }
	public Vector3f clone() { return new Vector3f(x, y, z); }
	
	public Vector3f set(Vector3f r) { return set(r.getX(), r.getY(), r.getZ()); }
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public void setX(float x) {	this.x = x; }
	public void setY(float y) {	this.y = y; }
	public void setZ(float z) {	this.z = z; }
	
	public Vector2f getXY() { return new Vector2f(x, y); }
	public Vector2f getYZ() { return new Vector2f(y, z); }
	public Vector2f getZX() { return new Vector2f(z, x); }
	
	public Vector2f getYX() { return new Vector2f(y, x); }
	public Vector2f getZY() { return new Vector2f(z, y); }
	public Vector2f getXZ() { return new Vector2f(x, z); }
	
	public float getX() { return x; }
	public float getY() { return y; }
	public float getZ() { return z; }
	
	/** @return The smallest value of both vectors */
	public static Vector3f min(Vector3f r1, Vector3f r2) {
		Vector3f result = ZERO;
		
		result.x = (r1.getX() < r2.getX()) ? r1.getX() : r2.getX();
		result.y = (r1.getY() < r2.getY()) ? r1.getY() : r2.getY();
		result.z = (r1.getZ() < r2.getZ()) ? r1.getZ() : r2.getZ();
		
		return result;
	}
	
	/** @return The largest value of both vectors */
	public static Vector3f max(Vector3f r1, Vector3f r2) {
		Vector3f result = ZERO;
		
		result.x = (r1.getX() > r2.getX()) ? r1.getX() : r2.getX();
		result.y = (r1.getY() > r2.getY()) ? r1.getY() : r2.getY();
		result.z = (r1.getZ() > r2.getZ()) ? r1.getZ() : r2.getZ();
		
		return result;
	}
}