package com.snakybo.sengine.core;

import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.glfw.GLFWWindow;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public abstract class SEngine
{
	private static RenderingEngine renderingEngine;
	private static Game game;
	
	private static boolean isRunning;
	
	public static void start(Game game)
	{
		if(isRunning)
		{
			return;
		}
		
		if(!GLFWWindow.isCreated())
		{
			throw new IllegalStateException("[SEngine] You have to create a window before starting the engine.");
		}
		
		SEngine.renderingEngine = new RenderingEngine();
		SEngine.isRunning = true;
		SEngine.game = game;
		
		game.initialize();
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

				if(GLFWWindow.isCloseRequested())
				{
					stop();
					return;
				}

				game.update();
				Input.update();
			}

			if(render)
			{
				game.render(renderingEngine);
				
				GLFWWindow.update();
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
		
		game.deinitialize();
		game.destroy();		
		
		GLFWWindow.destroy();
	}
}
