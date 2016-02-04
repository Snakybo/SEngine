package com.snakybo.sengine.utils;

/**
 * @author Kevin
 * @since Feb 4, 2016
 */
public final class Version implements Comparable<Version>
{
	private final int major;
	private final int minor;
	private final int patch;
	
	public Version(int major)
	{
		this(major, 0);
	}
	
	public Version(int major, int minor)
	{
		this(major, minor, 0);
	}
	
	public Version(int major, int minor, int patch)
	{
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}
	
	@Override
	public int compareTo(Version version)
	{
		if(major > version.major)
		{
			return 1;
		}
		else if(major < version.major)
		{
			return -1;
		}
		
		if(minor > version.minor)
		{
			return 1;
		}
		else if(minor > version.minor)
		{
			return -1;
		}
		
		if(patch > version.patch)
		{
			return 1;
		}
		else if(patch < version.patch)
		{
			return -1;
		}
		
		return 0;
	}
	
	@Override
	public String toString()
	{
		return major + "." + minor + "." + patch;
	}
	
	public final int getMajor()
	{
		return major;
	}
	
	public final int getMinor()
	{
		return minor;
	}
	
	public final int getPatch()
	{
		return patch;
	}
}
