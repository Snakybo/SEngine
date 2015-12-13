vec2 CalcParallaxTexCoords(sampler2D dispMap, mat3 tbnMatrix, vec3 directionToEye, vec2 texCoords, float scale, float bias)
{
	return texCoords.xy + (directionToEye * tbnMatrix).xy * (texture2D(dispMap, texCoords.xy).r * scale + bias);
}

float linstep(float low, float heigh, float value)
{
	return clamp((value - low) / (heigh - low), 0.0, 1.0);
}

float SampleShadowMap(sampler2D shadowMap, vec2 coords, float compare)
{
	return step(compare, texture2D(shadowMap, coords.xy).r);
}

float SampleVarianceShadowMap(sampler2D shadowMap, vec2 coords, float compare, float varianceMin, float lightBleedingAmount)
{
	vec2 moments = texture2D(shadowMap, coords.xy).xy;
	
	float p = step(compare, moments.x);
	float variance = max(moments.y - moments.x * moments.x, varianceMin);
	
	float d = compare - moments.x;
	float pMax = linstep(lightBleedingAmount, 1.0, variance / (variance + d * d));
	
	return min(max(p, pMax), 1.0);
}