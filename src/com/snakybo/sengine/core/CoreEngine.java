package com.snakybo.sengine.core;

import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Window;

/** The core engine
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class CoreEngine {
	private RenderingEngine renderingEngine;
	
	private Game game;
	
	private double frameTime;
	
	private boolean isRunning;
	
	/** Constructor for the core engine
	 * @param game The base class for your game which extends {@link Game} */
	public CoreEngine(Game game) {
		this.game = game;
		
		this.isRunning = false;
		
		game.setEngine(this);
	}
	
	/** Create a window for the game
	 * @param width The width of the window
	 * @param height The height of the window
	 * @param title The title of the window
	 * @param framerate The desired framerate for the window */
	public void createWindow(int width, int height, String title, double framerate) {
		frameTime = 1.0 / framerate;
		
		Window.createWindow(width, height, title);
		
		this.renderingEngine = new RenderingEngine();
	}
	
	/** Start the engine */
	public void start() {
		if(isRunning)
			return;
		
		loop();
	}
	
	/** Stop the engine */
	public void stop() {
		if(!isRunning)
			return;
		
		isRunning = false;
	}
	
	/** Main loop for the engine */
	private void loop() {
		isRunning = true;
		
		int frames = 0;
		long frameCounter = 0;
		
		game.init();
		
		double lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while(isRunning) {
			boolean render = false;
			
			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			
			lastTime = startTime;
			
			unprocessedTime += passedTime;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime) {
				render = true;
				
				unprocessedTime -= frameTime;
				
				if(Window.isCloseRequested())
					stop();
				
				game.input((float)frameTime);
				Input.update();
				
				game.update((float)frameTime);
				
				if(frameCounter >= 1.0) {
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if(render) {
				game.render(renderingEngine);
				Window.render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		dispose();
	}
	
	/** Dispose of the engine, destroying the window and exiting the application */
	private void dispose() {
		Window.dispose();
		
		System.exit(0);
	}
	
	/** @return The rendering engine */
	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}
}
