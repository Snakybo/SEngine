package com.snakybo.sengine.utils;

import org.lwjgl.glfw.GLFW;

public class Time
{
	private static double frameTime;
	
	static
	{
		setTargetFPS(60);
	}
	
	public static void setTargetFPS(double fps)
	{
		frameTime = 1.0 / fps;
	}
	
	public static double getCurrentTime()
	{
		return GLFW.glfwGetTime();
	}
	
	public static double getCurrentTimeMillis()
	{
		return getCurrentTime() * 1000;
	}
	
	public static double getFrameTime()
	{
		return frameTime;
	}
	
	public static float getDeltaTime()
	{
		return (float)getFrameTime();
	}
}
