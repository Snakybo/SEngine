package com.snakybo.engine.core;

import java.io.Serializable;

/** @author Kevin Krol */
public class Vector2f implements Serializable {
	private static final long serialVersionUID = 4754519659037348148L;
	
	/** A vector with the values of 0, 1 */
	public static final Vector2f UP = new Vector2f(0, 1);
	
	/** A vector with the values of 1, 1 */
	public static final Vector2f ONE = new Vector2f(1, 1);
	
	/** A vector with the values of 0, 0 */
	public static final Vector2f ZERO = new Vector2f(0, 0);
	
	/** A vector with the values of 1, 0 */
	public static final Vector2f RIGHT = new Vector2f(1, 0);
	
	public float x;
	public float y;
	
	/** Create an empty vector */
	public Vector2f() {
		this(0, 0);
	}
	
	/** Create a new vector based on another
	 * @param other The vector to copy */
	public Vector2f(Vector2f other) {
		this(other.x, other.y);
	}
	
	/** Create a new vector
	 * @param x The X value of the vector
	 * @param y The Y value of the vector */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	// Geometrical operations //
	
	/** Measure the distance between this vector and the other vector
	 * @param other The point we're measuring to
	 * @return The distance to the other point */
	public float distance(Vector2f other) {
		return (float)Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
	}
	
	/** Measure the squared distance between this vector and the other vector
	 * @param other The point we're measuring to
	 * @return The squared distance to the other point */
	public float distance2(Vector2f other) {
		return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
	}
	
	/** Get the angle between the two vectors
	 * @param other The vector we're measuring with
	 * @return The angle between the vectors */
	public float angle(Vector2f other) {
		return (float)(Math.atan2(other.y - y, other.x - x) / (Math.PI / 180));
	}
	
	/** Dot this vector against another
	 * @param other The vector to dot against
	 * @return The dot product of the two vectors */
	public float dot(Vector2f other) {
		return (x * other.x) + (y * other.y);
	}
	
	/** Check where the two vectors cross
	 * @param other The vector to cross with
	 * @return The cross product of the two vectors */
	public float cross(Vector2f other) {
		return (x * other.y) - (y * other.x);
	}
	
	/** Rotate this vector by the given angle
	 * @param angle The angle to rotate the vector by
	 * @return This vector */
	public Vector2f rotate(float angle) {
		double radians = Math.toRadians(angle);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		x = (float)((x * cos) - (y * sin));
		y = (float)((x * sin) + (y * cos));
		
		return this;
	}
	
	/** Rotate this vector by 90 degrees in the specified direction
	 * @param direction The direction to rotate in, where >= 0 is counter clockwise and < 0 is
	 *        clockwise
	 * @return This vector */
	public Vector2f rotate90(int direction) {
		float x = this.x;
		
		if(direction >= 0) {
			x = -y;
			y = x;
		} else {
			this.x = y;
			y = -x;
		}
		
		return this;
	}
	
	/** Linearly interpolate between the two vectors
	 * @param other The vector to interpolate with
	 * @param lerpFactor The lerp factor
	 * @return This vector */
	public Vector2f lerp(Vector2f other, float lerpFactor) {
		return other.sub(this).scale(lerpFactor).add(this);
	}
	
	/** @return Wheter or not the two vectors have the same direction */
	public boolean hasSameDirection(Vector2f other) {
		return dot(other) > 0;
	}
	
	/** @return Wheter or not the two vectors have the opposite direction */
	public boolean hasOppositeDirection(Vector2f other) {
		return dot(other) < 0;
	}
	
	// Mathematical operations //
	
	/** @return The length of this vector */
	public float length() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	/** @return The squared length of this vector */
	public float length2() {
		return (x * x) + (y * y);
	}
	
	/** Normalize the vector
	 * @return This vector */
	public Vector2f normalize() {
		final float length = length();
		
		if(length != 0) {
			x /= length;
			y /= length;
		}
		
		return this;
	}
	
