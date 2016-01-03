package com.snakybo.sengine.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.snakybo.sengine.core.AppData;
import com.snakybo.sengine.core.SEngine;

/**
 * @author Kevin
 * @since Dec 20, 2015
 */
public abstract class DirectoryManager
{
	public static void check(String directory)
	{
		if(!AppData.RUN_AS_JAR)
		{
			try
			{
				Path textures = Paths.get(directory);			
				if(!Files.exists(textures))
				{
					System.out.println("[DirectoryManager] Creating directory: " + directory);
					Files.createDirectory(textures);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}