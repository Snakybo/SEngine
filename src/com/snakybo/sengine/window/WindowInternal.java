package com.snakybo.sengine.window;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateCursor;
import static org.lwjgl.glfw.GLFW.glfwCreateStandardCursor;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIconifyCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;

import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.resource.texture.TextureLoader;
import com.snakybo.sengine.resource.texture.TextureLoader.TextureData;

/**
 * @author Kevin
 * @since Feb 4, 2016
 */
public abstract class WindowInternal
{
	private static GLFWErrorCallback errorCallback;	
	private static GLFWKeyCallback keyCallback;
	private static GLFWMouseButtonCallback mouseButtonCallback;
	private static GLFWWindowFocusCallback windowFocusCallback;
	private static GLFWWindowIconifyCallback windowIconifyCallback;
	private static GLFWScrollCallback scrollCallback;
	private static GLFWCursorEnterCallback cursorEnterCallback;
	
	public static void initialize()
	{
		if(errorCallback == null)
		{
			glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
			
			if(glfwInit() != GLFW_TRUE)
			{
				throw new IllegalStateException("[Window] Unable to initialze GLFW");
			}
		}
	}
	
	public static void update()
	{
		glfwSwapBuffers(Window.window);
		glfwPollEvents();
	}
	
	public static void destroy()
	{
		if(keyCallback != null)
		{
			keyCallback.release();
		}
		
		if(mouseButtonCallback != null)
		{
			mouseButtonCallback.release();
		}
		
		if(windowFocusCallback != null)
		{
			windowFocusCallback.release();
		}
		
		if(windowIconifyCallback != null)
		{
			windowIconifyCallback.release();	
		}
		
		if(scrollCallback != null)
		{
			scrollCallback.release();
		}
		
		if(cursorEnterCallback != null)
		{
			cursorEnterCallback.release();
		}
		
		glfwDestroyWindow(Window.window);
		glfwTerminate();
		
		errorCallback.release();
	}
	
	public static void createCursor(int shape)
	{
		long cursor = glfwCreateStandardCursor(shape);
		glfwSetCursor(Window.window, cursor);
	}
	
	public static void createCursor(String fileName, int xhot, int yhot)
	{
		TextureData textureData = TextureLoader.loadCursor(fileName);		
		GLFWImage image = GLFWImage.malloc().set(textureData.getWidth(), textureData.getHeight(), textureData.getData());
		
		long cursor = glfwCreateCursor(image, xhot, yhot);
		image.free();
		
		if(cursor == NULL)
		{
			throw new RuntimeException("[GLFWWindow] Error while creating cursor");
		}
		
		glfwSetCursor(Window.window, cursor);
	}
	
	public static void setKeyCallback(GLFWKeyCallback callback)
	{
		glfwSetKeyCallback(Window.window, keyCallback = callback);
	}
	
	public static void setMouseButtonCallback(GLFWMouseButtonCallback callback)
	{
		glfwSetMouseButtonCallback(Window.window, mouseButtonCallback = callback);
	}
	
	public static void setScrollCallback(GLFWScrollCallback callback)
	{
		glfwSetScrollCallback(Window.window, scrollCallback = callback);
	}
	
	public static void setWindowFocusCallback(GLFWWindowFocusCallback callback)
	{
		glfwSetWindowFocusCallback(Window.window, windowFocusCallback = callback);
	}
	
	public static void setWindowIconifyCallback(GLFWWindowIconifyCallback callback)
	{
		glfwSetWindowIconifyCallback(Window.window, windowIconifyCallback = callback);
	}
	
	public static void setCursorEnterCallback(GLFWCursorEnterCallback callback)
	{
		glfwSetCursorEnterCallback(Window.window, cursorEnterCallback = callback);
	}
	
	public static void setCursor(boolean enabled)
	{
		glfwSetInputMode(Window.window, GLFW_CURSOR, enabled ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
	}
	
	public static void setMousePosition(Vector2f position)
	{
		glfwSetCursorPos(Window.window, position.x, position.y);
	}
	
	public static Vector2f getMousePosition()
	{
		DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(Window.window, xpos, ypos);
		
		return new Vector2f((float)xpos.get(), Window.getHeight() - (float)ypos.get());
	}
	
	public static boolean isCloseRequested()
	{
		return glfwWindowShouldClose(Window.window) == 1 ? true : false;
	}
	
	public static boolean isCreated()
	{
		return Window.window != NULL;
	}
}
