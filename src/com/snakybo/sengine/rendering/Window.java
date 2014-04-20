package com.snakybo.sengine.rendering;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.snakybo.sengine.core.utils.Vector2f;

/** Window class
 * 
 * <p>
 * Main window management
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class Window {
	/** Create a window
	 * @param width The width of the window
	 * @param height The height of the window
	 * @param title The title of the window */
	public static void createWindow(int width, int height, String title) {
		Display.setTitle(title);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			
			Display.create();
			Keyboard.create();
			Mouse.create();
		} catch(LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	/** Render the window */
	public static void render() {
		Display.update();
	}
	
	/** Dispose of the window */
	public static void dispose() {
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}
	
	/** @return Whether or not a close is requested */
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
	
	/** @return Whether or not the window has been created */
	public static boolean isCreated() {
		return Display.isCreated();
	}
	
	/** Set the title of the display
	 * @param title The new title */
	public void setTitle(String title) {
		Display.setTitle(title);
	}
	
	/** @return The width of the window */
	public static int getWidth() {
		return Display.getDisplayMode().getWidth();
	}
	
	/** @return The height of the window */
	public static int getHeight() {
		return Display.getDisplayMode().getHeight();
	}
	
	/** @return The title of the window */
	public static String getTitle() {
		return Display.getTitle();
	}
	
	/** @return The center position of the window */
	public static Vector2f getCenter() {
		return new Vector2f(getWidth() / 2, getHeight() / 2);
	}
}
