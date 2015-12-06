package example;

import com.snakybo.sengine.components.Camera.CameraComponent;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.components.lighting.DirectionalLight;
import com.snakybo.sengine.components.lighting.PointLight;
import com.snakybo.sengine.components.lighting.SpotLight;
import com.snakybo.sengine.core.Game;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.rendering.Attenuation;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.resource.Material;
import com.snakybo.sengine.resource.Mesh;
import com.snakybo.sengine.resource.Texture;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.utils.math.Matrix4f;
import com.snakybo.sengine.utils.math.Quaternion;
import com.snakybo.sengine.utils.math.Vector3f;

// Your core game class should extend the Game class
public class ExampleGame extends Game
{
/*
	// Called whenever the engine has been started
	@Override
	public void init(Window window)
	{
		// Create a matrix holding information about the projection of the
		// camera, parameters are: FOV, aspect ratio, Z-Near and Z-Far
		Matrix4f cameraProjection = new Matrix4f().initPerspective(90,
				(float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000);

		// Create a new camera game-object
		GameObject camera = new GameObject();

		// Add a free-look component to the camera game-object
		camera.addComponent(new FreeLook(Window.getCenter()));

		// Add a free-move component to the camera game-object
		camera.addComponent(new FreeMove());

		// Add the camera-component to the camera game-object, passing in the
		// camera projection to the component
		camera.addComponent(new CameraComponent(cameraProjection));

		// Create a set of materials, there's no need to assign them to a
		// variable, they're only loaded once
		new Material(
				// The name of the material
				"bricks",

				// The diffuse texture of the material
				new Texture("example/bricks.png"),

				// The specular intensity of the material
				0.5f,

				// The specular exponent of the material
				4,

				// The normal-map of the material
				new Texture("example/bricks_normal.png"),

				// The displacement map of the material
				new Texture("example/bricks_disp.png"),

				// The displacement map scale
				0.03f,

				// The displacement map offset
				-0.5f);

		new Material("bull", new Texture("example/bull_texture.jpg"), 4, 8, new Texture("example/bull_normal.jpg"));

		// Create a mesh renderer for the bull game-object, note that we create
		// a material with the name "bull", this will cause it to use the
		// previously created material
		MeshRenderer bullMesh = new MeshRenderer(new Mesh("example/bull.obj"), new Material("bull"));

		// When creating game-objects you can pass in the position, rotation and
		// scale directly in the game object
		GameObject bullObject = new GameObject(new Vector3f(2, -0.4f, 2), new Quaternion(), 5);

		// Add the mesh renderer component to the bull game-object
		bullObject.addComponent(bullMesh);

		// Add the camera-and bull game-objects to the scene
		addChild(camera);
		addChild(bullObject);

		// You can also create game-objects on one line, and adding them
		// immediately
		addChild(new GameObject(new Vector3f(0, -1, 0))
				.addComponent(new MeshRenderer(new Mesh("internal/plane.obj"), new Material("bricks"))));

		// Add lighting to the scene
		addLights();
	}

	private void addLights()
	{
		// Create a new directional light, with a light-gray color and an
		// intensity of 0.1
		DirectionalLight directionalLight = new DirectionalLight(new Color(0.8f, 0.8f, 0.8f), 0.1f);

		// Create a new point light, with a cyan color, an intensity of 0.9 and
		// an attenuation-exponent value of 1
		PointLight pointLight = new PointLight(new Color(0, 1, 0.5f), 0.9f, new Attenuation(0, 0, 1));

		// Create a new spot light, with a yellow color, a full intensity of 1,
		// an attenuation-exponent value of 0.05 and a cutoff of 0.7
		SpotLight spotLight = new SpotLight(new Color(1, 1, 0), 1, new Attenuation(0, 0, 0.05f), 0.7f);

		// Create game-objects for each light
		GameObject directionalLightObject = new GameObject().addComponent(directionalLight);
		GameObject pointLightObject = new GameObject().addComponent(pointLight);
		GameObject spotLightObject = new GameObject().addComponent(spotLight);

		// Transform the light objects
		directionalLightObject.getTransform()
				.setRotation(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-90)));

		spotLightObject.getTransform().getLocalPosition().set(3, 0, 3);
		spotLightObject.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float) Math.toRadians(90)));

		// Add the lights to the scene
		addChild(directionalLightObject);
		addChild(pointLightObject);
		addChild(spotLightObject);
	}*/
}