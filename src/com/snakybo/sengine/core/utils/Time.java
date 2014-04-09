package com.snakybo.sengine.core.utils;

/** Time class
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Time {
	private static final long SECOND = 1000000000L;
	
	/** @return The current time in seconds */
	public static double getTime() {
		return (double)System.nanoTime() / (double)SECOND;
	}
}
