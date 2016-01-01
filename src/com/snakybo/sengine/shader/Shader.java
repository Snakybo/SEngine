package com.snakybo.sengine.shader;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.object.Transform;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.shader.ShaderUtils.ShaderStruct;
import com.snakybo.sengine.shader.ShaderUtils.ShaderUniform;
import com.snakybo.sengine.utils.Buffer;

import sun.java2d.pipe.RenderingEngine;

/**
 * @author Kevin
 * @since Dec 8, 2015
 */
public class Shader
{	
	private List<ShaderUniform> uniforms;
	private List<Integer> shaderIds;
	
	private Map<String, Integer> uniformLookup;
	
	private int programId;
	
	/**
	 * Create a new shader from the default shader. 
	 */
	public Shader()
	{
		this("default/default_shader");
	}
	
	/**
	 * Create a new shader from the specified shader.
	 * @param fileName The name of the shader, in a relative path from {@value ShaderUtils#SHADER_FOLDER}, and without {@code .glsl}.
	 */
	public Shader(String fileName)
	{
		uniforms = new ArrayList<ShaderUniform>();
		shaderIds = new ArrayList<Integer>();
		uniformLookup = new HashMap<String, Integer>();
		
		programId = glCreateProgram();
		
		if(programId == 0)
		{
			System.err.println("[Shader] Unable to create shader program");
			System.exit(1);
		}
		
		String source = ShaderUtils.loadShaderFile(fileName + ".glsl");	
		
		createShaders(source);
		addAttributes(source);
		
		link();
		
		addUniforms(source);
	}
	
	/**
	 * Destroy the shader, this will detach and destroy all shaders, as well as the program.
	 */
	public final void destroy()
	{
		for(int shaderId : shaderIds)
		{
			glDetachShader(programId, shaderId);
			glDeleteShader(shaderId);
		}
		
		glDeleteProgram(programId);
	}
	
	/**
	 * Bind the shader program, allowing it to be used.
	 */
	public final void bind()
	{
		glUseProgram(programId);
	}
	
	/**
	 * Attempt to automatically update the shader's uniforms, this will invoke {@link #updateUniforms(Transform, Material, String, String)} if it's unable to do so.
	 * @param transform The transform of the object.
	 * @param material The material currently being used.
	 * @param renderingEngine The rendering engine.
	 * @see #updateUniforms(Transform, Material, String, String)
	 */
	public final void updateUniforms(Transform transform, Material material, Camera camera)
	{
		ShaderUpdater.updateUniforms(this, transform, material, camera);
	}
	
	/**
	 * Fallback method for {@link #updateUniforms(Transform, Material, RenderingEngine)}, override this in your own class to add support for custom shader variables.
	 * @param transform The transform of the object.
	 * @param material The material currently being used.
	 * @param name The name of the uniform.
	 * @param type The type of the uniform.
	 * @see #updateUniforms(Transform, Material, RenderingEngine)
	 */
	protected void updateUniforms(Transform transform, Material material, Camera camera, String name, String type)
	{
	}
	
	/**
	 * Link the shader program.
	 */
	private void link()
	{
		glLinkProgram(programId);

		if(glGetProgrami(programId, GL_LINK_STATUS) == 0)
		{
			System.err.println("[Shader] Unable to link shader program: " + glGetProgramInfoLog(programId, 1024));
			System.exit(1);
		}

		glValidateProgram(programId);

		if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
		{
			System.err.println("[Shader] Unable to validate shader program: " + glGetProgramInfoLog(programId, 1024));
			System.exit(1);
		}
	}
	
	/**
	 * Create shaders out of a source, and add them to the program.
	 * @param source The source of the shader file.
	 */
	private void createShaders(String source)
	{
		if(source.contains("VS_BUILD"))
		{
			String vsSource = "#version " + ShaderUtils.getGLSLVersion() + "\n#define VS_BUILD\n#define GLSL_VERSION " + ShaderUtils.getGLSLVersion() + "\n" + source;
			
			int shaderId = ShaderUtils.createShader(GL_VERTEX_SHADER, programId, vsSource);
			shaderIds.add(shaderId);
		}
		
		if(source.contains("FS_BUILD"))
		{
			String fsSource = "#version " + ShaderUtils.getGLSLVersion() + "\n#define FS_BUILD\n#define GLSL_VERSION " + ShaderUtils.getGLSLVersion() + "\n" + source;
			
			int shaderId = ShaderUtils.createShader(GL_FRAGMENT_SHADER, programId, fsSource);
			shaderIds.add(shaderId);
		}
	}
	
	/**
	 * Find and add all attributes in a source.
	 * @param source The source of the shader file.
	 */
	private void addAttributes(String source)
	{
		String[] lines = source.split("\n");		
		int nextAttributeLocation = 0;
		
		for(String line : lines)
		{
			if(!line.startsWith("attribute"))
			{
				continue;
			}
			
			int begin = line.indexOf(' ', "attribute".length() + 1);
			int end = line.indexOf(';', begin);
			
			String name = line.substring(begin, end).trim();
			
			glBindAttribLocation(programId, nextAttributeLocation, name);
			nextAttributeLocation++;
		}
	}
	
