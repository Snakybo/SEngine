package com.snakybo.sengine.lighting.utils;

import com.snakybo.sengine.lighting.Light;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Vector3f;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public abstract class LightUtils
{
	private static Light currentLight;	
	private static Matrix4f currentLightMatrix;
	
	static
	{
		currentLightMatrix = Matrix4f.createScaleMatrix(new Vector3f());
	}
	
	public static void setCurrentLight(Light light)
	{
		currentLight = light;
	}
	
	public static void setCurrentLightMatrix(Matrix4f currentMatrix)
	{
		currentLightMatrix = currentMatrix;
	}
	
	public static Light getCurrentLight()
	{
		return currentLight;
	}
	
	public static Matrix4f getCurrentLightMatrix()
	{
		return currentLightMatrix;
	}
}
