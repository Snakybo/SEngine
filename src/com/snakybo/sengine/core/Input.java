package com.snakybo.sengine.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/** Input clas
 * 
 * <p>
 * All input in the engine is handled here
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Input {
	private static boolean[] lastKeys = new boolean[KeyCode.NUM_KEYCODES];
	private static boolean[] lastMouse = new boolean[MouseButton.NUM_MOUSEBUTTONS];
	
	/** Update the input manager */
	static void update() {
		for(int i = 0; i < KeyCode.NUM_KEYCODES; i++)
			lastKeys[i] = getKey(i);
		
		for(int i = 0; i < MouseButton.NUM_MOUSEBUTTONS; i++)
			lastMouse[i] = getMouse(i);
	}
	
	// Keyboard //
	
	/** @return The state of the specified key
	 * @param keyCode The key code */
	public static boolean getKey(int keyCode) {
		return Keyboard.isKeyDown(keyCode);
	}
	
	/** @return True when the specified key is down
	 * @param keyCode The key code */
	public static boolean getKeyDown(int keyCode) {
		return getKey(keyCode) && !lastKeys[keyCode];
	}
	
	/** @return True when the specified key is up
	 * @param keyCode The key code */
	public static boolean getKeyUp(int keyCode) {
		return !getKey(keyCode) && lastKeys[keyCode];
	}
	
	// Mouse //
	
	/** Set the position of the mouse
	 * @param position The new position */
	public static void setMousePosition(Vector2f position) {
		Mouse.setCursorPosition((int)position.getX(), (int)position.getY());
	}
	
	/** Enable or disable the mouse cursor
	 * @param enabled The new state of the cursor */
	public static void setCursor(boolean enabled) {
		Mouse.setGrabbed(!enabled);
	}
	
	/** @return The position of the mouse */
	public static Vector2f getMousePosition() {
		return new Vector2f(Mouse.getX(), Mouse.getY());
	}
	
	/** @return The state of the specified mouse button
	 * @param mouseButton The mouse button */
	public static boolean getMouse(int mouseButton) {
		return Mouse.isButtonDown(mouseButton);
	}
	
	/** @return True when the specified mouse button is down
	 * @param mouseButton The mouse button */
	public static boolean getMouseDown(int mouseButton) {
		return getMouse(mouseButton) && !lastMouse[mouseButton];
	}
	
	/** @return True when the specified mouse button is up
	 * @param mouseButton The mouse button */
	public static boolean getMouseUp(int mouseButton) {
		return !getMouse(mouseButton) && lastMouse[mouseButton];
	}
	
	/** Keycode class, contains every key on the keyboard */
	public class KeyCode {
		public static final int NUM_KEYCODES = 256;
		
		public static final int NONE = 0x00;
		public static final int ESCAPE = 0x01;
		public static final int NUM_1 = 0x02;
		public static final int NUM_2 = 0x03;
		public static final int NUM_3 = 0x04;
		public static final int NUM_4 = 0x05;
		public static final int NUM_5 = 0x06;
		public static final int NUM_6 = 0x07;
		public static final int NUM_7 = 0x08;
		public static final int NUM_8 = 0x09;
		public static final int NUM_9 = 0x0A;
		public static final int NUM_0 = 0x0B;
		public static final int MINUS = 0x0C;
		public static final int EQUALS = 0x0D;
		public static final int BACK = 0x0E;
		public static final int TAB = 0x0F;
		public static final int Q = 0x10;
		public static final int W = 0x11;
		public static final int E = 0x12;
		public static final int R = 0x13;
		public static final int T = 0x14;
		public static final int Y = 0x15;
		public static final int U = 0x16;
		public static final int I = 0x17;
		public static final int O = 0x18;
		public static final int P = 0x19;
		public static final int LBRACKET = 0x1A;
		public static final int RBRACKET = 0x1B;
		public static final int RETURN = 0x1C;
		public static final int LCONTROL = 0x1D;
		public static final int A = 0x1E;
		public static final int S = 0x1F;
		public static final int D = 0x20;
		public static final int F = 0x21;
		public static final int G = 0x22;
		public static final int H = 0x23;
		public static final int J = 0x24;
		public static final int K = 0x25;
		public static final int L = 0x26;
		public static final int SEMICOLON = 0x27;
		public static final int APOSTROPHE = 0x28;
		public static final int GRAVE = 0x29;
		public static final int LSHIFT = 0x2A;
		public static final int BACKSLASH = 0x2B;
		public static final int Z = 0x2C;
		public static final int X = 0x2D;
		public static final int C = 0x2E;
		public static final int V = 0x2F;
		public static final int B = 0x30;
		public static final int N = 0x31;
		public static final int M = 0x32;
		public static final int COMMA = 0x33;
		public static final int PERIOD = 0x34;
		public static final int SLASH = 0x35;
		public static final int RSHIFT = 0x36;
		public static final int MULTIPLY = 0x37;
		public static final int LMENU = 0x38;
		public static final int LALT = LMENU;
		public static final int SPACE = 0x39;
		public static final int CAPITAL = 0x3A;
		public static final int F1 = 0x3B;
		public static final int F2 = 0x3C;
		public static final int F3 = 0x3D;
		public static final int F4 = 0x3E;
		public static final int F5 = 0x3F;
		public static final int F6 = 0x40;
		public static final int F7 = 0x41;
		public static final int F8 = 0x42;
		public static final int F9 = 0x43;
		public static final int F10 = 0x44;
		public static final int NUMLOCK = 0x45;
		public static final int SCROLL = 0x46;
		public static final int NUMPAD7 = 0x47;
		public static final int NUMPAD8 = 0x48;
		public static final int NUMPAD9 = 0x49;
		public static final int SUBTRACT = 0x4A;
		public static final int NUMPAD4 = 0x4B;
		public static final int NUMPAD5 = 0x4C;
		public static final int NUMPAD6 = 0x4D;
		public static final int ADD = 0x4E;
		public static final int NUMPAD1 = 0x4F;
		public static final int NUMPAD2 = 0x50;
		public static final int NUMPAD3 = 0x51;
		public static final int NUMPAD0 = 0x52;
		public static final int DECIMAL = 0x53;
		public static final int F11 = 0x57;
		public static final int F12 = 0x58;
		public static final int F13 = 0x64;
		public static final int F14 = 0x65;
		public static final int F15 = 0x66;
		public static final int KANA = 0x70;
		public static final int CONVERT = 0x79;
		public static final int NOCONVERT = 0x7B;
		public static final int YEN = 0x7D;
		public static final int NUMPADEQUALS = 0x8D;
		public static final int CIRCUMFLEX = 0x90;
		public static final int AT = 0x91;
		public static final int COLON = 0x92;
		public static final int UNDERLINE = 0x93;
		public static final int KANJI = 0x94;
		public static final int STOP = 0x95;
		public static final int AX = 0x96;
		public static final int UNLABELED = 0x97;
		public static final int NUMPADENTER = 0x9C;
		public static final int RCONTROL = 0x9D;
		public static final int NUMPADCOMMA = 0xB3;
		public static final int DIVIDE = 0xB5;
		public static final int SYSRQ = 0xB7;
		public static final int RMENU = 0xB8;
		public static final int RALT = RMENU;
		public static final int PAUSE = 0xC5;
		public static final int HOME = 0xC7;
		public static final int UP = 0xC8;
		public static final int PRIOR = 0xC9;
		public static final int LEFT = 0xCB;
		public static final int RIGHT = 0xCD;
		public static final int END = 0xCF;
		public static final int DOWN = 0xD0;
		public static final int NEXT = 0xD1;
		public static final int INSERT = 0xD2;
		public static final int DELETE = 0xD3;
		public static final int LMETA = 0xDB;
		public static final int LWIN = LMETA;
		public static final int RMETA = 0xDC;
		public static final int RWIN = RMETA;
		public static final int APPS = 0xDD;
		public static final int POWER = 0xDE;
		public static final int SLEEP = 0xDF;
	}
	
	/** Mouse button class, contains every mouse button */
	public class MouseButton {
		public static final int NUM_MOUSEBUTTONS = 5;
		
		public static final int LEFT = 0x00;
		public static final int RIGHT = 0x01;
		public static final int MIDDLE = 0x02;
		public static final int BACK = 0x03;
		public static final int FORWARD = 0x04;
	}
}
