package com.craighorwood.diamondgun.screen;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
public class Screen
{
	private DiamondGun diamondGun;
	public final void init(DiamondGun diamondGun)
	{
		this.diamondGun = diamondGun;
	}
	protected void setScreen(Screen newScreen)
	{
		diamondGun.setScreen(newScreen);
	}
	public void render(Graphics g)
	{
	}
	public void tick(Input input)
	{
	}
	String[] chars = {
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ",
		"0123456789!-: "
	};
	public void drawBigString(String string, Graphics g, int x, int y)
	{
		string = string.toUpperCase();
		for (int i = 0; i < string.length(); i++)
		{
			char ch = string.charAt(i);
			for (int ys = 0; ys < chars.length; ys++)
			{
				int xs = chars[ys].indexOf(ch);
				if (xs >= 0)
				{
					g.drawImage(Images.font_big[xs][ys], x + (i << 4), y, null);
				}
			}
		}
	}
	public void drawString(String string, Graphics g, int x, int y)
	{
		string = string.toUpperCase();
		for (int i = 0; i < string.length(); i++)
		{
			char ch = string.charAt(i);
			for (int ys = 0; ys < chars.length; ys++)
			{
				int xs = chars[ys].indexOf(ch);
				if (xs >= 0)
				{
					g.drawImage(Images.font_small[xs][ys], x + (i << 3), y, null);
				}
			}
		}
	}
	public void removed()
	{
	}
}