	/** Limit this vector's length to the given value
	 * @param limit The max length
	 * @return This vector */
	public Vector2f limit(float limit) {
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
	public Vector2f clamp(float min, float max) {
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
	public Vector2f negate() {
		return new Vector2f(-x, -y);
	}
	
	/** Negate this vector without creating a new copy
	 * @return This vector */
	public Vector2f negateLocal() {
		x = -x;
		y = -y;
		
		return this;
	}
	
	/** Create a new vector based on the absolute values of this vector
	 * @return The new vector */
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	/** Get the absolute values of this vector without creating a new vector
	 * @return This vector */
	public Vector2f absLocal() {
		x = Math.abs(x);
		y = Math.abs(y);
		
		return this;
	}
	
	/** @return The greatest value of the vector */
	public float max() {
		return Math.max(x, y);
	}
	
	/** @return The smallest value of the vector */
	public float min() {
		return Math.min(x, y);
	}
	
	/** @param other The vector to base the new vector's values on
	 * @return A new vector based on the largest values from this vector and the other vector */
	public Vector2f max(Vector2f other) {
		return new Vector2f(Math.max(x, other.x), Math.max(y, other.y));
	}
	
	/** @param other The vector to base the new vector's values on
	 * @return A new vector based on the smallest values from this vector and the other vector */
	public Vector2f min(Vector2f other) {
		return new Vector2f(Math.min(x, other.x), Math.min(y, other.y));
	}
	
	/** Create a new vector based on this one with the specified value added to it
	 * @param value The value to adjust the vector by
	 * @return A new vector */
	public Vector2f add(float value) {
		return new Vector2f(x + value, y + value);
	}
	
	/** Adjust this vector by a given value
	 * @param value The value to adjust the vector by
	 * @return This vector */
	public Vector2f addSelf(float value) {
		x += value;
		y += value;
		
		return this;
	}
	
	/** Create a new vector based on this one with the specified vector added to it
	 * @param value The value to adjust the vector by
	 * @return A new vector */
	public Vector2f add(Vector2f other) {
		return new Vector2f(x + other.x, y + other.y);
	}
	
	/** Add a vector to this vector
	 * @param other The vector to add
	 * @return This vector */
	public Vector2f addSelf(Vector2f other) {
		x += other.x;
		y += other.y;
		
		return this;
	}
	
	/** Create a new vector based on this one with the specified value substracted from it
	 * @param value The value to adjust the vector by
	 * @return A new vector */
	public Vector2f sub(float value) {
		return new Vector2f(x - value, y - value);
	}
	
	/** Adjust this vector by a given value
	 * @param value The value to adjust the vector by
	 * @return This vector */
	public Vector2f subSelf(float value) {
		x -= value;
		y -= value;
		
		return this;
	}
	
	/** Create a new vector based on this one with the specified vector substracted from it
	 * @param value The value to adjust the vector by
	 * @return A new vector */
	public Vector2f sub(Vector2f other) {
		return new Vector2f(x - other.x, y - other.y);
	}
	
	/** Adjust this vector by a given vector
	 * @param other The vector to substract
	 * @return This vector */
	public Vector2f subSelf(Vector2f other) {
		x -= other.x;
		y -= other.y;
		
		return this;
	}
	
	/** Create a new vector based on this one multiplied with the specified value
	 * @param value The value to scale by
	 * @return A new vector */
	public Vector2f scale(float value) {
		return new Vector2f(x * value, y * value);
	}
	
	/** Scale this vector by the given value
	 * @param value The value to scale by
	 * @return This vector */
	public Vector2f scaleSelf(float value) {
		x *= value;
		y *= value;
		
		return this;
	}
	
	/** Create a new vector based on this one multiplied with the specified vector
	 * @param value The value to scale by
	 * @return A new vector */
	public Vector2f scale(Vector2f other) {
		return new Vector2f(x * other.x, y * other.y);
	}
	
	/** Scale this vector with another vector
	 * @param other The vector to multiply with
	 * @return This vector */
	public Vector2f scaleSelf(Vector2f other) {
		x *= other.x;
		y *= other.y;
		
		return this;
	}
	
	/** Create a new vector based on this one divided by the specified value
	 * @param value The value to divide with
	 * @return A new vector */
	public Vector2f div(float value) {
		return new Vector2f(x / value, y / value);
	}
	
	/** Divide this vector by the given value
	 * @param value The value to divide with
	 * @return This vector */
	public Vector2f divSelf(float value) {
		x /= value;
		y /= value;
		
		return this;
	}
	
	/** Create a new vector based on this one divided by the specified value
	 * @param value The value to divide with
	 * @return A new vector */
	public Vector2f div(Vector2f other) {
		return new Vector2f(x / other.x, y / other.y);
	}
	
	/** Divide this vector by the given vector
	 * @param other The vector to divide by
	 * @return This vector */
	public Vector2f divSelf(Vector2f other) {
		x /= other.x;
		y /= other.y;
		
		return this;
	}
	
	/** @return Wheter or not the vector is zero */
	public boolean isZero() {
		return (x == 0) && (y == 0);
	}
	
	/** @return Wheter or not the vector is one */
	public boolean isOne() {
		return (x == 1) && (y == 1);
	}
	
	// General operations //
	
	/** Clone this vector
	 * @return A copy of this vector */
	public Vector2f clone() {
		return new Vector2f(this);
	}
	
	/** @see Object#equals(Object) */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		if(obj == this)
			return true;
		
		if(!(obj instanceof Vector2f))
			return false;
		
		Vector2f object = (Vector2f)obj;
		
		return (Float.floatToIntBits(x) == Float.floatToIntBits(object.x))
				&& (Float.floatToIntBits(y) == Float.floatToIntBits(object.y));
	}
	
	/** @see Object#hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		
		return result;
	}
	
	/** @see Object#toString() */
	@Override
	public String toString() {
		return "(" + x + " " + y + ")";
	}
	
	// Setters //
	
	/** Set the values of the vector
	 * @param x The new X value of the vector
	 * @param y The new Y value of the vector
	 * @return This vector */
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	/** Set the values of the vector using another vector
	 * @param other The vector to copy the values of
	 * @return This vector */
	public Vector2f set(Vector2f other) {
		return set(other.x, other.y);
	}
	
	/** Set the X value of the vector
	 * @param x The new X value of the vector
	 * @return This vector */
	public Vector2f setX(float x) {
		this.x = x;
		
		return this;
	}
	
	/** Set the Y value of the vector
	 * @param y The new Y value of the vector
	 * @return This vector */
	public Vector2f setY(float y) {
		this.y = y;
		
		return this;
	}
	
	// Getters //
	
	/** @return A vector with the same direction as this vector */
	public Vector2f getNormal() {
		return new Vector2f(this).normalize();
	}
	
	/** @return The X value of the vector */
	public float getX() {
		return x;
	}
	
	/** @return The Y value of the vector */
	public float getY() {
		return y;
	}
}