#if GLSL_VERSION >= 150
	#define DeclareFragOutput(locationNumber, type) out type outputLocation##locationNumber
	#define SetFragOutput(locationNumber, val) outputLocation##locationNumber = val

	#define texture2D(tex, coord) texture(tex, coord)

	#ifdef VS_BUILD
		#define varying out
		#define attribute in
	#endif
	
	#ifdef FS_BUILD
		#define varying in
	#endif
#else
	#define DeclareFragOutput(locationNumber)
	#define SetFragOutput(locationNumber, val) gl_FragData[locationNumber] = val
	
	#ifdef VS_BUILD
		#define out varying
		#define in attribute
	#endif
	
	#ifdef FS_BUILD
		#define in varying
	#endif
#endif

#if GLSL_VERSION >= 400
	#define mad(a, b, c) fma(a, b, c)
#else
	#define mad(a, b, c) (a * b + c)
#endif

#define float2 vec2
#define float3 vec3
#define float4 vec4
#define int2 ivec2
#define int3 ivec3
#define int4 ivec4
#define bool2 bvec2
#define bool3 bvec3
#define bool4 bvec4

#define lerp(a, b, t) mix(a, b, t)
#define saturate(a) clamp(a, 0.0, 1.0)