package com.snakybo.sengine.core.utils;

import java.util.Arrays;

/** Matrix 4 math class
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class Matrix4f {
	private float[][] m;
	
	/** Constructor for the matrix */
	public Matrix4f() {
		m = new float[4][4];
	}
	
	@Override
	public String toString() {
		return "Matrix4f (m=" + Arrays.toString(m) + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof Matrix4f))
			return false;
		
		Matrix4f other = (Matrix4f)obj;
		
		if(!Arrays.deepEquals(m, other.m))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + Arrays.hashCode(m);
		
		return result;
	}

	/** Initialize the matrix to the default values */
	public Matrix4f initIdentity() {
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}
	
	/** Initialize the matrix to the default values for translation
	 * @param position The position
	 * @return This matrix, used for chaining */
	public Matrix4f initTranslation(Vector3f position) {
		return initTranslation(position.getX(), position.getY(), position.getZ());
	}
	
	/** Initialize the matrix to the default values for translation
	 * @param x The X position
	 * @param y The Y position
	 * @param z The Z position
	 * @return This matrix, used for chaining */
	public Matrix4f initTranslation(float x, float y, float z) {
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	/** Initialize the matrix to the default values for rotation
	 * @param rotation The rotation
	 * @return This matrix, used for chaining */
	public Matrix4f initRotation(Vector3f rotation) {
		return initRotation(rotation.getX(), rotation.getY(), rotation.getZ());
	}
	
	/** Initialize the matrix to the default values for rotation
	 * @param x The X rotation
	 * @param y The Y rotation
	 * @param z The Z rotation
	 * @return This matrix, used for chaining */
	public Matrix4f initRotation(float x, float y, float z) {
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();
		
		x = (float)Math.toRadians(x);
		y = (float)Math.toRadians(y);
		z = (float)Math.toRadians(z);
		
		rz.m[0][0] = (float)Math.cos(z);	rz.m[0][1] = -(float)Math.sin(z);	rz.m[0][2] = 0;						rz.m[0][3] = 0;
		rz.m[1][0] = (float)Math.sin(z);	rz.m[1][1] = (float)Math.cos(z);	rz.m[1][2] = 0;						rz.m[1][3] = 0;
		rz.m[2][0] = 0;						rz.m[2][1] = 0;						rz.m[2][2] = 1;						rz.m[2][3] = 0;
		rz.m[3][0] = 0;						rz.m[3][1] = 0;						rz.m[3][2] = 0;						rz.m[3][3] = 1;
		
		rx.m[0][0] = 1;						rx.m[0][1] = 0;						rx.m[0][2] = 0;						rx.m[0][3] = 0;
		rx.m[1][0] = 0;						rx.m[1][1] = (float)Math.cos(x);	rx.m[1][2] = -(float)Math.sin(x);	rx.m[1][3] = 0;
		rx.m[2][0] = 0;						rx.m[2][1] = (float)Math.sin(x);	rx.m[2][2] = (float)Math.cos(x);	rx.m[2][3] = 0;
		rx.m[3][0] = 0;						rx.m[3][1] = 0;						rx.m[3][2] = 0;						rx.m[3][3] = 1;
		
		ry.m[0][0] = (float)Math.cos(y);	ry.m[0][1] = 0;						ry.m[0][2] = -(float)Math.sin(y);	ry.m[0][3] = 0;
		ry.m[1][0] = 0;						ry.m[1][1] = 1;						ry.m[1][2] = 0;						ry.m[1][3] = 0;
		ry.m[2][0] = (float)Math.sin(y);	ry.m[2][1] = 0;						ry.m[2][2] = (float)Math.cos(y);	ry.m[2][3] = 0;
		ry.m[3][0] = 0;						ry.m[3][1] = 0;						ry.m[3][2] = 0;						ry.m[3][3] = 1;
		
		m = rz.mul(ry.mul(rx)).getM();
		
		return this;
	}
	
	/** Initialize the matrix to the default values for rotation
	 * @param forward The forward direction
	 * @param up The up direction
	 * @return This matrix, used for chaining */
	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalize();
		
		Vector3f r = up.normalize();
		r = r.cross(f);
		
		Vector3f u = f.cross(r);

		return initRotation(f, u, r);
	}

	/** Initialize the matrix to the default values for rotation
	 * @param forward The forward direction
	 * @param up The up direction
	 * @param right The right direction
	 * @return This matrix, used for chaining */
	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right) {
		Vector3f f = forward;
		Vector3f r = right;
		Vector3f u = up;

		m[0][0] = r.getX();	m[0][1] = r.getY();	m[0][2] = r.getZ();	m[0][3] = 0;
		m[1][0] = u.getX();	m[1][1] = u.getY();	m[1][2] = u.getZ();	m[1][3] = 0;
		m[2][0] = f.getX();	m[2][1] = f.getY();	m[2][2] = f.getZ();	m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

		return this;
	}
	
	/** Initialize the matrix to the default values for scale
	 * @param scale The scale
	 * @return This matrix, used for chaining */
	public Matrix4f initScale(Vector3f scale) {
		return initScale(scale.getX(), scale.getY(), scale.getZ());
	}
	
	/** Initialize the matrix to the default values for scale
	 * @param x The X scale
	 * @param y The Y scale
	 * @param z The Z scale
	 * @return This matrix, used for chaining */
	public Matrix4f initScale(float x, float y, float z) {
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	/** Initialize the matrix to the default values for a perspective projection
	 * @param fov The field of view
	 * @param aspectRatio The aspect ratio
	 * @param zNear The near clipping plane
	 * @param zFar The far clipping plane 
	 * @return This matrix, used for chaining */
	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		float tanHalfFOV = (float)Math.tan(fov / 2);
		float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);	m[0][1] = 0;					m[0][2] = 0;						m[0][3] = 0;
		m[1][0] = 0;									m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;						m[1][3] = 0;
		m[2][0] = 0;									m[2][1] = 0;					m[2][2] = (-zNear -zFar) / zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;									m[3][1] = 0;					m[3][2] = 1;						m[3][3] = 0;
		
		return this;
	}
	
	/** Initialize the matrix to the default values for an orthographic projection
	 * @param left The amount of pixels to the left
	 * @param right The amount of pixels to the right
	 * @param bottom The amount of pixels to the bottom
	 * @param top The amount of pixels to the top
	 * @param near The near clipping plane
	 * @param far The far clipping plane
	 * @return This matrix, used for chaining */
	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far) {
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		m[0][0] = 2 / width;	m[0][1] = 0;		m[0][2] = 0;		m[0][3] = -(right + left) / width;
		m[1][0] = 0;		m[1][1] = 2 / height;	m[1][2] = 0;		m[1][3] = -(top + bottom) / height;
		m[2][0] = 0;		m[2][1] = 0;		m[2][2] = -2 / depth;	m[2][3] = -(far + near) / depth;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

		return this;
	}
	
	/** Transform the matrix
	 * @param r The value to transform by 
	 * @return A new vector 3 with the transformation applied */
	public Vector3f transform(Vector3f r) {
		return new Vector3f(m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3],
		                    m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3],
		                    m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3]);
	}
	
	/** Multiply the matrix by another
	 * @param r The matrix to multiply by
	 * @return A new matrix */
	public Matrix4f mul(Matrix4f r) {
		Matrix4f res = new Matrix4f();
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				res.set(i, j, m[i][0] * r.get(0, j) +
							  m[i][1] * r.get(1, j) +
							  m[i][2] * r.get(2, j) +
							  m[i][3] * r.get(3, j));
			}
		}
		
		return res;
	}
	
	/** Set the value at the specified coordinates
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param value The new value */
	public void set(int x, int y, float value) {
		m[x][y] = value;
	}
	
	/** Set the M array
	 * @param m The new M array */
	public void setM(float[][] m) {
		this.m = m;
	}
	
	/** @return The value at the specified coordinates
	 * @param x The X coordinate
	 * @param y The Y coordinate */
	public float get(int x, int y) {
		return m[x][y];
	}
	
	/** @return The M array */
	public float[][] getM() {
		float[][] res = new float[4][4];
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				res[i][j] = m[i][j];
		
		return res;
	}
}
