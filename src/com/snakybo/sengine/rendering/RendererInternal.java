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
import com.snakybo.sengine.object.GameObject;
import com.snakybo.sengine.rendering.ShadowUtils.ShadowCameraTransform;
import com.snakybo.sengine.rendering.ShadowUtils.ShadowInfo;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.shader.ShaderUniformContainer;
import com.snakybo.sengine.window.Window;

/**
 * @author Kevin
 * @since Apr 4, 2014
 */
public class RendererInternal
{
	private Camera altCamera;
	
	public RendererInternal()
	{	
		altCamera = new Camera(Matrix4f.identity());
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);

		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_MULTISAMPLE);
	}
	
	public void render(GameObject obj)
	{
		Window.bindAsRenderTarget();
		
		Camera camera = Camera.getMainCamera();
		glClearColor(camera.getClearColor().x, camera.getClearColor().y, camera.getClearColor().z, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		obj.render(AmbientLight.getAmbientShader(), camera);
		
		for(Light light : Light.getLights())
		{
			renderLighting(obj, light);
		}
	}
	
	public void postRenderObjects()
	{
		renderSkyBox();
	}
	
	/**
	 * Render the shadows of an object.
	 * @param obj The object to render.
	 * @param light The current light.
	 * @param shadowInfo The shadow info of the object.
	 */
	private void renderShadow(GameObject obj, Light light)
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
			obj.render(ShadowUtils.getShadowMapShader(), altCamera);
			glDisable(GL_DEPTH_CLAMP);
			
			if(shadowInfo.getFlipFaces())
			{
				glCullFace(GL_BACK);
			}
				
			if(shadowInfo.getSoftness() > 0)
			{
				ShadowUtils.blurShadowMap(this, shadowMapIndex, shadowInfo.getSoftness());
			}
		}
		else
		{
			LightUtils.setCurrentLightMatrix(Matrix4f.createScaleMatrix(new Vector3f()));
			ShaderUniformContainer.set("shadowVarianceMin", 0.00002f);
			ShaderUniformContainer.set("shadowLightBleedingReduction", 0.0f);
		}
	}
	
	/**
	 * Render the lighting of an object.
	 * @param obj The object to render.
	 * @param light The current light.
	 */
	private void renderLighting(GameObject obj, Light light)
	{
		LightUtils.setCurrentLight(light);
		
		renderShadow(obj, light);
		
		Window.bindAsRenderTarget();

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);

		obj.render(light.getShader(), Camera.getMainCamera());

		glDepthMask(true);
		glDepthFunc(GL_LESS);
		glDisable(GL_BLEND);
	}
	
	private void renderSkyBox()
	{
		if(AmbientLight.getSkybox() != null)
		{
			AmbientLight.getSkybox().render();
		}
	}
	
	public void applyFilter(Shader filter, Texture src, Texture dest)
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
}