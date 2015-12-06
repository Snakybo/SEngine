package com.snakybo.sengine.rendering;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.snakybo.sengine.utils.math.Vector2f;

public abstract class Window
{
	public static void create(int width, int height, String title)
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);

			Display.create();
			Keyboard.create();
			Mouse.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}

	public static void update()
	{
		Display.update();
	}

	public static void bindAsRenderTarget()
	{
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glViewport(0, 0, getWidth(), getHeight());
	}

	public static void destroy()
	{
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public static boolean isCloseRequested()
	{
		return Display.isCloseRequested();
	}

	public static boolean isCreated()
	{
		return Display.isCreated();
	}
	
	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}

	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}

	public static String getTitle()
	{
		return Display.getTitle();
	}

	public static Vector2f getCenter()
	{
		return new Vector2f(getWidth() / 2, getHeight() / 2);
	}
}
