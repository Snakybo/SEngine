package com.snakybo.sengine.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.window.WindowInternal;

/**
 * @author Kevin
 * @since Feb 4, 2016
 */
public class InputInternal
{
	private static class KeyCallback extends GLFWKeyCallback
	{
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods)
		{
			if(action == GLFW_PRESS || action == GLFW_RELEASE)
			{
				boolean state = action == GLFW_PRESS ? true : false;
				
				Input.keys[key] = state;
				Input.lastKeys[key] = state;
			}
		}
	}
	
	private static class MouseButtonCallback extends GLFWMouseButtonCallback
	{
		@Override
		public void invoke(long window, int button, int action, int mods)
		{
			if(action == GLFW_PRESS || action == GLFW_RELEASE)
			{
				boolean state = action == GLFW_PRESS ? true : false;
				
				Input.mouseButtons[button] = state;
				Input.lastMouseButtons[button] = state;
			}
		}		
	}
	
	private static class ScrollCallback extends GLFWScrollCallback
	{
		@Override
		public void invoke(long window, double x, double y)
		{
			Input.scrollDelta = new Vector2f((float)x, (float)y);
		}		
	}
	
	public static void initialize()
	{
		WindowInternal.setKeyCallback(new KeyCallback());
		WindowInternal.setMouseButtonCallback(new MouseButtonCallback());
		WindowInternal.setScrollCallback(new ScrollCallback());
	}
	
	public static void update()
	{
		for(int i = 0; i < Input.lastKeys.length; i++)
		{
			Input.lastKeys[i] = false;
		}
		
		for(int i = 0; i < Input.lastMouseButtons.length; i++)
		{
			Input.lastMouseButtons[i] = false;
		}
		
		Input.scrollDelta = new Vector2f();
	}
}
