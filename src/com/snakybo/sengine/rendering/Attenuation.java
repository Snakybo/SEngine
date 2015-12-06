package com.snakybo.sengine.rendering;

import com.snakybo.sengine.utils.math.Vector3f;

// TODO: Write proper JavaDocs for the meaning of attenuation values

/** The attenuation class, this class holds information about lighting values *
 * @author Kevin
 * @since Apr 4, 2014 */
public class Attenuation extends Vector3f
{
	/** Constructor for the attenuation This constructor calls
	 * {@link #Attenuation(float)} with the parameter {@code 0.0f}
	 * @see #Attenuation(float) */
	public Attenuation()
	{
		this(0);
	}

	/** Constructor for the attenuation This constructor calls
	 * {@link #Attenuation(float, float)} with the parameter {@code 0.0f}
	 * @param constant The constant value
	 * @see #Attenuation(float, float) */
	public Attenuation(float constant)
	{
		this(constant, 0);
	}

	/** Constructor for the attenuation This constructor calls
	 * {@link #Attenuation(float, float, float)} with the parameter {@code 1.0f}
	 * @param constant The constant value
	 * @param linear The linear value
	 * @see #Attenuation(float, float, float) */
	public Attenuation(float constant, float linear)
	{
		this(constant, linear, 1.0f);
	}

	/** Constructor for the attenuation
	 * @param constant The constant value
	 * @param linear The linear value
	 * @param exponent The exponent value */
	public Attenuation(float constant, float linear, float exponent)
	{
		super(constant, linear, exponent);
	}

	/** Set the constant value of the attenuation
	 * @param constant The new exponent value */
	public final void setConstant(float constant)
	{
		x = constant;
	}

	/** Set the linear value of the attenuation
	 * @param linear The new exponent value */
	public final void setLinear(float linear)
	{
		y = linear;
	}

	/** Set the exponent value of the attenuation
	 * @param exponent The new exponent value */
	public final void setExponent(float exponent)
	{
		z = exponent;
	}

	/** @return The constant value of the attenuation */
	public final float getConstant()
	{
		return x;
	}

	/** @return The linear value of the attenuation */
	public final float getLinear()
	{
		return y;
	}

	/** @return The exponent value of the attenuation */
	public final float getExponent()
	{
		return z;
	}
}
