package com.snakybo.sengine.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CROSSHAIR_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HAND_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HRESIZE_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_IBEAM_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_VRESIZE_CURSOR;

import com.snakybo.sengine.window.WindowInternal;

/**
 * @author Kevin
 * @since Feb 2, 2016
 */
public abstract class CursorUtils
{
	public static void createArrowCursor()
	{
		WindowInternal.createCursor(GLFW_ARROW_CURSOR);
	}
	
	public static void createIBeamCursor()
	{
		WindowInternal.createCursor(GLFW_IBEAM_CURSOR);
	}
	
	public static void createCrosshairCursor()
	{
		WindowInternal.createCursor(GLFW_CROSSHAIR_CURSOR);
	}
	
	public static void createHandCursor()
	{
		WindowInternal.createCursor(GLFW_HAND_CURSOR);
	}
	
	public static void createHResizeCursor()
	{
		WindowInternal.createCursor(GLFW_HRESIZE_CURSOR);
	}
	
	public static void createVResizeCursor()
	{
		WindowInternal.createCursor(GLFW_VRESIZE_CURSOR);
	}
	
	public static void createCursor(String fileName, int xhot, int yhot)
	{
		WindowInternal.createCursor(fileName, xhot, yhot);
	}
}
