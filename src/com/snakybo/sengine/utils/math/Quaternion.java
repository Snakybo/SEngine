package com.snakybo.sengine.utils.math;

public class Quaternion
{
	public float x;
	public float y;
	public float z;
	public float w;

	public Quaternion()
	{
		this(0);
	}

	public Quaternion(float x)
	{
		this(x, 0);
	}

	public Quaternion(float x, float y)
	{
		this(x, y, 0);
	}

	public Quaternion(float x, float y, float z)
	{
		this(x, y, z, 1);
	}

	public Quaternion(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion(Matrix4f rot)
	{
		float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

		if (trace > 0)
		{
			float s = 0.5f / (float) Math.sqrt(trace + 1);
			w = 0.25f / s;
			x = (rot.get(1, 2) - rot.get(2, 1)) * s;
			y = (rot.get(2, 0) - rot.get(0, 2)) * s;
			z = (rot.get(0, 1) - rot.get(1, 0)) * s;
		}
		else
		{
			if (rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2))
			{
				float s = 2 * (float) Math.sqrt(1 + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
				w = (rot.get(1, 2) - rot.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.get(1, 0) + rot.get(0, 1)) / s;
				z = (rot.get(2, 0) + rot.get(0, 2)) / s;
			}
			else if (rot.get(1, 1) > rot.get(2, 2))
			{
				float s = 2 * (float) Math.sqrt(1 + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
				w = (rot.get(2, 0) - rot.get(0, 2)) / s;
				x = (rot.get(1, 0) + rot.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.get(2, 1) + rot.get(1, 2)) / s;
			}
			else
			{
				float s = 2 * (float) Math.sqrt(1 + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
				w = (rot.get(0, 1) - rot.get(1, 0)) / s;
				x = (rot.get(2, 0) + rot.get(0, 2)) / s;
				y = (rot.get(1, 2) + rot.get(2, 1)) / s;
				z = 0.25f * s;
			}
		}

		float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}

	public Quaternion(Vector3f axis, float angle)
	{
		float sinHalfAngle = (float) Math.sin(angle / 2);
		float cosHalfAngle = (float) Math.cos(angle / 2);

		this.x = axis.x * sinHalfAngle;
		this.y = axis.y * sinHalfAngle;
		this.z = axis.z * sinHalfAngle;
		this.w = cosHalfAngle;
	}

	public Quaternion(Quaternion r)
	{
		this(r.x, r.y, r.z, r.w);
	}

	public float length()
	{
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Quaternion normalized()
	{
		float length = length();

		return new Quaternion(x / length, y / length, z / length, w / length);
	}

	public Quaternion conjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}

	public Quaternion mul(float r)
	{
		return new Quaternion(x * r, y * r, z * r, w * r);
	}

	public Quaternion mul(Quaternion r)
	{
		float w_ = w * r.w - x * r.x - y * r.y - z * r.z;
		float x_ = x * r.w + w * r.x + y * r.z - z * r.y;
		float y_ = y * r.w + w * r.y + z * r.x - x * r.z;
		float z_ = z * r.w + w * r.z + x * r.y - y * r.x;

		return new Quaternion(x_, y_, z_, w_);
	}

	public Quaternion mul(Vector3f r)
	{
		float w_ = -x * r.x - y * r.y - z * r.z;
		float x_ = w * r.x + y * r.z - z * r.y;
		float y_ = w * r.y + z * r.x - x * r.z;
		float z_ = w * r.z + x * r.y - y * r.x;

		return new Quaternion(x_, y_, z_, w_);
	}

	public Quaternion sub(Quaternion r)
	{
		return new Quaternion(x - r.x, y - r.y, z - r.z, w - r.w);
	}

	public Quaternion add(Quaternion r)
	{
		return new Quaternion(x + r.x, y + r.y, z + r.z, w + r.w);
	}
	
	public float dot(Quaternion r)
	{
		return x * r.x + y * r.y + z * r.z + w * r.w;
	}

	public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest)
	{
		Quaternion correctedDest = dest;

		if (shortest && this.dot(dest) < 0)
			correctedDest = new Quaternion(-dest.x, -dest.y, -dest.z, -dest.w);

		return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
	}

	public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest)
	{
		final float EPSILON = 1e3f;

		float cos = this.dot(dest);
		Quaternion correctedDest = dest;

		if (shortest && cos < 0)
		{
			cos = -cos;
			correctedDest = new Quaternion(-dest.x, -dest.y, -dest.z, -dest.w);
		}

		if (Math.abs(cos) >= 1 - EPSILON)
			return nlerp(correctedDest, lerpFactor, false);

		float sin = (float) Math.sqrt(1 - cos * cos);
		float angle = (float) Math.atan2(sin, cos);
		float invSin = 1 / sin;

		float srcFactor = (float) Math.sin((1 - lerpFactor) * angle) * invSin;
		float destFactor = (float) Math.sin((lerpFactor) * angle) * invSin;

		return this.mul(srcFactor).add(correctedDest.mul(destFactor));
	}

	public Quaternion set(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Quaternion set(Quaternion r)
	{
		set(r.x, r.y, r.z, r.w);
		return this;
	}

	public Vector3f getForward()
	{
		return new Vector3f(0, 0, 1).rotate(this);
	}

	public Vector3f getBack()
	{
		return new Vector3f(0, 0, -1).rotate(this);
	}

	public Vector3f getUp()
	{
		return new Vector3f(0, 1, 0).rotate(this);
	}

	public Vector3f getDown()
	{
		return new Vector3f(0, -1, 0).rotate(this);
	}

	public Vector3f getRight()
	{
		return new Vector3f(1, 0, 0).rotate(this);
	}

	public Vector3f getLeft()
	{
		return new Vector3f(-1, 0, 0).rotate(this);
	}

	public boolean equals(Quaternion r)
	{
		return x == r.x && y == r.y && z == r.z && w == r.w;
	}
}
