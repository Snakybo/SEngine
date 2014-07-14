package com.snakybo.sengine.utils;

/** @author Kevin
 * @since Jul 5, 2014 */
public interface ReferenceCounter {
	void addReference();
	
	boolean removeReference();
	
	int getReferenceCount();
}
