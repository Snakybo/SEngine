/**
 * 
 */
package com.snakybo.sengine.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin
 * @since Dec 12, 2015
 */
public abstract class ResourceManager
{
	private static class Resource
	{
		private IResource resource;
		private int numReferences;
		
		private Resource(IResource resource)
		{
			this.resource = resource;
			this.numReferences = 0;
		}
		
		public void addReference()
		{
			numReferences++;
		}
		
		public void removeReference()
		{
			numReferences--;
		}
	}
	
	private static Map<String, Resource> resources;
	
	static
	{
		resources = new HashMap<String, Resource>();
	}
	
	public static void add(String identifier)
	{
		if(has(identifier))
		{
			add(identifier, null);
		}
	}
	
	public static void add(String identifier, IResource resource)
	{
		if(!has(identifier))
		{
			resources.put(identifier, new Resource(resource));
		}
		
		Resource target = resources.get(identifier);
		target.addReference();
	}
	
	public static void remove(String identifier)
	{
		if(has(identifier))
		{
			Resource resource = resources.get(identifier);
			resource.removeReference();
			
			if(resource.numReferences <= 0)
			{
				resource.resource.destroy();
			}
		}
	}
	
	public static boolean has(String identifier)
	{
		return resources.containsKey(identifier);
	}
	
	public static <T> T get(Class<T> type, String identifier)
	{
		return type.cast(get(identifier));
	}
	
	public static IResource get(String identifier)
	{
		if(has(identifier))
		{
			return resources.get(identifier).resource;
		}
		
		return null;
	}
}