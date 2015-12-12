package com.snakybo.sengine.utils.math;

/**
 * @author Kevin
 * @since Dec 12, 2015
 */
public class Vector2f
{
	public float x;
	public float y;

	public Vector2f()
	{
		this(0);
	}

	public Vector2f(float x)
	{
		this(x, 0);
	}

	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f r)
	{
		set(r);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 132;
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		
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
		
		if(!(obj instanceof Vector2f))
		{
			return false;
		}
		
		Vector2f other = (Vector2f)obj;
		
		if(Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
		{
			return false;
		}
		
		if(Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
		{
			return false;
		}
		
		return true;
	}

	@Override
	public String toString()
	{
		return "Vector2f(" + x + ", " + y + ")";
	}

	public float length()
	{
		return (float)Math.sqrt(x * x + y * y);
	}

	public float max()
	{
		return Math.max(x, y);
	}

	public float dot(Vector2f r)
	{
		return x * r.x + y * r.y;
	}

	public Vector2f normalized()
	{
		float length = length();

		return new Vector2f(x / length, y / length);
	}

	public float cross(Vector2f r)
	{
		return x * r.y - y * r.x;
	}

	public Vector2f lerp(Vector2f dest, float lerpFactor)
	{
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vector2f rotate(float angle)
	{
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vector2f((float)(x * cos - y * sin), (float)(x * sin + y * cos));
	}

	public Vector2f add(Vector2f r)
	{
		return new Vector2f(x + r.x, y + r.y);
	}

	public Vector2f add(float r)
	{
		return new Vector2f(x + r, y + r);
	}

	public Vector2f sub(Vector2f r)
	{
		return new Vector2f(x - r.x, y - r.y);
	}

	public Vector2f sub(float r)
	{
		return new Vector2f(x - r, y - r);
	}

	public Vector2f mul(Vector2f r)
	{
		return new Vector2f(x * r.x, y * r.y);
	}

	public Vector2f mul(float r)
	{
		return new Vector2f(x * r, y * r);
	}

	public Vector2f div(Vector2f r)
	{
		return new Vector2f(x / r.x, y / r.y);
	}

	public Vector2f div(float r)
	{
		return new Vector2f(x / r, y / r);
	}

	public Vector2f abs()
	{
		return new Vector2f(Math.abs(x), Math.abs(y));
	}

	public Vector2f set(float x, float y)
	{
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2f set(Vector2f r)
	{
		set(r.x, r.y);
		return this;
	}
}
