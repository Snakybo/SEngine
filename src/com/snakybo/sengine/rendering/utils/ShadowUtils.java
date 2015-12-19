package com.snakybo.sengine.rendering.utils;

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
	
	public static void setShadowMapAt(int index, Texture texture)
	{
		shadowMaps[index] = texture;
		tempShadowMaps[index] = new Texture(texture);
	}
	
	public static Texture getShadowMapAt(int index)
	{
		return shadowMaps[index];
	}
	
	public static Texture getTempShadowMapAt(int index)
	{
		return tempShadowMaps[index];
	}
	
	public static Shader getShadowMapShader()
	{
		return shadowMapShader;
	}
	
	public static int getNumShadowMaps()
	{
		return NUM_SHADOW_MAPS;
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
		private float lightBleedingReductionAmount;
		private float minVariance;
		
		private int size;		
		
		private boolean flipFaces;
		
		public ShadowInfo()
		{
			this(Matrix4f.identity(), false, 0, 1, 0.2f, 0.00002f);
		}

		public ShadowInfo(Matrix4f projection, boolean flipFaces, int size, float softness, float lightBleedingReductionAmount, float minVariance)
		{
			this.projection = projection;
			this.flipFaces = flipFaces;
			this.size = size;
			this.softness = softness;
			this.lightBleedingReductionAmount = lightBleedingReductionAmount;
			this.minVariance = minVariance;
		}
		
		@Override
		public String toString() {
			return "flipFaces: " + flipFaces + ", size: " + size + ", softness: " + softness + ", lightBleedingReductionAmount: " + lightBleedingReductionAmount + ", minVariance: " + minVariance;
		}

		public final Matrix4f getProjection()
		{
			return projection;
		}
		
		public final float getSoftness()
		{
			return softness;
		}
		
		public final float getLightBleedingReductionAmount()
		{
			return lightBleedingReductionAmount;
		}
		
		public final float getMinVariance()
		{
			return minVariance;
		}
		
		public final int getSize()
		{
			return size;
		}
		
		public final boolean getFlipFaces()
		{
			return flipFaces;
		}
	}
}