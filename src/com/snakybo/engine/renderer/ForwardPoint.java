package com.snakybo.engine.renderer;

import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Transform;

/** @author Kevin Krol
 *  @since Feb 12, 2014 */
public class ForwardPoint extends Shader {
	private static final ForwardPoint instance = new ForwardPoint();
	
	private ForwardPoint() {
		super();
		
		addVertexShaderFromFile("forward-point-vertex.glsl");
		addFragmentShaderFromFile("forward-point-fragment.glsl");
		
		setAttribLocation("position" , 0);
		setAttribLocation("texCoord", 1);
		setAttribLocation("normal", 2);
		
		compileShader();
		
		addUniform("model");
		addUniform("MVP");
		
		addUniform("specularIntensity");
		addUniform("specularExponent");
		addUniform("eyePos");
		
		addUniform("pointLight.base.color");
		addUniform("pointLight.base.intensity");
		addUniform("pointLight.atten.constant");
		addUniform("pointLight.atten.linear");
		addUniform("pointLight.atten.exponent");
		addUniform("pointLight.position");
		addUniform("pointLight.range");
	}
	
	/** Update uniforms */
	public void updateUniforms(Transform transform, Material material) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = getRenderer().getCamera().getProjection().mul(worldMatrix);
		
		material.getTexture().bind();
		
		setUniform("model", worldMatrix);
		setUniform("MVP", projectedMatrix);
		
		setUniformf("specularIntensity", material.getSpecularIntensity());
		setUniformf("specularExponent", material.getSpecularExponent());
		setUniform("eyePos", getRenderer().getCamera().getPosition());
		setUniform("pointLight", getRenderer().getPointLight());
	}
	
	public void setUniform(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	public void setUniform(String uniformName, PointLight pointLight) {
		setUniform(uniformName + ".base", pointLight.getBaseLight());
		setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent());
		setUniform(uniformName + ".position", pointLight.getPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
	
	public static ForwardPoint getInstance() { return instance; }
}
