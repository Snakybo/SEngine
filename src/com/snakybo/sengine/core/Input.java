package com.snakybo.sengine.core;

import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CROSSHAIR_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HAND_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HRESIZE_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_IBEAM_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_VRESIZE_CURSOR;

import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.rendering.glfw.GLFWWindow;

public class Input
{
	public class KeyCode
	{
		public static final int KEY_SPACE         = 0x20;
		public static final int KEY_APOSTROPHE    = 0x27;
		public static final int KEY_COMMA         = 0x2C;
		public static final int KEY_MINUS         = 0x2D;
		public static final int KEY_PERIOD        = 0x2E;
		public static final int KEY_SLASH         = 0x2F;
		public static final int KEY_0             = 0x30;
		public static final int KEY_1             = 0x31;
		public static final int KEY_2             = 0x32;
		public static final int KEY_3             = 0x33;
		public static final int KEY_4             = 0x34;
		public static final int KEY_5             = 0x35;
		public static final int KEY_6             = 0x36;
		public static final int KEY_7             = 0x37;
		public static final int KEY_8             = 0x38;
		public static final int KEY_9             = 0x39;
		public static final int KEY_SEMICOLON     = 0x3B;
		public static final int KEY_EQUAL         = 0x3D;
		public static final int KEY_A             = 0x41;
		public static final int KEY_B             = 0x42;
		public static final int KEY_C             = 0x43;
		public static final int KEY_D             = 0x44;
		public static final int KEY_E             = 0x45;
		public static final int KEY_F             = 0x46;
		public static final int KEY_G             = 0x47;
		public static final int KEY_H             = 0x48;
		public static final int KEY_I             = 0x49;
		public static final int KEY_J             = 0x4A;
		public static final int KEY_K             = 0x4B;
		public static final int KEY_L             = 0x4C;
		public static final int KEY_M             = 0x4D;
		public static final int KEY_N             = 0x4E;
		public static final int KEY_O             = 0x4F;
		public static final int KEY_P             = 0x50;
		public static final int KEY_Q             = 0x51;
		public static final int KEY_R             = 0x52;
		public static final int KEY_S             = 0x53;
		public static final int KEY_T             = 0x54;
		public static final int KEY_U             = 0x55;
		public static final int KEY_V             = 0x56;
		public static final int KEY_W             = 0x57;
		public static final int KEY_X             = 0x58;
		public static final int KEY_Y             = 0x59;
		public static final int KEY_Z             = 0x5A;
		public static final int KEY_LEFT_BRACKET  = 0x5B;
		public static final int KEY_BACKSLASH     = 0x5C;
		public static final int KEY_RIGHT_BRACKET = 0x5D;
		public static final int KEY_GRAVE_ACCENT  = 0x60;
		public static final int KEY_WORLD_1       = 0xA1;
		public static final int KEY_WORLD_2       = 0xA2;
		public static final int KEY_ESCAPE        = 0x100;
		public static final int KEY_ENTER         = 0x101;
		public static final int KEY_TAB           = 0x102;
		public static final int KEY_BACKSPACE     = 0x103;
		public static final int KEY_INSERT        = 0x104;
		public static final int KEY_DELETE        = 0x105;
		public static final int KEY_RIGHT         = 0x106;
		public static final int KEY_LEFT          = 0x107;
		public static final int KEY_DOWN          = 0x108;
		public static final int KEY_UP            = 0x109;
		public static final int KEY_PAGE_UP       = 0x10A;
		public static final int KEY_PAGE_DOWN     = 0x10B;
		public static final int KEY_HOME          = 0x10C;
		public static final int KEY_END           = 0x10D;
		public static final int KEY_CAPS_LOCK     = 0x118;
		public static final int KEY_SCROLL_LOCK   = 0x119;
		public static final int KEY_NUM_LOCK      = 0x11A;
		public static final int KEY_PRINT_SCREEN  = 0x11B;
		public static final int KEY_PAUSE         = 0x11C;
		public static final int KEY_F1            = 0x122;
		public static final int KEY_F2            = 0x123;
		public static final int KEY_F3            = 0x124;
		public static final int KEY_F4            = 0x125;
		public static final int KEY_F5            = 0x126;
		public static final int KEY_F6            = 0x127;
		public static final int KEY_F7            = 0x128;
		public static final int KEY_F8            = 0x129;
		public static final int KEY_F9            = 0x12A;
		public static final int KEY_F10           = 0x12B;
		public static final int KEY_F11           = 0x12C;
		public static final int KEY_F12           = 0x12D;
		public static final int KEY_F13           = 0x12E;
		public static final int KEY_F14           = 0x12F;
		public static final int KEY_F15           = 0x130;
		public static final int KEY_F16           = 0x131;
		public static final int KEY_F17           = 0x132;
		public static final int KEY_F18           = 0x133;
		public static final int KEY_F19           = 0x134;
		public static final int KEY_F20           = 0x135;
		public static final int KEY_F21           = 0x136;
		public static final int KEY_F22           = 0x137;
		public static final int KEY_F23           = 0x138;
		public static final int KEY_F24           = 0x139;
		public static final int KEY_F25           = 0x13A;
		public static final int KEY_KP_0          = 0x140;
		public static final int KEY_KP_1          = 0x141;
		public static final int KEY_KP_2          = 0x142;
		public static final int KEY_KP_3          = 0x143;
		public static final int KEY_KP_4          = 0x144;
		public static final int KEY_KP_5          = 0x145;
		public static final int KEY_KP_6          = 0x146;
		public static final int KEY_KP_7          = 0x147;
		public static final int KEY_KP_8          = 0x148;
		public static final int KEY_KP_9          = 0x149;
		public static final int KEY_KP_DECIMAL    = 0x14A;
		public static final int KEY_KP_DIVIDE     = 0x14B;
		public static final int KEY_KP_MULTIPLY   = 0x14C;
		public static final int KEY_KP_SUBTRACT   = 0x14D;
		public static final int KEY_KP_ADD        = 0x14E;
		public static final int KEY_KP_ENTER      = 0x14F;
		public static final int KEY_KP_EQUAL      = 0x150;
		public static final int KEY_LEFT_SHIFT    = 0x154;
		public static final int KEY_LEFT_CONTROL  = 0x155;
		public static final int KEY_LEFT_ALT      = 0x156;
		public static final int KEY_LEFT_SUPER    = 0x157;
		public static final int KEY_RIGHT_SHIFT   = 0x158;
		public static final int KEY_RIGHT_CONTROL = 0x159;
		public static final int KEY_RIGHT_ALT     = 0x15A;
		public static final int KEY_RIGHT_SUPER   = 0x15B;
		public static final int KEY_MENU          = 0x15C;
	}
	
