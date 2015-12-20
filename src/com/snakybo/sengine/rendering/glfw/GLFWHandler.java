package com.snakybo.sengine.rendering.glfw;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIconifyCallback;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;

import com.snakybo.sengine.core.Input;
import com.snakybo.sengine.core.SEngine;

/**
 * @author Kevin
 * @since Dec 20, 2015
 */
class GLFWHandler
{	
	private static class WindowFocusCallback extends GLFWWindowFocusCallback
	{
		@Override
		public void invoke(long window, int focused)
		{
			SEngine.onWindowFocusCallback(focused == 1 ? true : false);
		}		
	}
	
	private static class WindowIconifyCallback extends GLFWWindowIconifyCallback
	{
		@Override
		public void invoke(long window, int iconified)
		{
			SEngine.onWindowIconifyCallback(iconified == 1 ? true : false);
		}
	}
	
	private static class KeyCallback extends GLFWKeyCallback
	{
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods)
		{
			if(action == GLFW_PRESS || action == GLFW_RELEASE)
			{
				Input.onKeyCallback(key, action == GLFW_PRESS ? true : false);
			}
		}
	}
	
	private static class MouseCallback extends GLFWMouseButtonCallback
	{
		@Override
		public void invoke(long window, int button, int action, int mods)
		{
			if(action == GLFW_PRESS || action == GLFW_RELEASE)
			{
				Input.onMouseButtonCallback(button, action == GLFW_PRESS ? true : false);
			}
		}		
	}
	
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWWindowFocusCallback windowFocusCallback;
	private GLFWWindowIconifyCallback windowIconifyCallback;
	
	GLFWHandler()
	{
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		if(glfwInit() != GLFW_TRUE)
		{
			throw new IllegalStateException("[Window] Unable to initialze GLFW");
		}
	}
	
	void destroy()
	{
		errorCallback.release();
	}
	
	void createCallbacks(long window)
	{
		glfwSetKeyCallback(window, keyCallback = new KeyCallback());
		glfwSetMouseButtonCallback(window, mouseButtonCallback = new MouseCallback());
		glfwSetWindowFocusCallback(window, windowFocusCallback = new WindowFocusCallback());
		glfwSetWindowIconifyCallback(window, windowIconifyCallback = new WindowIconifyCallback());
	}
	
	void destroyCallbacks()
	{
		keyCallback.release();
		mouseButtonCallback.release();
		windowFocusCallback.release();
		windowIconifyCallback.release();
	}
}
