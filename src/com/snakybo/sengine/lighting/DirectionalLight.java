package com.snakybo.sengine.lighting;

import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.rendering.ShadowMapUtils.ShadowInfo;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;

/** This class extends the {@link Light} class
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Light */
public class DirectionalLight extends Light
{
	/** Constructor for the component
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @see Color */
	public DirectionalLight(Color color, float intensity)
	{
		super(color, intensity);

		setShader(new Shader("internal/forward-directional"));
		setShadowInfo(new ShadowInfo(Matrix4f.orthographic(-40, 40, -40, 40, -40, 40)));
	}

	/** @return The direction the light is pointed at
	 * @see Vector3f */
	public Vector3f getDirection()
	{
		return getTransform().getRotation().getForward();
	}
}