	public class MouseButton
	{
		public static final int BUTTON_1      = 0x0;
		public static final int BUTTON_2      = 0x1;
		public static final int BUTTON_3      = 0x2;
		public static final int BUTTON_4      = 0x3;
		public static final int BUTTON_5      = 0x4;
		public static final int BUTTON_6      = 0x5;
		public static final int BUTTON_7      = 0x6;
		public static final int BUTTON_8      = 0x7;
		public static final int BUTTON_LAST   = BUTTON_8;
		public static final int BUTTON_LEFT   = BUTTON_1;
		public static final int BUTTON_RIGHT  = BUTTON_2;
		public static final int BUTTON_MIDDLE = BUTTON_3;
	}
	
	private static boolean[] keys = new boolean[KeyCode.KEY_MENU];
	private static boolean[] lastKeys = new boolean[KeyCode.KEY_MENU];
	
	private static boolean[] mouseButtons = new boolean[MouseButton.BUTTON_8];
	private static boolean[] lastMouseButtons = new boolean[MouseButton.BUTTON_8];
	
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

	public static boolean getKey(int keyCode)
	{
		return keys[keyCode];
	}

	public static boolean getKeyDown(int keyCode)
	{
		return getKey(keyCode) && !lastKeys[keyCode];
	}

	public static boolean getKeyUp(int keyCode)
	{
		return !getKey(keyCode) && lastKeys[keyCode];
	}

	public static boolean getMouse(int mouseButton)
	{
		return mouseButtons[mouseButton];
	}

	public static boolean getMouseDown(int mouseButton)
	{
		return getMouse(mouseButton) && !lastMouseButtons[mouseButton];
	}

	public static boolean getMouseUp(int mouseButton)
	{
		return !getMouse(mouseButton) && lastMouseButtons[mouseButton];
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