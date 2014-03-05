package com.snakybo.engine.core;

import java.io.Serializable;
import java.util.Arrays;

/** @author Kevin Krol */
public class Matrix4f implements Serializable {
	private static final long serialVersionUID = 7665351985974304317L;
	
	private float[][] m;
	
	/** Createa a new matrix and set the components to the default value */
	public Matrix4f() {
		m = new float[4][4];
		
		initIdentity();
	}
	
	/** Initialize the matrix with another one
	 * @param other The matrix to clone */
	public Matrix4f(Matrix4f other) {
		m = other.m.clone();
	}
	
	// Initializers //
	
	/** Initialize the matrix
	 * @return This matrix */
	public Matrix4f initIdentity() {
		m[0][0] = 1;
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = 0;
		m[1][0] = 0;
		m[1][1] = 1;
		m[1][2] = 0;
		m[1][3] = 0;
		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = 1;
		m[2][3] = 0;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;
		
		return this;
	}
	
	/** Initialize the matrix for position
	 * @param position The position to initialize with
	 * @return This matrix */
	public Matrix4f initPosition(Vector3f position) {
		m[0][0] = 1;
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = position.x;
		m[1][0] = 0;
		m[1][1] = 1;
		m[1][2] = 0;
		m[1][3] = position.y;
		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = 1;
		m[2][3] = position.z;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;
		
		return this;
	}
	
	/** Initialize the matrix for rotation
	 * @param rotation The rotation to initialize with
	 * @return This matrix */
	public Matrix4f initRotation(Vector3f rotation) {
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();
		
		final float x = (float)Math.toRadians(rotation.x);
		final float y = (float)Math.toRadians(rotation.y);
		final float z = (float)Math.toRadians(rotation.z);
		
		rz.m[0][0] = (float)Math.cos(z);
		rz.m[0][1] = -(float)Math.sin(z);
		rz.m[0][2] = 0;
		rz.m[0][3] = 0;
		rz.m[1][0] = (float)Math.sin(z);
		rz.m[1][1] = (float)Math.cos(z);
		rz.m[1][2] = 0;
		rz.m[1][3] = 0;
		rz.m[2][0] = 0;
		rz.m[2][1] = 0;
		rz.m[2][2] = 1;
		rz.m[2][3] = 0;
		rz.m[3][0] = 0;
		rz.m[3][1] = 0;
		rz.m[3][2] = 0;
		rz.m[3][3] = 1;
		
		rx.m[0][0] = 1;
		rx.m[0][1] = 0;
		rx.m[0][2] = 0;
		rx.m[0][3] = 0;
		rx.m[1][0] = 0;
		rx.m[1][1] = (float)Math.cos(x);
		rx.m[1][2] = -(float)Math.sin(x);
		rx.m[1][3] = 0;
		rx.m[2][0] = 0;
		rx.m[2][1] = (float)Math.sin(x);
		rx.m[2][2] = (float)Math.cos(x);
		rx.m[2][3] = 0;
		rx.m[3][0] = 0;
		rx.m[3][1] = 0;
		rx.m[3][2] = 0;
		rx.m[3][3] = 1;
		
		ry.m[0][0] = (float)Math.cos(y);
		ry.m[0][1] = 0;
		ry.m[0][2] = -(float)Math.sin(y);
		ry.m[0][3] = 0;
		ry.m[1][0] = 0;
		ry.m[1][1] = 1;
		ry.m[1][2] = 0;
		ry.m[1][3] = 0;
		ry.m[2][0] = (float)Math.sin(y);
		ry.m[2][1] = 0;
		ry.m[2][2] = (float)Math.cos(y);
		ry.m[2][3] = 0;
		ry.m[3][0] = 0;
		ry.m[3][1] = 0;
		ry.m[3][2] = 0;
		ry.m[3][3] = 1;
		
		m = rz.scale(ry.scale(rx)).getM();
		
		return this;
	}
	
	/** Initialize the matrix for rotation
	 * @param forward The forward side
	 * @param up The up side
	 * @return This matrix */
	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f right = up.cross(forward);
		
		up = forward.cross(right);
		
