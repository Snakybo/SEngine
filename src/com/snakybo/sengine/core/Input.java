package com.snakybo.sengine.core;

import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CROSSHAIR_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HAND_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HRESIZE_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_IBEAM_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_VRESIZE_CURSOR;

import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.rendering.glfw.GLFWWindow;

public abstract class Input
{
	public enum KeyCode
	{
		KEY_SPACE         (0x20),
		KEY_APOSTROPHE    (0x27),
		KEY_COMMA         (0x2C),
		KEY_MINUS         (0x2D),
		KEY_PERIOD        (0x2E),
		KEY_SLASH         (0x2F),
		KEY_0             (0x30),
		KEY_1             (0x31),
		KEY_2             (0x32),
		KEY_3             (0x33),
		KEY_4             (0x34),
		KEY_5             (0x35),
		KEY_6             (0x36),
		KEY_7             (0x37),
		KEY_8             (0x38),
		KEY_9             (0x39),
		KEY_SEMICOLON     (0x3B),
		KEY_EQUAL         (0x3D),
		KEY_A             (0x41),
		KEY_B             (0x42),
		KEY_C             (0x43),
		KEY_D             (0x44),
		KEY_E             (0x45),
		KEY_F             (0x46),
		KEY_G             (0x47),
		KEY_H             (0x48),
		KEY_I             (0x49),
		KEY_J             (0x4A),
		KEY_K             (0x4B),
		KEY_L             (0x4C),
		KEY_M             (0x4D),
		KEY_N             (0x4E),
		KEY_O             (0x4F),
		KEY_P             (0x50),
		KEY_Q             (0x51),
		KEY_R             (0x52),
		KEY_S             (0x53),
		KEY_T             (0x54),
		KEY_U             (0x55),
		KEY_V             (0x56),
		KEY_W             (0x57),
		KEY_X             (0x58),
		KEY_Y             (0x59),
		KEY_Z             (0x5A),
		KEY_LEFT_BRACKET  (0x5B),
		KEY_BACKSLASH     (0x5C),
		KEY_RIGHT_BRACKET (0x5D),
		KEY_GRAVE_ACCENT  (0x60),
		KEY_WORLD_1       (0xA1),
		KEY_WORLD_2       (0xA2),
		KEY_ESCAPE        (0x100),
		KEY_ENTER         (0x101),
		KEY_TAB           (0x102),
		KEY_BACKSPACE     (0x103),
		KEY_INSERT        (0x104),
		KEY_DELETE        (0x105),
		KEY_RIGHT         (0x106),
		KEY_LEFT          (0x107),
		KEY_DOWN          (0x108),
		KEY_UP            (0x109),
		KEY_PAGE_UP       (0x10A),
		KEY_PAGE_DOWN     (0x10B),
		KEY_HOME          (0x10C),
		KEY_END           (0x10D),
		KEY_CAPS_LOCK     (0x118),
		KEY_SCROLL_LOCK   (0x119),
		KEY_NUM_LOCK      (0x11A),
		KEY_PRINT_SCREEN  (0x11B),
		KEY_PAUSE         (0x11C),
		KEY_F1            (0x122),
		KEY_F2            (0x123),
		KEY_F3            (0x124),
		KEY_F4            (0x125),
		KEY_F5            (0x126),
		KEY_F6            (0x127),
		KEY_F7            (0x128),
		KEY_F8            (0x129),
		KEY_F9            (0x12A),
		KEY_F10           (0x12B),
		KEY_F11           (0x12C),
		KEY_F12           (0x12D),
		KEY_F13           (0x12E),
		KEY_F14           (0x12F),
		KEY_F15           (0x130),
		KEY_F16           (0x131),
		KEY_F17           (0x132),
		KEY_F18           (0x133),
		KEY_F19           (0x134),
		KEY_F20           (0x135),
		KEY_F21           (0x136),
		KEY_F22           (0x137),
		KEY_F23           (0x138),
		KEY_F24           (0x139),
		KEY_F25           (0x13A),
		KEY_KP_0          (0x140),
		KEY_KP_1          (0x141),
		KEY_KP_2          (0x142),
		KEY_KP_3          (0x143),
		KEY_KP_4          (0x144),
		KEY_KP_5          (0x145),
		KEY_KP_6          (0x146),
		KEY_KP_7          (0x147),
		KEY_KP_8          (0x148),
		KEY_KP_9          (0x149),
		KEY_KP_DECIMAL    (0x14A),
		KEY_KP_DIVIDE     (0x14B),
		KEY_KP_MULTIPLY   (0x14C),
		KEY_KP_SUBTRACT   (0x14D),
		KEY_KP_ADD        (0x14E),
		KEY_KP_ENTER      (0x14F),
		KEY_KP_EQUAL      (0x150),
		KEY_LEFT_SHIFT    (0x154),
		KEY_LEFT_CONTROL  (0x155),
		KEY_LEFT_ALT      (0x156),
		KEY_LEFT_SUPER    (0x157),
		KEY_RIGHT_SHIFT   (0x158),
		KEY_RIGHT_CONTROL (0x159),
		KEY_RIGHT_ALT     (0x15A),
		KEY_RIGHT_SUPER   (0x15B),
		KEY_MENU          (0x15C);
		
