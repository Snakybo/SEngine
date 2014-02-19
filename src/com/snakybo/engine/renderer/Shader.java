package com.snakybo.engine.renderer;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Transform;
import com.snakybo.engine.core.Util;
import com.snakybo.engine.core.Vector3f;

public class Shader {
	private Renderer renderer;
	
	private int program;
	private HashMap<String, Integer> uniforms;
	
	public Shader() {
		program = glCreateProgram();
		uniforms = new HashMap<String, Integer>();
		
		if(program == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
	}
	
	public void bind() {
		glUseProgram(program);
	}
	
	public void compileShader() {
		glLinkProgram(program);
		
		if(glGetProgrami(program, GL_LINK_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
		
		glValidateProgram(program);
		
		if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
	}
	
	public void addUniform(String uniform) {
		int uniformLocation = glGetUniformLocation(program, uniform);
		
		if(uniformLocation == 0xFFFFFFFF) {
			System.err.println("Error: Could not find uniform: " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		uniforms.put(uniform, uniformLocation);
	}
	
	public void updateUniforms(Transform transform, Material material) { }
	
	public void addVertexShader(String text) { addProgram(text, GL_VERTEX_SHADER); }
	public void addGeometryShader(String text) { addProgram(text, GL_GEOMETRY_SHADER); }
	public void addFragmentShader(String text) { addProgram(text, GL_FRAGMENT_SHADER); }
	
	public void addVertexShaderFromFile(String text) { addProgram(loadShader(text), GL_VERTEX_SHADER); }
	public void addGeometryShaderFromFile(String text) { addProgram(loadShader(text), GL_GEOMETRY_SHADER); }
	public void addFragmentShaderFromFile(String text) { addProgram(loadShader(text), GL_FRAGMENT_SHADER); }
	
	public void setUniformi(String uniformName, int value) { glUniform1i(uniforms.get(uniformName), value); }
	public void setUniformf(String uniformName, float value) { glUniform1f(uniforms.get(uniformName), value); }
	public void setUniform(String uniformName, Vector3f value) { glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ()); }
	public void setUniform(String uniformName, Matrix4f value) { glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value)); }
	
	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);
		
		if(shader == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		glAttachShader(program, shader);
	}
	
	public void setAttribLocation(String attribute, int location) {
		glBindAttribLocation(program, location, attribute);
	}
	
	public void setRenderer(Renderer renderer) { this.renderer = renderer; }
	
	public Renderer getRenderer() { return renderer; }
	
	/** Load a shader file */
	private static String loadShader(String fileName) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		
		try {
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			String line;
			
			while((line = shaderReader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			
			shaderReader.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return shaderSource.toString();
	}
}
