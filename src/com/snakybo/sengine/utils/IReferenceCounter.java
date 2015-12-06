package com.snakybo.sengine.utils;

/** @author Kevin
 * @since Jul 5, 2014 */
public interface IReferenceCounter
{
	void addReference();

	boolean removeReference();

	int getReferenceCount();
}
