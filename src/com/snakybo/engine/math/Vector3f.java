package com.snakybo.engine.math;

import java.io.Serializable;

/** @author Kevin Krol */
public class Vector3f implements Serializable {
	private static final long serialVersionUID = 3821982106955994484L;
		
	public float x;
	public float y;
	public float z;
	
	/** Create an empty vector */
	public Vector3f() {
		this(0, 0, 0);
	}
	
	/** Create a new vector based on another
	 * @param other The vector to copy */
	public Vector3f(Vector3f other) {
		this(other.x, other.y, other.z);
	}
	
	/** Create a new vector
	 * @param x The X value of the vector
	 * @param y The Y value of the vector
	 * @param z The Z value of the vector */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	// Geometrical operations //
	
	/** Measure the distance between this vector and the other vector
	 * @param other The point we're measuring to
	 * @return The distance to the other point */
	public float distance(Vector3f other) {
		return (float)(Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y)
				+ (z - other.z) * (z - other.z)));
	}
	
	/** Measure the squared distance between this vector and the other vector
	 * @param other The point we're measuring to
	 * @return The squared distance to the other point */
	public float distance2(Vector3f other) {
		return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z)
				* (z - other.z);
	}
	
	/** Get the angle between the two vectors
	 * @param other The vector we're measuring with
	 * @return The angle between the vectors */
	public float angle(Vector3f other) {
		float dls = dot(other) / (length() * other.length());
		
		if(dls < -1.0f) {
			dls = -1.0f;
		} else if(dls > 1.0f) {
			dls = 1.0f;
		}
		
		return (float)Math.acos(dls);
	}
	
	/** Dot this vector against another
	 * @param other The vector to dot against
	 * @return The dot product of the two vectors */
	public float dot(Vector3f other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	/** Check where the two vectors cross
	 * @param other The vector to cross with
	 * @return This vector */
	public Vector3f cross(Vector3f other) {
		float _x = y * other.z - z * other.y;
		float _y = z * other.x - x * other.z;
		float _z = x * other.y - y * other.x;
		
		set(_x, _y, _z);
		
		return this;
	}
	
	/** Rotate this vector by the given angle
	 * @param angle The angle to rotate the vector by
	 * @return This vector */
	public Vector3f rotate(Vector3f axis, float angle) {
		float sinAngle = (float)Math.sin(-angle);
		float cosAngle = (float)Math.cos(-angle);
		
		return cross(axis.scale(sinAngle)).add(scale(cosAngle)).add(
				axis.scale(dot(axis.scale(1 - cosAngle))));
	}
	
	/** Rotate this vector by the given quaternion
	 * @param roatation The quaternion to rotate the vector by
	 * @return This vector */
	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();
		Quaternion w = rotation.scale(this).mul(conjugate);
		
		set(w.x, w.y, w.z);
		
		return this;
	}
	
	/** Linearly interpolate between the two vectors
	 * @param other The vector to interpolate with
	 * @param lerpFactor The lerp factor
	 * @return This vector */
	public Vector3f lerp(Vector3f other, float lerpFactor) {
		return other.sub(this).scale(lerpFactor).add(this);
	}
	
	/** Spherically interpolate between the two vectors
	 * @param other The vector to interpolate with
	 * @param slerpFactor The slerp factor
	 * @return This vector */
	public Vector3f slerp(Vector3f other, float slerpFactor) {
		final float dot = dot(other);
		
		if(dot > 0.9995f || dot < -0.9995f)
			return lerp(other, slerpFactor);
		
		final float theta0 = (float)Math.acos(dot);
		final float theta = theta0 * slerpFactor;
		
		final float sT = (float)Math.sin(theta);
		final float tX = other.x - x * dot;
		final float tY = other.y - y * dot;
		final float tZ = other.z - z * dot;
		
		final float l2 = (tX * tX) + (tY * tY) + (tZ * tZ);
		final float d1 = sT * ((l2 < 0.0001f) ? 1f : 1f / (float)Math.sqrt(12));
		
		return scale((float)Math.cos(theta)).add(new Vector3f(tX * d1, tY * d1, tZ * d1))
				.normalize();
	}
	
	/** @return Wheter or not the two vectors have the same direction */
	public boolean hasSameDirection(Vector3f other) {
		return dot(other) > 0;
	}
	
	/** @return Wheter or not the two vectors have the opposite direction */
	public boolean hasOppositeDirection(Vector3f other) {
		return dot(other) < 0;
	}
	
	// Mathematical operations //
	
	/** @return The length of this vector */
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	/** @return The squared length of this vector */
	public float length2() {
		return (x * x) + (y * y) + (z * z);
	}
	
	/** Normalize the vector
	 * @return This vector */
	public Vector3f normalize() {
		final float length = length();
		
		if(length != 0) {
			x /= length;
			y /= length;
			z /= length;
		}
		
		return this;
	}
	
	/** Limit this vector's length to the given value
	 * @param limit The max length
	 * @return This vector */
	public Vector3f limit(float limit) {
		if(length2() > limit * limit) {
			normalize();
			scale(limit);
		}
		
		return this;
	}
	
	/** Clamp this vector's lenght to the given value
	 * @param min The min length of the vector
	 * @param max The max length of the vector
	 * @return This vector */
	public Vector3f clamp(float min, float max) {
		final float length = length2();
		
		if(length == 0)
			return this;
		
		if(length > max * max)
			return normalize().scale(max);
		
		if(length < min * min)
			return normalize().scale(min);
		
		return this;
	}
	
	/** @return A copy of this vector negated */
	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}
	
	/** Negate this vector without creating a new copy
	 * @return This vector */
	public Vector3f negateSelf() {
		x = -x;
		y = -y;
		z = -z;
		
		return this;
	}
	
	/** Create a new vector based on the absolute values of this vector
	 * @return The new vector */
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	/** Get the absolute values of this vector without creating a new vector
	 * @return This vector */
	public Vector3f absSelf() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		
		return this;
	}
	
	/** @return The greatest value of the vector */
	public float max() {
		return Math.max(x, Math.max(y, z));
	}
	
	/** @return The smallest value of the vector */
	public float min() {
		return Math.min(x, Math.min(y, z));
	}
	
	/** @param other The vector to base the new vector's values on
	 * @return A new vector based on the largest values from this vector and the other vector */
	public Vector3f max(Vector3f other) {
		return new Vector3f(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z));
	}
	
	/** @param other The vector to base the new vector's values on
	 * @return A new vector based on the smallest values from this vector and the other vector */
	public Vector3f min(Vector3f other) {
		return new Vector3f(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z));
	}
	
	/** Create a new vector based on this one with the specified value added to it
	 * @param value The value to adjust the vector by
	 * @return A new vector */
	public Vector3f add(float value) {
		return new Vector3f(x + value, y + value, z + value);
	}
	
	/** Adjust this vector by a given value
	 * @param value The value to adjust the vector by
	 * @return This vector */
	public Vector3f addSelf(float value) {
		x += value;
		y += value;
		z += value;
		
		return this;
	}
	
	/** Create a new vector based on this one with the specified vector added to it
	 * @param value The value to adjust the vector by
	 * @return A new vector */
	public Vector3f add(Vector3f other) {
		return new Vector3f(x + other.x, y + other.y, z + other.z);
	}
	
	/** Add a vector to this vector
	 * @param other The vector to add
	 * @return This vector */
	public Vector3f addSelf(Vector3f other) {
		x += other.x;
		y += other.y;
		z += other.z;
		
		return this;
	}
	
	/** Create a new vector based on this one with the specified value substracted from it
	 * @param value The value to adjust the vector by
	 * @return A new vector */
	public Vector3f sub(float value) {
		return new Vector3f(x - value, y - value, z - value);
	}
	
	/** Adjust this vector by a given value
	 * @param value The value to adjust the vector by
	 * @return This vector */
	public Vector3f subSelf(float value) {
		x -= value;
		y -= value;
		z -= value;
		
		return this;
	}
	
	/** Create a new vector based on this one with the specified vector substracted from it
	 * @param value The value to adjust the vector by
	 * @return A new vector */
	public Vector3f sub(Vector3f other) {
		return new Vector3f(x - other.x, y - other.y, z - other.z);
	}
	
	/** Adjust this vector by a given vector
	 * @param other The vector to substract
	 * @return This vector */
	public Vector3f subSelf(Vector3f other) {
		x -= other.x;
		y -= other.y;
		z -= other.z;
		
		return this;
	}
	
	/** Create a new vector based on this one multiplied with the specified value
	 * @param value The value to scale by
	 * @return A new vector */
	public Vector3f scale(float value) {
		return new Vector3f(x * value, y * value, z * value);
	}
	
	/** Scale this vector by the given value
	 * @param value The value to scale by
	 * @return This vector */
	public Vector3f scaleSelf(float value) {
		x *= value;
		y *= value;
		z *= value;
		
		return this;
	}
	
	/** Create a new vector based on this one multiplied with the specified vector
	 * @param value The value to scale by
	 * @return A new vector */
	public Vector3f scale(Vector3f other) {
		return new Vector3f(x * other.x, y * other.y, z * other.z);
	}
	
	/** Scale this vector with another vector
	 * @param other The vector to multiply with
	 * @return This vector */
	public Vector3f scaleSelf(Vector3f other) {
		x *= other.x;
		y *= other.y;
		z *= other.z;
		
		return this;
	}
	
	/** Create a new vector based on this one divided by the specified value
	 * @param value The value to divide with
	 * @return A new vector */
	public Vector3f div(float value) {
		return new Vector3f(x / value, y / value, z / value);
	}
	
	/** Divide this vector by the given value
	 * @param value The value to divide with
	 * @return This vector */
	public Vector3f divSelf(float value) {
		x /= value;
		y /= value;
		z /= value;
		
		return this;
	}
	
	/** Create a new vector based on this one divided by the specified value
	 * @param value The value to divide with
	 * @return A new vector */
	public Vector3f div(Vector3f other) {
		return new Vector3f(x / other.x, y / other.y, z / other.z);
	}
	
	/** Divide this vector by the given vector
	 * @param other The vector to divide by
	 * @return This vector */
	public Vector3f divSelf(Vector3f other) {
		x /= other.x;
		y /= other.y;
		z /= other.z;
		
		return this;
	}
	
	/** @return Wheter or not the vector is zero */
	public boolean isZero() {
		return (x == 0) && (y == 0) && (z == 0);
	}
	
	/** @return Wheter or not the vector is one */
	public boolean isOne() {
		return (x == 1) && (y == 1) && (z == 1);
	}
	
	// General operations //
	
	/** Clone this vector
	 * @return A copy of this vector */
	public Vector3f clone() {
		return new Vector3f(this);
	}
	
	/** @see Object#equals(Object) */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		if(obj == this)
			return true;
		
		if(!(obj instanceof Vector3f))
			return false;
		
		Vector3f object = (Vector3f)obj;
		
		return (Float.floatToIntBits(x) == Float.floatToIntBits(object.x))
				&& (Float.floatToIntBits(y) == Float.floatToIntBits(object.y))
				&& (Float.floatToIntBits(z) == Float.floatToIntBits(object.z));
	}
	
	/** @see Object#hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		
		return result;
	}
	
	/** @see Object#toString() */
	@Override
	public String toString() {
		return "(" + x + " " + y + " " + z + ")";
	}
	
	// Setters //
	
	/** Set the values of the vector
	 * @param x The new X value of the vector
	 * @param y The new Y value of the vector
	 * @param z The new Z value of the vector
	 * @return This vector */
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	/** Set the values of the vector using another vector
	 * @param other The vector to copy the values of
	 * @return This vector */
	public Vector3f set(Vector3f other) {
		return set(other.x, other.y, other.z);
	}
	
	/** Set the X value of the vector
	 * @param x The new X value of the vector
	 * @return This vector */
	public Vector3f setX(float x) {
		this.x = x;
		
		return this;
	}
	
	/** Set the Y value of the vector
	 * @param y The new Y value of the vector
	 * @return This vector */
	public Vector3f setY(float y) {
		this.y = y;
		
		return this;
	}
	
	/** Set the Z value of the vector
	 * @param z The new Z value of the vector
	 * @return This vector */
	public Vector3f setZ(float z) {
		this.z = z;
		
		return this;
	}
	
	// Getters //
	
	/** @return A vector with the same direction as this vector */
	public Vector3f getNormal() {
		return new Vector3f(this).normalize();
	}
	
	/** @return The X and Y values of this vector */
	public Vector2f getXY() {
		return new Vector2f(x, y);
	}
	
	/** @return The Y and Z values of this vector */
	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}
	
	/** @return The Z and X values of this vector */
	public Vector2f getZX() {
		return new Vector2f(z, x);
	}
	
	/** @return The Y and X values of this vector */
	public Vector2f getYX() {
		return new Vector2f(y, x);
	}
	
	/** @return The Z and Y values of this vector */
	public Vector2f getZY() {
		return new Vector2f(z, y);
	}
	
	/** @return The X and Z values of this vector */
	public Vector2f getXZ() {
		return new Vector2f(x, z);
	}
	
	/** @return The X value of the vector */
	public float getX() {
		return x;
	}
	
	/** @return The Y value of the vector */
	public float getY() {
		return y;
	}
	
	/** @return The Z value of the vector */
	public float getZ() {
		return z;
	}
}