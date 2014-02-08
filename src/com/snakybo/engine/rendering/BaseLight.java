package snakybo.base.engine;

public class BaseLight {
	private Vector3f color;
	private float intensity;
	
	public BaseLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	/** Set the color */
	public void setColor(Vector3f color) {
		this.color = color;
	}

	/** Set the intensity */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	/** @return Vector3f: color */
	public Vector3f getColor() {
		return color;
	}

	/** @return Vector3f: intensity */
	public float getIntensity() {
		return intensity;
	}
}
