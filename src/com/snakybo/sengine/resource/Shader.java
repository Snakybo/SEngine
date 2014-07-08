package com.snakybo.sengine.resource;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.components.DirectionalLight;
import com.snakybo.sengine.components.PointLight;
import com.snakybo.sengine.components.SpotLight;
import com.snakybo.sengine.core.object.Transform;
import com.snakybo.sengine.rendering.Material;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.resource.management.ShaderData;
import com.snakybo.sengine.utils.Buffer;
import com.snakybo.sengine.utils.math.Matrix4f;
import com.snakybo.sengine.utils.math.Vector3f;

public class Shader {
	private static Map<String, ShaderData> resourceMap = new HashMap<String, ShaderData>();
	
	private ShaderData resource;
	private String fileName;
	
	public Shader() {
		this("basicShader");
	}
	
	public Shader(String fileName) {
		this.fileName = fileName;
		
		ShaderData existingResource = resourceMap.get(fileName);
		
		if(existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			resource = new ShaderData(fileName);
			resourceMap.put(fileName, resource);
		}
	}
	
	public Shader(Shader other) {
		fileName = other.fileName;
		resource = other.resource;
		
		resource.addReference();
	}
	
	@Override
	protected void finalize() {
		if(resource.removeReference() && !fileName.isEmpty())
			resourceMap.remove(fileName);
	}
	
	public void bind() {
		glUseProgram(resource.getProgram());
	}
	
	public void updateUniforms(Transform transform, Material material) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f mvpMatrix = RenderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);
		
		for(int i = 0; i < resource.getUniformNames().length; i++) {
			String uniformName = resource.getUniformNames()[i];
			String uniformType = resource.getUniformTypes()[i];
			
			if(uniformName.startsWith("R_")) {
				String unprefixedName = uniformName.substring(2);
				
				if(uniformType.equals("sampler2D")) {
					int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedName);
					
					material.getTexture(unprefixedName).bind(samplerSlot);
					setUniformi(uniformName, samplerSlot);
				} else if(uniformType.equals("vec3")) {
					setUniformVector3f(uniformName, RenderingEngine.rGetVector3f(unprefixedName));
				} else if(uniformType.equals("float")) {
					setUniformf(uniformName, RenderingEngine.rGetFloat(unprefixedName));
				} else if(uniformType.equals("DirectionalLight")) {
					setUniformDirectionalLight(uniformName, (DirectionalLight)RenderingEngine.getActiveLight());
				} else if(uniformType.equals("PointLight")) {
					setUniformPointLight(uniformName, (PointLight)RenderingEngine.getActiveLight());
				} else if(uniformType.equals("SpotLight")) {
					setUniformSpotLight(uniformName, (SpotLight)RenderingEngine.getActiveLight());
				} else {
					RenderingEngine.updateUniformStruct(transform, material, this, uniformName, uniformType);
				}
			} else if(uniformType.equals("sampler2D")) {
				int samplerSlot = RenderingEngine.getSamplerSlot(uniformName);
				
				material.getTexture(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			} else if(uniformName.startsWith("T_")) {
				if(uniformName.equals("T_MVP")) {
					setUniformMatrix4f(uniformName, mvpMatrix);
				} else if(uniformName.equals("T_model")) {
					setUniformMatrix4f(uniformName, worldMatrix);
				} else {
					throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
				}	
			} else if(uniformName.startsWith("C_")) {
				if(uniformName.equals("C_eyePos")) {
					setUniformVector3f(uniformName, RenderingEngine.getMainCamera().getTransform().getTransformedPosition());
				} else {
					throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
				}
			} else {
				if(uniformType.equals("vec3")) {
					setUniformVector3f(uniformName, material.getVector3f(uniformName));
				} else if(uniformType.equals("float")) {
					setUniformf(uniformName, material.getFloat(uniformName));
				} else {
					throw new IllegalArgumentException(uniformType + " is not a supported type in Material");
				}
			}
		}
	}
	
	public void setUniformi(String uniformName, int value) {
		glUniform1i(resource.getUniformMap().get(uniformName), value);
	}
	
	public void setUniformf(String uniformName, float value) {
		glUniform1f(resource.getUniformMap().get(uniformName), value);
	}
	
	public void setUniformMatrix4f(String uniformName, Matrix4f value) {
		glUniformMatrix4(resource.getUniformMap().get(uniformName), true, Buffer.createFlippedBuffer(value));
	}
	
	public void setUniformVector3f(String uniformName, Vector3f value) {
		glUniform3f(resource.getUniformMap().get(uniformName), value.x, value.y, value.z);
	}
	
	private void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
		setUniformVector3f(uniformName + ".direction", directionalLight.getDirection());
		setUniformVector3f(uniformName + ".base.color", directionalLight.getColor());
		
		setUniformf(uniformName + ".base.intensity", directionalLight.getIntensity());
	}
	
	private void setUniformPointLight(String uniformName, PointLight pointLight) {
		setUniformVector3f(uniformName + ".position", pointLight.getTransform().getTransformedPosition());
		setUniformVector3f(uniformName + ".base.color", pointLight.getColor());
		
		setUniformf(uniformName + ".base.intensity", pointLight.getIntensity());
		setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
	
	private void setUniformSpotLight(String uniformName, SpotLight spotLight) {
		setUniformVector3f(uniformName + ".position", spotLight.getTransform().getTransformedPosition());
		setUniformVector3f(uniformName + ".direction", spotLight.getDirection());
		setUniformVector3f(uniformName + ".base.color", spotLight.getColor());
		
		setUniformf(uniformName + ".base.intensity", spotLight.getIntensity());
		setUniformf(uniformName + ".atten.constant", spotLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.linear", spotLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.exponent", spotLight.getAttenuation().getExponent());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
		setUniformf(uniformName + ".range", spotLight.getRange());
	}
}
