package com.snakybo.sengine.lighting;

import com.snakybo.sengine.lighting.utils.Attenuation;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public class PointLight extends Light
{
	private static final int COLOR_DEPTH = 256;
	
	private Attenuation attenuation;

	private float range;
	
	public PointLight()
	{
		this(new Color());
	}
	
	public PointLight(Color color)
	{
		this(color, 0);
	}
	
	public PointLight(Color color, float intensity)
	{
		this(color, intensity, new Attenuation());
	}
	
	public PointLight(Color color, float intensity, Attenuation attenuation)
	{
		super(color, intensity, new Shader("internal/forward-point"));
		
		float a = attenuation.getExponent();
		float b = attenuation.getLinear();
		float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();

		this.range = (float)((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));
		this.attenuation = attenuation;
	}
	
	public float getRange()
	{
		return range;
	}
	
	public Attenuation getAttenuation()
	{
		return attenuation;
	}
}
