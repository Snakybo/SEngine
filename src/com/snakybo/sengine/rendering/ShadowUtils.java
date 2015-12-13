package com.snakybo.sengine.rendering;

import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public class ShadowUtils
{	
	private static final String SHADOW_MAP_SHADER_NAME = "internal/shadowMapGenerator";
	private static final int NUM_SHADOW_MAPS = 10;
	
	private static Texture[] shadowMaps;
	private static Texture[] tempShadowMaps;
	
	private static Shader shadowMapShader;
	
	static
	{
		shadowMaps = new Texture[NUM_SHADOW_MAPS];
		tempShadowMaps = new Texture[NUM_SHADOW_MAPS];
		
		shadowMapShader = new Shader(SHADOW_MAP_SHADER_NAME);
	}
	
	public static Texture[] getShadowMaps()
	{
		return shadowMaps;
	}
	
	public static Texture[] getTempShadowMaps()
	{
		return tempShadowMaps;
	}
	
	public static Shader getShadowMapShader()
	{
		return shadowMapShader;
	}
	
	public static final class ShadowCameraTransform
	{
		private Vector3f position;
		private Quaternion rotation;
		
		public ShadowCameraTransform(Vector3f position, Quaternion rotation)
		{
			this.position = position;
			this.rotation = rotation;
		}
		
		public final Vector3f getPosition()
		{
			return position;
		}
		
		public final Quaternion getRotation()
		{
			return rotation;
		}
	}
	
	public static final class ShadowInfo
	{
		private Matrix4f projection;
		
		private float softness;
		private float lightBleedReductionAmount;
		private float minVariance;
		
		private int size;		
		
		private boolean flipFaces;
		
		public ShadowInfo()
		{
			this(Matrix4f.identity(), false, 0, 1, 0.2f, 0.00002f);
		}

		public ShadowInfo(Matrix4f projection, boolean flipFaces, int size, float softness, float lightBleedReductionAmount, float minVariance)
		{
			this.projection = projection;
			this.flipFaces = flipFaces;
			this.size = size;
			this.softness = softness;
			this.lightBleedReductionAmount = lightBleedReductionAmount;
			this.minVariance = minVariance;
		}

		public final Matrix4f getProjection()
		{
			return projection;
		}
		
		public final float getSoftness()
		{
			return softness;
		}
		
		public final float getLightBleedReductionAmount()
		{
			return lightBleedReductionAmount;
		}
		
		public final float getMinVariance()
		{
			return minVariance;
		}
		
		public int getSize()
		{
			return size;
		}
		
		public boolean getFlipFaces()
		{
			return flipFaces;
		}
	}
}
