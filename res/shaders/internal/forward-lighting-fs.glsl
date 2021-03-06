struct BaseLight
{
	vec3 color;
	float intensity;
};

struct Attenuation
{
	float constant;
	float linear;
	float exponent;
};

struct DirectionalLight
{
	BaseLight baseLight;
	vec3 direction;
};

struct PointLight
{
	BaseLight baseLight;
	Attenuation attenuation;
	vec3 position;
	float range;
};

struct SpotLight
{
	BaseLight baseLight;
	Attenuation attenuation;
	vec3 position;
	vec3 direction;
	float range;	
	float cutoff;
};

vec4 CalcLight(BaseLight baseLight, vec3 direction, vec3 normal, vec3 worldPos, float specularIntensity, float specularPower, vec3 eyePos)
{
	float diffuseFactor = clamp(dot(normal, -direction), 0.0, 1.0);

	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);

	diffuseColor = vec4(baseLight.color, 1.0) * baseLight.intensity * diffuseFactor;

	vec3 directionToEye = normalize(eyePos - worldPos);
	vec3 reflectDirection = normalize(reflect(direction, normal));

	float specularFactor = dot(directionToEye, reflectDirection);
	specularFactor = clamp(pow(specularFactor, specularPower), 0.0, 1.0);

	if(specularFactor > 0)
	{
		specularColor = vec4(baseLight.color, 1.0) * specularIntensity * specularFactor;
	}

	return diffuseColor + specularColor;
}

vec4 CalcPointLight(PointLight pointLight, vec3 normal, vec3 worldPos, float specularIntensity, float specularPower, vec3 eyePos)
{
	vec3 lightDirection = worldPos - pointLight.position;

	float distanceToPoint = length(lightDirection);

	if(distanceToPoint > pointLight.range)
	{
		return vec4(0, 0, 0, 0);
	}

	lightDirection = normalize(lightDirection);
	
	vec4 color = CalcLight(pointLight.baseLight, lightDirection, normal, worldPos, specularIntensity, specularPower, eyePos);
	float attenuation = pointLight.attenuation.constant + pointLight.attenuation.linear * distanceToPoint + pointLight.attenuation.exponent * distanceToPoint * distanceToPoint + 0.0001;
				 
	return color / attenuation;
}

vec4 CalcSpotLight(SpotLight spotLight, vec3 normal, vec3 worldPos, float specularIntensity, float specularPower, vec3 eyePos)
{
	vec3 lightDirection = worldPos - spotLight.position;

	float distanceToPoint = length(lightDirection);

	if(distanceToPoint > spotLight.range)
	{
		return vec4(0, 0, 0, 0);
	}

	lightDirection = normalize(lightDirection);
	
	vec4 color = CalcLight(spotLight.baseLight, lightDirection, normal, worldPos, specularIntensity, specularPower, eyePos);
	float attenuation = spotLight.attenuation.constant + spotLight.attenuation.linear * distanceToPoint + spotLight.attenuation.exponent * distanceToPoint * distanceToPoint + 0.0001;
				 
	return color / attenuation;
}