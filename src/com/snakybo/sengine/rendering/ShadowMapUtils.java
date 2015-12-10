package com.snakybo.sengine.rendering;

import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.math.Matrix4f;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public class ShadowMapUtils
{	
	private static final String SHADOW_MAP_SHADER_NAME = "internal/shadowMapGenerator";
	
	private static Shader shadowMapShader;
	
	static
	{
		shadowMapShader = new Shader(SHADOW_MAP_SHADER_NAME);
	}
	
	public static Shader getShadowMapShader()
	{
		return shadowMapShader;
	}
	
	public static class ShadowInfo
	{
		private Matrix4f projection;

		public ShadowInfo(Matrix4f projection)
		{
			this.projection = projection;
		}

		public Matrix4f getProjection()
		{
			return projection;
		}
	}
}
