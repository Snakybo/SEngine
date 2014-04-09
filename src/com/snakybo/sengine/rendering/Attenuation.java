package com.snakybo.sengine.rendering;

import com.snakybo.sengine.core.utils.Vector3f;

/** Attenuation class extends {@link Vector3f}
 * 
 * <p>
 * Attenuation is used primarily in lights
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Attenuation extends Vector3f {
	/** Constructor for the attenuation
	 * @param constant The constant value
	 * @param linear The linear value
	 * @param exponent The exponent value */
	public Attenuation(float constant, float linear, float exponent) {
		super(constant, linear, exponent);
	}
	
	/** @return The constant of the attenuation */
	public float getConstant() {
		return getX();
	}
	
	/** @return The linear of the attenuation */
	public float getLinear() {
		return getY();
	}
	
	/** @return The exponent of the attenuation */
	public float getExponent() {
		return getZ();
	}
}
