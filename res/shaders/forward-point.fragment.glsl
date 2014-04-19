#version 120
#include "lighting.fh.glsl"

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos) {
	return CalcPointLight(R_pointLight, normal, worldPos);
}

#include "lightingMain.fh.glsl"
