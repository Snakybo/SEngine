/**
 * 
 */
package com.snakybo.sengine.lighting;

import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public abstract class AmbientLight
{
	private static final String AMBIENT_SHADER_NAME = "internal/forward-ambient";
	
	private static Color ambientColor;
	private static Shader ambientShader;
	
	static
	{
		ambientColor = new Color(0, 0, 0);
		ambientShader = new Shader(AMBIENT_SHADER_NAME);
	}
	
	public static void setAmbientColor(Color color)
	{
		ambientColor = color;
	}
	
	public static Color getAmbientColor()
	{
		return ambientColor;
	}
	
	public static Shader getAmbientShader()
	{
		return ambientShader;
	}
}
