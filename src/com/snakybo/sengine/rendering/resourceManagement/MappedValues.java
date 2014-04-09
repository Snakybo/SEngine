package com.snakybo.sengine.rendering.resourceManagement;

import com.snakybo.sengine.core.utils.Vector3f;

import java.util.HashMap;

/** Mapped values
 * 
 * A set of core hashmaps used in multiple classes
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public abstract class MappedValues {
	private HashMap<String, Vector3f> vector3fHashMap;
	private HashMap<String, Float> floatHashMap;
	
	/** Constructor for the mapped values */
	public MappedValues() {
		vector3fHashMap = new HashMap<String, Vector3f>();
		floatHashMap = new HashMap<String, Float>();
	}
	
	/** Add a vector 3 to the vector hash map
	 * @param name The name of the vector
	 * @param vector3f The vector */
	public void addVector3f(String name, Vector3f vector3f) {
		vector3fHashMap.put(name, vector3f);
	}
	
	/** Add a float value to the float hash map
	 * @param name The name of the float
	 * @param floatValue The float */
	public void addFloat(String name, float floatValue) {
		floatHashMap.put(name, floatValue);
	}
	
	/** @return A vector from the vector hash map
	 * @param name The name of the vector */
	public Vector3f getVector3f(String name) {
		Vector3f result = vector3fHashMap.get(name);
		if(result != null)
			return result;
		
		return new Vector3f(0, 0, 0);
	}
	
	/** @return A float value from the float hash map
	 * @param name The name of the float */
	public float getFloat(String name) {
		Float result = floatHashMap.get(name);
		if(result != null)
			return result;
		
		return 0;
	}
}
