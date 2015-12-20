package com.snakybo.sengine.core;

import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Window;

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
		
		if(!Window.isCreated())
		{
			Window.create(1280, 720, "SEngine");
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

				game.update();
				Input.update();
			}

			if(render)
			{
				game.render(renderingEngine);
				
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
		
		game.deinitialize();
		game.destroy();		
		
		Window.destroy();
	}
}
