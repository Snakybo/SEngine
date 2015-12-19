/**
 * 
 */
package com.snakybo.sengine.resource.mesh;

/**
 * @author Kevin
 *
 */
public enum Primitive
{
	CAPSULE("default/capsule.obj"),
	CUBE("default/cube.obj"),
	PLANE("default/plane.obj"),
	SPHERE("default/sphere.obj");
	
	private final String fileName;
	
	private Primitive(String fileName)
	{
		this.fileName = fileName;
	}
	
	public Mesh get()
	{
		return new Mesh(fileName);
	}
}
