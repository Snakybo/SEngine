package com.snakybo.sengine.core;

import com.snakybo.sengine.object.GameObjectInternal;
import com.snakybo.sengine.rendering.RendererInternal;
import com.snakybo.sengine.window.Window;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public abstract class SEngine
{
	private static Game game;
	
	private static boolean isRunning;
	
	public static void start(Game game)
	{
		if(isRunning)
		{
			return;
		}
		
		if(!Window.isCreated())
		{
			throw new IllegalStateException("[SEngine] You have to create a window before starting the engine.");
		}
		
		SEngine.isRunning = true;
		SEngine.game = game;
		
		game.create();
		
		run();
	}
	
	public static void stop()
	{
		if(!isRunning)
		{
			return;
		}

		isRunning = false;
	}
	
	public static void onWindowFocusCallback(boolean focused)
	{
		if(game != null)
		{
			game.onFocus(focused);
		}
	}
	
	public static void onWindowIconifyCallback(boolean iconified)
	{
		if(game != null)
		{
			game.onIconify(iconified);
		}
	}
	
	public static void onCursorEnterCallback(boolean entered)
	{
		if(game != null)
		{
			game.onCursorEnter(entered);
		}
	}
	
	private static void run()
	{
		double unprocessedTime = 0.0;

		while(isRunning)
		{
			boolean render = false;
			
			Time.update();
			unprocessedTime += Time.getPassedTime();

			while(unprocessedTime > Time.getFrameTime())
			{
				render = true;

				unprocessedTime -= Time.getFrameTime();

				if(Window.isCloseRequested())
				{
					stop();
					return;
				}

				GameObjectInternal.updateInternal();
				GameObjectInternal.updateGameObjects();
				
				Input.update();
			}

			if(render)
			{
				RendererInternal.preRenderScene();
				RendererInternal.renderScene();
				RendererInternal.postRenderScene();
				
				Window.update();
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
		
		game.destroy();		
		
		Window.destroy();
	}
}
