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

in vec2 texCoord0;

uniform vec3 color;
uniform sampler2D diffuse;

DeclareFragOutput(0, vec4);
void main()
{
	SetFragOutput(0, color * texture2D(diffuse, texCoord0));
}
#endif