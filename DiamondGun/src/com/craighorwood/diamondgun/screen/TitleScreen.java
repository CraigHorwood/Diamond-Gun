package com.craighorwood.diamondgun.screen;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
public class TitleScreen extends Screen
{
	private boolean firstTick = true;
	private String[] options;
	private int selectedOption = 0;
	private String highScore = "";
	public TitleScreen()
	{
		options = new String[] { "START", "CONTROLS" };
		if (InputOutput.saveExists()) options = new String[] { "START", "CONTINUE", "CONTROLS" };
		if (InputOutput.highScoreExists()) highScore = "HIGH SCORE - " + InputOutput.loadHighScore();
	}
	public void render(Graphics g)
	{
		if (firstTick)
		{
			Sound.start.play();
			firstTick = false;
		}
		g.drawImage(Images.title, 0, 0, null);
		String msg = "BY CRAIG HORWOOD";
		drawString(msg, g, 160 - msg.length() * 4, 104);
		for (int i = 0; i < options.length; i++)
		{
			msg = options[i];
			if (i == selectedOption) msg = "- " + msg + " -";
			drawString(msg, g, 160 - msg.length() * 4, 152 + i * 12);
		}
		if (Stats.highScoreFailed)
		{
			msg = "YOUR HIGH SCORE FAILED TO SAVE";
			drawString(msg, g, 160 - msg.length() * 4, 208);
		}
		else if (highScore != "")
		{
			drawString(highScore, g, 160 - highScore.length() * 4, 208);
		}
	}
	public void tick(Input input)
	{
		if (input.isButtonPressed(Input.UP) && selectedOption > 0) selectedOption--;
		else if (input.isButtonPressed(Input.DOWN) && selectedOption < options.length - 1) selectedOption++;
		if (input.isButtonPressed(Input.START))
		{
			if (selectedOption == 0) setScreen(new GameplayScreen(-1, -1, -1, -1, -1, -1, -1, -1, false));
			else if (selectedOption == options.length - 1) setScreen(new InstructionsScreen());
			else
			{
				int[] saved = InputOutput.loadGame();
				setScreen(new GameplayScreen(saved[0], saved[1], saved[2], saved[3], saved[4], saved[5], saved[6], saved[7], saved[8] == 1));
			}
		}
	}
}