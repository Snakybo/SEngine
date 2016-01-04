#if defined(VS_BUILD)

out vec2 texCoord0;

attribute vec3 position;
attribute vec2 texCoord;

uniform mat4 T_MVP;

void main()
{
	gl_Position = T_MVP * vec4(position, 1.0);
	texCoord0 = texCoord; 
}

#elif defined(FS_BUILD)

#include "internal/sampling"

in vec2 texCoord0;

uniform sampler2D R_filterTexture;
uniform vec3 R_blurScale;

DeclareFragOutput(0, vec4);
void main()
{
	vec4 color = vec4(1);
	
	color += texture2D(R_filterTexture, texCoord0 + (vec2(-3) * R_blurScale.xy)) * (1.0 / 64.0);
	color += texture2D(R_filterTexture, texCoord0 + (vec2(-2) * R_blurScale.xy)) * (6.0 / 64.0);
	color += texture2D(R_filterTexture, texCoord0 + (vec2(-1) * R_blurScale.xy)) * (15.0 / 64.0);
	color += texture2D(R_filterTexture, texCoord0 + (vec2(0) * R_blurScale.xy)) * (20.0 / 64.0);
	color += texture2D(R_filterTexture, texCoord0 + (vec2(1) * R_blurScale.xy)) * (15.0 / 64.0);
	color += texture2D(R_filterTexture, texCoord0 + (vec2(2) * R_blurScale.xy)) * (6.0 / 64.0);
	color += texture2D(R_filterTexture, texCoord0 + (vec2(3) * R_blurScale.xy)) * (1.0 / 64.0);
	
	SetFragOutput(0, color);
}

#endif