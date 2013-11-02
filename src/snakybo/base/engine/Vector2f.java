package snakybo.base.engine;

public class Vector2f {
	private float x;
	private float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/** @return Float: Total length */
	public float length() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	/** @return Float: Dot */
	public float dot(Vector2f r) {
		return x * r.getX() + y * r.getY();
	}
	
	/** @return Vector2f: Normalized Vector2f */
	public Vector2f normalized() {
		float length = length();
		
		return new Vector2f(x / length, y / length);
	}
	
	/** @return Vector2f: Rotated Vector2f */
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f((float)(x * cos - y * sin),(float)(x * sin + y * cos));
	}
	
	/** @return Vector2f: Vector2f with specified Vector2f added to it */
	public Vector2f add(Vector2f r) {
		return new Vector2f(x + r.getX(), y + r.getY());
	}
	
	/** @return Vector2f: Vector2f with specified floats added to it */
	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}
	
	/** @return Vector2f: Vector2f with specified Vector2f subtracted from it */
	public Vector2f sub(Vector2f r) {
		return new Vector2f(x - r.getX(), y - r.getY());
	}
	
	/** @return Vector2f: Vector2f with specified floats subtracted from it */
	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}
	
	/** @return Vector2f: Vector2f with specified Vector2f multiplied to it */
	public Vector2f mul(Vector2f r) {
		return new Vector2f(x * r.getX(), y * r.getY());
	}
	
	/** @return Vector2f: Vector2f with specified floats multiplied to it */
	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}
	
	/** @return Vector2f: Vector2f with specified Vector2f divided from it */
	public Vector2f div(Vector2f r) {
		return new Vector2f(x / r.getX(), y / r.getY());
	}
	
	/** @return Vector2f: Vector2f with specified floats divided from it */
	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}
	
	/** @return Vector2f: Absolute value of Vector2f */
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	/** @return Vector2f: Vector2f as String */
	public String toString() {
		return "(" + x + " " + y + ")";
	}
	
	/** Set X */
	public void setX(float x) {
		this.x = x;
	}
	
	/** Set Y */
	public void setY(float y) {
		this.y = y;
	}
	
	/** @return Float: X */
	public float getX() {
		return x;
	}

	/** @return Float: Y */
	public float getY() {
		return y;
	}
}
