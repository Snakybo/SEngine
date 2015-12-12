package com.snakybo.sengine.utils.math;

import java.util.Arrays;

/**
 * @author Kevin
 * @since Dec 12, 2015
 */
public class Matrix4f
{
	private float[][] m;

	public Matrix4f()
	{
		this(new float[4][4]);
	}
	
	public Matrix4f(float[][] values)
	{
		m = values;
	}
	
	public Matrix4f(Matrix4f other)
	{
		this(other.m);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		
		result = prime * result + Arrays.deepHashCode(m);
		
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(!(obj instanceof Matrix4f))
		{
			return false;
		}
		
		Matrix4f other = (Matrix4f)obj;
		if(!Arrays.deepEquals(m, other.m))
		{
			return false;
		}
		
		return true;
	}

	public final Vector3f transform(Vector3f amount)
	{
		float x = m[0][0] * amount.x + m[0][1] * amount.y + m[0][2] * amount.z + m[0][3];
		float y = m[1][0] * amount.x + m[1][1] * amount.y + m[1][2] * amount.z + m[1][3];
		float z = m[2][0] * amount.x + m[2][1] * amount.y + m[2][2] * amount.z + m[2][3];
		
		return new Vector3f(x, y, z);
	}

	public final Matrix4f mul(Matrix4f other)
	{
		Matrix4f res = new Matrix4f();

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				float value = m[i][0] * other.get(0, j) + m[i][1] * other.get(1, j) + m[i][2] * other.get(2, j) + m[i][3] * other.get(3, j);
				res.set(i, j, value);
			}
		}

		return res;
	}

	public final float[][] getM()
	{
		float[][] res = new float[4][4];

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				res[i][j] = m[i][j];
			}
		}

		return res;
	}

	public final float get(int x, int y)
	{
		return m[x][y];
	}

	public final void setM(float[][] m)
	{
		this.m = m;
	}

	public final void set(int x, int y, float value)
	{
		m[x][y] = value;
	}
	
	public static Matrix4f createTranslationMatrix(Vector3f translation)
	{
		return createTranslationMatrix(translation.x, translation.y, translation.z);
	}

	public static Matrix4f createTranslationMatrix(float x, float y, float z)
	{
		Matrix4f result = new Matrix4f();
		
		result.m[0][0] = 1;	result.m[0][1] = 0;	result.m[0][2] = 0;	result.m[0][3] = x;
		result.m[1][0] = 0;	result.m[1][1] = 1;	result.m[1][2] = 0;	result.m[1][3] = y;
		result.m[2][0] = 0;	result.m[2][1] = 0;	result.m[2][2] = 1;	result.m[2][3] = z;
		result.m[3][0] = 0;	result.m[3][1] = 0;	result.m[3][2] = 0;	result.m[3][3] = 1;
	
		return result;
	}
	
	public static Matrix4f createRotationMatrix(Vector3f rotation)
	{
		return createRotationMatrix(rotation.x, rotation.y, rotation.z);
	}

	public static Matrix4f createRotationMatrix(float x, float y, float z)
	{
		Matrix4f result = new Matrix4f();
		
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();
	
		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);
	
		rz.m[0][0] = (float) Math.cos(z);	rz.m[0][1] = -(float) Math.sin(z);	rz.m[0][2] = 0;						rz.m[0][3] = 0;
		rz.m[1][0] = (float) Math.sin(z);	rz.m[1][1] = (float) Math.cos(z);	rz.m[1][2] = 0;						rz.m[1][3] = 0;
		rz.m[2][0] = 0;						rz.m[2][1] = 0;						rz.m[2][2] = 1;						rz.m[2][3] = 0;
		rz.m[3][0] = 0;						rz.m[3][1] = 0;						rz.m[3][2] = 0;						rz.m[3][3] = 1;
	
		rx.m[0][0] = 1;						rx.m[0][1] = 0;						rx.m[0][2] = 0;						rx.m[0][3] = 0;
		rx.m[1][0] = 0;						rx.m[1][1] = (float) Math.cos(x);	rx.m[1][2] = -(float) Math.sin(x);	rx.m[1][3] = 0;
		rx.m[2][0] = 0;						rx.m[2][1] = (float) Math.sin(x);	rx.m[2][2] = (float) Math.cos(x);	rx.m[2][3] = 0;
		rx.m[3][0] = 0;						rx.m[3][1] = 0;						rx.m[3][2] = 0;						rx.m[3][3] = 1;
	
		ry.m[0][0] = (float) Math.cos(y);	ry.m[0][1] = 0;						ry.m[0][2] = -(float) Math.sin(y);	ry.m[0][3] = 0;
		ry.m[1][0] = 0;						ry.m[1][1] = 1;						ry.m[1][2] = 0;						ry.m[1][3] = 0;
		ry.m[2][0] = (float) Math.sin(y);	ry.m[2][1] = 0;						ry.m[2][2] = (float) Math.cos(y);	ry.m[2][3] = 0;
		ry.m[3][0] = 0;						ry.m[3][1] = 0;						ry.m[3][2] = 0;						ry.m[3][3] = 1;
	
		result.m = rz.mul(ry.mul(rx)).getM();	
		return result;
	}
	
	public static Matrix4f createRotationMatrix(Quaternion quaternion)
	{
		float x = quaternion.x;
		float y = quaternion.y;
		float z = quaternion.z;
		float w = quaternion.w;
		
		Vector3f forward = new Vector3f(2 * (x * z - w * y), 2 * (y * z + w * x), 1 - 2 * (x * x + y * y));
		Vector3f up = new Vector3f(2 * (x * y + w * z), 1 - 2 * (x * x + z * z), 2 * (y * z - w * x));
		Vector3f right = new Vector3f(1 - 2 * (y * y + z * z), 2 * (x * y - w * z), 2 * (x * z + w * y));

		return Matrix4f.createRotationMatrix(forward, up, right);
	}
	
	public static Matrix4f createRotationMatrix(Vector3f forward, Vector3f up)
	{
		Vector3f f = forward.normalized();
	
		Vector3f r = up.normalized();
		r = r.cross(f);
	
		Vector3f u = f.cross(r);
	
		return createRotationMatrix(f, u, r);
	}

	public static Matrix4f createRotationMatrix(Vector3f forward, Vector3f up, Vector3f right)
	{
		Matrix4f result = new Matrix4f();
		
		Vector3f f = forward;
		Vector3f r = right;
		Vector3f u = up;
	
		result.m[0][0] = r.x;	result.m[0][1] = r.y;	result.m[0][2] = r.z;	result.m[0][3] = 0;
		result.m[1][0] = u.x;	result.m[1][1] = u.y;	result.m[1][2] = u.z;	result.m[1][3] = 0;
		result.m[2][0] = f.x;	result.m[2][1] = f.y;	result.m[2][2] = f.z;	result.m[2][3] = 0;
		result.m[3][0] = 0;		result.m[3][1] = 0;		result.m[3][2] = 0;		result.m[3][3] = 1;
	
		return result;
	}
	
	public static Matrix4f createScaleMatrix(Vector3f scale)
	{
		return createScaleMatrix(scale.x, scale.y, scale.z);
	}

	public static Matrix4f createScaleMatrix(float x, float y, float z)
	{
		Matrix4f result = new Matrix4f();
		
		result.m[0][0] = x;	result.m[0][1] = 0;	result.m[0][2] = 0;	result.m[0][3] = 0;
		result.m[1][0] = 0;	result.m[1][1] = y;	result.m[1][2] = 0;	result.m[1][3] = 0;
		result.m[2][0] = 0;	result.m[2][1] = 0;	result.m[2][2] = z;	result.m[2][3] = 0;
		result.m[3][0] = 0;	result.m[3][1] = 0;	result.m[3][2] = 0;	result.m[3][3] = 1;
	
		return result;
	}
	
	public static Matrix4f identity()
	{
		Matrix4f result = new Matrix4f();
		
		result.m[0][0] = 1;	result.m[0][1] = 0;	result.m[0][2] = 0;	result.m[0][3] = 0;
		result.m[1][0] = 0;	result.m[1][1] = 1;	result.m[1][2] = 0;	result.m[1][3] = 0;
		result.m[2][0] = 0;	result.m[2][1] = 0;	result.m[2][2] = 1;	result.m[2][3] = 0;
		result.m[3][0] = 0;	result.m[3][1] = 0;	result.m[3][2] = 0;	result.m[3][3] = 1;

		return result;
	}

	public static Matrix4f perspective(float fov, float aspectRatio, float zNear, float zFar)
	{
		Matrix4f result = new Matrix4f();
		
		fov = (float) Math.toRadians(fov);
	
		float tanHalfFOV = (float) Math.tan(fov / 2);
		float zRange = zNear - zFar;
	
		result.m[0][0] = 1f / (tanHalfFOV * aspectRatio);	result.m[0][1] = 0;					result.m[0][2] = 0;							result.m[0][3] = 0;
		result.m[1][0] = 0;									result.m[1][1] = 1f / tanHalfFOV;	result.m[1][2] = 0;							result.m[1][3] = 0;
		result.m[2][0] = 0;									result.m[2][1] = 0;					result.m[2][2] = (-zNear - zFar) / zRange;	result.m[2][3] = 2 * zFar * zNear / zRange;
		result.m[3][0] = 0;									result.m[3][1] = 0;					result.m[3][2] = 1;							result.m[3][3] = 0;
	
		return result;
	}

	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far)
	{
		Matrix4f result = new Matrix4f();
		
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;
	
		result.m[0][0] = 2 / width;		result.m[0][1] = 0;				result.m[0][2] = 0;				result.m[0][3] = -(right + left) / width;
		result.m[1][0] = 0;				result.m[1][1] = 2 / height;	result.m[1][2] = 0;				result.m[1][3] = -(top + bottom) / height;
		result.m[2][0] = 0;				result.m[2][1] = 0;				result.m[2][2] = -2 / depth;	result.m[2][3] = -(far + near) / depth;
		result.m[3][0] = 0;				result.m[3][1] = 0;				result.m[3][2] = 0;				result.m[3][3] = 1;
		
		return result;
	}
}
