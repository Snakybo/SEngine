package com.snakybo.sengine.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * @author Kevin
 * @since Dec 20, 2015
 */
public abstract class Time
{
	private static long totalFrameCount;
	
	private static double frameTime;	
	private static double currentTime;
	private static double lastTime;
	private static double passedTime;
	private static double frameCounter;
	
	private static int currentFrameCount;
	private static int fps;
	
	static
	{
		setTargetFPS(60);
		
		lastTime = glfwGetTime();
		currentTime = glfwGetTime();
	}
	
	static void update()
	{		
		currentTime = glfwGetTime();
		passedTime = currentTime - lastTime;
		lastTime = currentTime;
		
		frameCounter += passedTime;
		
		if(frameCounter >= 1.0)
		{
			fps = currentFrameCount;
			
			frameCounter = 0;
			currentFrameCount = 0;
		}
	}
	
	static void onRender()
	{
		totalFrameCount++;
		currentFrameCount++;
	}
	
	public static void setTargetFPS(double fps)
	{
		frameTime = 1.0 / fps;
	}
	
	public static long getTotalFrameCount()
	{
		return totalFrameCount;
	}
	
	public static double getCurrentTime()
	{
		return currentTime;
	}
	
	public static double getCurrentTimeMillis()
	{
		return getCurrentTime() * 1000;
	}
	
	public static float getDeltaTime()
	{
		return (float)getFrameTime();
	}
	
	public static int getFPS()
	{
		return fps;
	}
	
	static double getFrameTime()
	{
		return frameTime;
	}
	
	static double getPassedTime()
	{
		return passedTime;
	}
}
