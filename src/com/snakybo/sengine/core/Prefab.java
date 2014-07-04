package com.snakybo.sengine.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.core.utils.Quaternion;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Material;
import com.snakybo.sengine.rendering.Mesh;
import com.snakybo.sengine.rendering.Texture;

/** @author Kevin
 * @since Jul 3, 2014 */
public class Prefab {
	public static GameObject load(String fileName) {
		BufferedReader prefabReader = null;
		
		GameObject go = new GameObject();
		
		try {
			prefabReader = new BufferedReader(new FileReader("./res/prefabs/" + fileName + ".prefab"));
			String line;
			
			Mesh mesh = null;
			Material material = new Material();
			
			while((line = prefabReader.readLine()) != null) {
				String[] args = line.split(" ");
				
				if(args[0].equals("Mesh")) {
					mesh = new Mesh(args[1]);
				} else if(args[0].equals("Material")) {
					if(args[1].equals("Diffuse")) {
						material.addDiffuseTexture(new Texture(args[2]));
					} else if(args[1].equals("NormalMap")) {
						material.addNormalMap(new Texture(args[2]));
					} else if(args[1].equals("DispMap")) {
						material.addDispMap(new Texture(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));
					} else if(args[1].equals("Specular")) {
						material.addSpecular(Float.parseFloat(args[2]), Float.parseFloat(args[3]));
					}
				} else if(args[0].equals("Transform")) {
					if(args[1].equals("Position")) {
						go.getTransform().getPosition().set(Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));
					} else if(args[1].equals("Rotation")) {
						go.getTransform().setRotation(new Quaternion(new Vector3f(Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4])), (float)Math.toRadians(Float.parseFloat(args[5]))));
					} else if(args[1].equals("Scale")) {
						go.getTransform().getScale().set(Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));
					}
				} else if(args[0].equals("Component")) { // TODO: Component adding does not work
					try {
						Constructor<?> constructor = Class.forName(args[1]).getConstructor();
						Object[] parameters = new Object[args.length - 2];
						
						for(int i = 2; i < args.length; i++)
							parameters[i - 2] = args[i];
						
						createObject(constructor, parameters);
					} catch(ClassNotFoundException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(mesh != null && material != null)
				go.addComponent(new MeshRenderer(mesh, material));
			
			prefabReader.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return go;
	}
	
	private static Object createObject(Constructor<?> constructor, Object[] parameters) {
		System.out.println("Constructor: " + constructor.toString());
		Object object = null;
		
		try {
			object = constructor.newInstance(parameters);
			System.out.println("Object: " + object.toString());
			return object;
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return object;
	}
}