		final int key;
		
		private KeyCode(int key)
		{
			this.key = key;
		}
	}
	
	public enum MouseButton
	{
		BUTTON_1      (0x0),
		BUTTON_2      (0x1),
		BUTTON_3      (0x2),
		BUTTON_4      (0x3),
		BUTTON_5      (0x4),
		BUTTON_6      (0x5),
		BUTTON_7      (0x6),
		BUTTON_8      (0x7),
		BUTTON_LAST   (BUTTON_8),
		BUTTON_LEFT   (BUTTON_1),
		BUTTON_RIGHT  (BUTTON_2),
		BUTTON_MIDDLE (BUTTON_3);
		
		private final int button;
		
		private MouseButton(MouseButton button)
		{
			this(button.button);
		}
		
		private MouseButton(int button)
		{
			this.button = button;
		}
	}
	
	private static boolean[] keys = new boolean[KeyCode.KEY_MENU.key];
	private static boolean[] lastKeys = new boolean[KeyCode.KEY_MENU.key];
	
	private static boolean[] mouseButtons = new boolean[MouseButton.BUTTON_8.button];
	private static boolean[] lastMouseButtons = new boolean[MouseButton.BUTTON_8.button];
	
	private static Vector2f scrollDelta;
	
	public static void update()
	{
		for(int i = 0; i < lastKeys.length; i++)
		{
			lastKeys[i] = false;
		}
		
		for(int i = 0; i < lastMouseButtons.length; i++)
		{
			lastMouseButtons[i] = false;
		}
		
		scrollDelta = new Vector2f();
	}
	
	public static void onKeyCallback(int key, boolean state)
	{
		keys[key] = state;
		lastKeys[key] = state;
	}
	
	public static void onMouseButtonCallback(int button, boolean state)
	{
		mouseButtons[button] = state;
		lastMouseButtons[button] = state;
	}
	
	public static void onScrollCallback(double x, double y)
	{
		scrollDelta = new Vector2f((float)x, (float)y);
	}
	
	public static void createArrowCursor()
	{
		GLFWWindow.createCursor(GLFW_ARROW_CURSOR);
	}
	
	public static void createIBeamCursor()
	{
		GLFWWindow.createCursor(GLFW_IBEAM_CURSOR);
	}
	
	public static void createCrosshairCursor()
	{
		GLFWWindow.createCursor(GLFW_CROSSHAIR_CURSOR);
	}
	
	public static void createHandCursor()
	{
		GLFWWindow.createCursor(GLFW_HAND_CURSOR);
	}
	
	public static void createHResizeCursor()
	{
		GLFWWindow.createCursor(GLFW_HRESIZE_CURSOR);
	}
	
	public static void createVResizeCursor()
	{
		GLFWWindow.createCursor(GLFW_VRESIZE_CURSOR);
	}
	
	public static void createCursor(String fileName, int xhot, int yhot)
	{
		GLFWWindow.createCursor(fileName, xhot, yhot);
	}

	public static void setMousePosition(Vector2f position)
	{
		GLFWWindow.setMousePosition(position);
	}

	public static void setCursor(boolean enabled)
	{
		GLFWWindow.setCursor(enabled);
	}

	public static boolean getKey(KeyCode keyCode)
	{
		return keys[keyCode.key];
	}

	public static boolean getKeyDown(KeyCode keyCode)
	{
		return getKey(keyCode) && !lastKeys[keyCode.key];
	}

	public static boolean getKeyUp(KeyCode keyCode)
	{
		return !getKey(keyCode) && lastKeys[keyCode.key];
	}
	
	public static boolean getMouse(int mouseButton)
	{
		return mouseButtons[mouseButton];
	}

	public static boolean getMouse(MouseButton mouseButton)
	{
		return mouseButtons[mouseButton.button];
	}
	
	public static boolean getMouseDown(int mouseButton)
	{
		return getMouse(mouseButton) && !lastMouseButtons[mouseButton];
	}

	public static boolean getMouseDown(MouseButton mouseButton)
	{
		return getMouse(mouseButton) && !lastMouseButtons[mouseButton.button];
	}
	
	public static boolean getMouseUp(int mouseButton)
	{
		return !getMouse(mouseButton) && lastMouseButtons[mouseButton];
	}

	public static boolean getMouseUp(MouseButton mouseButton)
	{
		return !getMouse(mouseButton) && lastMouseButtons[mouseButton.button];
	}

	public static Vector2f getMousePosition()
	{
		return GLFWWindow.getMousePosition();
	}
	
	public static Vector2f getSrollDelta()
	{
		return scrollDelta;
	}
}
