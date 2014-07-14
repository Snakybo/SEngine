#include "internal/common.glh"

#include "internal/forward-rendering/forwardlighting.glh"

#if defined(VS_BUILD)

#include "internal/forward-rendering/forwardlighting.vsh"

#elif defined(FS_BUILD)

#include "internal/lighting.glh"

uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

uniform DirectionalLight R_directionalLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos) {
	return CalcLight(R_directionalLight.baseLight, -R_directionalLight.direction, normal, worldPos, specularIntensity, specularPower, C_eyePos);
}

#include "internal/lightingMain.fsh"

#endif