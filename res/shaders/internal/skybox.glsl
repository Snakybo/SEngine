#if defined(VS_BUILD)

out vec3 texCoord0;

attribute vec3 position;

uniform mat4 T_MVP;

void main()
{
	gl_Position = T_MVP * vec4(position, 1.0);
	texCoord0 = position; 
}

#elif defined(FS_BUILD)

in vec3 texCoord0;

uniform samplerCube R_skyboxTexture;

DeclareFragOutput(0, vec4);
void main()
{
	SetFragOutput(0, texture(R_skyboxTexture, texCoord0));
}
#endif