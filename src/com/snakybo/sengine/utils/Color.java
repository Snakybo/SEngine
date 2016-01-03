package com.snakybo.sengine.utils;

import com.snakybo.sengine.math.MathUtils;

/**
 * @author Kevin
 * @since Jul 6, 2014
 */
public class Color
{
	private float r;
	private float g;
	private float b;
	private float a;
	
	public Color()
	{
		this(0, 0, 0);
	}
	
	public Color(float r, float g, float b)
	{
		this(r, g, b, 1);
	}
	
	public Color(float r, float g, float b, float a)
	{
		setRed(r);
		setGreen(g);
		setBlue(b);
		setAlpha(a);
	}
	
	public float max()
	{
		return Math.max(r, Math.max(g, b));
	}
	
	public void setRed(float r)
	{
		this.r = MathUtils.clamp(r, 0f, 1f);
	}
	
	public void setGreen(float g)
	{
		this.g = MathUtils.clamp(g, 0f, 1f);
	}
	
	public void setBlue(float b)
	{
		this.b = MathUtils.clamp(b, 0f, 1f);
	}
	
	public void setAlpha(float a)
	{
		this.a = MathUtils.clamp(a, 0f, 1f);
	}
	
	public float getRed()
	{
		return r;
	}
	
	public float getGreen()
	{
		return g;
	}
	
	public float getBlue()
	{
		return b;
	}
	
	public float getAlpha()
	{
		return a;
	}
}
