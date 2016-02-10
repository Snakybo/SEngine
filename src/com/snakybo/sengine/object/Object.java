package com.snakybo.sengine.object;

/**
 * @author Kevin
 * @since Jan 10, 2016
 */
public class Object
{
	public static void destroy(Object object)
	{
		if(object instanceof GameObject)
		{
			((GameObject)object).destroy();
		}
		else if(object instanceof Component)
		{
			((Component)object).getGameObject().removeComponent((Component)object);
		}
	}
}
