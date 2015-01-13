package com.craighorwood.diamondgun.screen;
import java.awt.Color;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
public class PauseScreen extends Screen
{
	private GameplayScreen parent;
	private String saveMessage = "PRESS Z TO SAVE";
	public PauseScreen(GameplayScreen parent)
	{
		this.parent = parent;
	}
	public void render(Graphics g)
	{
		g.setColor(Color.DARK_GRAY);
		g.fillRect(78, 102, 164, 48);
		g.setColor(Color.GRAY);
		g.fillRect(80, 104, 160, 44);
		String msg = "PAUSED";
		drawBigString(msg, g, 112, 110);
		drawString(saveMessage, g, 160 - saveMessage.length() * 4, 132);
	}
	public void tick(Input input)
	{
		if (input.isButtonPressed(Input.PAUSE))
		{
			Music.resumeMusic();
			setScreen(parent);
		}
		else if (input.isButtonPressed(Input.JUMP) && saveMessage.equals("PRESS Z TO SAVE"))
		{
			if (parent.saveGame()) saveMessage = "SAVE SUCCESSFUL";
			else saveMessage = "SAVE FAILED";
		}
	}
}