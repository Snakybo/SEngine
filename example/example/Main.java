package example;

import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.utils.math.Vector3f;

public class Main {
	public static void main(String[] args) {
		// Create a new instance of the engine, passing in your core game class
		SEngine engine = new SEngine(new ExampleGame());
		
		// Create a new window, passing in the width, height and title of the window
		Window window = new Window(1280, 720, "Example game");
		
		// Set the ambient color to a dark gray
		window.setAmbientColor(new Vector3f(0.2f, 0.2f, 0.2f));
		
		// Set the clear color to purple
		window.setClearColor(new Vector3f(0.5f, 0.0f, 1.0f));
		
		// Set the desired level of AA, 16 in this case.
		window.setAA(Window.MSAA, 16);
		
		// Create the window
		window.create();
		
		// Start the engine, passing in the window and the desired framerate
		engine.start(window, 60.0);
	}
}