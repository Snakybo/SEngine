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
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.Light;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.resource.Material;
import com.snakybo.sengine.resource.Shader;
import com.snakybo.sengine.utils.MappedValues;

public class RenderingEngine extends MappedValues {
	private static final int SAMPLER_LAYER_DIFFUSE = 0;
	private static final int SAMPLER_LAYER_NORMAL_MAP = 1;
	private static final int SAMPLER_LAYER_DISPLACEMENT_MAP = 2;
	
	private static final Shader AMBIENT_SHADER = new Shader("internal/forward-rendering/forward-ambient");
	
	private static Camera mainCamera;
	
	private HashMap<String, Integer> samplerMap;
	
	private List<Light> lights;
	private Light activeLight;
	
	private Window window;
	
	public RenderingEngine(Window window) {
		this.window = window;
		
		samplerMap = new HashMap<String, Integer>();
		lights = new ArrayList<Light>();
		
		samplerMap.put(Material.DIFFUSE, SAMPLER_LAYER_DIFFUSE);
		samplerMap.put(Material.NORMAL_MAP, SAMPLER_LAYER_NORMAL_MAP);
		samplerMap.put(Material.DISP_MAP, SAMPLER_LAYER_DISPLACEMENT_MAP);
		
		setVector3f("ambient", window.getAmbientColor());
		
		initOpenGl();
	}
	
	public void render(GameObject object) {
		window.bindAsRenderTarget();
		
		glClearColor(window.getClearColor().x, window.getClearColor().y, window.getClearColor().z, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		object.renderAll(this, AMBIENT_SHADER);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);
		
		for(Light light : lights) {
			activeLight = light;
			object.renderAll(this, light.getShader());
		}
		
		glDepthMask(true);
		glDepthFunc(GL_LESS);
		glDisable(GL_BLEND);
	}
	
	private void initOpenGl() {
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		//glEnable(GL_DEPTH_CLAMP);
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void setMainCamera(Camera camera) {
		mainCamera = camera;
	}
	
	public static Camera getMainCamera() {
		return mainCamera;
	}
	
	public void addLight(Light light) {
		lights.add(light);
	}
	
	public int getSamplerSlot(String samplerName) {
		return samplerMap.get(samplerName);
	}
	
	public Light getActiveLight() {
		return activeLight;
	}
}
