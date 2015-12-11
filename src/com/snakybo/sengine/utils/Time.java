package com.snakybo.sengine.utils;

import org.lwjgl.glfw.GLFW;

public class Time
{
	public static double getCurrentTime()
	{
		return GLFW.glfwGetTime();
	}
	
	public static double getCurrentTimeMillis()
	{
		return getCurrentTime() * 1000;
	}
}
