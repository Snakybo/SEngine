package snakybo.base.engine;

public class Attenuation {
	private float constant;
	private float linear;
	private float exponent;
	
	public Attenuation(float constant, float linear, float exponent) {
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
	}
	
	/** Set constant */
	public void setConstant(float constant) {
		this.constant = constant;
	}
	
	/** Set linear */
	public void setLinear(float linear) {
		this.linear = linear;
	}
	
	/** Set exponent */
	public void setExponent(float exponent) {
		this.exponent = exponent;
	}

	/** Get constant */
	public float getConstant() {
		return constant;
	}

	/** Get linear */
	public float getLinear() {
		return linear;
	}

	/** Get exponent */
	public float getExponent() {
		return exponent;
	}
}
