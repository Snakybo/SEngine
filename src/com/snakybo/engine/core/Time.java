package com.snakybo.engine.core;

public class Time {
	/** @return The time in seconds*/
	public static double getTime() {
		return (double)System.nanoTime() / (double)1000000000L;
	}
}
