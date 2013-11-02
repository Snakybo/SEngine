package snakybo.base.engine;

public class Time {
	public static final long SECOND = 1000000000L;
	
	private static double delta;
	
	/** Set Delta time */
	public static void setDelta(double delta) {
		Time.delta = delta;
	}
	
	/** @return Long: System nanotime */
	public static long getTime() {
		return System.nanoTime();
	}
	
	/** @return Double: Delta time */
	public static double getDelta() {
		return delta;
	}
}
