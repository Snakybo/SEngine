package com.snakybo.sengine.core;

import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.utils.Input;
import com.snakybo.sengine.utils.Time;

/** Core class for the engine
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class SEngine
{
	private RenderingEngine renderingEngine;
	private Game game;

	private double frameTime;

	private boolean isRunning;

	/** Constructor for the engine
	 * @param game The base class for your game */
	public SEngine(Game game)
	{
		if(!Window.isCreated())
		{
			Window.create(1280, 720, "SEngine");
		}
		
		isRunning = false;
		this.game = game;
		
		renderingEngine = new RenderingEngine();
	}

	/** Start the engine
	 * @param frameRate The desired frame rate of the game */
	public void start(double frameRate)
	{
		if(isRunning)
		{
			return;
		}
		
		frameTime = 1.0 / frameRate;
		isRunning = true;

		game.internalInit(renderingEngine);
		game.init();

		run();
	}

	/** Stop the engine
	 * <p>
	 * This method stops the game loop and destroys the window
	 * </p>
	*/
	public void stop()
	{
		if (!isRunning)
			return;

		isRunning = false;

		Window.destroy();
	}

	/** Main loop of the engine, timing logic is handled here */
	private void run()
	{
		int frames = 0;

		double lastTime = Time.getTime();
		double unprocessedTime = 0.0;
		double frameCounter = 0.0;

		while (isRunning)
		{
			boolean render = false;

			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime;
			frameCounter += passedTime;

			if (frameCounter >= 1.0)
			{
				System.out.println(frames);
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

				game.input(frameTime);
				Input.update();

				game.update(frameTime);
			}

			if (render)
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
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
