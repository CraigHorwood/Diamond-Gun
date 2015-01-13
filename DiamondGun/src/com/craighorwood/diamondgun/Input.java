package com.craighorwood.diamondgun;
import java.awt.event.KeyEvent;
public class Input
{
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	public static final int JUMP = 4;
	public static final int FIRE = 5;
	public static final int GUNS = 6;
	public static final int START = 7;
	public static final int PAUSE = 8;
	private boolean[] buttons = new boolean[9];
	private boolean[] oldButtons = new boolean[9];
	public void set(int key, boolean down)
	{
		int button = -1;
		if (key == KeyEvent.VK_LEFT) button = LEFT;
		if (key == KeyEvent.VK_RIGHT) button = RIGHT;
		if (key == KeyEvent.VK_UP) button = UP;
		if (key == KeyEvent.VK_DOWN) button = DOWN;
		if (key == KeyEvent.VK_Z || key == KeyEvent.VK_A) button = JUMP;
		if (key == KeyEvent.VK_X || key == KeyEvent.VK_S) button = FIRE;
		if (key == KeyEvent.VK_CONTROL) button = GUNS;
		if (key == KeyEvent.VK_ENTER) button = START;
		if (key == KeyEvent.VK_ESCAPE) button = PAUSE;
		if (button >= 0)
		{
			buttons[button] = down;
		}
	}
	public boolean isButtonDown(int button)
	{
		return buttons[button];
	}
	public boolean isButtonPressed(int button)
	{
		return buttons[button] && !oldButtons[button];
	}
	public void tick()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			oldButtons[i] = buttons[i];
		}
	}
	public void releaseAll()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i] = false;
		}
	}
}