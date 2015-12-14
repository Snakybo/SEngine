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
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_RG32F;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.lighting.AmbientLight;
import com.snakybo.sengine.lighting.Light;
import com.snakybo.sengine.lighting.utils.LightUtils;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.rendering.utils.FilterUtils;
import com.snakybo.sengine.rendering.utils.ShadowUtils;
import com.snakybo.sengine.rendering.utils.ShadowUtils.ShadowCameraTransform;
import com.snakybo.sengine.rendering.utils.ShadowUtils.ShadowInfo;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.skybox.Skybox;

/**
 * @author Kevin
 * @since Apr 4, 2014
 */
public class RenderingEngine
{
	private static final Matrix4f SHADOW_MAP_BIAS_MATRIX = Matrix4f.createScaleMatrix(0.5f, 0.5f, 0.5f).mul(Matrix4f.createTranslationMatrix(1, 1, 1));
	
	private static Skybox skyBox;
	private static EnumSet<RenderFlag> renderFlags = EnumSet.of(RenderFlag.NORMAL);
	
	private Map<String, Integer> samplerMap;
	private Map<String, Object> dataContainer;
	
	private Camera shadowMapCamera;
	
	public RenderingEngine()
	{		
		samplerMap = new HashMap<String, Integer>();
		samplerMap.put("diffuse", 0);
		samplerMap.put("normalMap", 1);
		samplerMap.put("dispMap", 2);
		samplerMap.put("shadowMap", 3);
		samplerMap.put("filterTexture", 0);
		
		dataContainer = new HashMap<String, Object>();
		dataContainer.put("ambient", AmbientLight.getAmbientColor());
		
		for(int i = 0; i < ShadowUtils.getNumShadowMaps(); i++)
		{
			int size = 1 << (i + 1);			
			ShadowUtils.setShadowMapAt(i, new Texture(size, size, null, GL_TEXTURE_2D, GL_LINEAR, GL_RG32F, GL_RGBA, true, GL_COLOR_ATTACHMENT0));
		}
		
		shadowMapCamera = new Camera(Matrix4f.identity());
		LightUtils.setCurrentLightMatrix(Matrix4f.createScaleMatrix(new Vector3f()));
		
		initializeGL();
	}
	
