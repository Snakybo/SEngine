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
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.util.ArrayList;
import java.util.HashMap;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.Light;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.resource.Material;
import com.snakybo.sengine.resource.Shader;
import com.snakybo.sengine.utils.MappedValues;
import com.snakybo.sengine.utils.math.Vector3f;

public class RenderingEngine extends MappedValues {
	private static RenderingEngine instance = null;
	
	private static final int SAMPLER_LAYER_DIFFUSE = 0;
	private static final int SAMPLER_LAYER_NORMAL_MAP = 1;
	private static final int SAMPLER_LAYER_DISPLACEMENT_MAP = 2;
	
	private static HashMap<String, Integer> samplerMap;
	
	private static ArrayList<Light> lights;
	
	private static Light activeLight;
	
	private static Shader ambientShader;
	
	private static Camera mainCamera;
	
	public static void create(Vector3f ambientColor, Vector3f clearColor) {
		if(instance != null)
			return;
		
		instance = new RenderingEngine();
		
		lights = new ArrayList<Light>();
		samplerMap = new HashMap<String, Integer>();
		
		samplerMap.put(Material.DIFFUSE, SAMPLER_LAYER_DIFFUSE);
		samplerMap.put(Material.NORMAL_MAP, SAMPLER_LAYER_NORMAL_MAP);
		samplerMap.put(Material.DISP_MAP, SAMPLER_LAYER_DISPLACEMENT_MAP);
		
		instance.setVector3f("ambient", ambientColor);
		
		ambientShader = new Shader("internal/forward-rendering/forward-ambient");
		
		glClearColor(clearColor.x, clearColor.y, clearColor.z, 1.0f);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_DEPTH_CLAMP);
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void render(GameObject object) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		object.renderAll(ambientShader);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);
		
		for(Light light : lights) {
			activeLight = light;
			object.renderAll(light.getShader());
		}
		
		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void addLight(Light light) {
		lights.add(light);
	}
	
	public static void addCamera(Camera camera) {
		mainCamera = camera;
	}
	
	public static int getSamplerSlot(String samplerName) {
		return samplerMap.get(samplerName);
	}
	
	public static Light getActiveLight() {
		return activeLight;
	}
	
	public static Camera getMainCamera() {
		return mainCamera;
	}
	
	public static Vector3f rGetVector3f(String name) {
		return instance.getVector3f(name);
	}
	
	public static float rGetFloat(String name) {
		return instance.getFloat(name);
	}
}
