package com.snakybo.engine.core;

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

import com.snakybo.engine.renderer.Attenuation;
import com.snakybo.engine.renderer.BaseLight;
import com.snakybo.engine.renderer.Camera;
import com.snakybo.engine.renderer.DirectionalLight;
import com.snakybo.engine.renderer.ForwardAmbient;
import com.snakybo.engine.renderer.ForwardDirectional;
import com.snakybo.engine.renderer.ForwardPoint;
import com.snakybo.engine.renderer.ForwardSpot;
import com.snakybo.engine.renderer.PointLight;
import com.snakybo.engine.renderer.Shader;
import com.snakybo.engine.renderer.SpotLight;
import com.snakybo.engine.renderer.Window;

/** @author Kevin Krol
 *  @since Feb 8, 2014 */
public class Renderer {
	private Camera mainCamera;
	
	private Vector3f ambientLight;
	
	private DirectionalLight directionalLight;
	private PointLight pointLight;
	private SpotLight spotLight;
	
	public Renderer() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_DEPTH_CLAMP);
		
		glEnable(GL_TEXTURE_2D);
		
		mainCamera = new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(), 0.1f, 1000.0f);
		
		ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);
		
		directionalLight = new DirectionalLight(new BaseLight(new Vector3f(0.1f, 0.5f, 0.5f), 0.8f), new Vector3f(1, 1, 1));
		pointLight = new PointLight(new BaseLight(new Vector3f(0.5f, 0.5f, 0.1f), 0.4f), new Attenuation(0, 0, 1), new Vector3f(5, 0, 5), 100);
		spotLight = new SpotLight(new PointLight(new BaseLight(new Vector3f(0, 1, 1), 0.4f), new Attenuation(0, 0, 0.1f), new Vector3f(7, 0, 7), 100), new Vector3f(1, 0, 0), 0.7f);
	}
	
	//TODO remove this shit
	public void input(float delta) {
		mainCamera.input(delta);
	}
	
	/** Render a game object
	 * @param object The game object to render */
	public void render(GameObject object) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		Shader forwardAmbient = ForwardAmbient.getInstance();
		Shader forwardDirectional = ForwardDirectional.getInstance();
		Shader forwardPoint = ForwardPoint.getInstance();
		Shader forwardSpot = ForwardSpot.getInstance();
		
		forwardAmbient.setRenderer(this);
		forwardDirectional.setRenderer(this);
		forwardPoint.setRenderer(this);
		forwardSpot.setRenderer(this);
		
		object.render(forwardAmbient);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);
		
		object.render(forwardDirectional);
		object.render(forwardPoint);
		object.render(forwardSpot);
		
		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static String getOpenGLVersion() { return glGetString(GL_VERSION); }
	
	public void setCamera(Camera camera) { this.mainCamera = camera; }
	
	public Camera getCamera() { return mainCamera; }
	
	public Vector3f getAmbientLight() { return ambientLight; }
	
	public DirectionalLight getDirectionalLight() { return directionalLight; }
	public PointLight getPointLight() { return pointLight; }
	public SpotLight getSpotLight() { return spotLight; }
}
