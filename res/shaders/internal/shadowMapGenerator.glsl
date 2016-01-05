// Vertex shader
#ifdef VS_BUILD

attribute vec3 position;

uniform mat4 T_MVP;

void main()
{
	gl_Position = T_MVP * vec4(position, 1.0);
}

#endif

// Fragment shader
#ifdef FS_BUILD

DeclareFragOutput(0, vec4);
void main()
{
	float depth = gl_FragCoord.z;
	
	float dx = dFdx(depth);
	float dy = dFdy(depth);
	
	float moment2 = depth * depth + 0.25 * (dx * dx + dy * dy);

	SetFragOutput(0, vec4(depth, moment2, 0, 0));
}

#endif