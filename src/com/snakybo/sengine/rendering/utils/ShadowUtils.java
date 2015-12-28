package com.snakybo.sengine.rendering.utils;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_RG32F;

import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.shader.ShaderUniformContainer;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public class ShadowUtils
{	
	public static final int NUM_SHADOW_MAPS = 10;
	public static final Matrix4f SHADOW_MAP_BIAS_MATRIX = Matrix4f.createScaleMatrix(0.5f, 0.5f, 0.5f).mul(Matrix4f.createTranslationMatrix(1, 1, 1));
	
	private static final String SHADOW_MAP_SHADER_NAME = "internal/shadowMapGenerator";
	
	private static Texture[] shadowMaps;
	private static Texture[] tempShadowMaps;
	
	private static Shader shadowMapShader;
	
	static
	{
		shadowMaps = new Texture[NUM_SHADOW_MAPS];
		tempShadowMaps = new Texture[NUM_SHADOW_MAPS];
		
		shadowMapShader = new Shader(SHADOW_MAP_SHADER_NAME);
		
		for(int i = 0; i < NUM_SHADOW_MAPS; i++)
		{
			int size = 1 << (i + 1);			
			setShadowMapAt(i, new Texture(size, size, null, GL_TEXTURE_2D, GL_LINEAR, GL_RG32F, GL_RGBA, true, GL_COLOR_ATTACHMENT0));
		}
	}
	
	public static void blurShadowMap(RenderingEngine renderingEngine, int shadowMapIndex, float amount)
	{
		Texture shadowMap = ShadowUtils.getShadowMapAt(shadowMapIndex);
		Texture tempShadowMap = ShadowUtils.getTempShadowMapAt(shadowMapIndex);
		
		ShaderUniformContainer.set("blurScale", new Vector3f(amount / shadowMap.getWidth(), 0, 0));
		renderingEngine.applyFilter(FilterUtils.getShader(), shadowMap, tempShadowMap);
		
		ShaderUniformContainer.set("blurScale", new Vector3f(0, amount / shadowMap.getHeight(), 0));
		renderingEngine.applyFilter(FilterUtils.getShader(), tempShadowMap, shadowMap);
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
