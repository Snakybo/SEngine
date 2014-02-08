package com.snakybo.engine.core;

public class Quaternion {
	public static final Quaternion IDENTITY = new Quaternion();
	
	private float x;
	private float y;
	private float z;
	private float w;
	
	/** Create a quaternion */
	public Quaternion() {
		this(0, 0, 0, 0);
	}
	
	/** Create a quaternion */
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/** @return The length of the quaternion */
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	/** Conjugate the Quaternion */
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	
	/** Normalize the Quaternion */
	public Quaternion normalize() {
		float length = length();
		
		return new Quaternion(x / length, y / length, z / length, w / length);
	}
	
	/** Multiply the quaternion
	 * @param r The quaternion to multiply with */
	public Quaternion mul(Quaternion r) {
		float _w = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float _x = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float _y = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float _z = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(_x, _y, _z, _w);
	}
	
	/** Multiply the quaternion
	 * @param r The vector to multiply with */
	public Quaternion mul(Vector3f r) {
		float _w = -x * r.getX() - y * r.getY() - z * r.getZ();
		float _x =  w * r.getX() + y * r.getZ() - z * r.getY();
		float _y =  w * r.getY() + z * r.getX() - x * r.getZ();
		float _z =  w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(_x, _y, _z, _w);
	}
	
	public String toString() { return "(" + x + " " + y + " " + z + " " + w + ")"; }
	
	public void setX(float x) { this.x = x; }
	public void setY(float y) {	this.y = y; }
	public void setZ(float z) { this.z = z; }
	public void setW(float w) { this.w = w; }
	
	public float getX() { return x; }
	public float getY() { return y; }
	public float getZ() { return z; }
	public float getW() { return w; }
}