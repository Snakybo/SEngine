package com.snakybo.sengine.resource.management;

import static org.lwjgl.opengl.GL11.glGetInteger;
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
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL30.GL_MAJOR_VERSION;
import static org.lwjgl.opengl.GL30.GL_MINOR_VERSION;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snakybo.sengine.utils.ReferenceCounter;

public class ShaderData extends ReferenceCounter {
	private static int supportedOpenGlLevel = 0;
	private static String glslVersion = "";
	
	private int program;
	
	private List<Integer> shaders;
	private List<String> uniformNames;
	private List<String> uniformTypes;
	
	private Map<String, Integer> uniformMap;
	
	public ShaderData(String fileName) {
		super();
		
		shaders = new ArrayList<Integer>();
		uniformNames = new ArrayList<String>();
		uniformTypes = new ArrayList<String>();
		
		uniformMap = new HashMap<String, Integer>();
		
		program = glCreateProgram();
		
		if(program == 0) {
			System.err.println("Error creating shader program");
			System.exit(1);
		}
		
		if(supportedOpenGlLevel == 0)
			determineOpenGlLevel();
		
		String shaderText = loadShader(fileName + ".glsl");
		
		String vertexShaderText = "#version " + glslVersion + "\n#define VS_BUILD\n#define GLSL_VERSION " + glslVersion + "\n" + shaderText;
		String fragmentShaderText = "#version " + glslVersion + "\n#define FS_BUILD\n#define GLSL_VERSION " + glslVersion + "\n" + shaderText;
		
		addVertexShader(vertexShaderText);
		addFragmentShader(fragmentShaderText);
		
		addAllAttributes(vertexShaderText, supportedOpenGlLevel < 320 ? "attribute" : "attribute"); // TODO: Support the "in" keyword in shaders
		
		compileShader();
		
		addShaderUniforms(vertexShaderText);
		addShaderUniforms(fragmentShaderText);
	}
	
	@Override
	protected void finalize() {
		for(int shader : shaders) {
			glDetachShader(program, shader);
			glDeleteShader(shader);
		}
		
		glDeleteProgram(program);
	}
	
