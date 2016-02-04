package com.snakybo.sengine.core;

import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;

import com.snakybo.sengine.core.input.InputInternal;
import com.snakybo.sengine.object.GameObjectInternal;
import com.snakybo.sengine.rendering.RendererInternal;
import com.snakybo.sengine.utils.Version;
import com.snakybo.sengine.window.WindowInternal;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public abstract class SEngine
{
	private class WindowFocusCallback extends GLFWWindowFocusCallback
	{
		@Override
		public void invoke(long window, int focused)
		{
			onFocus(focused == 1 ? true : false);
		}		
	}
	
	private class WindowIconifyCallback extends GLFWWindowIconifyCallback
	{
		@Override
		public void invoke(long window, int iconified)
		{
			onIconify(iconified == 1 ? true : false);
		}
	}
	
	private class CursorEnterCallback extends GLFWCursorEnterCallback
	{
		@Override
		public void invoke(long window, int entered)
		{
			onCursorEnter(entered == 1 ? true : false);
		}
	}
	
	public static final Version VERSION = new Version(0, 1, 0);
	
	private static SEngine instance;
	
	private boolean running;
	
	protected SEngine()
	{
		instance = this;
		
		initialize();
		
		if(!WindowInternal.isCreated())
		{
			throw new IllegalStateException("[SEngine] You have to create a window in initialize()");
		}
		
		WindowInternal.setWindowFocusCallback(new WindowFocusCallback());
		WindowInternal.setWindowIconifyCallback(new WindowIconifyCallback());
		WindowInternal.setCursorEnterCallback(new CursorEnterCallback());
		
		InputInternal.initialize();
		RendererInternal.initialize();
		
		onCreate();
		
		loop();
	}
	
	protected abstract void initialize();
	
	protected void onCreate()
	{
	}
	
	protected void onDestroy()
	{
	}
	
	protected void onIconify(boolean iconified)
	{
	}
	
	protected void onFocus(boolean focused)
	{
	}
	
	protected void onCursorEnter(boolean entered)
	{
	}
	
	private final void loop()
	{
		running = true;
		double unprocessedTime = 0.0;

		while(running)
		{
			boolean render = false;
			
			Time.update();
			unprocessedTime += Time.getPassedTime();

			while(unprocessedTime > Time.getFrameTime())
			{
				render = true;

				unprocessedTime -= Time.getFrameTime();

				if(WindowInternal.isCloseRequested())
				{
					stop();
				}

				GameObjectInternal.updateInternal();
				GameObjectInternal.updateGameObjects();
				
				InputInternal.update();
			}

			if(render)
			{
				RendererInternal.renderScene();
				
				WindowInternal.update();
				Time.onRender();
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		onDestroy();
		WindowInternal.destroy();
	}
	
	public static void stop()
	{
		instance.running = false;
	}
}
