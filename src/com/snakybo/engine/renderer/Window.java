package com.snakybo.engine.renderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.snakybo.engine.core.Vector2f;

public class Window {

	/** Create LWJGL Display */
	public static void createWindow(int width, int height, String title) {
		Display.setTitle(title);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	/** Render display */	
	public static void render() {
		Display.update();
	}
	
	/** Destroy the display */
	public static void destroy() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	/** @return True if a close was requested */
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
	
	/** @return The width of the display */
	public static int getWidth() {
		return Display.getDisplayMode().getWidth();
	}
	
	/** @return The height of the display */
	public static int getHeight() {
		return Display.getDisplayMode().getHeight();
	}
	
	/** @return The title of the display */
	public static String getTitle() {
		return Display.getTitle();
	}
	
	/** @return The center of the display */
	public Vector2f getCenter() {
		return new Vector2f(getWidth() / 2, getHeight() / 2);
	}
}