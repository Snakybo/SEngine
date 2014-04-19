package com.snakybo.sengine.rendering;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.IIOException;

import com.snakybo.sengine.components.BaseLight;
import com.snakybo.sengine.components.DirectionalLight;
import com.snakybo.sengine.components.PointLight;
import com.snakybo.sengine.components.SpotLight;
import com.snakybo.sengine.core.Transform;
import com.snakybo.sengine.core.utils.Buffer;
import com.snakybo.sengine.core.utils.Matrix4f;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.resourceManagement.ShaderResource;

/** Shader
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class Shader {
	private static HashMap<String, ShaderResource> loadedShaders = new HashMap<String, ShaderResource>();
	
	private ShaderResource resource;
	private String fileName;
	
	/** GLSL Struct class */
	private class GLSLStruct {
		public String name;
		public String type;
	}
	
	/** Constructor for the shader
	 * 
	 * <p>
	 * Shader files have a naming convention: <b><i>fileName</i>.vertex.glsl</b> and
	 * <b><i>fileName</i>.fragment.glsl</b>
	 * </p>
	 * 
	 * @param fileName The shader file to load */
	public Shader(String fileName) {
		this.fileName = fileName;
		
		ShaderResource oldResource = loadedShaders.get(fileName);
		
		if(oldResource != null) {
			resource = oldResource;
			resource.addReference();
		} else {
			resource = new ShaderResource();
			
			String vertexShaderText = loadShader(fileName + ".vertex.glsl");
			String fragmentShaderText = loadShader(fileName + ".fragment.glsl");
			
			addVertexShader(vertexShaderText);
			addFragmentShader(fragmentShaderText);
			
			addAllAttributes(vertexShaderText);
			
			compileShader();
			
			addAllUniforms(vertexShaderText);
			addAllUniforms(fragmentShaderText);
		}
	}
	
	@Override
	protected void finalize() {
		if(resource.removeReference() && !fileName.isEmpty()) {
			loadedShaders.remove(fileName);
		}
	}
	
	/** Bind the program and allow it to be used with OpenGL */
	public void bind() {
		glUseProgram(resource.getProgram());
	}
	
	/** Update all the uniforms in the program
	 * @param transform The transform of the object
	 * @param material The material of the object
	 * @param renderingEngine The rendering engine */
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f MVPMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);
		
		for(int i = 0; i < resource.getUniformNames().size(); i++) {
			String uniformName = resource.getUniformNames().get(i);
			String uniformType = resource.getUniformTypes().get(i);
			
			if(uniformType.equals("sampler2D")) {
				int samplerSlot = renderingEngine.getSamplerSlot(uniformName);
				material.getTexture(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			} else if(uniformName.startsWith("T_")) {
				if(uniformName.equals("T_MVP"))
					setUniform(uniformName, MVPMatrix);
				else if(uniformName.equals("T_model"))
					setUniform(uniformName, worldMatrix);
				else
					throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
			} else if(uniformName.startsWith("R_")) {
				String unprefixedUniformName = uniformName.substring(2);
				if(uniformType.equals("vec3"))
					setUniform(uniformName, renderingEngine.getVector3f(unprefixedUniformName));
				else if(uniformType.equals("float"))
					setUniformf(uniformName, renderingEngine.getFloat(unprefixedUniformName));
				else if(uniformType.equals("DirectionalLight"))
					setUniformDirectionalLight(uniformName, (DirectionalLight)renderingEngine.getActiveLight());
				else if(uniformType.equals("PointLight"))
					setUniformPointLight(uniformName, (PointLight)renderingEngine.getActiveLight());
				else if(uniformType.equals("SpotLight"))
					setUniformSpotLight(uniformName, (SpotLight)renderingEngine.getActiveLight());
				else
					renderingEngine.updateUniformStruct(transform, material, this, uniformName, uniformType);
			} else if(uniformName.startsWith("C_")) {
				if(uniformName.equals("C_eyePos"))
					setUniform(uniformName, renderingEngine.getMainCamera().getTransform().getPosition());
				else
					throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
			} else {
				if(uniformType.equals("vec3"))
					setUniform(uniformName, material.getVector3f(uniformName));
				else if(uniformType.equals("float"))
					setUniformf(uniformName, material.getFloat(uniformName));
				else
					throw new IllegalArgumentException(uniformType + " is not a supported type in Material");
			}
		}
	}
	
	/** Add all attributes to the program
	 * @param shaderText The text of the shader */
	private void addAllAttributes(String shaderText) {
		final String ATTRIBUTE_KEYWORD = "attribute";
		int attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);
		int attribNumber = 0;
		while(attributeStartLocation != -1) {
			if(!(attributeStartLocation != 0
					&& (Character.isWhitespace(shaderText.charAt(attributeStartLocation - 1)) || shaderText
							.charAt(attributeStartLocation - 1) == ';') && Character.isWhitespace(shaderText
					.charAt(attributeStartLocation + ATTRIBUTE_KEYWORD.length()))))
				continue;
			
			int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
			int end = shaderText.indexOf(";", begin);
			
			String attributeLine = shaderText.substring(begin, end).trim();
			String attributeName =
					attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length()).trim();
			
			glBindAttribLocation(resource.getProgram(), attribNumber, attributeName);
			attribNumber++;
			
			attributeStartLocation =
					shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
		}
	}
	
	/** Find uniforms in structs
	 * @param shaderText The text of the shader
	 * @return A hashmap with all the uniforms in a GLSL Struct */
	private HashMap<String, ArrayList<GLSLStruct>> findUniformStructs(String shaderText) {
		HashMap<String, ArrayList<GLSLStruct>> result = new HashMap<String, ArrayList<GLSLStruct>>();
		
		final String STRUCT_KEYWORD = "struct";
		int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
		while(structStartLocation != -1) {
			if(!(structStartLocation != 0
					&& (Character.isWhitespace(shaderText.charAt(structStartLocation - 1)) || shaderText
							.charAt(structStartLocation - 1) == ';') && Character.isWhitespace(shaderText
					.charAt(structStartLocation + STRUCT_KEYWORD.length()))))
				continue;
			
			int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
			int braceBegin = shaderText.indexOf("{", nameBegin);
			int braceEnd = shaderText.indexOf("}", braceBegin);
			
			String structName = shaderText.substring(nameBegin, braceBegin).trim();
			ArrayList<GLSLStruct> glslStructs = new ArrayList<GLSLStruct>();
			
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
				
				GLSLStruct glslStruct = new GLSLStruct();
				glslStruct.name = componentName;
				glslStruct.type = componentType;
				
				glslStructs.add(glslStruct);
				
				componentSemicolonPos = shaderText.indexOf(";", componentSemicolonPos + 1);
			}
			
			result.put(structName, glslStructs);
			
			structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
		}
		
		return result;
	}
	
	/** Add all uniforms to the program
	 * @param shaderText The text of the shader */
	private void addAllUniforms(String shaderText) {
		HashMap<String, ArrayList<GLSLStruct>> structs = findUniformStructs(shaderText);
		
		final String UNIFORM_KEYWORD = "uniform";
		int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);
		while(uniformStartLocation != -1) {
			if(!(uniformStartLocation != 0
					&& (Character.isWhitespace(shaderText.charAt(uniformStartLocation - 1)) || shaderText
							.charAt(uniformStartLocation - 1) == ';') && Character.isWhitespace(shaderText
					.charAt(uniformStartLocation + UNIFORM_KEYWORD.length()))))
				continue;
			
			int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
			int end = shaderText.indexOf(";", begin);
			
			String uniformLine = shaderText.substring(begin, end).trim();
			
			int whiteSpacePos = uniformLine.indexOf(' ');
			String uniformName = uniformLine.substring(whiteSpacePos + 1, uniformLine.length()).trim();
			String uniformType = uniformLine.substring(0, whiteSpacePos).trim();
			
			resource.getUniformNames().add(uniformName);
			resource.getUniformTypes().add(uniformType);
			addUniform(uniformName, uniformType, structs);
			
			uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
		}
	}
	
	private void addUniform(String uniformName, String uniformType, HashMap<String, ArrayList<GLSLStruct>> structs) {
		boolean addThis = true;
		ArrayList<GLSLStruct> structComponents = structs.get(uniformType);
		
		if(structComponents != null) {
			addThis = false;
			for(GLSLStruct struct : structComponents) {
				addUniform(uniformName + "." + struct.name, struct.type, structs);
			}
		}
		
		if(!addThis)
			return;
		
		int uniformLocation = glGetUniformLocation(resource.getProgram(), uniformName);
		
		if(uniformLocation == 0xFFFFFFFF) {
			System.err.println("Error: Could not find uniform: " + uniformName);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		resource.getUniforms().put(uniformName, uniformLocation);
	}
	
	/** Add a vertex shader to the program
	 * @param text The text of the shader */
	private void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}
	
	/** Add a fragment shader to the program
	 * @param text The text of the shader */
	private void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}
	
	/** Compile the shader */
	private void compileShader() {
		glLinkProgram(resource.getProgram());
		
		if(glGetProgrami(resource.getProgram(), GL_LINK_STATUS) == 0) {
			System.err.println("Error in shader " + fileName + ":\n" + glGetProgramInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}
		
		glValidateProgram(resource.getProgram());
		
		if(glGetProgrami(resource.getProgram(), GL_VALIDATE_STATUS) == 0) {
			System.err.println("Error in shader " + fileName + ":\n" + glGetProgramInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}
	}
	
	/** Add a program to the shader
	 * @param text The text of the program
	 * @param type The type of the program */
	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);
		
		if(shader == 0) {
			System.err.println("Error in shader " + fileName + ":\n" + "Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println("Error in shader " + fileName + ":\n" + glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		glAttachShader(resource.getProgram(), shader);
	}
	
	/** Set an int uniform
	 * @param uniformName The name of the uniform
	 * @param value The int to set as uniform */
	public void setUniformi(String uniformName, int value) {
		glUniform1i(resource.getUniforms().get(uniformName), value);
	}
	
	/** Set a float uniform
	 * @param uniformName The name of the uniform
	 * @param value The float to set as uniform */
	public void setUniformf(String uniformName, float value) {
		glUniform1f(resource.getUniforms().get(uniformName), value);
	}
	
	/** Set a vector 3 uniform
	 * @param uniformName The name of the uniform
	 * @param value The vector to set as uniform */
	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(resource.getUniforms().get(uniformName), value.getX(), value.getY(), value.getZ());
	}
	
	/** Set a matrix 4 uniform
	 * @param uniformName The name of the uniform
	 * @param value The matrix to set as uniform */
	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4(resource.getUniforms().get(uniformName), true, Buffer.createFlippedBuffer(value));
	}
	
	/** Set a base light uniform
	 * @param uniformName The name of the uniform
	 * @param baseLight The base light */
	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	/** Set a directional light uniform
	 * @param uniformName The name of the uniform
	 * @param directionalLight The directional light */
	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
		setUniformBaseLight(uniformName + ".baseLight", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}
	
	/** Set a point light uniform
	 * @param uniformName The name of the uniform
	 * @param pointLight The point light */
	public void setUniformPointLight(String uniformName, PointLight pointLight) {
		setUniformBaseLight(uniformName + ".baseLight", pointLight);
		setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
		setUniform(uniformName + ".position", pointLight.getTransform().getPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
	
	/** Set a spot light uniform
	 * @param uniformName The name of the uniform
	 * @param spotLight The spot light */
	public void setUniformSpotLight(String uniformName, SpotLight spotLight) {
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
	
	/** Load a shader file
	 * @param fileName The shader file
	 * @return A string with the content of the file */
	private static String loadShader(String fileName) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		final String INCLUDE_DIRECTIVE = "#include";
		
		try {
			File shaderFile = new File("./res/shaders/" + fileName);
			
			if(!shaderFile.exists() || shaderFile.isDirectory())
				throw new IIOException("The shader '" + fileName + "' doesn't exist or is a dictionary");
			
			shaderReader = new BufferedReader(new FileReader(shaderFile));
			String line;
			
			while((line = shaderReader.readLine()) != null) {
				if(line.startsWith(INCLUDE_DIRECTIVE)) {
					shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
				} else
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
