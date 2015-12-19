package com.snakybo.sengine.rendering;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import com.snakybo.sengine.core.Input;
import com.snakybo.sengine.math.Vector2f;

public abstract class Window
{
	@SuppressWarnings("unused")
	private static GLFWErrorCallback errorCallback;
	
	@SuppressWarnings("unused")
	private static GLFWKeyCallback keyCallback;
	
	@SuppressWarnings("unused")
	private static GLFWMouseButtonCallback mouseButtonCallback;
	
	private static String title;
	private static int width;
	private static int height;
	
	private static long window;
	
	public static void create(int width, int height, String title)
	{
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		if(glfwInit() != GLFW_TRUE)
		{
			throw new IllegalStateException("[Window] Unable to initialze GLFW");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if(window == NULL)
		{
			throw new RuntimeException("[Window] Unable to create window");
		}
		
		glfwSetKeyCallback(window, keyCallback = new Input.KeyCallback());
		glfwSetMouseButtonCallback(window, mouseButtonCallback = new Input.MouseCallback());
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		glfwShowWindow(window);
		
		createCapabilities();
		
		Window.title = title;
		Window.width = width;
		Window.height = height;
	}

	public static void update()
	{
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public static void bindAsRenderTarget()
	{
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glViewport(0, 0, width, height);
	}

	public static void destroy()
	{
		glfwDestroyWindow(window);
		glfwTerminate();
		
		errorCallback = null;
	}
	
	public static boolean isCloseRequested()
	{
		return glfwWindowShouldClose(window) == 1 ? true : false;
	}

	public static boolean isCreated()
	{
		return window != NULL;
	}
	
	public static void setTitle(String title)
	{
		Window.title = title;
		glfwSetWindowTitle(window, title);
	}
	
	public static Vector2f getCenter()
	{
		return new Vector2f(width / 2, height / 2);
	}
	
	public static String getTitle()
	{
		return title;
	}
	
	public static int getWidth()
	{
		return width;
	}
	
	public static int getHeight()
	{
		return height;
	}
	
	public static long getWindow()
	{
		return window;
	}
}
