package com.snakybo.sengine.resource;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.components.lighting.DirectionalLight;
import com.snakybo.sengine.components.lighting.PointLight;
import com.snakybo.sengine.components.lighting.SpotLight;
import com.snakybo.sengine.core.object.Transform;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.resource.management.ShaderData;
import com.snakybo.sengine.utils.Buffer;
import com.snakybo.sengine.utils.math.Matrix4f;
import com.snakybo.sengine.utils.math.Vector3f;

public class Shader
{
	private static final String DEFAULT_SHADER = "internal/default_shader";

	private static Map<String, ShaderData> resourceMap = new HashMap<String, ShaderData>();

	private ShaderData resource;
	private String fileName;

	public Shader()
	{
		this(DEFAULT_SHADER);
	}

	public Shader(String fileName)
	{
		this.fileName = fileName;

		ShaderData existingResource = resourceMap.get(fileName);

		if (existingResource != null)
		{
			resource = existingResource;
			resource.addReference();
		}
		else
		{
			resource = new ShaderData(fileName);
			resourceMap.put(fileName, resource);
		}
	}

	public Shader(Shader other)
	{
		fileName = other.fileName;
		resource = other.resource;

		resource.addReference();
	}

	@Override
	protected void finalize() throws Throwable
	{
		try
		{
			if (resource.removeReference() && !fileName.isEmpty())
				resourceMap.remove(fileName);
		}
		finally
		{
			super.finalize();
		}
	}

	public final void bind()
	{
		glUseProgram(resource.getProgram());
	}

	public final void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine)
	{
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f mvpMatrix = RenderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);

		for (int i = 0; i < resource.getUniformNames().length; i++)
		{
			String uniformName = resource.getUniformNames()[i];
			String uniformType = resource.getUniformTypes()[i];

			if (uniformName.startsWith("R_"))
			{
				String unprefixedName = uniformName.substring(2);

				if (unprefixedName.equals("lightMatrix"))
				{
					setUniformMatrix4f(uniformName, renderingEngine.getLightMatrix().mul(worldMatrix));
				}
				else if (uniformType.equals("sampler2D"))
				{
					int samplerSlot = renderingEngine.getSamplerSlot(unprefixedName);

					renderingEngine.getTexture(unprefixedName).bind(samplerSlot);
					setUniformi(uniformName, samplerSlot);
				}
				else if (uniformType.equals("vec3"))
				{
					setUniformVector3f(uniformName, renderingEngine.getVector3f(unprefixedName));
				}
				else if (uniformType.equals("float"))
				{
					setUniformf(uniformName, renderingEngine.getFloat(unprefixedName));
				}
				else if (uniformType.equals("DirectionalLight"))
				{
					setUniformDirectionalLight(uniformName, (DirectionalLight) renderingEngine.getActiveLight());
				}
				else if (uniformType.equals("PointLight"))
				{
					setUniformPointLight(uniformName, (PointLight) renderingEngine.getActiveLight());
				}
				else if (uniformType.equals("SpotLight"))
				{
					setUniformSpotLight(uniformName, (SpotLight) renderingEngine.getActiveLight());
				}
				else
				{
					updateUniforms(transform, material, uniformName, uniformType);
				}
			}
			else if (uniformType.equals("sampler2D"))
			{
				int samplerSlot = renderingEngine.getSamplerSlot(uniformName);
				material.getTexture(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			}
			else if (uniformName.startsWith("T_"))
			{
				if (uniformName.equals("T_MVP"))
				{
					setUniformMatrix4f(uniformName, mvpMatrix);
				}
				else if (uniformName.equals("T_model"))
				{
					setUniformMatrix4f(uniformName, worldMatrix);
				}
				else
				{
					throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
				}
			}
			else if (uniformName.startsWith("C_"))
			{
				if (uniformName.equals("C_eyePos"))
				{
					setUniformVector3f(uniformName, RenderingEngine.getMainCamera().getTransform().getPosition());
				}
				else
				{
					throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
				}
			}
			else
			{
				if (uniformType.equals("vec3"))
				{
					setUniformVector3f(uniformName, material.getVector3f(uniformName));
				}
				else if (uniformType.equals("float"))
				{
					setUniformf(uniformName, material.getFloat(uniformName));
				}
				else
				{
					throw new IllegalArgumentException(uniformType + " is not a supported type in Material");
				}
			}
		}
	}

	protected void updateUniforms(Transform transform, Material material, String uniformName, String uniformType)
	{
	}

	public final void setUniformi(String uniformName, int value)
	{
		glUniform1i(resource.getUniformMap().get(uniformName), value);
	}

	public final void setUniformf(String uniformName, float value)
	{
		glUniform1f(resource.getUniformMap().get(uniformName), value);
	}

	public final void setUniformMatrix4f(String uniformName, Matrix4f value)
	{
		glUniformMatrix4(resource.getUniformMap().get(uniformName), true, Buffer.createFlippedBuffer(value));
	}

	public final void setUniformVector3f(String uniformName, Vector3f value)
	{
		glUniform3f(resource.getUniformMap().get(uniformName), value.x, value.y, value.z);
	}

	private final void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight)
	{
		setUniformVector3f(uniformName + ".direction", directionalLight.getDirection());
		setUniformVector3f(uniformName + ".baseLight.color", directionalLight.getColor());

		setUniformf(uniformName + ".baseLight.intensity", directionalLight.getIntensity());
	}

	private final void setUniformPointLight(String uniformName, PointLight pointLight)
	{
		setUniformVector3f(uniformName + ".position", pointLight.getTransform().getPosition());
		setUniformVector3f(uniformName + ".baseLight.color", pointLight.getColor());

		setUniformf(uniformName + ".baseLight.intensity", pointLight.getIntensity());
		setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}

	private final void setUniformSpotLight(String uniformName, SpotLight spotLight)
	{
		setUniformVector3f(uniformName + ".pointLight.position", spotLight.getTransform().getPosition());
		setUniformVector3f(uniformName + ".direction", spotLight.getDirection());
		setUniformVector3f(uniformName + ".pointLight.baseLight.color", spotLight.getColor());

		setUniformf(uniformName + ".pointLight.baseLight.intensity", spotLight.getIntensity());
		setUniformf(uniformName + ".pointLight.attenuation.constant", spotLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".pointLight.attenuation.linear", spotLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".pointLight.attenuation.exponent", spotLight.getAttenuation().getExponent());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
		setUniformf(uniformName + ".pointLight.range", spotLight.getRange());
	}
}
