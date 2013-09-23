package com.base.engine.components;

public class Time
{
	public static final long SECOND = 1000000000L;

	private static double delta;

	public static long getTime()
	{
		return System.nanoTime();
	}

	public static double getDelta()
	{
		return delta;
	}

	public static void setDelta(double _delta)
	{
		delta = _delta;
	}
}
