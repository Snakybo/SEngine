// Vertex shader
#ifdef VS_BUILD

#include "internal/forward-lighting-vs"

#endif

// Fragment shader
#ifdef FS_BUILD

#include "internal/forward-lighting-fs"

in vec2 texCoord0;
in vec3 worldPos0;
in vec4 shadowMapCoords0;
in mat3 tbnMatrix;

uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

uniform SpotLight R_spotLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos)
{
	vec3 lightDirection = normalize(worldPos - R_spotLight.position);
	float spotFactor = dot(lightDirection, R_spotLight.direction);
	
	if(spotFactor > R_spotLight.cutoff)
	{
		return CalcSpotLight(R_spotLight, normal, worldPos, specularIntensity, specularPower, C_eyePos) * (1.0 - (1.0 - spotFactor) / (1.0 - R_spotLight.cutoff));
	}
	
	return vec4(0, 0, 0, 0);
}

#include "internal/lighting"

#endif