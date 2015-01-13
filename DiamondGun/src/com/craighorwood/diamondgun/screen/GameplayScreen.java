package com.craighorwood.diamondgun.screen;
import java.awt.Color;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.level.Level;
public class GameplayScreen extends Screen
{
	private int xLevel = 0;
	private int yLevel = 7;
	public Level level;
	public GameplayScreen(int xLevel, int yLevel, int gunLevel, int xSpawn, int ySpawn, int bossesKilled, int time, int deaths, boolean endBossMusic)
	{
		if (xLevel >= 0 && yLevel >= 0 && gunLevel >= 0 && bossesKilled >= 0 && time >= 0 && deaths >= 0)
		{
			this.xLevel = xLevel;
			this.yLevel = yLevel;
			Level.gunLevel = gunLevel;
			Level.bossesKilled = (byte) bossesKilled;
			Stats.time = time;
			Stats.deaths = deaths;
			Level.endBossMusic = endBossMusic;
			if (gunLevel > 0 && bossesKilled < 5)
			{
				int songIndex = gunLevel;
				if (endBossMusic && bossesKilled < 5) songIndex = 6;
				if (gunLevel != 5 || yLevel <= 7 || endBossMusic) Music.startMusic(songIndex, 0);
			}
		}
		level = new Level(this, xLevel >= 0 ? xLevel : this.xLevel, yLevel >= 0 ? yLevel : this.yLevel, xSpawn >= 0 ? xSpawn : 32, ySpawn >= 0 ? ySpawn : 144);
	}
	public void render(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 320, 240);
		level.render(g);
		if (level.player.removed)
		{
			g.setColor(Color.DARK_GRAY);
			g.fillRect(78, 102, 164, 48);
			g.setColor(Color.GRAY);
			g.fillRect(80, 104, 160, 44);
			String msg = "YOU DEAD";
			drawBigString(msg, g, 96, 106);
			msg = "PRESS CTRL TO"; // Fix word wrap.
			drawString(msg, g, 160 - msg.length() * 4, 126);
			msg = "CONTINUE";
			drawString(msg, g, 160 - msg.length() * 4, 138);
		}
	}
	public void tick(Input input)
	{
		Stats.time++;
		if (input.isButtonPressed(Input.PAUSE) && !level.player.removed)
		{
			Music.pauseMusic();
			setScreen(new PauseScreen(this));
		}
		level.tick(input);
	}
	public void transition(int xby, int yby)
	{
		xLevel += xby;
		yLevel += yby;
		if (xLevel == 10)
		{
			setScreen(new WinScreen());
			return;
		}
		else if (xLevel == 8 && yLevel == 8 && Level.gunLevel < 6 && !Level.endBossMusic) Music.stopMusic();
		else if (xLevel == 9 && yLevel == 7 && !Level.endBossMusic)
		{
			Music.startMusic(6, 0);
			Level.endBossMusic = true;
		}
		level.player.x -= xby * 304;
		level.player.y -= yby * 224;
		if (xby < 0) level.player.x -= 8;
		Level newLevel = new Level(this, xLevel, yLevel, (int) level.player.x, (int) level.player.y + yby * 16);
		newLevel.player.removed = true;
		newLevel.player = level.player;
		newLevel.addEntity(newLevel.player);
		newLevel.player.carryingBlock = false;
		this.level = newLevel;
	}
	public void respawn()
	{
		Level newLevel = new Level(this, xLevel, yLevel, level.xSpawn, level.ySpawn);
		newLevel.player.gunLevel = newLevel.player.equippedGun = level.player.gunLevel;
		newLevel.player.dir = level.player.dir;
		if (xLevel == 0 && yLevel == 3) newLevel.player.y = 64;
		else if (xLevel == 1 && yLevel == 3) newLevel.player.y = 160;
		else if (xLevel == 3 && yLevel == 1) newLevel.player.y = 160;
		else if (xLevel == 7)
		{
			if (yLevel == 7)
			{
				newLevel.player.y = 120;
			}
			else if (yLevel == 6)
			{
				newLevel.player.y = 160;
			}
			else if (yLevel == 5)
			{
				newLevel.player.x = 80;
				newLevel.player.y = 160;
			}
			else if (yLevel == 3)
			{
				newLevel.player.x = 48;
				newLevel.player.y = 150;
			}
			else if (yLevel == 2)
			{
				newLevel.player.x = 88;
				newLevel.player.y = 112;
			}
		}
		this.level = newLevel;
	}
	public boolean saveGame()
	{
		return level.saveGame((byte) xLevel, (byte) yLevel);
	}
}