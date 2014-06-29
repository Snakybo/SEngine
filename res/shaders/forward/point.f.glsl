#version 120
#include "lighting.f.glsl"

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos) {
	return CalcPointLight(R_pointLight, normal, worldPos);
}

#include "lightingMain.f.glsl"
