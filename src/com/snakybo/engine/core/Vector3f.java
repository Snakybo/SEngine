package snakybo.base.engine;

public class Vector3f {
	private float x;
	private float y;
	private float z;
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** @return Float: Total Length */
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	/** @return Vector3f: Dot */
	public float dot(Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}
	
	/** @return Vector3f: Crossproduct of Vector3f */
	public Vector3f cross(Vector3f r) {
		float x_ = y * r.getZ() - z * r.getY();
		float y_ = z * r.getX() - x * r.getZ();
		float z_ = x * r.getY() - y * r.getX();
		
		return new Vector3f(x_, y_, z_);
	}
	
	/** @return Vector3f: A normalized Vector3f */
	public Vector3f normalized() {
		float length = length();
		
		return new Vector3f(x / length, y / length, z / length);
	}
	
	/** @return Vector3f: Vector3f rotated by angle and axis */
	public Vector3f rotate(float angle, Vector3f axis) {
		float sinHalfAngle = (float)Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float)Math.cos(Math.toRadians(angle / 2));
		
		float rX = axis.getX() * sinHalfAngle;
		float rY = axis.getY() * sinHalfAngle;
		float rZ = axis.getZ() * sinHalfAngle;
		float rW = cosHalfAngle;
		
		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.mul(this).mul(conjugate);
		
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	/** @return Vector3f: Vector3f with specified Vector3f added to it */
	public Vector3f add(Vector3f r) {
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}
	
	/** @return Vector3f: Vector3f with specified float added to it  */
	public Vector3f add(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}
	
	/**  @return Vector3f: Vector3f subtracted by specified Vector3f  */
	public Vector3f sub(Vector3f r) {
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}
	
	/** @return Vector3f: Vector3f subtracted by specified float */
	public Vector3f sub(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}
	
	/** @return Vector3f: Vector3f multiplied by specified Vector3f */
	public Vector3f mul(Vector3f r) {
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}
	
	/** @return Vector3f: Vector3f multiplied by specified float */
	public Vector3f mul(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}
	
	/** @return Vector3f: Vector3f divided by specified Vector3f  */
	public Vector3f div(Vector3f r) {
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}
	
	/** @return Vector3f: Vector3f divided by specified float */	
	public Vector3f div(float r) {
		return new Vector3f(x / r, y / r, z / r);
	}
	
	/** @return Vector3f: Absolute value of Vector3f */
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
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
}