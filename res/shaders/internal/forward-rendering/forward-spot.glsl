#include "internal/common.glh"
#include "internal/forward-rendering/forwardlighting.glh"

#if defined(VS_BUILD)

#include "internal/forward-rendering/forwardlighting.vsh"

#elif defined(FS_BUILD)

#include "internal/lighting.glh"

uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

uniform SpotLight R_spotLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos) {
	vec3 lightDirection = normalize(worldPos - R_spotLight.pointLight.position);
	float spotFactor = dot(lightDirection, R_spotLight.direction);
	
	if(spotFactor > R_spotLight.cutoff)
		return CalcPointLight(R_spotLight.pointLight, normal, worldPos, specularIntensity, specularPower, C_eyePos) * (1.0 - (1.0 - spotFactor) / (1.0 - R_spotLight.cutoff));
	
	return vec4(0, 0, 0, 0);
}

#include "internal/lightingMain.fsh"

#endif