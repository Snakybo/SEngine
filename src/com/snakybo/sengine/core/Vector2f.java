package com.snakybo.sengine.core;

/** Vector 2 math class
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class Vector2f {
	private float x;
	private float y;
	
	/** Constructor for the vector 2
	 * @param x The X component
	 * @param y The Y component */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Vector2f (x=" + x + ", y=" + y + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof Vector2f))
			return false;
		
		Vector2f other = (Vector2f)obj;
		
		if(Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		
		if(Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		
		return result;
	}
	
	/** @return The length of the vector */
	public float length() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	/** @return The highest component of the vector */
	public float max() {
		return Math.max(x, y);
	}
	
	/** @return The dot product of this vector and another
	 * @param r The other vector */
	public float dot(Vector2f r) {
		return x * r.getX() + y * r.getY();
	}
	
	/** @return The cross product of this vector and another
	 * @param r The other vector */
	public float cross(Vector2f r) {
		return x * r.getY() - y * r.getX();
	}
	
	/** @return A new vector that has been normalized */
	public Vector2f normalize() {
		float length = length();
		
		return new Vector2f(x / length, y / length);
	}
	
	/** Rotate the vector
	 * @param angle The angle to rotate by
	 * @return A new vector that has been rotated */
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f((float)(x * cos - y * sin), (float)(x * sin + y * cos));
	}
	
	/** Linearly interpolate the vector
	 * @param dest The destination
	 * @param lerpFactor The lerp factor
	 * @return A new vector that has been lerped */
	public Vector2f lerp(Vector2f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}
	
	/** Add the specified vector to the vector
	 * @param r The vector to add
	 * @return A new vector with the addition */
	public Vector2f add(Vector2f r) {
		return new Vector2f(x + r.getX(), y + r.getY());
	}
	
	/** Add the specified value to the vector
	 * @param r The value to add
	 * @return A new vector with the addition */
	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}
	
	/** Subtract the specified vector from the vector
	 * @param r The vector to subtract
	 * @return A new vector with the subtraction */
	public Vector2f sub(Vector2f r) {
		return new Vector2f(x - r.getX(), y - r.getY());
	}
	
	/** Subtract the specified value from the vector
	 * @param r The value to subtract
	 * @return A new vector with the subtraction */
	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}
	
	/** Multiply the vector by another
	 * @param r The vector to multiply by
	 * @return A new vector that has been multiplied */
	public Vector2f mul(Vector2f r) {
		return new Vector2f(x * r.getX(), y * r.getY());
	}
	
	/** Multiply the vector by the specified value
	 * @param r The value to multiply by
	 * @return A new vector that has been multiplied */
	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}
	
	/** Divide the vector by another
	 * @param r The vector to divide by
	 * @return A new vector that has been divided */
	public Vector2f div(Vector2f r) {
		return new Vector2f(x / r.getX(), y / r.getY());
	}
	
	/** Divide the vector by the specified value
	 * @param r The value to divide by
	 * @return A new vector that has been divided */
	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}
	
	/** @return The absolute value of the vector */
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	/** Set the components of the vector
	 * @param x The new X component
	 * @param y The new Y component
	 * @return This vector, used for chaining */
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	/** Set the values of this vector to those of another
	 * @param r The vector to clone
	 * @return This vector, used for chaining */
	public Vector2f set(Vector2f r) {
		set(r.getX(), r.getY());
		
		return this;
	}
	
	/** Set the X component of the vector
	 * @param x The new X component */
	public void setX(float x) {
		this.x = x;
	}
	
	/** Set the Y component of the vector
	 * @param y The new Y component */
	public void setY(float y) {
		this.y = y;
	}
	
	/** @return The X component of the vector */
	public float getX() {
		return x;
	}
	
	/** @return The Y component of the vector */
	public float getY() {
		return y;
	}
}