	private void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}
	
	private void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}
	
	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);
		
		if(shader == 0) {
			System.err.println("Error creating shader type " + type);
			System.exit(1);
		}
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println("Error compiling shader of type " + type + "\n" + glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		glAttachShader(program, shader);
		
		shaders.add(shader);
	}
	
	private void addAllAttributes(String vertexShaderText, String attributeKeyword) {
		int currentAttribLocation = 0;
		int attributeLocation = vertexShaderText.indexOf(attributeKeyword);
		
		while(attributeLocation != -1) {
			boolean isCommented = false;
			
			int lastLineEnd = vertexShaderText.indexOf(";", attributeLocation);
			
			if(lastLineEnd != -1) {
				String potentialCommentSection = vertexShaderText.substring(lastLineEnd, (lastLineEnd + (lastLineEnd - attributeLocation)));
				
				isCommented = potentialCommentSection.contains("//");
			}
			
			if(!isCommented) {
				int begin = attributeLocation + attributeKeyword.length();
				int end = vertexShaderText.indexOf(";", begin);
				
				String attributeLine = vertexShaderText.substring(begin + 1, (begin + (end - begin)));
				
				begin = attributeLine.indexOf(" ");
				
				String attributeName = attributeLine.substring(begin + 1);
				
				glBindAttribLocation(program, currentAttribLocation, attributeName);
				currentAttribLocation++;
			}
			
			attributeLocation = vertexShaderText.indexOf(attributeKeyword, attributeLocation + attributeKeyword.length());
		}
	}
	
	private void addShaderUniforms(String shaderText) {
		final String UNIFORM_KEY = "uniform";
		
		List<UniformStruct> structs = findUniformStructs(shaderText);
		
		int uniformLocation = shaderText.indexOf(UNIFORM_KEY);
		
		while(uniformLocation != -1) {
			boolean isCommented = false;
			
			int lastLineEnd = shaderText.indexOf(";", uniformLocation);
			
			if(lastLineEnd != -1) {
				String potentialCommentSection = shaderText.substring(lastLineEnd, (lastLineEnd + (lastLineEnd - uniformLocation)));
				
				isCommented = potentialCommentSection.contains("//");
			}
			
			if(!isCommented) {
				int begin = uniformLocation + UNIFORM_KEY.length();
				int end = shaderText.indexOf(";", begin);
				
				String uniformLine = shaderText.substring(begin + 1, (begin + (end - begin)));
				
				begin = uniformLine.indexOf(" ");
				
				String uniformName = uniformLine.substring(begin + 1);
				String uniformType = uniformLine.substring(0, begin);
				
				uniformNames.add(uniformName);
				uniformTypes.add(uniformType);
				
				addUniform(uniformName, uniformType, structs);
			}
			
			uniformLocation = shaderText.indexOf(UNIFORM_KEY, uniformLocation + UNIFORM_KEY.length());
		}
	}
	
	private void addUniform(String uniformName, String uniformType, List<UniformStruct> structs) {
		boolean addThis = true;
		
		for(int i = 0; i < structs.size(); i++) {
			if(structs.get(i).getName().compareTo(uniformType) == 0) {
				addThis = false;
				
				for(int j = 0; i < structs.get(i).getMemberNames().length; j++)
					addUniform(uniformName + "." + structs.get(i).getMemberNames()[j].getName(), structs.get(i).getMemberNames()[j].getType(), structs);
			}
		}
		
		if(!addThis)
			return;
		
		int location = glGetUniformLocation(program, uniformName);
		
		uniformMap.put(uniformName, location);
	}
	
	private void compileShader() {
		glLinkProgram(program);

		if(glGetProgrami(program, GL_LINK_STATUS) == 0) {
			System.err.println("Error while linking a shader program \n" + glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}

		glValidateProgram(program);

		if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Shader failed to validate \n" + glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
	}
	
	public int getProgram() {
		return program;
	}
	
	public Integer[] getShaders() {
		return shaders.toArray(new Integer[shaders.size()]);
	}
	
	public String[] getUniformNames() {
		return uniformNames.toArray(new String[uniformNames.size()]);
	}
	
	public String[] getUniformTypes() {
		return uniformTypes.toArray(new String[uniformTypes.size()]);
	}
	
	public Map<String, Integer> getUniformMap() {
		return uniformMap;
	}
	
	private static void determineOpenGlLevel() {
		int majorVersion = glGetInteger(GL_MAJOR_VERSION);
		int minorVersion = glGetInteger(GL_MINOR_VERSION);
		
		supportedOpenGlLevel = majorVersion * 100 + minorVersion * 10;
		
		if(supportedOpenGlLevel >= 330) {
			glslVersion = Integer.toString(supportedOpenGlLevel);
		} else if(supportedOpenGlLevel >= 320) {
			glslVersion = "150";
		} else if(supportedOpenGlLevel >= 310) {
			glslVersion = "140";
		} else if(supportedOpenGlLevel >= 300) {
			glslVersion = "130";
		} else if(supportedOpenGlLevel >= 210) {
			glslVersion = "120";
		} else if(supportedOpenGlLevel >= 200) {
			glslVersion = "110";
		} else {
			System.err.println("Error: OpenGL Version " + majorVersion + "." + minorVersion + " does not support shaders.");
			System.exit(1);
		}
	}
	
	private static String loadShader(String fileName) {
		final String INCLUDE_DIRECTIVE = "#include";
		
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		
		try {
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			String line;
			
			while((line = shaderReader.readLine()) != null) {
				if(line.startsWith(INCLUDE_DIRECTIVE)) {
					shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
				} else {
					shaderSource.append(line).append("\n");
				}
			}
			
			shaderReader.close();
		} catch(IOException e) {
			System.err.println("Unable to load shader: " + fileName);
			e.printStackTrace();
		}
		
		return shaderSource.toString();
	}
	
	private static List<UniformStruct> findUniformStructs(String shaderText) {
		final String STRUCT_KEY = "struct";
		
		List<UniformStruct> result = new ArrayList<UniformStruct>();
		
		int structLocation = shaderText.indexOf(STRUCT_KEY);
		
		while(structLocation != -1) {
			structLocation += STRUCT_KEY.length() + 1;
			
			int braceOpening = shaderText.indexOf("{", structLocation);
			int braceClosing = shaderText.indexOf("}", structLocation);
			
			result.add(new UniformStruct(
					findUniformStructName(shaderText.substring(structLocation, braceOpening - structLocation)),
					findUniformStructComponents(shaderText.substring(braceOpening, braceClosing - braceOpening))));
			
			structLocation = shaderText.indexOf(STRUCT_KEY, structLocation);			
		}
		
		return result;
	}
	
	private static String findUniformStructName(String text) {
		return text.split("\\r?\\n")[0];
	}
	
	private static List<TypedData> findUniformStructComponents(String text) {
		final char[] charsToIgnore = {' ', '\n', 't', '{'};
		
		List<TypedData> result = new ArrayList<TypedData>();
		String[] structLines = text.split(";");
				
		for(int i = 0; i < structLines.length; i++) {
			int nameBegin = -1;
			int nameEnd = -1;
			
			for(int j = 0; j < structLines[i].length(); j++) {
				boolean isIgnorableCharacter = false;
				
				for(int k = 0; k < charsToIgnore.length; k++) {
					if(structLines[i].equals(charsToIgnore[k])) {
						isIgnorableCharacter = true;
						break;
					}
				}
				
				if(nameBegin == -1 && !isIgnorableCharacter) {
					nameBegin = j;
				} else if(nameBegin != -1 && isIgnorableCharacter) {
					nameEnd = j;
					break;
				}
			}
			
			if(nameBegin == -1 || nameEnd == -1)
				continue;
			
			TypedData newData = new TypedData(structLines[i].substring(nameEnd + 1), structLines[i].substring(nameBegin, nameEnd - nameBegin));
			
			result.add(newData);
		}
		
		return result;
	}
	
	public static class TypedData {
		private String name;
		private String type;
		
		public TypedData(String name, String type) {
			this.name = name;
			this.type = type;
		}
		
		public String getName() {
			return name;
		}
		
		public String getType() {
			return type;
		}
	}
	
	public static class UniformStruct {
		private List<TypedData> memberNames;
		
		private String name;
		
		public UniformStruct(String name, List<TypedData> memberNames) {
			this.name = name;
			this.memberNames = memberNames;
		}
		
		public String getName() {
			return name;
		}
		
		public TypedData[] getMemberNames() {
			return memberNames.toArray(new TypedData[memberNames.size()]);
		}
	}
}
