vec2 CalcParallaxTexCoords(sampler2D dispMap, mat3 tbnMatrix, vec3 directionToEye, vec2 texCoords, float scale, float bias) {
	return texCoords + (directionToEye * tbnMatrix).xy * (texture2D(dispMap, texCoords).r * scale + bias);
}