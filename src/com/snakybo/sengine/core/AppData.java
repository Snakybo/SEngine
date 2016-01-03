package com.snakybo.sengine.core;

/**
 * @author Kevin
 * @since Jan 3, 2016
 */
public abstract class AppData
{
	public static final String ENGINE_VERSION_STRING = "0.1.0";
	public static final int ENGINE_VERSION_MAJOR;
	public static final int ENGINE_VERSION_MINOR;
	public static final int ENGINE_VERSION_PATCH;
	
	public static final boolean RUN_AS_JAR;
	
	static
	{
		RUN_AS_JAR = SEngine.class.getResource("SEngine.class").toString().startsWith("jar:");
		
		// Generate the engine version integers
		String v = ENGINE_VERSION_STRING;
		ENGINE_VERSION_MAJOR = Integer.parseInt(v.substring(0, v.indexOf('.')));
		ENGINE_VERSION_MINOR = Integer.parseInt(v.substring(v.indexOf('.') + 1, v.lastIndexOf('.')));
		ENGINE_VERSION_PATCH = Integer.parseInt(v.substring(v.lastIndexOf('.') + 1));
	}
}