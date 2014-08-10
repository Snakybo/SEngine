package com.snakybo.sengine.components.lighting;

import com.snakybo.sengine.resource.Shader;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.utils.math.Matrix4f;
import com.snakybo.sengine.utils.math.Vector3f;

/** This class extends the {@link BaseLight} class
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see BaseLight */
public class DirectionalLight extends BaseLight {
	/** Constructor for the component
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @see Color */
	public DirectionalLight(Color color, float intensity) {
		super(color, intensity);
		
		setShader(new Shader("internal/forward-rendering/forward-directional"));
		setShadowInfo(new ShadowInfo(new Matrix4f().initOrthographic(-40, 40, -40, 40, -40, 40)));
	}
	
	/** @return The direction the light is pointed at
	 * @see Vector3f */
	public Vector3f getDirection() {
		return getTransform().getRotation().getForward();
	}
}
