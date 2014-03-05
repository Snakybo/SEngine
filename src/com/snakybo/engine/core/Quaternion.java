package com.snakybo.engine.core;

import java.io.Serializable;

/** @author Kevin Krol */
public class Quaternion implements Serializable {
	private static final long serialVersionUID = 1006541201686498617L;
	
	/** A quaternion with the values of 0, 0, 0, 1 */
	public static final Quaternion IDENTITY = new Quaternion(0, 0, 0, 1);
	
	public float x;
	public float y;
	public float z;
	public float w;
	
	/** Create an empty quaternion */
	public Quaternion() {
		this(0, 0, 0, 1);
	}
	
	/** Create a new quaternion based on another
	 * @param other The quaternion to copy */
	public Quaternion(Quaternion other) {
		this(other.x, other.y, other.z, other.w);
	}
	
	/** Create a quaternion */
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/** Create a new quaternion
	 * @param x The X value of the quaternion
	 * @param y The Y value of the quaternion
	 * @param z The Z value of the quaternion
	 * @param w The W value of the quaternion */
	public Quaternion(Vector3f axis, float angle) {
		float sinHalfAngle = (float)Math.sin(angle / 2);
		float cosHalfAngle = (float)Math.cos(angle / 2);
		
		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = cosHalfAngle;
	}
	
	// Geometrical operations //
	
	/** Dot this quaternion against another
	 * @param other The quaternion to dot against
	 * @return The dot product of the two quaternions */
	public float dot(Quaternion other) {
		return (x * other.x) + (y * other.y) + (z * other.z) + (w * other.w);
	}
	
	/** Spherically interpolate between the two quaternions
	 * @param other The quaternion to interpolate with
	 * @param slerpFactor The slerp factor
	 * @return This quaternion */
	public Quaternion slerp(Quaternion other, float slerpFactor) {
		final float dot = dot(other);
		float absDot = dot < 0.f ? -dot : dot;
		
		float scale0 = 1 - slerpFactor;
		float scale1 = slerpFactor;
		
		if((1 - absDot) > 0.1) {
			final double angle = Math.acos(absDot);
			final double invSinTheta = 1f / Math.sin(angle);
			
			scale0 = (float)(Math.sin((1 - slerpFactor) * angle) * invSinTheta);
			scale1 = (float)(Math.sin((slerpFactor * angle)) * invSinTheta);
		}
		
		if(dot < 0.f)
			scale1 = -scale1;
		
		x = (scale0 * x) + (scale1 * other.x);
		y = (scale0 * y) + (scale1 * other.y);
		z = (scale0 * z) + (scale1 * other.z);
		w = (scale0 * w) + (scale1 * other.w);
		
		return this;
	}
	
	// Mathematical operations //
	
	/** @return The length of this quaternion */
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	/** @return The squared length of this quaternion */
	public float length2() {
		return (x * x) + (y * y) + (z * z) + (w * w);
	}
	
	/** Conjugate the quaternion
	 * @return This quaternion */
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
		
