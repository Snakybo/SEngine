package com.snakybo.sengine.core.utils;


/** Vector 3 math class
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class Vector3f {
	private float x;
	private float y;
	private float z;
	
	/** Constructor for the vector 3
	 * @param x The X component
	 * @param y The Y component
	 * @param z The Z component */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "Vector3f (x=" + x + ", y=" + y + ", z=" + z + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof Vector3f))
			return false;
		
		Vector3f other = (Vector3f)obj;
		
		if(Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		
		if(Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		
		if(Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		
		return result;
	}
	
	/** @return The length of the vector */
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	/** @return The highest component of the vector */
	public float max() {
		return Math.max(x, Math.max(y, z));
	}
	
	/** @return The dot product of this vector and another
	 * @param r The other vector */
	public float dot(Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}
	
	/** @return The cross product of this vector and another
	 * @param r The other vector */
	public Vector3f cross(Vector3f r) {
		float x_ = y * r.getZ() - z * r.getY();
		float y_ = z * r.getX() - x * r.getZ();
		float z_ = x * r.getY() - y * r.getX();
		
		return new Vector3f(x_, y_, z_);
	}
	
	/** @return A new vector that has been normalized */
	public Vector3f normalize() {
		float length = length();
		
		return new Vector3f(x / length, y / length, z / length);
	}
	
	/** Rotate the vector
	 * @param axis The axis to rotate on
	 * @param angle The angle to rotate by
	 * @return A new vector that has been rotated */
	public Vector3f rotate(Vector3f axis, float angle) {
		float sinAngle = (float)Math.sin(-angle);
		float cosAngle = (float)Math.cos(-angle);
		
		return this.cross(axis.mul(sinAngle)).add( // Rotation on local X
				(this.mul(cosAngle)).add( // Rotation on local Z
						axis.mul(this.dot(axis.mul(1 - cosAngle))))); // Rotation on local Y
	}
	
	/** Rotate the vector
	 * @param rotation The quaternion to rotate by
	 * @return A new vector that has been rotated */
	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.mul(this).mul(conjugate);
		
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	/** Linearly interpolate the vector
	 * @param dest The destination
	 * @param lerpFactor The lerp factor
	 * @return A new vector that has been lerped */
	public Vector3f lerp(Vector3f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}
	
	/** Add the specified vector to the vector
	 * @param r The vector to add
	 * @return A new vector with the addition */
	public Vector3f add(Vector3f r) {
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}
	
	/** Add the specified value to the vector
	 * @param r The value to add
	 * @return A new vector with the addition */
	public Vector3f add(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}
	
	/** Subtract the specified vector from the vector
	 * @param r The vector to subtract
	 * @return A new vector with the subtraction */
	public Vector3f sub(Vector3f r) {
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}
	
	/** Subtract the specified value from the vector
	 * @param r The value to subtract
	 * @return A new vector with the subtraction */
	public Vector3f sub(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}
	
	/** Multiply the vector by another
	 * @param r The vector to multiply by
	 * @return A new vector that has been multiplied */
	public Vector3f mul(Vector3f r) {
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}
	
	/** Multiply the vector by the specified value
	 * @param r The value to multiply by
	 * @return A new vector that has been multiplied */
	public Vector3f mul(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}
	
	/** Divide the vector by another
	 * @param r The vector to divide by
	 * @return A new vector that has been divided */
	public Vector3f div(Vector3f r) {
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}
	
	/** Divide the vector by the specified value
	 * @param r The value to divide by
	 * @return A new vector that has been divided */
	public Vector3f div(float r) {
		return new Vector3f(x / r, y / r, z / r);
	}
	
	/** @return The absolute value of the vector */
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	/** @return The X and Y component of the vector */
	public Vector2f getXY() {
		return new Vector2f(x, y);
	}
	
	/** @return The Y and Z component of the vector */
	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}
	
	/** @return The Z and X component of the vector */
	public Vector2f getZX() {
		return new Vector2f(z, x);
	}
	
	/** @return The Y and X component of the vector */
	public Vector2f getYX() {
		return new Vector2f(y, x);
	}
	
	/** @return The Z and Y component of the vector */
	public Vector2f getZY() {
		return new Vector2f(z, y);
	}
	
	/** @return The X and Z component of the vector */
	public Vector2f getXZ() {
		return new Vector2f(x, z);
	}
	
	/** Set the components of the vector
	 * @param x The new X component
	 * @param y The new Y component
	 * @param z The new Z component
	 * @return This vector, used for chaining */
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	/** Set the values of this vector to those of another
	 * @param r The vector to clone
	 * @return This vector, used for chaining */
	public Vector3f set(Vector3f r) {
		set(r.getX(), r.getY(), r.getZ());
		
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
	
	/** Set the Z component of the vector
	 * @param z The new Z component */
	public void setZ(float z) {
		this.z = z;
	}
	
	/** @return The X component of the vector */
	public float getX() {
		return x;
	}
	
	/** @return The Y component of the vector */
	public float getY() {
		return y;
	}
	
	/** @return The Z component of the vector */
	public float getZ() {
		return z;
	}
}
