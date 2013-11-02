package snakybo.base.engine;

public class SpotLight {
	private PointLight pointLight;
	private Vector3f direction;
	private float cutoff;
	
	public SpotLight(PointLight pointLight, Vector3f direction, float cutoff) {
		this.pointLight = pointLight;
		this.direction = direction.normalized();
		this.cutoff = cutoff;
	}

	/** Set point light */
	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
	}
	
	/** Set direction */
	public void setDirection(Vector3f direction) {
		this.direction = direction.normalized();
	}
	
	/** Set cut off */
	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}

	/** Get point light */
	public PointLight getPointLight() {
		return pointLight;
	}

	/** Get direction */
	public Vector3f getDirection() {
		return direction;
	}

	/** Get cut off */
	public float getCutoff() {
		return cutoff;
	}
}
