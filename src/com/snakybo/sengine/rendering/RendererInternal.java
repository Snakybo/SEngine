package com.snakybo.sengine.rendering;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.lighting.AmbientLight;
import com.snakybo.sengine.lighting.Light;
import com.snakybo.sengine.lighting.utils.LightUtils;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.object.GameObjectInternal;
import com.snakybo.sengine.rendering.ShadowUtils.ShadowCameraTransform;
import com.snakybo.sengine.rendering.ShadowUtils.ShadowInfo;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.shader.ShaderUniformContainer;
import com.snakybo.sengine.skybox.Skybox;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.window.Window;

/**
 * @author Kevin
 * @since Apr 4, 2014
 */
public abstract class RendererInternal
{
	private static Camera altCamera;
	
	static
	{	
		altCamera = new Camera(Matrix4f.identity());
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);

		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_MULTISAMPLE);
	}
	
	public static void preRenderScene()
	{
		Window.bindAsRenderTarget();
		
		Color cc = Camera.getMainCamera().getClearColor();
		glClearColor(cc.getRed(), cc.getGreen(), cc.getBlue(), 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public static void renderScene()
	{
		GameObjectInternal.renderGameObjects(AmbientLight.getAmbientShader(), Camera.getMainCamera());
		
		for(Light light : Light.getLights())
		{
			renderSceneLighting(light);
		}
	}
	
	public static void postRenderScene()
	{
		Skybox skybox = AmbientLight.getSkybox();
		
		if(skybox != null)
		{
			skybox.render();
		}
	}
	
	public static void applyFilter(Shader filter, Texture src, Texture dest)
	{
		if(src == dest)
		{
			throw new IllegalArgumentException("[RenderingEngine] The source texture cannot be the same as the destination texture");
		}
		
		if(dest == null)
		{
			Window.bindAsRenderTarget();
		}
		else
		{
			dest.bindAsRenderTarget();
		}
		
		ShaderUniformContainer.set("filterTexture", src);
		
		altCamera.setProjection(Matrix4f.identity());
		altCamera.getTransform().setPosition(new Vector3f());
		altCamera.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), Math.toRadians(180)));
		
		glClear(GL_DEPTH_BUFFER_BIT);
		
		filter.bind();
		filter.updateUniforms(FilterUtils.getTransform(), FilterUtils.getMaterial(), altCamera);
		FilterUtils.getMesh().draw();
		
		ShaderUniformContainer.set("filterTexture", null);
	}

	private static void renderSceneLighting(Light light)
	{
		LightUtils.setCurrentLight(light);
		
		renderSceneShadow(light);
		
		Window.bindAsRenderTarget();
	
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);
	
		GameObjectInternal.renderGameObjects(light.getShader(), Camera.getMainCamera());
	
		glDepthMask(true);
		glDepthFunc(GL_LESS);
		glDisable(GL_BLEND);
	}

	private static void renderSceneShadow(Light light)
	{
		ShadowInfo shadowInfo = light.getShadowInfo();
		
		int shadowMapIndex = 0;
		if(shadowInfo.getSize() > 0)
		{
			shadowMapIndex = shadowInfo.getSize() - 1;
		}
		
		if(shadowMapIndex < 0 && shadowMapIndex >= ShadowUtils.NUM_SHADOW_MAPS)
		{
			throw new RuntimeException("[RenderingEngine] Invalid shadow map index: " + shadowMapIndex);
		}
		
		ShaderUniformContainer.set("shadowMap", ShadowUtils.getShadowMapAt(shadowMapIndex));
		ShadowUtils.getShadowMapAt(shadowMapIndex).bindAsRenderTarget();
		
		glClearColor(1, 1, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		if(shadowInfo.getSize() > 0)
		{
			ShadowCameraTransform shadowCameraTransform = light.calculateShadowCameraTransform();
			
			altCamera.setProjection(shadowInfo.getProjection());
			altCamera.getTransform().setPosition(shadowCameraTransform.getPosition());
			altCamera.getTransform().setRotation(shadowCameraTransform.getRotation());
			
			LightUtils.setCurrentLightMatrix(ShadowUtils.getShadowMapBiasMatrix(altCamera.getViewProjection()));
			
			ShaderUniformContainer.set("shadowVarianceMin", shadowInfo.getMinVariance());
			ShaderUniformContainer.set("shadowLightBleedingReduction", shadowInfo.getLightBleedingReductionAmount());
			
			if(shadowInfo.getFlipFaces())
			{
				glCullFace(GL_FRONT);
			}
			
			glEnable(GL_DEPTH_CLAMP);
			GameObjectInternal.renderGameObjects(ShadowUtils.getShadowMapShader(), altCamera);
			glDisable(GL_DEPTH_CLAMP);
			
			if(shadowInfo.getFlipFaces())
			{
				glCullFace(GL_BACK);
			}
				
			if(shadowInfo.getSoftness() > 0)
			{
				ShadowUtils.blurShadowMap(shadowMapIndex, shadowInfo.getSoftness());
			}
		}
		else
		{
			LightUtils.setCurrentLightMatrix(Matrix4f.createScaleMatrix(new Vector3f()));
			ShaderUniformContainer.set("shadowVarianceMin", 0.00002f);
			ShaderUniformContainer.set("shadowLightBleedingReduction", 0.0f);
		}
	}
}