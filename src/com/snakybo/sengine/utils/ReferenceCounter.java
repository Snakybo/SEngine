package com.snakybo.sengine.utils;

/** @author Kevin
 * @since Jul 5, 2014 */
public class ReferenceCounter {
	private int refCount;
	
	public ReferenceCounter() {
		refCount = 0;
	}
	
	public void addReference() {
		refCount++;
	}
	
	public boolean removeReference() {
		refCount--;
		
		return refCount == 0;
	}
	
	public int getReferenceCount() {
		return refCount;
	}
}
