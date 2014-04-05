package com.snakybo.sengine.rendering.resourceManagement;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glCreateProgram;

/** Shader resource
 * 
 * Resource manager for shaders
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class ShaderResource {
	private HashMap<String, Integer> uniforms;
	
	private ArrayList<String> uniformNames;
	private ArrayList<String> uniformTypes;
	
	private int program;
	private int refCount;
	
	/** Constructor for the shader resource */
	public ShaderResource() {
		this.program = glCreateProgram();
		this.refCount = 1;
		
		if(program == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
		
		uniforms = new HashMap<String, Integer>();
		
		uniformNames = new ArrayList<String>();
		uniformTypes = new ArrayList<String>();
	}
	
	@Override
	protected void finalize() {
		glDeleteBuffers(program);
	}
	
	/** Add a reference to the resource */
	public void addReference() {
		refCount++;
	}
	
	/** Remove a reference from the resource
	 * @return Whether or not there are no more references remaining */
	public boolean removeReference() {
		refCount--;
		
		return refCount == 0;
	}
	
	/** @return The shader program */
	public int getProgram() {
		return program;
	}
	
	/** @return The uniforms of the shader */
	public HashMap<String, Integer> getUniforms() {
		return uniforms;
	}
	
	/** @return The uniform names of the shader */
	public ArrayList<String> getUniformNames() {
		return uniformNames;
	}
	
	/** @return The uniform types of the shader */
	public ArrayList<String> getUniformTypes() {
		return uniformTypes;
	}
}
