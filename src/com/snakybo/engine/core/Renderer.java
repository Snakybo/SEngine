package com.snakybo.engine.core;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import com.snakybo.engine.rendering.BasicShader;
import com.snakybo.engine.rendering.Camera;
import com.snakybo.engine.rendering.Shader;
import com.snakybo.engine.rendering.Window;

/** @author Kevin Krol
 *  @since Feb 8, 2014 */
public class Renderer {
	private Camera mainCamera;
	
	public Renderer() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_DEPTH_CLAMP);
		
		glEnable(GL_TEXTURE_2D);
		
		mainCamera = new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(), 0.1f, 1000.0f);
	}
	
	//TODO remove this shit
	public void input(float delta) {
		mainCamera.input(delta);
	}
	
	/** Render a game object
	 * @param object The game object to render */
	public void render(GameObject object) {
		Shader shader = BasicShader.getInstance();
		shader.setRenderingEngine(this);
		
		clearScreen();
		object.render(shader);
	}
	
	/** Clear screen */
	private static void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public static String getOpenGLVersion() { return glGetString(GL_VERSION); }
	
	public void setCamera(Camera camera) { this.mainCamera = camera; }
	
	public Camera getCamera() { return mainCamera; }
}
