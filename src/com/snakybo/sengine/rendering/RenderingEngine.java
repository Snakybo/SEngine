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

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.lighting.Light;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.rendering.ShadowMap.ShadowInfo;
import com.snakybo.sengine.resource.Texture;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.utils.IDataContainer;
import com.snakybo.sengine.utils.math.Matrix4f;

/** The rendering engine, this is the main renderer in the engine
 * @author Kevin
 * @since Apr 4, 2014 */
public class RenderingEngine implements IDataContainer
{
	private static final Shader AMBIENT_SHADER = new Shader("internal/forward-rendering/forward-ambient");
	private static final Shader SHADOW_MAP_SHADER = new Shader("internal/shadowMapGenerator");

	private static final Matrix4f biasMatrix = new Matrix4f().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4f().initTranslation(1.0f, 1.0f, 1.0f));
	
	private static Color ambientColor;

	private Map<String, Integer> samplerMap;
	private Map<String, Object> data;
	
	private Light activeLight;
	
	private Camera shadowMapCamera;
	private Matrix4f lightMatrix;

	/** Constructor for the rendering engine
	 * @param window The window the rendering engine should render in */
	public RenderingEngine()
	{
		samplerMap = new HashMap<String, Integer>();
		data = new HashMap<String, Object>();

		shadowMapCamera = new Camera(new Matrix4f().initIdentity());

		samplerMap.put("diffuse", 0);
		samplerMap.put("normalMap", 1);
		samplerMap.put("dispMap", 2);
		samplerMap.put("shadowMap", 3);
		
		data.put("ambient", ambientColor);
		data.put("shadowMap", new Texture(1024, 1024, null, GL_TEXTURE_2D, GL_NEAREST, GL_DEPTH_COMPONENT16, GL_DEPTH_COMPONENT, true, GL_DEPTH_ATTACHMENT));

		initOpenGl();
	}

	/** Render the object passed in, calls {@link #renderLighting(GameObject)}
	 * when the object has been rendered using the ambient shader
	 * @param object The object to render */
	public void render(GameObject object)
	{
		Window.bindAsRenderTarget();

		Camera mainCamera = Camera.getMainCamera();		
		glClearColor(mainCamera.getClearColor().x, mainCamera.getClearColor().y, mainCamera.getClearColor().z, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		object.renderAll(this, AMBIENT_SHADER);

		for(Light light : Light.getLights())
		{
			activeLight = light;
			ShadowInfo shadowInfo = activeLight.getShadowInfo();

			// Render the shadows
			get(Texture.class, "shadowMap").bindAsRenderTarget();
			glClear(GL_DEPTH_BUFFER_BIT);

			if(shadowInfo != null)
			{
				shadowMapCamera.setProjection(shadowInfo.getProjection());
				shadowMapCamera.getTransform().getPosition().set(activeLight.getTransform().getPosition());
				shadowMapCamera.getTransform().setRotation(activeLight.getTransform().getRotation());

				lightMatrix = biasMatrix.mul(shadowMapCamera.getViewProjection());

				Camera tempCamera = mainCamera;
				Camera.setMainCamera(shadowMapCamera);

				glCullFace(GL_FRONT);
				object.renderAll(this, SHADOW_MAP_SHADER);
				glCullFace(GL_BACK);

				Camera.setMainCamera(tempCamera);
			}

			// Render the lighting
			Window.bindAsRenderTarget();

			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			glDepthMask(false);
			glDepthFunc(GL_EQUAL);

			object.renderAll(this, light.getShader());

			glDepthMask(true);
			glDepthFunc(GL_LESS);
			glDisable(GL_BLEND);
		}
	}

	/** Initialize OpenGL */
	private final void initOpenGl()
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
		data.put(name, value);
	}
	
	@Override
	public <T extends Object> T get(Class<T> type, String name)
	{
		if(!data.containsKey(name))
		{
			throw new IllegalArgumentException("No data with the name: " + name + " found.");
		}
		
		return type.cast(data.get(name));
	}
	
	public final static void setAmbientColor(Color color)
	{
		ambientColor =  color;
	}

	/** @return The element in the sampler map with the name passed in
	 * @param samplerName The name of the sampler slot */
	public final int getSamplerSlot(String samplerName)
	{
		return samplerMap.get(samplerName);
	}

	/** @return The active light, this changes depending on which light is
	 *         currently being used to render an object */
	public final Light getActiveLight()
	{
		return activeLight;
	}

	public final Matrix4f getLightMatrix()
	{
		return lightMatrix;
	}
}
