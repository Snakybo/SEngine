package com.snakybo.sengine.components.lighting;

import com.snakybo.sengine.rendering.Attenuation;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;

/** This class extends the {@link Light} class
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Light */
public class PointLight extends Light
{
	private static final int COLOR_DEPTH = 256;

	private Attenuation attenuation;

	private float range;

	/** Constructor for the component
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @param attenuation The attenuation of the light
	 * @see Color
	 * @see Attenuation */
	public PointLight(Color color, float intensity, Attenuation attenuation)
	{
		super(color, intensity);

		this.attenuation = attenuation;

		float a = attenuation.getExponent();
		float b = attenuation.getLinear();
		float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();

		this.range = (float) ((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));

		setShader(new Shader("internal/forward-rendering/forward-point"));
	}

	/** Set the range of the light
	 * @param range The new range */
	public void setRange(float range)
	{
		this.range = range;
	}

	/** @return The range of the light */
	public float getRange()
	{
		return range;
	}

	/** @return The attenuation of the light
	 * @see Attenuation */
	public Attenuation getAttenuation()
	{
		return attenuation;
	}
}