	/**
	 * Find and add all uniforms in a source.
	 * @param source The source of the shader file.
	 */
	private void addUniforms(String source)
	{	
		Iterable<ShaderStruct> structs = findUniformsFromStructs(source);	
		String[] lines = source.split("\n");
		
		for(String line : lines)
		{
			if(!line.startsWith("uniform"))
			{
				continue;
			}
			
			int begin = "uniform".length();
			int end = line.indexOf(';', begin);
			
			String name = line.substring(line.indexOf(' ', begin + 1), end).trim();
			String type = line.substring(begin, line.indexOf(' ', begin + 1)).trim();
			
			addUniform(name, type, structs);
		}
	}
	
	/**
	 * Find all structs in a source, this will scan the source for structs and return a list of the structs contents.
	 * @param source The source of the shader file.
	 * @return A list of struct data.
	 */
	private Iterable<ShaderStruct> findUniformsFromStructs(String source)
	{
		List<ShaderStruct> result = new ArrayList<ShaderStruct>();	
		String[] lines = source.split("\n");
		
		for(int i = 0; i < lines.length; i++)
		{
			String line = lines[i].trim();
			
			if(!line.startsWith("struct"))
			{
				continue;
			}
			
			List<ShaderUniform> uniforms = new ArrayList<ShaderUniform>();
			
			int begin = "struct".length();
			int end = line.indexOf('{', begin) >= 0 ? line.indexOf('{', begin) : line.length();
			
			String structName = line.substring(begin, end).trim();
			
			int structBegin = ShaderUtils.getUniformStructBegin(source, i) + 1;
			int structEnd = ShaderUtils.getUniformStructEnd(source, structBegin);
			
			for(int j = structBegin; j < structEnd; j++)
			{
				line = lines[j].trim();
				
				if(line.startsWith("//") || line.startsWith("#"))
				{
					continue;
				}
				
				String name = line.substring(line.indexOf(' '), line.indexOf(';')).trim();
				String type = line.substring(0, line.indexOf(' ')).trim();
				
				ShaderUniform uniform = new ShaderUniform(name, type);
				uniform.setStructName(structName);
				
				uniforms.add(uniform);
			}
			
			result.add(new ShaderStruct(structName, uniforms));			
		}
		
		return result;
	}
	
	/**
	 * Add a single uniform.
	 * @param name The name of the uniform.
	 * @param type The type of the uniform.
	 * @param structs A list of known structs, used to determine if the uniform is part of a struct.
	 * @see #addUniform(String, String, Iterable, String)
	 */
	private void addUniform(String name, String type, Iterable<ShaderStruct> structs)
	{
		addUniform(name, type, structs, "");
	}
	
	/**
	 * Add a single uniform.
	 * @param name The name of the uniform.
	 * @param type The type of the uniform.
	 * @param structs A list of known structs, used to determine if the uniform is part of a struct.
	 * @param structName The name of the struct.
	 */
	private void addUniform(String name, String type, Iterable<ShaderStruct> structs, String structName)
	{		
		for(ShaderStruct struct : structs)
		{
			if(struct.getName().equals(type))
			{
				for(ShaderUniform uniform : struct.getUniforms())
				{
					addUniform(name + "." + uniform.getName(), uniform.getType(), structs, struct.getName());
				}
				
				return;
			}
		}
		
		int location = glGetUniformLocation(programId, name);
		
		uniformLookup.put(name, location);
		
		if(name.contains("."))
		{
			name = name.substring(0, name.indexOf('.'));
		}
		
		ShaderUniform uniform = new ShaderUniform(name, type, location);
		uniform.setStructName(structName);
		uniforms.add(uniform);
	}
	
	/**
	 * Set the {@code int} value of an uniform.
	 * @param name The name of the uniform.
	 * @param value The value of the uniform.
	 */
	public final void setUniformi(String name, int value)
	{
		glUniform1i(uniformLookup.get(name), value);
	}
	
	/**
	 * Set the {@code float} value of an uniform.
	 * @param name The name of the uniform.
	 * @param value The value of the uniform.
	 */
	public final void setUniformf(String name, float value)
	{
		glUniform1f(uniformLookup.get(name), value);
	}

	/**
	 * Set the {@code vec2} value of an uniform.
	 * @param name The name of the uniform.
	 * @param value The value of the uniform.
	 */
	public final void setUniform(String name, Vector2f value)
	{
		glUniform2f(uniformLookup.get(name), value.x, value.y);
	}
	
	/**
	 * Set the {@code vec3} value of an uniform.
	 * @param name The name of the uniform.
	 * @param value The value of the uniform.
	 */
	public final void setUniform(String name, Vector3f value)
	{
		glUniform3f(uniformLookup.get(name), value.x, value.y, value.z);
	}
	
	/**
	 * Set the {@code mat4} value of an uniform.
	 * @param name The name of the uniform.
	 * @param value The value of the uniform.
	 */
	public final void setUniform(String name, Matrix4f value)
	{
		glUniformMatrix4fv(uniformLookup.get(name), true, Buffer.createFlippedBuffer(value));
	}
	
	/**
	 * @return A list containing all uniforms.
	 */
	public final Iterable<ShaderUniform> getUniforms()
	{
		return uniforms;
	}
	
	/**
	 * @return A list containing the names of all uniforms.
	 */
	public final Iterable<String> getUniformNames()
	{
		List<String> result = new ArrayList<String>();
		
		for(ShaderUniform uniform : uniforms)
		{
			result.add(uniform.getName());
		}
		
		return result;
	}
	
	/**
	 * @return A list containing the types of all uniforms.
	 */
	public final Iterable<String> getUniformTypes()
	{
		List<String> result = new ArrayList<String>();
		
		for(ShaderUniform uniform : uniforms)
		{
			result.add(uniform.getType());
		}
		
		return result;
	}
}
