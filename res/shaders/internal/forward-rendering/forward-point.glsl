#include "internal/common.glh"

#if defined(VS_BUILD)

out vec2 texCoord0;
out vec3 worldPos0;
out vec4 shadowMapCoords0;
out mat3 tbnMatrix;

#include "internal/forward-rendering/forwardlighting.vsh"

#elif defined(FS_BUILD)

#include "internal/lighting.glh"

in vec2 texCoord0;
in vec3 worldPos0;
in vec4 shadowMapCoords0;
in mat3 tbnMatrix;

uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos) {
	return CalcPointLight(R_pointLight, normal, worldPos, specularIntensity, specularPower, C_eyePos);
}

#include "internal/lightingMain.fsh"

#endif