package com.snakybo.sengine.components.lighting;

import com.snakybo.sengine.utils.math.Matrix4f;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public abstract class LightUtils
{
	private static Light currentLight;
	
	private static Matrix4f currentLightMatrix;
	
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
