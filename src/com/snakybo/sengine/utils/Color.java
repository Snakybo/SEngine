package com.snakybo.sengine.utils;

import com.snakybo.sengine.utils.math.Vector3f;

/** @author Kevin
 * @since Jul 6, 2014 */
public class Color extends Vector3f {
	/** Constructor for the color
	 * @param red The value of the red component
	 * @param green The value of the green component
	 * @param blue The value of the blue component */
	public Color(float red, float green, float blue) {
		super(red, green, blue);
	}
	
	/** Set the red component of the color
	 * @param red The new value of the red component */
	public void setRed(float red) {
		x = red;
	}
	
	/** Set the green component of the color
	 * @param green The new value of the green component */
	public void setGreen(float green) {
		y = green;
	}
	
	/** Set the blue component of the color
	 * @param blue The new value of the blue component */
	public void setBlue(float blue) {
		z = blue;
	}
	
	/** @return The value of the red component of the color */
	public float getRed() {
		return x;
	}
	
	/** @return The value of the green component of the color */
	public float getGreen() {
		return y;
	}
	
	/** @return The value of the blue component of the color */
	public float getBlue() {
		return z;
	}
}
