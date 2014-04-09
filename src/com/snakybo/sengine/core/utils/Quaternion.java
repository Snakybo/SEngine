package com.snakybo.sengine.core.utils;


/** Quaternion math class
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class Quaternion {
	private float x;
	private float y;
	private float z;
	private float w;
	
	/** Constructor for the quaternion
	 * @param x The X component
	 * @param y The Y component
	 * @param z The Z component
	 * @param w The W component */
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/** Constructor for the quaternion
	 * 
	 * <p>
	 * Initialize the quaternion with a rotation
	 * </p>
	 * 
	 * @param axis The axis of rotation
	 * @param angle The angle of rotation */
	public Quaternion(Vector3f axis, float angle) {
		float sinHalfAngle = (float)Math.sin(angle / 2);
		float cosHalfAngle = (float)Math.cos(angle / 2);
		
		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = cosHalfAngle;
	}
	
	/** Constructor for the quaternion
	 * 
	 * <p>
	 * Initialize the quaternion with a matrix 4
	 * </p>
	 * 
	 * @param rot The matrix 4 to initialize the quaternion with */
	public Quaternion(Matrix4f rot) {
		float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);
		
		if(trace > 0) {
			float s = 0.5f / (float)Math.sqrt(trace + 1.0f);
			
			w = 0.25f / s;
			x = (rot.get(1, 2) - rot.get(2, 1)) * s;
			y = (rot.get(2, 0) - rot.get(0, 2)) * s;
			z = (rot.get(0, 1) - rot.get(1, 0)) * s;
		} else {
			if(rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)) {
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
				
				w = (rot.get(1, 2) - rot.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.get(1, 0) + rot.get(0, 1)) / s;
				z = (rot.get(2, 0) + rot.get(0, 2)) / s;
			} else if(rot.get(1, 1) > rot.get(2, 2)) {
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
				
				w = (rot.get(2, 0) - rot.get(0, 2)) / s;
				x = (rot.get(1, 0) + rot.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.get(2, 1) + rot.get(1, 2)) / s;
			} else {
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
				
				w = (rot.get(0, 1) - rot.get(1, 0)) / s;
				x = (rot.get(2, 0) + rot.get(0, 2)) / s;
				y = (rot.get(1, 2) + rot.get(2, 1)) / s;
				z = 0.25f * s;
			}
		}
		
		float length = (float)Math.sqrt(x * x + y * y + z * z + w * w);
		
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}
	
	@Override
	public String toString() {
		return "Quaternion (x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "(";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof Quaternion))
			return false;
		
		Quaternion other = (Quaternion)obj;
		
		if(Float.floatToIntBits(w) != Float.floatToIntBits(other.w))
			return false;
		
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
		
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		
		return result;
	}
	
	/** @return The length of the quaternion */
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	/** @return A normalized copy of the quaternion */
	public Quaternion normalize() {
		float length = length();
		
		return new Quaternion(x / length, y / length, z / length, w / length);
	}
	
	/** @return A conjugated copy of the quaternion */
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	
	/** Multiply the quaternion by a specified value
	 * @param r The value to multiply by
	 * @return A new quaternion with the multiplication */
	public Quaternion mul(float r) {
		return new Quaternion(x * r, y * r, z * r, w * r);
	}
	
	/** Multiply the quaternion by a specified value
	 * @param r The quaternion to multiply by
	 * @return A new quaternion with the multiplication */
	public Quaternion mul(Quaternion r) {
		float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	/** Multiply the quaternion by a specified value
	 * @param r The vector to multiply by
	 * @return A new quaternion with the multiplication */
	public Quaternion mul(Vector3f r) {
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	/** Subtract the specified quaternion from this quaternion
	 * @param r The value to subtract by
	 * @return A new quaternion with the subtraction */
	public Quaternion sub(Quaternion r) {
		return new Quaternion(x - r.getX(), y - r.getY(), z - r.getZ(), w - r.getW());
	}
	
	/** Add the specified quaternion to this quaternion
	 * @param r The value to add
	 * @return A new quaternion with the addition */
	public Quaternion add(Quaternion r) {
		return new Quaternion(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
	}
	
	/** Create a rotation matrix from the quaternion */
	public Matrix4f toRotationMatrix() {
		Vector3f forward = new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
		Vector3f up = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
		Vector3f right = new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));
		
		return new Matrix4f().initRotation(forward, up, right);
	}
	
	/** @return The dot product of the quaternion */
	public float dot(Quaternion r) {
		return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
	}
	
	/** Linearly interpolate the quaternion
	 * @param dest The destination
	 * @param lerpFactor The factor to lerp by
	 * @param shortest Whether or not to use the shortest route
	 * @return A quaternion that has been lerped */
	public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest) {
		Quaternion correctedDest = dest;
		
		if(shortest && this.dot(dest) < 0)
			correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
		
		return correctedDest.sub(this).mul(lerpFactor).add(this).normalize();
	}
	
	/** Linearly interpolate the quaternion
	 * @param dest The destination
	 * @param lerpFactor The factor to lerp by
	 * @param shortest Whether or not to use the shortest route
	 * @return A quaternion that has been lerped */
	public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest) {
		final float EPSILON = 1e3f;
		
		float cos = this.dot(dest);
		Quaternion correctedDest = dest;
		
		if(shortest && cos < 0) {
			cos = -cos;
			correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
		}
		
		if(Math.abs(cos) >= 1 - EPSILON)
			return nlerp(correctedDest, lerpFactor, false);
		
		float sin = (float)Math.sqrt(1.0f - cos * cos);
		float angle = (float)Math.atan2(sin, cos);
		float invSin = 1.0f / sin;
		
		float srcFactor = (float)Math.sin((1.0f - lerpFactor) * angle) * invSin;
		float destFactor = (float)Math.sin((lerpFactor) * angle) * invSin;
		
		return this.mul(srcFactor).add(correctedDest.mul(destFactor));
	}
	
	/** @return The forward direction */
	public Vector3f getForward() {
		return new Vector3f(0, 0, 1).rotate(this);
	}
	
	/** @return The back direction */
	public Vector3f getBack() {
		return new Vector3f(0, 0, -1).rotate(this);
	}
	
	/** @return The up direction */
	public Vector3f getUp() {
		return new Vector3f(0, 1, 0).rotate(this);
	}
	
	/** @return The down direction */
	public Vector3f getDown() {
		return new Vector3f(0, -1, 0).rotate(this);
	}
	
	/** @return The right direction */
	public Vector3f getRight() {
		return new Vector3f(1, 0, 0).rotate(this);
	}
	
	/** @return The left direction */
	public Vector3f getLeft() {
		return new Vector3f(-1, 0, 0).rotate(this);
	}
	
	/** Set the quaternion to the specified values
	 * @param x The new X component
	 * @param y The new Y component
	 * @param z The new Z component
	 * @param w The new W component
	 * @return this quaternion, used for chaining */
	public Quaternion set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	/** Set the quaternion to the specified quaternion
	 * @param r The quaternion to clone
	 * @return this quaternion, used for chaining */
	public Quaternion set(Quaternion r) {
		set(r.getX(), r.getY(), r.getZ(), r.getW());
		
		return this;
	}
	
	/** Set the X component of the quaternion
	 * @param x The new X component */
	public void setX(float x) {
		this.x = x;
	}
	
	/** Set the Y component of the quaternion
	 * @param y The new Y component */
	public void setY(float y) {
		this.y = y;
	}
	
	/** Set the Z component of the quaternion
	 * @param z The new Z component */
	public void setZ(float z) {
		this.z = z;
	}
	
	/** Set the W component of the quaternion
	 * @param w The new W component */
	public void setW(float w) {
		this.w = w;
	}
	
	/** @return The X component of the quaternion */
	public float getX() {
		return x;
	}
	
	/** @return The Y component of the quaternion */
	public float getY() {
		return y;
	}
	
	/** @return The Z component of the quaternion */
	public float getZ() {
		return z;
	}
	
	/** @return The W component of the quaternion */
	public float getW() {
		return w;
	}
}
