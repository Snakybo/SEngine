package com.snakybo.engine.renderer.resourcemanagement;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.engine.math.Vector3f;

/** @author Kevin Krol
 * @since Apr 1, 2014 */
public abstract class MappedValues {
	private Map<String, Vector3f> vector3fMap;
	private Map<String, Float> floatMap;
	
	public MappedValues() {
		vector3fMap = new HashMap<String, Vector3f>();
		floatMap = new HashMap<String, Float>();
	}
		
	/** Add a vector to the material
	 * @param name The name of the vector, used to get the vector
	 * @param value The vector */
	public void addVector3f(String name, Vector3f value) {
		vector3fMap.put(name, value);
	}
	
	/** Add a float to the material
	 * @param name The name of the float, used to get the float
	 * @param value The float */
	public void addFloat(String name, float value) {
		floatMap.put(name, value);
	}
	
	/** @return The vector with the given name
	 * @param name The name of the vector */
	public Vector3f getVector3f(String name) {
		Vector3f result = vector3fMap.get(name);
		
		if(result != null)
			return result;
		
		return new Vector3f();
	}
	
	/** @return The float with the given name
	 * @param name The name of the float */
	public float getFloat(String name) {
		Float result = floatMap.get(name);
		
		if(result != null)
			return result;
		
		return 0;
	}
}
