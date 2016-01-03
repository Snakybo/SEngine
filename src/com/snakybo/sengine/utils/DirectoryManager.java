package com.snakybo.sengine.utils;

import java.io.File;

import com.snakybo.sengine.resource.ResourceLoader;

/**
 * @author Kevin
 * @since Dec 20, 2015
 */
public abstract class DirectoryManager
{
	public static void check(String path)
	{
		File file = ResourceLoader.loadResource(path);
		
		if(!file.isDirectory() || !file.exists())
		{
			file.mkdir();
		}
	}
}