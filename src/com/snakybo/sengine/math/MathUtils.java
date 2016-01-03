package com.snakybo.sengine.math;

/** 
 * @author Kevin
 * @since Jul 12, 2014
 */
public abstract class MathUtils
{
	public static <T extends Comparable<T>> T clamp(T value, T min, T max)
	{
		if(value.compareTo(min) < 0)
		{
			return min;
		}
		else if(value.compareTo(max) > 0)
		{
			return max;
		}
		
		return value;
	}
}
