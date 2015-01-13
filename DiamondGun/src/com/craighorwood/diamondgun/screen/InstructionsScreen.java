package com.craighorwood.diamondgun.screen;
import java.awt.Color;
import java.awt.Graphics;

import com.craighorwood.diamondgun.Input;
public class InstructionsScreen extends Screen
{
	private String[] lines = {
		"LEFT AND RIGHT                    MOVE",
		"",
		"UP AND DOWN                      CLIMB",
		"",
		"Z                                 JUMP",
		"",
		"X                             FIRE GUN",
		"",
		"CONTROL                     CHANGE GUN",
		"",
		"ESCAPE                           PAUSE",
	};
	private int time = 0;
	public void render(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 320, 240);
		drawBigString("CONTROLS", g, 94, 16);
		for (int i = 0; i < lines.length; i++)
		{
			drawString(lines[i], g, 8, 64 + i * 8);
		}
		if (time > 40) drawString("PRESS ENTER TO RETURN TO TITLE", g, 40, 208);
	}
	public void tick(Input input)
	{
		time++;
		if (time > 40 && input.isButtonPressed(Input.START))
		{
			setScreen(new TitleScreen());
		}
	}
}