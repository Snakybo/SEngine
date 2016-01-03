package com.snakybo.sengine.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin
 * @since Dec 19, 2015
 */
public abstract class StringUtils
{
	public static String[] removeEmptyStrings(String[] data)
	{
		List<String> result = new ArrayList<String>();
		
		for(String element : data)
		{
			if(element.isEmpty())
			{
				continue;
			}
			
			result.add(element);
		}
		
		return result.toArray(new String[0]);
	}
	
	public static String getFileExtension(String directory, String fileName)
	{
		if(fileName.lastIndexOf('.') != -1)
		{
			return fileName.substring(fileName.lastIndexOf('.'));
		}
		
		String[] fileFolder = fileName.split("/");
		for(int i = 0; i < fileFolder.length - 1; i++)
		{
			directory += fileFolder[i];
		}
		
		String rawFileName = fileFolder[fileFolder.length - 1];
		
		File dir = new File(directory);
		File[] files = dir.listFiles();
		
		for(File file : files)
		{
			if(file.isFile())
			{
				String name = file.getName();
				
				if(name.lastIndexOf('.') != -1)
				{
					name = name.substring(0, name.lastIndexOf('.'));
				}
				
				if(name.equals(rawFileName))
				{
					return file.getName().substring(file.getName().lastIndexOf('.') + 1);
				}
			}
		}
		
		return null;
	}
}