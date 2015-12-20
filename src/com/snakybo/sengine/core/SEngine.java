package com.snakybo.sengine.core;

import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.utils.Time;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public abstract class SEngine
{
	private static RenderingEngine renderingEngine;
	private static Game game;

	private static double frameTime;

	private static boolean isRunning;
	
	public static void start(Game game, double fps)
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
		SEngine.frameTime = 1.0 / fps;
		SEngine.isRunning = true;
		SEngine.game = game;
		
		game.internalInit();
		game.init();
		
		run();
	}
	
	public static void stop()
	{
		if(!isRunning)
		{
			return;
		}

		isRunning = false;

		Window.destroy();
	}
	
	private static void run()
	{
		int frames = 0;

		double lastTime = Time.getCurrentTime();
		double unprocessedTime = 0.0;
		double frameCounter = 0.0;

		while(isRunning)
		{
			boolean render = false;

			double startTime = Time.getCurrentTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime;
			frameCounter += passedTime;

			if(frameCounter >= 1.0)
			{
				String windowTitle = Window.getTitle();				
				if(windowTitle.contains("(FPS"))
				{
					windowTitle = windowTitle.substring(0, windowTitle.lastIndexOf("(FPS") - 1);
				}
				
				Window.setTitle(windowTitle + " (FPS: " + frames + ")");
				
				frames = 0;
				frameCounter = 0.0;
			}

			while(unprocessedTime > frameTime)
			{
				render = true;

				unprocessedTime -= frameTime;

				if(Window.isCloseRequested())
				{
					stop();
					return;
				}

				game.update((float)frameTime);
				Input.update();
			}

			if(render)
			{
				game.render(renderingEngine);
				Window.update();
				frames++;
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
	}
}
