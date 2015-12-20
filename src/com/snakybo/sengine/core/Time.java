package com.snakybo.sengine.core;

import org.lwjgl.glfw.GLFW;

/**
 * @author Kevin
 * @since Dec 20, 2015
 */
public class Time
{
	private static long totalFrameCount;
	
	private static double frameTime;	
	private static double currentTime;
	private static double lastTime;
	private static double passedTime;
	private static double frameCounter;
	
	private static int currentFrameCount;
	
	static
	{
		setTargetFPS(60);
		
		lastTime = GLFW.glfwGetTime();
		currentTime = GLFW.glfwGetTime();
	}
	
	static void update()
	{		
		currentTime = GLFW.glfwGetTime();
		passedTime = currentTime - lastTime;
		lastTime = currentTime;
		
		frameCounter += passedTime;
		
		if(frameCounter >= 1.0)
		{
			System.out.println(currentFrameCount);
			
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
	
	static double getFrameTime()
	{
		return frameTime;
	}
	
	static double getPassedTime()
	{
		return passedTime;
	}
}
