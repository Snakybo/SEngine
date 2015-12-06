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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.lighting.BaseLight;
import com.snakybo.sengine.components.lighting.BaseLight.ShadowInfo;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.resource.Material;
import com.snakybo.sengine.resource.Shader;
import com.snakybo.sengine.resource.Texture;
import com.snakybo.sengine.utils.MappedValues;
import com.snakybo.sengine.utils.math.Matrix4f;

/** The rendering engine, this is the main renderer in the engine
 * @author Kevin
 * @since Apr 4, 2014 */
public class RenderingEngine extends MappedValues
{
	private static final int SAMPLER_LAYER_DIFFUSE = 0;
	private static final int SAMPLER_LAYER_NORMAL_MAP = 1;
	private static final int SAMPLER_LAYER_DISPLACEMENT_MAP = 2;
	private static final int SAMPLER_LAYER_SHADOW_MAP = 3;

	private static final Shader AMBIENT_SHADER = new Shader("internal/forward-rendering/forward-ambient");
	private static final Shader SHADOW_MAP_SHADER = new Shader("internal/shadowMapGenerator");

	private static final Matrix4f biasMatrix = new Matrix4f().initScale(0.5f, 0.5f, 0.5f)
			.mul(new Matrix4f().initTranslation(1.0f, 1.0f, 1.0f));

	private static Camera mainCamera;
	private static Camera altCamera;

	private HashMap<String, Integer> samplerMap;

	private List<BaseLight> baseLights;
	private BaseLight activeLight;

	private Window window;
	private Matrix4f lightMatrix;

	/** Constructor for the rendering engine
	 * @param window The window the rendering engine should render in */
	public RenderingEngine(Window window)
	{
		this.window = window;

		samplerMap = new HashMap<String, Integer>();
		baseLights = new ArrayList<BaseLight>();

		altCamera = new Camera(new Matrix4f().initIdentity());

		samplerMap.put(Material.DIFFUSE, SAMPLER_LAYER_DIFFUSE);
		samplerMap.put(Material.NORMAL_MAP, SAMPLER_LAYER_NORMAL_MAP);
		samplerMap.put(Material.DISP_MAP, SAMPLER_LAYER_DISPLACEMENT_MAP);
		samplerMap.put(Material.SHADOW_MAP, SAMPLER_LAYER_SHADOW_MAP);

		setVector3f("ambient", window.getAmbientColor());
		setTexture("shadowMap", new Texture(1024, 1024, null, GL_TEXTURE_2D, GL_NEAREST, GL_DEPTH_COMPONENT16,
				GL_DEPTH_COMPONENT, true, GL_DEPTH_ATTACHMENT));

		initOpenGl();
	}

	/** Render the object passed in, calls {@link #renderLighting(GameObject)}
	 * when the object has been rendered using the ambient shader
	 * @param object The object to render */
	public void render(GameObject object)
	{
		window.bindAsRenderTarget();

		glClearColor(window.getClearColor().x, window.getClearColor().y, window.getClearColor().z, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		object.renderAll(this, AMBIENT_SHADER);

		for (BaseLight light : baseLights)
		{
			activeLight = light;
			ShadowInfo shadowInfo = activeLight.getShadowInfo();

			// Render the shadows
			getTexture("shadowMap").bindAsRenderTarget();
			glClear(GL_DEPTH_BUFFER_BIT);

			if (shadowInfo != null)
			{
				altCamera.setProjection(shadowInfo.getProjection());
				altCamera.getTransform().getPosition().set(activeLight.getTransform().getPosition());
				altCamera.getTransform().setRotation(activeLight.getTransform().getRotation());

				lightMatrix = biasMatrix.mul(altCamera.getViewProjection());

				Camera temp = mainCamera;
				mainCamera = altCamera;

				object.renderAll(this, SHADOW_MAP_SHADER);

				mainCamera = temp;
			}

			// Render the lighting
			window.bindAsRenderTarget();

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

	/** Add a light to the rendering engine
	 * @param light The light to add to the rendering engine */
	public final void addLight(BaseLight light)
	{
		baseLights.add(light);
	}

	/** Set the main camera
	 * @param camera The camera to use */
	public final static void setMainCamera(Camera camera)
	{
		mainCamera = camera;
	}

	/** @return The main camera */
	public final static Camera getMainCamera()
	{
		return mainCamera;
	}

	/** @return The element in the sampler map with the name passed in
	 * @param samplerName The name of the sampler slot */
	public final int getSamplerSlot(String samplerName)
	{
		return samplerMap.get(samplerName);
	}

	/** @return The active light, this changes depending on which light is
	 *         currently being used to render an object */
	public final BaseLight getActiveLight()
	{
		return activeLight;
	}

	public final Matrix4f getLightMatrix()
	{
		return lightMatrix;
	}
}
