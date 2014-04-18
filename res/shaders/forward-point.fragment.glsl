#version 120
#include "lighting.glsl"

varying mat3 tbnMatrix0;
varying vec3 worldPos0;
varying vec3 normal0;
varying vec2 texCoord0;

uniform sampler2D diffuse;
uniform sampler2D normalMap;

uniform PointLight R_pointLight;

void main() {
	vec3 normal = normalize(tbnMatrix0 * (255.0 / 128.0 * texture2D(normalMap, texCoord0.xy).xyz - 1));
	
    gl_FragColor = texture2D(diffuse, texCoord0.xy) * CalcPointLight(R_pointLight, normalize(normal), worldPos0);
}
