package com.snakybo.sengine.rendering;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_ONE;
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
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
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
import com.snakybo.sengine.rendering.ShadowMapUtils.ShadowInfo;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.skybox.Skybox;

/**
 * @author Kevin
 * @since Apr 4, 2014
 */
public class RenderingEngine implements IRenderingEngine
{
	private static final Matrix4f SHADOW_MAP_BIAS_MATRIX = Matrix4f.createScaleMatrix(0.5f, 0.5f, 0.5f).mul(Matrix4f.createTranslationMatrix(1, 1, 1));
	
	private static Skybox skyBox;
	private static EnumSet<RenderFlag> renderMode = EnumSet.of(RenderFlag.NORMAL);
	
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
		
		dataContainer = new HashMap<String, Object>();
		dataContainer.put("ambient", AmbientLight.getAmbientColor());
		dataContainer.put("shadowMap", new Texture(1024, 1024, null, GL_TEXTURE_2D, GL_NEAREST, GL_DEPTH_COMPONENT16, GL_DEPTH_COMPONENT, true, GL_DEPTH_ATTACHMENT));
		
		shadowMapCamera = new Camera(Matrix4f.identity());
		
		initializeGL();
	}
	
	@Override
	public void render(GameObject obj)
	{
		Camera mainCamera = Camera.getMainCamera();	
		
		Window.bindAsRenderTarget();
		
		glClearColor(mainCamera.getClearColor().x, mainCamera.getClearColor().y, mainCamera.getClearColor().z, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		obj.render(this, AmbientLight.getAmbientShader());
		
		if(!renderMode.contains(RenderFlag.WIREFRAME) && !renderMode.contains(RenderFlag.NO_LIGHTING))
		{
			for(Light light : Light.getLights())
			{
				renderLighting(obj, light);
			}
		}
	}
	
	@Override
	public void postRenderObjects()
	{
		if(!renderMode.contains(RenderFlag.NO_SKYBOX))
		{
			renderSkyBox();
		}
	}
	
	@Override
	public int getTextureSamplerSlot(String samplerName)
	{
		if(samplerMap.containsKey(samplerName))
		{
			return samplerMap.get(samplerName);
		}
		
		throw new IllegalArgumentException("[RenderingEngine] No texture sampler slot found for sampler with name: " + samplerName);
	}
	
	/**
	 * Render the shadows of an object.
	 * @param obj The object to render.
	 * @param light The current light.
	 * @param shadowInfo The shadow info of the object.
	 */
	private void renderShadow(GameObject obj, Light light, ShadowInfo shadowInfo)
	{
		get(Texture.class, "shadowMap").bindAsRenderTarget();
		
		glClear(GL_DEPTH_BUFFER_BIT);

		if(shadowInfo != null)
		{
			shadowMapCamera.setProjection(shadowInfo.getProjection());
			shadowMapCamera.getTransform().getPosition().set(light.getTransform().getPosition());
			shadowMapCamera.getTransform().setRotation(light.getTransform().getRotation());

			LightUtils.setCurrentLightMatrix(SHADOW_MAP_BIAS_MATRIX.mul(shadowMapCamera.getViewProjection()));

			if(!renderMode.contains(RenderFlag.NO_SHADOWS))
			{
				Camera tempCamera = Camera.getMainCamera();
				Camera.setMainCamera(shadowMapCamera);
	
				glCullFace(GL_FRONT);
				obj.render(this, ShadowMapUtils.getShadowMapShader());
				glCullFace(GL_BACK);
	
				Camera.setMainCamera(tempCamera);
			}
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
		ShadowInfo shadowInfo = light.getShadowInfo();
		
		renderShadow(obj, light, shadowInfo);
		
		Window.bindAsRenderTarget();

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);

		obj.render(this, light.getShader());

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
	
	/**
	 * Initialize OpenGL.
	 */
	private void initializeGL()
	{
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);

		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_DEPTH_CLAMP);
		glEnable(GL_TEXTURE_2D);
	}

	@Override
	public void set(String name, Object value)
	{
		dataContainer.put(name, value);
	}

	@Override
	public <T> T get(Class<T> type, String name)
	{
		if(!dataContainer.containsKey(name))
		{
			throw new IllegalArgumentException("[RenderingEngine] No data with the name: " + name + " found.");
		}
		
		return type.cast(dataContainer.get(name));
	}
	
	public static void setSkybox(Skybox skyBox)
	{
		RenderingEngine.skyBox = skyBox;
	}
	
	public static void setRenderingMode(RenderFlag flag)
	{
		renderMode.add(flag);
	}
	
	public static void removeRenderFlg(RenderFlag flag)
	{
		renderMode.remove(flag);
	}
	
	public static EnumSet<RenderFlag> getRenderMode()
	{
		return renderMode;
	}
}
