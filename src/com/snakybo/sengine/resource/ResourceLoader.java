package com.snakybo.sengine.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author Kevin
 * @since Jan 3, 2016
 */
public abstract class ResourceLoader
{
	public static File loadResource(String path)
	{
		return new File("./res/" + path);
	}
	
	public static FileReader loadResourceAsFileReader(String path) throws FileNotFoundException
	{
		return new FileReader(loadResource(path));
	}
	
	public static BufferedReader loadResourceAsBufferedReader(String path) throws FileNotFoundException
	{
		return new BufferedReader(loadResourceAsFileReader(path));
	}
}