package com.snakybo.engine.renderer.resourcemanagement;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glCreateProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** @author Kevin Krol
 * @since Apr 3, 2014 */
public class ShaderResource {
	private int program;
	
	private HashMap<String, Integer> uniforms;
	
	private List<String> uniformNames;
	private List<String> uniformTypes;
	
	private int references;
	
	public ShaderResource() {
		this.program = glCreateProgram();
		this.references = 1;
		
		if(program == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
		
		this.uniforms = new HashMap<String, Integer>();
		
		this.uniformNames = new ArrayList<String>();
		this.uniformTypes = new ArrayList<String>();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		glDeleteBuffers(program);
	}
	
	public void addReference() {
		references++;
	}
	
	public boolean removeReference() {
		references--;
		
		return references == 0;
	}
	
	public int getProgram() {
		return program;
	}
	
	public HashMap<String, Integer> getUniforms() {
		return uniforms;
	}
	
	public List<String> getUniformNames() {
		return uniformNames;
	}
	
	public List<String> getUniformTypes() {
		return uniformTypes;
	}
}
