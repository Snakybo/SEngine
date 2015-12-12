package com.snakybo.sengine.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{
	public static String[] removeEmptyStrings(String[] data)
	{
		List<String> filtered = new ArrayList<String>();
		
		for(String checking : data)
		{
			if(checking.isEmpty())
			{
				continue;
			}
			
			filtered.add(checking);
		}
		
		return filtered.toArray(new String[0]);
	}

	public static int[] toIntArray(Integer[] data)
	{
		int[] result = new int[data.length];

		for(int i = 0; i < data.length; i++)
		{
			result[i] = data[i].intValue();
		}

		return result;
	}

	public static int indexOfWholeWord(String string, String keyword)
	{
		return indexOfWholeWord(string, keyword, 0);
	}

	public static int indexOfWholeWord(String string, String keyword, int startIndex)
	{
		String regex = "\\b" + keyword + "\\b";
		String line = string.substring(startIndex);

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);

		if(matcher.find())
		{
			return matcher.start();
		}

		return -1;
	}
}
