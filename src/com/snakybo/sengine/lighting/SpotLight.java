package com.snakybo.sengine.lighting;

import com.snakybo.sengine.lighting.utils.Attenuation;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;

/** This class extends the {@link PointLight} class
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see PointLight */
public class SpotLight extends PointLight
{
	private float cutoff;

	/** Constructor for the component
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @param attenuation The attenuation of the light
	 * @param cutoff The range at which the light should cut off
	 * @see Color
	 * @see Attenuation */
	public SpotLight(Color color, float intensity, Attenuation attenuation, float cutoff)
	{
		super(color, intensity, attenuation);

		this.cutoff = cutoff;

		setShader(new Shader("internal/forward-spot"));
	}

	/** Set the range at which the light should cut off
	 * @param cutoff The new cutoff */
	public void setCutoff(float cutoff)
	{
		this.cutoff = cutoff;
	}

	/** @return The direction the light is pointed at
	 * @see Vector3f */
	public Vector3f getDirection()
	{
		return getTransform().getRotation().getForward();
	}

	/** @return The cutoff of the light */
	public float getCutoff()
	{
		return cutoff;
	}
}
