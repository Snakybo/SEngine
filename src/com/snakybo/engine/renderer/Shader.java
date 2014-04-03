package com.snakybo.engine.renderer;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
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
import java.util.ArrayList;
import java.util.HashMap;

import com.snakybo.engine.components.light.BaseLight;
import com.snakybo.engine.components.light.DirectionalLight;
import com.snakybo.engine.components.light.PointLight;
import com.snakybo.engine.components.light.SpotLight;
import com.snakybo.engine.core.Transform;
import com.snakybo.engine.core.Util;
import com.snakybo.engine.math.Matrix4f;
import com.snakybo.engine.math.Vector3f;
import com.snakybo.engine.renderer.resourcemanagement.ShaderResource;

/** @author Kevin Krol */
public class Shader {
	private static HashMap<String, ShaderResource> loadedShaders = new HashMap<String, ShaderResource>();
	
	private ShaderResource resource;
	
	private String fileName;
	
	public Shader(String fileName) {
		this.fileName = fileName;
		
		ShaderResource existingResource = loadedShaders.get(fileName);
		
		if(existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			resource = new ShaderResource();
			loadedShaders.put(fileName, resource);
			
			String vertexText = loadShader(fileName + ".vertex.glsl");
			String fragmentText = loadShader(fileName + ".fragment.glsl");
			
			addVertexShader(vertexText);
			addFragmentShader(fragmentText);
			
			addAllAttributes(vertexText);
			
			compileShader();
			
			addAllUniforms(vertexText);
			addAllUniforms(fragmentText);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		if(resource.removeReference() && !fileName.isEmpty())
			loadedShaders.remove(fileName);
	}
	
	/** Bind the shader */
	public void bind() {
		glUseProgram(resource.getProgram());
	}
	
	/** Compile the shader */
	private void compileShader() {
		glLinkProgram(resource.getProgram());
		
		if(glGetProgrami(resource.getProgram(), GL_LINK_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}
		
		glValidateProgram(resource.getProgram());
		
		if(glGetProgrami(resource.getProgram(), GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}
	}
	
	private void addAllUniforms(String text) {
		final String UNIFORM_KEYWORD = "uniform";
		
		HashMap<String, ArrayList<GlslStruct>> structs = findUniformStructs(text);
		int startLocation = text.indexOf(UNIFORM_KEYWORD);
		
		while(startLocation != -1) {
			if(!(startLocation != 0
					&& (Character.isWhitespace(text.charAt(startLocation - 1)) || text
							.charAt(startLocation - 1) == ';') && Character.isWhitespace(text
					.charAt(startLocation + UNIFORM_KEYWORD.length()))))
				continue;
			
			int begin = startLocation + UNIFORM_KEYWORD.length() + 1;
			int end = text.indexOf(";", begin);
			
			String uniformLine = text.substring(begin, end).trim();
			
			int whitespacePos = uniformLine.indexOf(' ');
			
			String uniformType = uniformLine.substring(0, whitespacePos).trim();
			String uniformName =
					uniformLine.substring(whitespacePos + 1, uniformLine.length()).trim();
			
			resource.getUniformNames().add(uniformName);
			resource.getUniformTypes().add(uniformType);
			
			addUniform(uniformName, uniformType, structs);
			
			startLocation = text.indexOf(UNIFORM_KEYWORD, startLocation + UNIFORM_KEYWORD.length());
		}
	}
	
	private void addAllAttributes(String text) {
		final String ATTRIBUTE_KEYWORD = "attribute";
		
		int startLocation = text.indexOf(ATTRIBUTE_KEYWORD);
		int attribNum = 0;
		
		while(startLocation != -1) {
			if(!(startLocation != 0
					&& (Character.isWhitespace(text.charAt(startLocation - 1)) || text
							.charAt(startLocation - 1) == ';') && Character.isWhitespace(text
					.charAt(startLocation + ATTRIBUTE_KEYWORD.length()))))
				continue;
			
			int begin = startLocation + ATTRIBUTE_KEYWORD.length() + 1;
			int end = text.indexOf(";", begin);
			
			String attributeLine = text.substring(begin, end).trim();
			String attributeName =
					attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length())
							.trim();
			
			setAttribLocation(attributeName, attribNum);
			attribNum++;
			
			startLocation =
					text.indexOf(ATTRIBUTE_KEYWORD, startLocation + ATTRIBUTE_KEYWORD.length());
		}
		
	}
	
	/** Update all the uniforms of the shader
	 * @param transform The transformation of the object
	 * @param material The material of the object
	 * @param renderer The renderer */
	public void updateUniforms(Transform transform, Material material, Renderer renderer) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f mvpMatrix = renderer.getCamera().getProjection().scale(worldMatrix);
		
		for(int i = 0; i < resource.getUniformNames().size(); i++) {
			String uniformName = resource.getUniformNames().get(i);
			String uniformType = resource.getUniformTypes().get(i);
			
			if(uniformType.equals("sampler2D")) {
				int samplerSlot = renderer.getSamplerSlot(uniformName);
				
				material.getTexture(uniformName).bind();
				setUniformi(uniformName, samplerSlot);
			} else if(uniformName.startsWith("t_")) {
				if(uniformName.equals("t_mvp")) {
					setUniform(uniformName, mvpMatrix);
				} else if(uniformName.endsWith("t_model")) {
					setUniform(uniformName, worldMatrix);
				} else {
					throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
				}
			} else if(uniformName.startsWith("r_")) {
				String unprefixedUniformName = uniformName.substring(2);
				
				if(uniformType.equals("vec3")) {
					setUniform(uniformName, renderer.getVector3f(unprefixedUniformName));
				} else if(uniformType.equals("float")) {
					setUniformf(uniformName, renderer.getFloat(unprefixedUniformName));
				} else if(uniformType.equals("DirectionalLight")) {
					setUniformDirectionalLight(uniformName, (DirectionalLight)renderer.getActiveLight());
				} else if(uniformType.equals("PointLight")) {
					setUniformPointLight(uniformName, (PointLight)renderer.getActiveLight());
				}  else if(uniformType.equals("SpotLight")) {
					setUniformSpotLight(uniformName, (SpotLight)renderer.getActiveLight());
				} else {
					renderer.updateUniformStruct(transform, material, this, uniformName, uniformType);
				}
			} else if(uniformName.startsWith("c_")) {
				if(uniformName.equals("c_eyePos")) {
					setUniform(uniformName, renderer.getCamera().getTransform().getTransformedPosition());
				} else {
					throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
				}
			} else {
				if(uniformType.equals("vec3")) {
					setUniform(uniformName, material.getVector3f(uniformName));
				} else if(uniformType.equals("float")) {
					setUniformf(uniformName, material.getFloat(uniformName));
				} else {
					throw new IllegalArgumentException(uniformType + " is not a valid component of Material");
				}
			}
		}
	}
	
	/** Add a vertex shader to the shader
	 * @param text The content of the shader */
	private void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}
	
