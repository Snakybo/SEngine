package com.snakybo.sengine.utils.math;

/** @author Kevin
 * @since Jul 12, 2014 */
public class MathUtils
{
	public static <T extends Comparable<T>> T clamp(T val, T min, T max)
	{
		if (val.compareTo(min) < 0)
		{
			return min;
		}
		else if (val.compareTo(max) > 0)
		{
			return max;
		}
		else
		{
			return val;
		}
	}
}
