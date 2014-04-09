package com.snakybo.sengine.rendering;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.util.ArrayList;
import java.util.HashMap;

import com.snakybo.sengine.components.BaseLight;
import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.core.GameObject;
import com.snakybo.sengine.core.Transform;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.resourceManagement.MappedValues;

/** The rendering engine extends {@link MappedValues}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class RenderingEngine extends MappedValues {
	private HashMap<String, Integer> samplerMap;
	private ArrayList<BaseLight> lights;
	private BaseLight activeLight;
	
	private Shader forwardAmbient;
	private Camera mainCamera;
	
	/** Constructor for the rendering engine */
	public RenderingEngine() {
		super();
		
		lights = new ArrayList<BaseLight>();
		samplerMap = new HashMap<String, Integer>();
		samplerMap.put("diffuse", 0);
		
		addVector3f("ambient", new Vector3f(0.1f, 0.1f, 0.1f));
		
		forwardAmbient = new Shader("forward-ambient");
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_DEPTH_CLAMP);
		
		glEnable(GL_TEXTURE_2D);
	}
	
	/** Update shader uniforms with structs
	 * @param transform The transform of the game object
	 * @param material The material of the game object
	 * @param shader The active shader
	 * @param uniformName The name of the uniform
	 * @param uniformType The type of the uniform */
	public void updateUniformStruct(Transform transform, Material material, Shader shader, String uniformName,
			String uniformType) {
		throw new IllegalArgumentException(uniformType + " is not a supported type in RenderingEngine");
	}
	
	/** Render a game object
	 * @param gameObject The game object to render */
	public void render(GameObject gameObject) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		gameObject.renderAll(forwardAmbient, this);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);
		
		for(BaseLight light : lights) {
			activeLight = light;
			gameObject.renderAll(light.getShader(), this);
		}
		
		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	/** Add a light to the rendering engine
	 * @param light The light to add */
	public void addLight(BaseLight light) {
		lights.add(light);
	}
	
	/** Add a camera to the rendering engine
	 * @param camera The camera to add */
	public void addCamera(Camera camera) {
		mainCamera = camera;
	}
	
	/** Set the main camera
	 * @param mainCamera The new main camera */
	public void setMainCamera(Camera mainCamera) {
		this.mainCamera = mainCamera;
	}
	
	/** @return The OpenGL version of the user's system */
	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}
	
	/** @return The sampler in the specified slot
	 * @param samplerName The name of the sampler */
	public int getSamplerSlot(String samplerName) {
		return samplerMap.get(samplerName);
	}
	
	/** @return The active light */
	public BaseLight getActiveLight() {
		return activeLight;
	}
	
	/** @return The main camera */
	public Camera getMainCamera() {
		return mainCamera;
	}
}