	/** Add a geometry shader to the shader
	 * @param text The content of the shader */
	@SuppressWarnings("unused")
	private void addGeometryShader(String text) {
		addProgram(text, GL_GEOMETRY_SHADER);
	}
	
	/** Add a fragment shader to the shader
	 * @param text The content of the shader */
	private void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}
	
	/** Set an integer uniform
	 * @param uniformName The name of the uniform
	 * @param value The value of the uniform */
	public void setUniformi(String uniformName, int value) {
		glUniform1i(resource.getUniforms().get(uniformName), value);
	}
	
	/** Set an float uniform
	 * @param uniformName The name of the uniform
	 * @param value The value of the uniform */
	public void setUniformf(String uniformName, float value) {
		glUniform1f(resource.getUniforms().get(uniformName), value);
	}
	
	/** Set an vector uniform
	 * @param uniformName The name of the uniform
	 * @param value The value of the uniform */
	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(resource.getUniforms().get(uniformName), value.getX(), value.getY(), value.getZ());
	}
	
	/** Set an matrix uniform
	 * @param uniformName The name of the uniform
	 * @param value The value of the uniform */
	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4(resource.getUniforms().get(uniformName), true, Util.createFlippedBuffer(value));
	}
	
	private class GlslStruct {
		public String name;
		public String type;
	}
	
	private void addUniform(String uniformName, String uniformType,
			HashMap<String, ArrayList<GlslStruct>> structs) {
		boolean addThis = true;
		ArrayList<GlslStruct> components = structs.get(uniformType);
		
		if(components != null) {
			addThis = false;
			
			for(GlslStruct struct : components)
				addUniform(uniformName + "." + struct.name, struct.type, structs);
		}
		
		if(!addThis)
			return;
		
		int uniform = glGetUniformLocation(resource.getProgram(), uniformName);
		
		if(uniform == 0xFFFFFFFF) {
			System.err.println("Error: Could not find uniform: " + uniformName);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		resource.getUniforms().put(uniformName, uniform);
	}
	
	private HashMap<String, ArrayList<GlslStruct>> findUniformStructs(String shaderText) {
		final String STRUCT_KEYWORD = "struct";
		
		HashMap<String, ArrayList<GlslStruct>> result =
				new HashMap<String, ArrayList<GlslStruct>>();
		
		int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
		
		while(structStartLocation != -1) {
			if(!(structStartLocation != 0
					&& (Character.isWhitespace(shaderText.charAt(structStartLocation - 1)) || shaderText
							.charAt(structStartLocation - 1) == ';') && Character
						.isWhitespace(shaderText.charAt(structStartLocation
								+ STRUCT_KEYWORD.length()))))
				continue;
			
			int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
			int braceBegin = shaderText.indexOf("{", nameBegin);
			int braceEnd = shaderText.indexOf("}", braceBegin);
			
			String structName = shaderText.substring(nameBegin, braceBegin).trim();
			ArrayList<GlslStruct> glslStructs = new ArrayList<GlslStruct>();
			
			int componentSemicolonPos = shaderText.indexOf(";", braceBegin);
			while(componentSemicolonPos != -1 && componentSemicolonPos < braceEnd) {
				int componentNameEnd = componentSemicolonPos + 1;
				
				while(Character.isWhitespace(shaderText.charAt(componentNameEnd - 1))
						|| shaderText.charAt(componentNameEnd - 1) == ';')
					componentNameEnd--;
				
				int componentNameStart = componentSemicolonPos;
				
				while(!Character.isWhitespace(shaderText.charAt(componentNameStart - 1)))
					componentNameStart--;
				
				int componentTypeEnd = componentNameStart;
				
				while(Character.isWhitespace(shaderText.charAt(componentTypeEnd - 1)))
					componentTypeEnd--;
				
				int componentTypeStart = componentTypeEnd;
				
				while(!Character.isWhitespace(shaderText.charAt(componentTypeStart - 1)))
					componentTypeStart--;
				
				String componentName = shaderText.substring(componentNameStart, componentNameEnd);
				String componentType = shaderText.substring(componentTypeStart, componentTypeEnd);
				
				GlslStruct glslStruct = new GlslStruct();
				glslStruct.name = componentName;
				glslStruct.type = componentType;
				
				glslStructs.add(glslStruct);
				
				componentSemicolonPos = shaderText.indexOf(";", componentSemicolonPos + 1);
			}
			
			result.put(structName, glslStructs);
			
			structStartLocation =
					shaderText.indexOf(STRUCT_KEYWORD,
							structStartLocation + STRUCT_KEYWORD.length());
		}
		
		return result;
	}
	
	/** Add a program to the shader
	 * @param text The content of the program
	 * @param type The type of program */
	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);
		
		if(shader == 0) {
			System.err
					.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		glAttachShader(resource.getProgram(), shader);
	}
	
	/** Set the attribute location
	 * @param attribute The attribute
	 * @param location The location */
	private void setAttribLocation(String attribute, int location) {
		glBindAttribLocation(resource.getProgram(), location, attribute);
	}
	
	/** Load a shader file
	 * @param fileName The name of the shader file
	 * @return The content of the file */
	private static String loadShader(String fileName) {
		final String INCLUDE_DIRECTIVE = "#include";
		
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		
		try {
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			String line;
			
			while((line = shaderReader.readLine()) != null) {
				if(line.startsWith(INCLUDE_DIRECTIVE)) {
					shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2,
							line.length() - 1)));
				} else {
					shaderSource.append(line).append("\n");
				}
			}
			
			shaderReader.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return shaderSource.toString();
	}
	
	/** Set an base light uniform
	 * @param uniformName The name of the uniform
	 * @param baseLight The base light */
	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	/** Set an directional light uniform
	 * @param uniformName The name of the uniform
	 * @param directionalLight The directional light */
	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}
	
	/** Set an point light uniform
	 * @param uniformName The name of the uniform
	 * @param pointLight The point light */
	public void setUniformPointLight(String uniformName, PointLight pointLight) {
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniformf(uniformName + ".atten.constant", pointLight.getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getExponent());
		setUniform(uniformName + ".position", pointLight.getTransform().getTransformedPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
	
	/** Set an spot light uniform
	 * @param uniformName The name of the uniform
	 * @param spotLight The spot light */
	public void setUniformSpotLight(String uniformName, SpotLight spotLight) {
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
}
