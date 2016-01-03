package com.snakybo.sengine.lighting.utils;

/**
 * @author Kevin
 * @since Apr 4, 2014
 */
public final class Attenuation
{
	public float constant;
	public float linear;
	public float exponent;
	
	public Attenuation()
	{
		this(0);
	}
	
	public Attenuation(float constant)
	{
		this(constant, 0);
	}
	
	public Attenuation(float constant, float linear)
	{
		this(constant, linear, 1);
	}
	
	public Attenuation(float constant, float linear, float exponent)
	{
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
	}
}