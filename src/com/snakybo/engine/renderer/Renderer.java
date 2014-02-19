package com.snakybo.engine.renderer;

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
import java.util.List;

import com.snakybo.engine.components.BaseLight;
import com.snakybo.engine.core.GameObject;
import com.snakybo.engine.core.Vector3f;

/** @author Kevin Krol
 *  @since Feb 8, 2014 */
public class Renderer {
	private Camera mainCamera;
	
	private Vector3f ambientLight;
	
	private List<BaseLight> lights;
	
	private BaseLight activeLight;
	
	public Renderer() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_DEPTH_CLAMP);
		
		glEnable(GL_TEXTURE_2D);
		
		mainCamera = new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(), 0.1f, 1000.0f);
		
		ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);
		
		lights = new ArrayList<BaseLight>();
	}
	
	//TODO remove this shit
	public void input(float delta) {
		mainCamera.input(delta);
	}
	
	/** Render a game object
	 * @param object The game object to render */
	public void render(GameObject object) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		lights.clear();
		object.addToRenderer(this);
		
		Shader forwardAmbient = ForwardAmbient.getInstance();
		forwardAmbient.setRenderer(this);
		
		object.render(forwardAmbient);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);
		
		for(BaseLight light : lights) {
			activeLight = light;
			
			light.getShader().setRenderer(this);
			object.render(light.getShader());
		}
		
		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public void addLight(BaseLight light) {
		lights.add(light);
	}
	
	public void setCamera(Camera camera) { this.mainCamera = camera; }
	
	public Camera getCamera() { return mainCamera; }
	
	public Vector3f getAmbientLight() { return ambientLight; }
	public BaseLight getActiveLight() {	return activeLight;	}
}
