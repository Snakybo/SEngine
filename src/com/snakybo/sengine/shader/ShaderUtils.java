package com.snakybo.sengine.shader;

import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL30.GL_MAJOR_VERSION;
import static org.lwjgl.opengl.GL30.GL_MINOR_VERSION;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A collection of shader utilities.
 * @author Kevin
 * @since Dec 8, 2015
 */
public abstract class ShaderUtils
{
	/**
	 * Container class for Shader uniforms.
	 * @author Kevin
	 * @since Dec 8, 2015
	 */
	public static class ShaderUniform
	{
		private String structName;
		private String name;
		private String type;		
		private int location;
		
		/**
		 * Create a new ShaderUniform.
		 * @param name The name of the uniform.
		 * @param type The type of the uniform.
		 */
		public ShaderUniform(String name, String type)
		{
			this(name, type, -1);
		}
		
		/**
		 * Create a new ShaderUniform.
		 * @param name The name of the uniform.
		 * @param type The type of the uniform.
		 * @param location = The OpenGL location of the uniform.
		 */
		public ShaderUniform(String name, String type, int location)
		{
			this.structName = "";
			this.name = name;
			this.type = type;
			this.location = location;			
		}
		
		/**
		 * Set the name of the uniform's struct, only used when the uniform is part of a struct.
		 * @param structName The name of the struct.
		 */
		void setStructName(String structName)
		{
			this.structName = structName;
		}
		
		/**
		 * @return The name of the uniform's struct.
		 */
		String getStructName()
		{
			return structName;
		}
		
		/**
		 * @return The name of the uniform.
		 */
		public String getName()
		{
			return name;
		}
		
		/**
		 * @return The type of the uniform.
		 */
		public String getType()
		{
			return type;
		}
		
		/**
		 * @return The OpenGL location of the uniform.
		 */
		public int getLocation()
		{
			return location;
		}
	}
	
	/**
	 * Container class for Shader structs.
	 * @author Kevin
	 * @since Dec 8, 2015
	 */
	public static class ShaderStruct
	{
		private Iterable<ShaderUniform> uniforms;
		private String name;
		
		/**
		 * Create a new ShaderStruct.
		 * @param name The name of the struct.
		 * @param type A list containing all uniforms in this struct.
		 */
		public ShaderStruct(String name, Iterable<ShaderUniform> uniforms)
		{
			this.uniforms = uniforms;
			this.name = name;
		}
		
		/**
		 * @return A list containing all uniforms in this struct.
		 */
		public Iterable<ShaderUniform> getUniforms()
		{
			return uniforms;
		}
		
		/**
		 * @return The name of the struct.
		 */
		public String getName()
		{
			return name;
		}
	}
	
	/**
	 * The default location for shaders.
	 */
	public static final String SHADER_FOLDER = "./res/shaders/";
	
	private static int openGLVersion;
	private static int glslVersion;
	
	static
	{
		int majorVersion = glGetInteger(GL_MAJOR_VERSION);
		int minorVersion = glGetInteger(GL_MINOR_VERSION);

		openGLVersion = majorVersion * 100 + minorVersion * 10;

		if(openGLVersion >= 330)
		{
			glslVersion = openGLVersion;
		}
		else if(openGLVersion >= 320)
		{
			glslVersion = 150;
		}
		else if(openGLVersion >= 310)
		{
			glslVersion = 140;
		}
		else if(openGLVersion >= 300)
		{
			glslVersion = 130;
		}
		else if(openGLVersion >= 210)
		{
			glslVersion = 120;
		}
		else if(openGLVersion >= 200)
		{
			glslVersion = 110;
		}
		else
		{
			System.err.println("[Shader] OpenGL " + majorVersion + "." + minorVersion + " does not support shaders");
			System.exit(1);
		}
	}
	
	/**
	 * Create a new shader.
	 * @param type The type of the shader.
	 * @param programId The ID of the shader program.
	 * @param source The source of the shader file.
	 * @return The ID of the shader.
	 */
	public static int createShader(int type, int programId, String source)
	{
		int shaderId = glCreateShader(type);
		
		if(shaderId == 0)
		{
			System.err.println("[Shader] Unable to create shader");
			System.exit(1);
		}
		
		glShaderSource(shaderId, source);
		glCompileShader(shaderId);

		if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
		{
			System.err.println("[Shader] Unable to compile shader: " + glGetShaderInfoLog(shaderId, 1024));
			System.exit(1);
		}

		glAttachShader(programId, shaderId);

		return shaderId;
	}
	
	/**
	 * Get the Start of a struct in a shader source.
	 * @param source The source of the shader file.
	 * @param begin The line to start seaching.
	 * @return The line on which an opening bracket was found.
	 */
	public static int getUniformStructBegin(String source, int begin)
	{
		String[] lines = source.split("\n");
		
		for(int i = begin; i < lines.length; i++)
		{
			String line = lines[i];
			
			if(line.contains("{"))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Get the ending of a struct in a shader source.
	 * @param source The source of the shader file.
	 * @param begin The line to start seaching.
	 * @return The line on which an closing bracket was found.
	 */
	public static int getUniformStructEnd(String source, int begin)
	{
		String[] lines = source.split("\n");
		
		for(int i = begin; i < lines.length; i++)
		{
			String line = lines[i];
			
			if(line.contains("}"))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Load a shader.
	 * @param fileName The file name of the shader.
	 * @return The contents of the shader file.
	 */
	public static String loadShaderFile(String fileName)
	{
		final String INCLUDE_DIRECTIVE = "#include";

		String result = "";
		BufferedReader shaderReader = null;

		try
		{
			shaderReader = new BufferedReader(new FileReader(SHADER_FOLDER + fileName));
			String line;

			while((line = shaderReader.readLine()) != null)
			{
				if(line.startsWith(INCLUDE_DIRECTIVE))
				{
					result += loadShaderFile(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1));
				}
				else
				{
					result += line + "\n";
				}
			}

			shaderReader.close();
		}
		catch(IOException e)
		{
			System.err.println("[Shader] No shader with name: " + fileName + " found.");
			System.exit(1);
		}

		return result;
	}
	
	/**
	 * @return The OpenGL version.
	 */
	public static int getOpenGLVersion()
	{
		return openGLVersion;
	}
	
	/**
	 * @return The GLSL version.
	 */
	public static int getGLSLVersion()
	{
		return glslVersion;
	}
}