		return initRotation(forward, up, right);
	}
	
	/** Initialize the matrix for rotation
	 * @param forward The forward side
	 * @param up The up side
	 * @param right The right side
	 * @return This matrix */
	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right) {
		m[0][0] = right.x;
		m[0][1] = right.y;
		m[0][2] = right.z;
		m[0][3] = 0;
		m[1][0] = up.x;
		m[1][1] = up.y;
		m[1][2] = up.z;
		m[1][3] = 0;
		m[2][0] = forward.x;
		m[2][1] = forward.y;
		m[2][2] = forward.z;
		m[2][3] = 0;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;
		
		return this;
	}
	
	/** Initialize the matrix for scale
	 * @param scale The scale to initialize with
	 * @return This matrix */
	public Matrix4f initScale(Vector3f scale) {
		initIdentity();
		
		m[0][0] = scale.x;
		m[1][1] = scale.y;
		m[2][2] = scale.z;
		
		return this;
	}
	
	/** Initialize the matrix for a perspective view
	 * @param fov The Field of View
	 * @param aspect The aspect ratio
	 * @param zNear The near clipping plane
	 * @param zFar The far clipping plane
	 * @return This matrix */
	public Matrix4f initPerspective(float fov, float aspect, float zNear, float zFar) {
		float tanHalfFOV = (float)Math.tan(fov / 2);
		float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * aspect);
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = 0;
		m[1][0] = 0;
		m[1][1] = 1.0f / tanHalfFOV;
		m[1][2] = 0;
		m[1][3] = 0;
		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = (-zNear - zFar) / zRange;
		m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 1;
		m[3][3] = 0;
		
		
		return this;
	}
	
	/** Initialize the matrix for an orthographic view
	 * @param left The amount of pixels to the left
	 * @param right The amount of pixels to the right
	 * @param bottom The amount of pixels to the bottom
	 * @param top The amount of pixels to the top
	 * @param near The near clipping plane
	 * @param far The far clipping plane
	 * @return This matrix */
	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near,
			float far) {
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;
		
		m[0][0] = 2 / width;
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = -(right + left) / width;
		m[1][0] = 0;
		m[1][1] = 2 / height;
		m[1][2] = 0;
		m[1][3] = -(top + bottom) / height;
		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = -2 / depth;
		m[2][3] = -(far + near) / depth;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;
		
		return this;
	}
	
	// Geometrical operations //
	
	/** Transform the matrix
	 * @param other The vector to transform by
	 * @return A new vector */
	public Vector3f transform(Vector3f other) {
		return new Vector3f(m[0][0] * other.x + m[0][1] * other.y + m[0][2] * other.z + m[0][3],
				m[1][0] * other.x + m[1][1] * other.y + m[1][2] * other.z + m[1][3], m[2][0]
						* other.x + m[2][1] * other.y + m[2][2] * other.z + m[2][3]);
	}
	
	/** Scale the matrix by another
	 * @param other The matrix to scale by
	 * @return A new matrix */
	public Matrix4f scale(Matrix4f r) {
		Matrix4f res = new Matrix4f();
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				res.set(i, j, m[i][0] * r.get(0, j) + m[i][1] * r.get(1, j) + m[i][2] * r.get(2, j)
						+ m[i][3] * r.get(3, j));
			}
		}
		
		return res;
	}
	
	/** Scale the matrix by another
	 * @param other The matrix to scale by
	 * @return This matrix */
	public Matrix4f scaleSelf(Matrix4f other) {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				set(i,
						j,
						m[i][0] * other.get(0, j) + m[i][1] * other.get(1, j) + m[i][2]
								* other.get(2, j) + m[i][3] * other.get(3, j));
			}
		}
		
		return this;
	}
	
	// General opertations //
	
	/** Clone this matrix
	 * @return A copy of this matrix */
	public Matrix4f clone() {
		return new Matrix4f(this);
	}
	
	/** @see Object#equals(Object obj) */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		if(obj == this)
			return true;
		
		if(!(obj instanceof Matrix4f))
			return false;
		
		Matrix4f object = (Matrix4f)obj;
		
		return Arrays.deepEquals(m, object.m);
	}
	
	/** @see Object#hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + Arrays.hashCode(m);
		
		return result;
	}
	
	// Setters //
	
	/** Set the values of the matrix to those of another
	 * @param other The matrix to clone
	 * @return This matrix */
	public Matrix4f set(Matrix4f other) {
		m = other.m.clone();
		
		return this;
	}
	
	/** @see Object#toString() */
	@Override
	public String toString() {
		return "(" + m[0][0] + ", " + m[0][1] + ", " + m[0][2] + ", " + m[0][3] + " - " + m[1][0]
				+ ", " + m[1][1] + ", " + m[1][2] + ", " + m[1][3] + " - " + m[2][0] + ", "
				+ m[2][1] + ", " + m[2][2] + ", " + m[2][3] + " - " + m[3][0] + ", " + m[3][1]
				+ ", " + m[3][2] + ", " + m[3][3] + ")";
	}
	
	/** Set the matrix array to the specified array
	 * @param m The new matrix array
	 * @return This matrix */
	public Matrix4f set(float[][] m) {
		this.m = m;
		
		return this;
	}
	
	/** Set a value in the matrix
	 * @param x The X coordinate of the matrix
	 * @param y The Y coordinate of the matrix
	 * @param value The new value of the specified coordinates
	 * @return This matrix */
	public Matrix4f set(int x, int y, float value) {
		m[x][y] = value;
		
		return this;
	}
	
	// Getters //
	
	/** Get the value at the specified coordinates
	 * @param x The X coordinate of the matrix
	 * @param y The Y coordinate of the matrix
	 * @return The value of the specified coordinates */
	public float get(int x, int y) {
		return m[x][y];
	}
	
	/** @return A copy of the matrix array */
	public float[][] getM() {
		return m.clone();
	}
}
