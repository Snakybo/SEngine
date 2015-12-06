package com.snakybo.sengine.resource.management;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.utils.IDataContainer;
import com.snakybo.sengine.utils.IReferenceCounter;

/** @author Kevin
 * @since Jul 8, 2014 */
public class MaterialData implements IReferenceCounter, IDataContainer
{
	private Map<String, Object> data;
	
	private int refCount;

	public MaterialData()
	{
		data = new HashMap<String, Object>();
		
		refCount = 0;
	}

	@Override
	public void addReference()
	{
		refCount++;
	}

	@Override
	public boolean removeReference()
	{
		refCount--;

		return refCount == 0;
	}

	@Override
	public int getReferenceCount()
	{
		return refCount;
	}
	
	@Override
	public void set(String name, Object value)
	{
		data.put(name, value);
	}
	
	@Override
	public <T extends Object> T get(Class<T> type, String name)
	{
		if(!data.containsKey(name))
		{
			throw new IllegalArgumentException("No data with the name: " + name + " found.");
		}
		
		return type.cast(data.get(name));
	}
}
