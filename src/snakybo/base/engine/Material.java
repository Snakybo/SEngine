package snakybo.base.engine;

public class Material {
	private Texture texture;
	private Vector3f color;
	private float specularIntensity;
	private float specularExponent;
	
	public Material(Texture texture) {
		this(texture, new Vector3f(1, 1, 1));
	}
	
	public Material(Texture texture, Vector3f color) {
		this(texture, color, 2, 32);
	}
	
	public Material(Texture texture, Vector3f color, float specularIntensity, float specularExponent) {
		this.texture = texture;
		this.color = color;
		this.specularIntensity = specularIntensity;
		this.specularExponent = specularExponent;
	}

	/** Set texture */
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	/** Set color */
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	/** Set specular intensity */
	public void setSpecularIntensity(float specularIntensity) {
		this.specularIntensity = specularIntensity;
	}
	
	/** Set specular exponent */
	public void setSpecularExponent(float specularExponent) {
		this.specularExponent = specularExponent;
	}
	
	/** @return Vector3f: Texture */
	public Texture getTexture() {
		return texture;
	}

	/** @return Vector3f: RGB Color */
	public Vector3f getColor() {
		return color;
	}
	
	/** @return Float: specular intensity */
	public float getSpecularIntensity() {
		return specularIntensity;
	}
	
	/** @return Float: specular exponent */
	public float getSpecularExponent() {
		return specularExponent;
	}
}
