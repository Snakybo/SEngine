package com.snakybo.sengine.lighting;

import com.snakybo.sengine.lighting.utils.Attenuation;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.rendering.utils.ShadowUtils.ShadowInfo;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 * TODO: Make SpotLight extend PointLight, currently the shader system doesn't support it.
 */
public class SpotLight extends Light
{
	private static final int COLOR_DEPTH = 256;
	
	private Attenuation attenuation;

	private float range;
	private float cutoff;
	
	public SpotLight()
	{
		this(new Color());
	}
	
	public SpotLight(Color color)
	{
		this(color, 0);
	}
	
	public SpotLight(Color color, float intensity)
	{
		this(color, intensity, new Attenuation());
	}
	
	public SpotLight(Color color, float intensity, Attenuation attenuation)
	{
		this(color, intensity, attenuation, (float)Math.toRadians(170f));
	}
	
	public SpotLight(Color color, float intensity, Attenuation attenuation, float viewAngle)
	{
		this(color, intensity, attenuation, viewAngle, 0);
	}
	
	public SpotLight(Color color, float intensity, Attenuation attenuation, float viewAngle, int shadowMapSize)
	{
		this(color, intensity, attenuation, viewAngle, shadowMapSize, 1);
	}
	
	public SpotLight(Color color, float intensity, Attenuation attenuation, float viewAngle, int shadowMapSize, float shadowSoftness)
	{
		this(color, intensity, attenuation, viewAngle, shadowMapSize, shadowSoftness, 0.2f);
	}
	
	public SpotLight(Color color, float intensity, Attenuation attenuation, float viewAngle, int shadowMapSize, float shadowSoftness, float lightBleedReductionAmount)
	{
		this(color, intensity, attenuation, viewAngle, shadowMapSize, shadowSoftness, lightBleedReductionAmount, 0.00002f);
	}

	public SpotLight(Color color, float intensity, Attenuation attenuation, float viewAngle, int shadowMapSize, float shadowSoftness, float lightBleedReductionAmount, float minVariance)
	{
		super(color, intensity, new Shader("internal/forward-spot"));
		
		float a = attenuation.getExponent();
		float b = attenuation.getLinear();
		float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();

		this.range = (float)((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));
		this.attenuation = attenuation;		
		this.cutoff = (float)Math.cos(viewAngle / 2);
		
		if(shadowMapSize > 0)
		{
			Matrix4f projection = Matrix4f.perspective(viewAngle, 1f, 0.1f, range);
			shadowInfo = new ShadowInfo(projection, false, shadowMapSize, shadowSoftness, lightBleedReductionAmount, minVariance);
		}
	}
	
	public final Attenuation getAttenuation()
	{
		return attenuation;
	}
	
	public final float getRange()
	{
		return range;
	}

	public final float getCutoff()
	{
		return cutoff;
	}
}
