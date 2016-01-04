#if defined(VS_BUILD)

#include "internal/forward-lighting-vs"

#elif defined(FS_BUILD)

#include "internal/forward-lighting-fs"

in vec2 texCoord0;
in vec3 worldPos0;
in vec4 shadowMapCoords0;
in mat3 tbnMatrix;

uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos)
{
	return CalcPointLight(R_pointLight, normal, worldPos, specularIntensity, specularPower, C_eyePos);
}

#include "internal/lighting"

#endif