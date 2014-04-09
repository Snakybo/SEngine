package com.snakybo.sengine.core.utils;

import java.util.ArrayList;

/** Uilities class
 * 
 * <p>
 * A set of useful utilities
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Utils {	
	/** Remove empty strings from an array
	 * @param data The string array
	 * @return The data array without empty strings */
	public static String[] removeEmptyStrings(String[] data) {
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < data.length; i++)
			if(!data[i].equals(""))
				result.add(data[i]);
		
		String[] res = new String[result.size()];
		result.toArray(res);
		
		return res;
	}
	
	/** Convert an interger array to an int array
	 * @param data The integer array
	 * @return An int array with the data */
	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];
		
		for(int i = 0; i < data.length; i++)
			result[i] = data[i].intValue();
		
		return result;
	}
}
