#include "internal/common.glh"

#if defined(VS_BUILD)

out vec2 texCoord0;
out vec3 worldPos0;
out mat3 tbnMatrix;

attribute vec3 position;
attribute vec2 texCoord;
attribute vec3 normal;
attribute vec3 tangent;

uniform mat4 T_model;
uniform mat4 T_MVP;

void main()
{
	gl_Position = T_MVP * vec4(position, 1.0);
	texCoord0 = texCoord; 
	worldPos0 = (T_model * vec4(position, 1.0)).xyz;

	vec3 n = normalize((T_model * vec4(normal, 0.0)).xyz);
	vec3 t = normalize((T_model * vec4(tangent, 0.0)).xyz);
	t = normalize(t - dot(t, n) * n);

	vec3 biTangent = cross(t, n);
	tbnMatrix = mat3(t, biTangent, n);
}

#elif defined(FS_BUILD)

#include "internal/sampling.glh"

in vec2 texCoord0;
in vec3 worldPos0;
in mat3 tbnMatrix;

uniform vec3 R_ambient;
uniform vec3 C_eyePos;
uniform vec3 color;

uniform sampler2D diffuse;
uniform sampler2D dispMap;

uniform float dispMapScale;
uniform float dispMapBias;

DeclareFragOutput(0, vec4);
void main()
{
	vec3 directionToEye = normalize(C_eyePos - worldPos0);
	vec2 texCoords = CalcParallaxTexCoords(dispMap, tbnMatrix, directionToEye, texCoord0, dispMapScale, dispMapBias);
	
	SetFragOutput(0, (vec4(color, 1) * texture2D(diffuse, texCoords)) * vec4(R_ambient, 1));
}

#endif