	public void render(GameObject obj)
	{
		Camera mainCamera = Camera.getMainCamera();	
		
		Window.bindAsRenderTarget();
		
		glClearColor(mainCamera.getClearColor().x, mainCamera.getClearColor().y, mainCamera.getClearColor().z, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		obj.render(this, AmbientLight.getAmbientShader(), mainCamera);
		
		if(!renderFlags.contains(RenderFlag.WIREFRAME) && !renderFlags.contains(RenderFlag.NO_LIGHTING))
		{
			for(Light light : Light.getLights())
			{
				renderLighting(obj, light);
			}
		}
	}
	
	public void postRenderObjects()
	{
		if(!renderFlags.contains(RenderFlag.NO_SKYBOX))
		{
			renderSkyBox();
		}
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
		
		if(shadowMapIndex < 0 && shadowMapIndex >= ShadowUtils.getNumShadowMaps())
		{
			throw new RuntimeException("[RenderingEngine] Invalid shadow map index: " + shadowMapIndex);
		}
		
		set("shadowMap", ShadowUtils.getShadowMapAt(shadowMapIndex));
		ShadowUtils.getShadowMapAt(shadowMapIndex).bindAsRenderTarget();
		
		glClearColor(1, 1, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		if(shadowInfo.getSize() > 0 && !renderFlags.contains(RenderFlag.NO_SHADOWS))
		{
			ShadowCameraTransform shadowCameraTransform = light.calculateShadowCameraTransform();
			
			shadowMapCamera.setProjection(shadowInfo.getProjection());
			shadowMapCamera.getTransform().setPosition(shadowCameraTransform.getPosition());
			shadowMapCamera.getTransform().setRotation(shadowCameraTransform.getRotation());
			
			LightUtils.setCurrentLightMatrix(SHADOW_MAP_BIAS_MATRIX.mul(shadowMapCamera.getViewProjection()));
			
			set("shadowVarianceMin", shadowInfo.getMinVariance());
			set("shadowLightBleedingReduction", shadowInfo.getLightBleedingReductionAmount());
			
			if(shadowInfo.getFlipFaces())
			{
				glCullFace(GL_FRONT);
			}
			
			glEnable(GL_DEPTH_CLAMP);
			obj.render(this, ShadowUtils.getShadowMapShader(), shadowMapCamera);
			glDisable(GL_DEPTH_CLAMP);
			
			if(shadowInfo.getFlipFaces())
			{
				glCullFace(GL_BACK);
			}
				
			if(shadowInfo.getSoftness() > 0)
			{
				blurShadowMap(shadowMapIndex, shadowInfo.getSoftness());
			}
		}
		else
		{
			LightUtils.setCurrentLightMatrix(Matrix4f.createScaleMatrix(new Vector3f()));
			set("shadowVarianceMin", 0.00002f);
			set("shadowLightBleedingReduction", 0.0f);
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

		obj.render(this, light.getShader(), Camera.getMainCamera());

		glDepthMask(true);
		glDepthFunc(GL_LESS);
		glDisable(GL_BLEND);
	}
	
	private void renderSkyBox()
	{
		if(skyBox != null)
		{
			skyBox.render(this);
		}
	}
	
	private void blurShadowMap(int shadowMapIndex, float amount)
	{
		Texture shadowMap = ShadowUtils.getShadowMapAt(shadowMapIndex);
		Texture tempShadowMap = ShadowUtils.getTempShadowMapAt(shadowMapIndex);
		
		set("blurScale", new Vector3f(amount / shadowMap.getWidth(), 0, 0));
		applyFilter(FilterUtils.getShader(), shadowMap, tempShadowMap);
		
		set("blurScale", new Vector3f(0, amount / shadowMap.getHeight(), 0));
		applyFilter(FilterUtils.getShader(), shadowMap, tempShadowMap);
	}
	
	private void applyFilter(Shader filter, Texture src, Texture dest)
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
		
		set("filterTexture", src);
		
		shadowMapCamera.setProjection(Matrix4f.identity());
		shadowMapCamera.getTransform().setPosition(new Vector3f());
		shadowMapCamera.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), Math.toRadians(180)));
		
		glClear(GL_DEPTH_BUFFER_BIT);
		
		filter.bind();
		filter.updateUniforms(FilterUtils.getTransform(), FilterUtils.getMaterial(), this, shadowMapCamera);
		FilterUtils.getMesh().draw();
		
		set("filterTexture", null);
	}
	
	/**
	 * Initialize OpenGL.
	 */
	private void initializeGL()
	{
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);

		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
	}
	
	public void set(String name, Object value)
	{
		dataContainer.put(name, value);
	}
	
	public <T> T get(Class<T> type, String name)
	{
		if(!dataContainer.containsKey(name))
		{
			throw new IllegalArgumentException("[RenderingEngine] No data with the name: " + name + " found.");
		}
		
		return type.cast(dataContainer.get(name));
	}
	
	public int getTextureSamplerSlot(String samplerName)
	{
		if(samplerMap.containsKey(samplerName))
		{
			return samplerMap.get(samplerName);
		}
		
		throw new IllegalArgumentException("[RenderingEngine] No texture sampler slot found for sampler with name: " + samplerName);
	}
	
	public static void setSkybox(Skybox skyBox)
	{
		RenderingEngine.skyBox = skyBox;
	}
	
	public static void addRenderFlag(RenderFlag flag)
	{
		renderFlags.add(flag);
	}
	
	public static void removeRenderFlg(RenderFlag flag)
	{
		renderFlags.remove(flag);
	}
	
	public static void setRenderFlag(RenderFlag flag)
	{
		renderFlags = EnumSet.of(flag);
	}
	
	public static EnumSet<RenderFlag> getRenderFlags()
	{
		return renderFlags;
	}
}
