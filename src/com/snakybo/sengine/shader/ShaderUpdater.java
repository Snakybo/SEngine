package com.snakybo.sengine.shader;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.lighting.DirectionalLight;
import com.snakybo.sengine.lighting.PointLight;
import com.snakybo.sengine.lighting.SpotLight;
import com.snakybo.sengine.lighting.utils.LightUtils;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.object.Transform;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.shader.ShaderUtils.ShaderUniform;
import com.snakybo.sengine.utils.Color;

import sun.java2d.pipe.RenderingEngine;

/**
 * @author Kevin
 * @since Dec 8, 2015
 */
public abstract class ShaderUpdater
{
	/**
	 * Attempt to update the uniforms of a shader, called by {@link Shader#updateUniforms(Transform, Material, RenderingEngine)}.
	 * If it's unable to update an uniform, it will call {@link Shader#updateUniforms(Transform, Material, String, String)}.
	 * @param shader The shader to update.
	 * @param transform The transform of the object.
	 * @param material The material currently being used.
	 * @param camera The current camera.
	 */
	public static void updateUniforms(Shader shader, Transform transform, Material material, Camera camera)
	{
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f mvpMatrix = camera.getViewProjection().mul(worldMatrix);
		
		for(ShaderUniform uniform : shader.getUniforms())
		{
			String name = uniform.getName();
			String type = !uniform.getStructName().isEmpty() ? uniform.getStructName() : uniform.getType();
			
			if(name.startsWith("R_"))
			{
				String unprefixedName = name.substring(2);
				
				if(unprefixedName.equals("lightMatrix"))
				{
					shader.setUniform(name, LightUtils.getCurrentLightMatrix().mul(worldMatrix));
				}
				else if(type.equals("sampler2D"))
				{
					int samplerSlot = ShaderUniformContainer.getSampler(unprefixedName);

					ShaderUniformContainer.getTexture(unprefixedName).bind(samplerSlot);
					shader.setUniformi(name, samplerSlot);
				}
				else if(type.equals("vec3"))
				{
					try
					{
						shader.setUniform(name, ShaderUniformContainer.getVector3f(unprefixedName));
					}
					catch(ClassCastException e)
					{
						shader.setUniform(name, ShaderUniformContainer.getColor(unprefixedName));
					}
				}
				else if(type.equals("float"))
				{
					shader.setUniformf(name, ShaderUniformContainer.getFloat(unprefixedName));
				}
				else if(type.equals("DirectionalLight"))
				{
					setUniform(shader, name,(DirectionalLight)LightUtils.getCurrentLight());
				}
				else if(type.equals("PointLight"))
				{
					setUniform(shader, name, (PointLight)LightUtils.getCurrentLight());
				}
				else if(type.equals("SpotLight"))
				{
					setUniform(shader, name, (SpotLight)LightUtils.getCurrentLight());
				}
				else
				{
					shader.updateUniforms(transform, material, camera, name, type);
				}
			}
			else if(type.startsWith("sampler2D"))
			{
				int samplerSlot = ShaderUniformContainer.getSampler(name);
				material.get(Texture.class, name).bind(samplerSlot);
				shader.setUniformi(name, samplerSlot);
			}
			else if(name.startsWith("T_"))
			{
				if(name.equals("T_MVP"))
				{
					shader.setUniform(name, mvpMatrix);
				}
				else if(name.equals("T_model"))
				{
					shader.setUniform(name, worldMatrix);
				}
				else
				{
					throw new IllegalArgumentException(name + " is not a valid component of Transform");
				}
			}
			else if(name.startsWith("C_"))
			{
				if(name.equals("C_eyePos"))
				{
					shader.setUniform(name, Camera.getMainCamera().getTransform().getPosition());
				}
				else
				{
					throw new IllegalArgumentException(name + " is not a valid component of Camera");
				}
			}
			else
			{
				if(type.equals("vec3") || type.equals("float3"))
				{
					try
					{
						shader.setUniform(name, material.get(Vector3f.class, name));
					}
					catch(ClassCastException e)
					{
						shader.setUniform(name, material.get(Color.class, name));
					}
				}
				else if(type.equals("float"))
				{
					shader.setUniformf(name, material.get(Float.class, name));
				}
				else
				{
					throw new IllegalArgumentException(type + " is not a supported type in Material");
				}
			}
		}
	}
	
	/**
	 * Convenience method to update a DirectionalLight uniform.
	 * @param shader The shader to update.
	 * @param name The name of the uniform.
	 * @param light The DirectionalLight.
	 * @see DirectionalLight
	 */
	private static void setUniform(Shader shader, String name, DirectionalLight light)
	{
		shader.setUniform(name + ".direction", light.getTransform().getRotation().getForward());
		shader.setUniform(name + ".baseLight.color", light.getColor());

		shader.setUniformf(name + ".baseLight.intensity", light.getIntensity());
	}

	/**
	 * Convenience method to update a PointLight uniform.
	 * @param shader The shader to update.
	 * @param name The name of the uniform.
	 * @param light The PointLight.
	 * @see PointLight
	 */
	private static void setUniform(Shader shader, String name, PointLight light)
	{
		shader.setUniform(name + ".position", light.getTransform().getPosition());
		shader.setUniform(name + ".baseLight.color", light.getColor());

		shader.setUniformf(name + ".baseLight.intensity", light.getIntensity());
		shader.setUniformf(name + ".attenuation.constant", light.getAttenuation().constant);
		shader.setUniformf(name + ".attenuation.linear", light.getAttenuation().linear);
		shader.setUniformf(name + ".attenuation.exponent", light.getAttenuation().exponent);
		shader.setUniformf(name + ".range", light.getRange());
	}

	/**
	 * Convenience method to update a SpotLight uniform.
	 * @param shader The shader to update.
	 * @param name The name of the uniform.
	 * @param light The SpotLight.
	 * @see SpotLight
	 */
	private static void setUniform(Shader shader, String name, SpotLight light)
	{
		shader.setUniform(name + ".position", light.getTransform().getPosition());
		shader.setUniform(name + ".direction", light.getTransform().getRotation().getForward());
		shader.setUniform(name + ".baseLight.color", light.getColor());

		shader.setUniformf(name + ".baseLight.intensity", light.getIntensity());
		shader.setUniformf(name + ".attenuation.constant", light.getAttenuation().constant);
		shader.setUniformf(name + ".attenuation.linear", light.getAttenuation().linear);
		shader.setUniformf(name + ".attenuation.exponent", light.getAttenuation().exponent);
		shader.setUniformf(name + ".cutoff", light.getCutoff());
		shader.setUniformf(name + ".range", light.getRange());
	}
}
