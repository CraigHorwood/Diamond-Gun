package com.craighorwood.diamondgun.screen;
import java.awt.*;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.level.Level;
public class WinScreen extends Screen
{
	private Level level;
	private String[] tally = { "TIME: ", "DEATHS: ", "SCORE: " };
	private int tallyStage = 0;
	private int tallyCounter = 90;
	private int tallyRender = 0;
	private double creditsOffset = 380;
	public WinScreen()
	{
		tally[0] += Stats.getTimeString();
		tally[1] += Stats.deaths;
		int totalScore = Stats.getTotalScore();
		tally[2] += totalScore;
		if (totalScore > InputOutput.highScore)
		{
			Stats.highScoreFailed = !InputOutput.saveHighScore(totalScore);
		}
		level = new Level(this, 3, 4, -16, 120);
		level.player.isWinScreen = true;
		level.player.xa = 1;
	}
	public void tick(Input input)
	{
		level.tick(input);
		if (tallyStage == 0)
		{
			if (--tallyCounter == 0)
			{
				level.player.xa = 0;
				tallyCounter = 300;
				tallyStage++;
			}
		}
		else if (tallyStage == 1)
		{
			if (--tallyCounter % 100 == 0)
			{
				Sound.place.play();
				tallyRender++;
				if (tallyCounter == 0)
				{
					tallyCounter = 80;
					tallyStage++;
				}
			}
		}
		else if (tallyStage == 2)
		{
			if (--tallyCounter == 0)
			{
				level.player.xa = 1;
				tallyCounter = 150;
				tallyStage++;
			}
		}
		else if (tallyStage == 3)
		{
			if (--tallyCounter == 0)
			{
				Music.startMusic(0, 0);
				Music.shouldLoop = false;
				tallyStage++;
			}
		}
		else creditsOffset -= 0.3;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sky, 0, 0, null);
		if (creditsOffset >= 260)
		{
			for (int i = 0; i < tallyRender; i++)
			{
				drawString(tally[i], g, 160, (int) creditsOffset - 340 + (i << 4));
			}
		}
		else if (creditsOffset < 240)
		{
			g.drawImage(Images.credits, 112, (int) creditsOffset, null);
		}
		level.render(g);
		if (creditsOffset <= -740) setScreen(new TitleScreen());
	}
}