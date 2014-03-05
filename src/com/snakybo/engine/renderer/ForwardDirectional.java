package com.snakybo.engine.renderer;

import com.snakybo.engine.components.BaseLight;
import com.snakybo.engine.components.DirectionalLight;
import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Transform;

/** @author Kevin Krol
 *  @since Feb 12, 2014 */
public class ForwardDirectional extends Shader {
	private static final ForwardDirectional instance = new ForwardDirectional();
	
	private ForwardDirectional() {
		addVertexShaderFromFile("forward/directional.vertex.glsl");
		addFragmentShaderFromFile("forward/directional.fragment.glsl");
		
		setAttribLocation("position" , 0);
		setAttribLocation("texCoord", 1);
		setAttribLocation("normal", 2);
		
		compileShader();
		
		addUniform("model");
		addUniform("MVP");
		
		addUniform("specularIntensity");
		addUniform("specularPower");
		addUniform("eyePos");
		
		addUniform("directionalLight.base.color");
		addUniform("directionalLight.base.intensity");
		addUniform("directionalLight.direction");
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
		setUniformDirectionalLight("directionalLight", (DirectionalLight)renderer.getActiveLight());
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
	
	/** @return The instance of this shader */
	public static ForwardDirectional getInstance() { return instance; }
}
