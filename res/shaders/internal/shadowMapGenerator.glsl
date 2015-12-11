#include "internal/common.glh"

#if defined(VS_BUILD)

attribute vec3 position;

uniform mat4 T_MVP;

void main()
{
	gl_Position = T_MVP * vec4(position, 1.0);
}

#elif defined(FS_BUILD)

DeclareFragOutput(0, vec4);
void main()
{
	SetFragOutput(0, vec4(gl_FragCoord.z));
}

#endif