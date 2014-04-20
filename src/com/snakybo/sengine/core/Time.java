package com.snakybo.sengine.core;

/** Time class
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Time {
	private static final long SECOND = 1000000000L;
	
	static int fps = 0;
	
	/** @return The current time in seconds */
	public static double getTime() {
		return (double)System.nanoTime() / (double)SECOND;
	}
	
	/** @return The current fps of the game */
	public static int getFps() {
		return fps;
	}
}