		/* x = -x; y = -y; z = -z;
		 * 
		 * return this; */
	}
	
	/** Normalize the quaternion
	 * @return This quaternion */
	public Quaternion normalize() {
		float length = length2();
		
		if(length != 0) {
			length = (float)Math.sqrt(length);
			
			x /= length;
			y /= length;
			z /= length;
			w /= length;
		}
		
		return this;
	}
	
	/** Create a new quaternion based on this one that has been multiplied by the specified
	 * quaternion
	 * @param r The quaternion to multiply with
	 * @return A new quaternion */
	public Quaternion mul(Quaternion other) {
		final float _w = w * other.w - x * other.x - y * other.y - z * other.z;
		final float _x = x * other.w + w * other.x + y * other.z - z * other.y;
		final float _y = y * other.w + w * other.y + z * other.x - x * other.z;
		final float _z = z * other.w + w * other.z + x * other.y - y * other.x;
		
		return new Quaternion(_x, _y, _z, _w);
	}
	
	/** Multiply this quaternion by another one
	 * @param other The quaternion to multiply by
	 * @return This quaternion */
	public Quaternion mulSelf(Quaternion other) {
		final float _w = w * other.w - x * other.x - y * other.y - z * other.z;
		final float _x = x * other.w + w * other.x + y * other.z - z * other.y;
		final float _y = y * other.w + w * other.y + z * other.x - x * other.z;
		final float _z = z * other.w + w * other.z + x * other.y - y * other.x;
		
		x = _x;
		y = _y;
		z = _z;
		w = _w;
		
		return this;
	}
	
	/** Create a new quaternion that has been scaled by the specified vector
	 * @param r The vector to multiply with */
	public Quaternion scale(Vector3f r) {
		final float _w = -x * r.getX() - y * r.getY() - z * r.getZ();
		final float _x = w * r.getX() + y * r.getZ() - z * r.getY();
		final float _y = w * r.getY() + z * r.getX() - x * r.getZ();
		final float _z = w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(_x, _y, _z, _w);
	}
	
	/** Multiply this quaternion by a vector
	 * @param other The vector to multiply by
	 * @return This quaternion */
	public Quaternion scaleSelf(Vector3f other) {
		final float _w = -x * other.x - y * other.y - z * other.z;
		final float _x = w * other.x + y * other.z - z * other.y;
		final float _y = w * other.y + z * other.x - x * other.z;
		final float _z = w * other.z + x * other.y - y * other.x;
		
		x = _x;
		y = _y;
		z = _z;
		w = _w;
		
		return this;
	}
	
	/** Create a new quaternion that has been scaled by the specified value
	 * @param value The value to scale by
	 * @return A new quaternion */
	public Quaternion scale(float value) {
		return new Quaternion(x * value, y * value, z * value, w * value);
	}
	
	/** Scale this quaternion by the given value
	 * @param value The value to scale by
	 * @return This quaternion */
	public Quaternion scaleSelf(float value) {
		x *= value;
		y *= value;
		z *= value;
		w *= value;
		
		return this;
	}
	
	// General operations //
	
	/** Clone this quaternion
	 * @return A copy of this quaternion */
	public Quaternion clone() {
		return new Quaternion(this);
	}
	
	/** @see Object#equals(Object) */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		if(obj == this)
			return true;
		
		if(!(obj instanceof Quaternion))
			return false;
		
		Quaternion object = (Quaternion)obj;
		
		return (Float.floatToIntBits(x) == Float.floatToIntBits(object.x))
				&& (Float.floatToIntBits(y) == Float.floatToIntBits(object.y))
				&& (Float.floatToIntBits(z) == Float.floatToIntBits(object.z))
				&& (Float.floatToIntBits(w) == Float.floatToIntBits(object.w));
	}
	
	/** @see Object#hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		
		return result;
	}
	
	/** @see Object#toString() */
	@Override
	public String toString() {
		return "(" + x + " " + y + " " + z + " " + w + ")";
	}
	
	// Setters //
	
	/** Set the values of the vector
	 * @param x The X value of the quaternion
	 * @param y The Y value of the quaternion
	 * @param z The Z value of the quaternion
	 * @param w The W value of the quaternion
	 * @return This quaternion */
	public Quaternion set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		
		return this;
	}
	
	/** Set the values of the quaternion using another quaternion
	 * @param other The quaternion to copy the values of
	 * @return This quaternion */
	public Quaternion set(Quaternion other) {
		return set(other.x, other.y, other.z, other.w);
	}
	
	/** Set the values of the quaternion from an axis and an angle
	 * @param axis The axis
	 * @param angle The angle
	 * @return This quaternion */
	public Quaternion set(Vector3f axis, float angle) {
		final float sinHalfAngle = (float)Math.sin(angle / 2);
		final float cosHalfAngle = (float)Math.cos(angle / 2);
		
		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = cosHalfAngle;
		
		return this;
	}
	
	/** Set the X value of the quaternion
	 * @param x The new X value of the quaternion
	 * @return This quaternion */
	public Quaternion setX(float x) {
		this.x = x;
		
		return this;
	}
	
	/** Set the Y value of the quaternion
	 * @param y The new Y value of the quaternion
	 * @return This quaternion */
	public Quaternion setY(float y) {
		this.y = y;
		
		return this;
	}
	
	/** Set the Z value of the quaternion
	 * @param z The new Z value of the quaternion
	 * @return This quaternion */
	public Quaternion setZ(float z) {
		this.z = z;
		
		return this;
	}
	
	/** Set the W value of the quaternion
	 * @param w The new W value of the quaternion
	 * @return This quaternion */
	public Quaternion setW(float w) {
		this.w = w;
		
		return this;
	}
	
	/** Convert the quaternion to a rotation matrix
	 * @return A new matrix of the quaternion's rotation */
	public Matrix4f toRotationMatrix() {
		Vector3f forward =
				new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x),
						1.0f - 2.0f * (x * x + y * y));
		Vector3f up =
				new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z),
						2.0f * (y * z - w * x));
		Vector3f right =
				new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z),
						2.0f * (x * z + w * y));
		
		return new Matrix4f().initRotation(forward, up, right);
	}
	
	/** @return The front side of the quaternion */
	public Vector3f front() {
		return new Vector3f(0, 0, 1).rotate(this);
	}
	
	/** @return The back side of the quaternion */
	public Vector3f back() {
		return new Vector3f(0, 0, -1).rotate(this);
	}
	
	/** @return The top side of the quaternion */
	public Vector3f up() {
		return new Vector3f(0, 1, 0).rotate(this);
	}
	
	/** @return The bottom side of the quaternion */
	public Vector3f down() {
		return new Vector3f(0, -1, 0).rotate(this);
	}
	
	/** @return The right side of the quaternion */
	public Vector3f right() {
		return new Vector3f(1, 0, 0).rotate(this);
	}
	
	/** @return The left side of the quaternion */
	public Vector3f left() {
		return new Vector3f(-1, 0, 0).rotate(this);
	}
	
	/** @return The X value of the quaternion */
	public float getX() {
		return x;
	}
	
	/** @return The Y value of the quaternion */
	public float getY() {
		return y;
	}
	
	/** @return The Z value of the quaternion */
	public float getZ() {
		return z;
	}
	
	/** @return The w value of the quaternion */
	public float getW() {
		return w;
	}
}