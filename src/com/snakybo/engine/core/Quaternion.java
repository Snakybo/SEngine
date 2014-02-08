package snakybo.base.engine;

public class Quaternion {
	private float x;
	private float y;
	private float z;
	private float w;
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/** @return Float: Total length of the Quaternion */
	public float length() {		
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	/** Normalize the Quaternion */
	public Quaternion normalize() {
		float length = length();
		
		return new Quaternion(x / length, y / length, z / length, w / length);
	}
	
	/** Conjugate the Quaternion */
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	
	/** Multiply with the specified Quarternion */
	public Quaternion mul(Quaternion r) {
		float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	/** Multiply with the specified Vector3f */
	public Quaternion mul(Vector3f r) {
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ =  w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ =  w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ =  w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	/** Set X */
	public void setX(float x) {
		this.x = x;
	}
	
	/** Set Y */
	public void setY(float y) {
		this.y = y;
	}
	
	/** Set Z */
	public void setZ(float z) {
		this.z = z;
	}
	
	/** Set W */
	public void setW(float w) {
		this.w = w;
	}
	
	/** @return Float: X */
	public float getX() {
		return x;
	}

	/** @return Float: Y */
	public float getY() {
		return y;
	}

	/** @return Float: Z */
	public float getZ() {
		return z;
	}

	/** @return Float: W */
	public float getW() {
		return w;
	}
}