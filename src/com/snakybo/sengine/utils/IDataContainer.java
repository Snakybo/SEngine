package com.snakybo.sengine.utils;

/**
 * @author Kevin
 * @since Dec 16, 2015
 */
public interface IDataContainer
{
	void set(String name, Object value);
	
	<T extends Object> T get(Class<T> type, String name);
}
