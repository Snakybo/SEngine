package com.snakybo.sengine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author Kevin
 * @since Jan 3, 2016
 */
public abstract class FileUtils
{
	public static File loadFile(String path)
	{
		return new File("./" + path);
	}
	
	public static FileReader loadFileReader(String path)
	{
		FileReader fileReader = null;
		
		try
		{
			fileReader = new FileReader(loadFile(path));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return fileReader;
	}
	
	public static BufferedReader loadBufferedReader(String path)
	{
		return new BufferedReader(loadFileReader(path));
	}
}
