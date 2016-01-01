package com.snakybo.sengine.lighting;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.object.Component;
import com.snakybo.sengine.rendering.utils.ShadowUtils.ShadowCameraTransform;
import com.snakybo.sengine.rendering.utils.ShadowUtils.ShadowInfo;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;

/** Base class for every light in the game
 * <p>
 * This class extends the {@link Component} class
 * </p>
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component */
public abstract class Light extends Component
{
	private static List<Light> lights = new ArrayList<Light>();
	
	protected ShadowInfo shadowInfo;
	
	protected Shader shader;
	protected Color color;	

	protected float intensity;

	/** Constructor for the component
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @see Color */
	public Light(Color color, float intensity, Shader shader)
	{
		this.color = color;
		this.intensity = intensity;
		this.shader = shader;
		this.shadowInfo = new ShadowInfo();
	}

	@Override
	protected void onAddedToScene()
	{
		lights.add(this);
	}
	
	public ShadowCameraTransform calculateShadowCameraTransform()
	{
		return new ShadowCameraTransform(getTransform().getPosition(), getTransform().getRotation());
	}

	/** @return The shader the light uses
	 * @see Shader */
	public Shader getShader()
	{
		return shader;
	}

	/** @return The shadow information of the light */
	public ShadowInfo getShadowInfo()
	{
		return shadowInfo;
	}

	/** @return The color of the light
	 * @see Color */
	public Color getColor()
	{
		return color;
	}

	/** @return The intensity of the light */
	public float getIntensity()
	{
		return intensity;
	}
	
	public static Iterable<Light> getLights()
	{
		return lights;
	}
}
