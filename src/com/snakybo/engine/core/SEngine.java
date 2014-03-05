package com.snakybo.engine.core;

import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Window;

/** @author Kevin Krol */
public class SEngine {
	private boolean isRunning;
	
	private Renderer renderer;
	private Game game;
	
	private double frameTime;
	
	/** Initialize the engine
	 * @param game Your main game class that extends {@link Game} */
	public SEngine(Game game) {
		this.game = game;
		this.isRunning = false;
	}
	
	/** @see {@link #createWindow(int, int, String, double)} */
	public void createWindow(int width, int height) {
		createWindow(width, height, "SEngine game");
	}
	
	/** @see {@link #createWindow(int, int, String, double)} */
	public void createWindow(int width, int height, String title) {
		createWindow(width, height, title, 60);
	}
	
	/** Create a window
	 * @param width The width of the window
	 * @param height The height of the window
	 * @param title The title of the window
	 * @param frameRate The framerate of the window */
	public void createWindow(int width, int height, String title, double frameRate) {
		frameTime = 1.0 / frameRate;
		
		Window.createWindow(width, height, title);
		
		renderer = new Renderer();
	}
	
	/** Start the engine, does nothing if the engine is already started */
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
	
	/** Main update loop of the engine */
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
				
				if(frameCounter >= 1) {
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			
			if(render) {
				game.render(renderer);
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
		
		destroy();
	}
	
	/** Destroy the engine */
	private void destroy() {
		Window.destroy();
	}
}
