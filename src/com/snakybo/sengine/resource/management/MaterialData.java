package com.snakybo.sengine.resource.management;

import com.snakybo.sengine.utils.MappedValues;
import com.snakybo.sengine.utils.IReferenceCounter;

/** @author Kevin
 * @since Jul 8, 2014 */
public class MaterialData extends MappedValues implements IReferenceCounter
{
	private int refCount;

	public MaterialData()
	{
		super();

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
}
