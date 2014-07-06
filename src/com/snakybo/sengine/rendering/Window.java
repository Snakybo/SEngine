package com.snakybo.sengine.rendering;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import com.snakybo.sengine.utils.math.Vector2f;
import com.snakybo.sengine.utils.math.Vector3f;

public class Window {
	public static final int MSAA = 0x01;
	
	private ContextAttribs contextAttribs;
	
	private Vector3f ambientColor;
	private Vector3f clearColor;
	
	private int width;
	private int height;
	
	private int aaType;
	private int aaSamples;
	
	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		
		ambientColor = new Vector3f(1.0f, 1.0f, 1.0f);
		clearColor = new Vector3f(0.0f, 0.0f, 0.0f);
		
		Display.setTitle(title);
	}
	
	public void create() {
		PixelFormat pixelFormat = new PixelFormat();
		
		if(aaType == MSAA)
			pixelFormat = pixelFormat.withSamples(aaSamples);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			
			Display.create(pixelFormat, contextAttribs);
			Keyboard.create();
			Mouse.create();
		} catch(LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void render() {
		Display.update();
	}
	
	public void bindAsRenderTarget() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glViewport(0, 0, getWidth(), getHeight());
	}
	
	public void destroy() {
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}
	
	public boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
	
	public boolean isCreated() {
		return Display.isCreated();
	}
	
	public void setAA(int aaType, int aaSamples) {
		this.aaType = aaType;
		this.aaSamples = aaSamples;
	}
	
	public void setContextAttribs(ContextAttribs contextAttribs) {
		this.contextAttribs = contextAttribs;
	}
	
	public void setAmbientColor(Vector3f ambientColor) {
		this.ambientColor = ambientColor;
	}
	
	public void setClearColor(Vector3f clearColor) {
		this.clearColor = clearColor;
	}
	
	public Vector3f getAmbientColor() {
		return ambientColor;
	}
	
	public Vector3f getClearColor() {
		return clearColor;
	}
	
	public static int getWidth() {
		return Display.getDisplayMode().getWidth();
	}
	
	public static int getHeight() {
		return Display.getDisplayMode().getHeight();
	}
	
	public static String getTitle() {
		return Display.getTitle();
	}
	
	public static Vector2f getCenter() {
		return new Vector2f(getWidth() / 2, getHeight() / 2);
	}
}
