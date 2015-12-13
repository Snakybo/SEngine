package com.snakybo.sengine.lighting;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.rendering.utils.ShadowUtils.ShadowCameraTransform;
import com.snakybo.sengine.rendering.utils.ShadowUtils.ShadowInfo;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.Color;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public class DirectionalLight extends Light
{
	private float halfShadowArea;
	
	public DirectionalLight()
	{
		this(new Color());
	}
	
	public DirectionalLight(Color color)
	{
		this(color, 0);
	}
	
	public DirectionalLight(Color color, float intensity)
	{
		this(color, intensity, 0);
	}
	
	public DirectionalLight(Color color, float intensity, int shadowMapSize)
	{
		this(color, intensity, shadowMapSize, 80);
	}
	
	public DirectionalLight(Color color, float intensity, int shadowMapSize, float shadowArea)
	{
		this(color, intensity, shadowMapSize, shadowArea, 1);
	}
	
	public DirectionalLight(Color color, float intensity, int shadowMapSize, float shadowArea, float shadowSoftness)
	{
		this(color, intensity, shadowMapSize, shadowArea, shadowSoftness, 0.2f);
	}
	
	public DirectionalLight(Color color, float intensity, int shadowMapSize, float shadowArea, float shadowSoftness, float lightBleedReductionAmount)
	{
		this(color, intensity, shadowMapSize, shadowArea, shadowSoftness, lightBleedReductionAmount, 0.00002f);
	}
	
	public DirectionalLight(Color color, float intensity, int shadowMapSize, float shadowArea, float shadowSoftness, float lightBleedReductionAmount, float minVariance)
	{
		super(color, intensity, new Shader("internal/forward-directional"));
		
		halfShadowArea = shadowArea / 2f;
		
		if(shadowMapSize > 0)
		{
			Matrix4f projection = Matrix4f.orthographic(-halfShadowArea, halfShadowArea, -halfShadowArea, halfShadowArea, -halfShadowArea, halfShadowArea);			
			shadowInfo = new ShadowInfo(projection, true, shadowMapSize, shadowSoftness, lightBleedReductionAmount, minVariance);
		}
	}
	
	@Override
	public ShadowCameraTransform calculateShadowCameraTransform()
	{
		Vector3f mainCameraPosition = Camera.getMainCamera().getTransform().getPosition();
		Quaternion mainCameraRotation = Camera.getMainCamera().getTransform().getRotation();
		
		Vector3f resultPosition = mainCameraPosition.add(mainCameraRotation.getForward().mul(halfShadowArea));
		Quaternion resultRotation = getTransform().getRotation();
		
		float worldTexelSize = (halfShadowArea * 2) / (float)(1 << shadowInfo.getSize());
		
		Vector3f lightSpaceCameraPosition = resultPosition.rotate(resultRotation.conjugate());
		
		lightSpaceCameraPosition.x = worldTexelSize * (float)Math.floor(lightSpaceCameraPosition.x / worldTexelSize);
		lightSpaceCameraPosition.y = worldTexelSize * (float)Math.floor(lightSpaceCameraPosition.y / worldTexelSize);
		
		resultPosition = lightSpaceCameraPosition.rotate(resultRotation);
		
		return new ShadowCameraTransform(resultPosition, resultRotation);
	}
	
	public final float getHalfShadowArea()
	{
		return halfShadowArea;
	}
}
