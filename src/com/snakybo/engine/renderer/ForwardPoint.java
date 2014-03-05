package com.snakybo.engine.renderer;

import com.snakybo.engine.components.BaseLight;
import com.snakybo.engine.components.PointLight;
import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Transform;

/** @author Kevin Krol
 *  @since Feb 12, 2014 */
public class ForwardPoint extends Shader {
	private static final ForwardPoint instance = new ForwardPoint();
	
	private ForwardPoint() {
		super();
		
		addVertexShaderFromFile("forward/point.vertex.glsl");
		addFragmentShaderFromFile("forward/point.fragment.glsl");
		
		setAttribLocation("position" , 0);
		setAttribLocation("texCoord", 1);
		setAttribLocation("normal", 2);
		
		compileShader();
		
		addUniform("model");
		addUniform("MVP");
		
		addUniform("specularIntensity");
		addUniform("specularPower");
		addUniform("eyePos");
		
		addUniform("pointLight.base.color");
		addUniform("pointLight.base.intensity");
		addUniform("pointLight.atten.constant");
		addUniform("pointLight.atten.linear");
		addUniform("pointLight.atten.exponent");
		addUniform("pointLight.position");
		addUniform("pointLight.range");
	}
	
	/** @see #updateUniforms(Transform, Material, Renderer) */
	public void updateUniforms(Transform transform, Material material, Renderer renderer) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderer.getCamera().getProjection().scale(worldMatrix);
		
		material.getTexture("diffuse").bind();
		
		setUniform("model", worldMatrix);
		setUniform("MVP", projectedMatrix);
		
		setUniformf("specularIntensity", material.getFloat("specularIntensity"));
		setUniformf("specularPower", material.getFloat("specularPower"));
		setUniform("eyePos", renderer.getCamera().getTransform().getTransformedPosition());
		setUniformPointLight("pointLight", (PointLight)renderer.getActiveLight());
	}
	
	/** Set an base light uniform
	 * @param uniformName The name of the uniform 
	 * @param baseLight The base light */
	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
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
	
	/** @return The instance of this shader */
	public static ForwardPoint getInstance() { return instance; }
}
