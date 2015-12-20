package com.snakybo.sengine.rendering.glfw;

import static org.lwjgl.glfw.GLFW.GLFW_ALPHA_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_DEPTH_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_DOUBLE_BUFFER;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateCursor;
import static org.lwjgl.glfw.GLFW.glfwCreateStandardCursor;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
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

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;

import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.resource.texture.TextureLoader;
import com.snakybo.sengine.resource.texture.TextureLoader.TextureData;
import com.snakybo.sengine.utils.Buffer;

/**
 * @author Kevin
 * @since Dec 20, 2015
 */
public abstract class GLFWWindow
{
	private static GLFWHandler handler;
	
	private static long window;
	private static int samples;
	private static boolean vsync;
	
	static
	{
		handler = new GLFWHandler();
	}
	
	public static void createFullscreen(String title)
	{
		createFullscreen(title, getPrimaryMonitor());
	}
	
	public static void createFullscreen(String title, long monitor)
	{
		GLFWVidMode vidMode = glfwGetVideoMode(monitor);		
		create(title, vidMode.width(), vidMode.height(), monitor);
	}
	
	public static void createWindowed(String title, int width, int height)
	{
		create(title, width, height, NULL);
	}
	
	private static void create(String title, int width, int height, long monitor)
	{
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		if(monitor != NULL)
		{
			GLFWVidMode vidMode = glfwGetVideoMode(monitor);
			
			glfwWindowHint(GLFW_RED_BITS, vidMode.redBits());
			glfwWindowHint(GLFW_GREEN_BITS, vidMode.greenBits());
			glfwWindowHint(GLFW_BLUE_BITS, vidMode.blueBits());
			glfwWindowHint(GLFW_REFRESH_RATE, vidMode.refreshRate());
		}
		else
		{
			glfwWindowHint(GLFW_RED_BITS, 8);
			glfwWindowHint(GLFW_GREEN_BITS, 8);
			glfwWindowHint(GLFW_BLUE_BITS, 8);
			glfwWindowHint(GLFW_ALPHA_BITS, 8);
			glfwWindowHint(GLFW_DEPTH_BITS, 16);
			glfwWindowHint(GLFW_DOUBLE_BUFFER, 1);
		}
		
		glfwWindowHint(GLFW_SAMPLES, samples);
		
		window = glfwCreateWindow(width, height, title, monitor, NULL);
		if(window == NULL)
		{
			throw new RuntimeException("[Window] Unable to create window");
		}
		
		handler.createCallbacks(window);
		
		glfwMakeContextCurrent(window);		
		glfwShowWindow(window);
		createCapabilities();
	}
	
	public static void update()
	{
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public static void bindAsRenderTarget()
	{
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glViewport(0, 0, getWidth(), getHeight());
	}
	
	public static void createCursor(int shape)
	{
		long cursor = glfwCreateStandardCursor(shape);
		glfwSetCursor(window, cursor);
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
		
		glfwSetCursor(window, cursor);
	}
	
	public static void destroy()
	{
		handler.destroyCallbacks();
		
		glfwDestroyWindow(window);
		glfwTerminate();
		
		handler.destroy();	
	}
	
	public static boolean isCloseRequested()
	{
		return glfwWindowShouldClose(window) == 1 ? true : false;
	}
	
	public static boolean isCreated()
	{
		return window != NULL;
	}
	
	public static void setSamples(int samples)
	{
		GLFWWindow.samples = samples;
	}
	
	public static void setVSync(boolean vsync)
	{
		GLFWWindow.vsync = vsync;
		glfwSwapInterval(vsync ? 1 : 0);
	}
	
	public static void setSize(int width, int height)
	{
		setSize(new Vector2f(width, height));
	}
	
	public static void setSize(Vector2f size)
	{
		glfwSetWindowSize(window, (int)size.x, (int)size.y);
	}
	
	public static void setTitle(String title)
	{
		glfwSetWindowTitle(window, title);
	}
	
	public static void setMousePosition(Vector2f position)
	{
		glfwSetCursorPos(window, position.x, position.y);
	}

	public static void setCursor(boolean enabled)
	{
		glfwSetInputMode(window, GLFW_CURSOR, enabled ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
	}
	
	public static Vector2f getMousePosition()
	{
		DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(window, xpos, ypos);
		
		return new Vector2f((float)xpos.get(), getHeight() - (float)ypos.get());
	}
	
	public static Vector2f getCenter()
	{
		return getSize().div(2);
	}
	
	public static Vector2f getSize()
	{
		IntBuffer width = Buffer.createIntBuffer(1);
		IntBuffer height = Buffer.createIntBuffer(1);
		
		glfwGetWindowSize(window, width, height);
		
		return new Vector2f(width.get(), height.get());
	}
	
	public static long getPrimaryMonitor()
	{
		return glfwGetPrimaryMonitor();
	}
	
	public static int getWidth()
	{
		return (int)getSize().x;
	}
	
	public static int getHeight()
	{
		return (int)getSize().y;
	}
	
	public static boolean isVSyncEnabled()
	{
		return vsync;
	}
}
