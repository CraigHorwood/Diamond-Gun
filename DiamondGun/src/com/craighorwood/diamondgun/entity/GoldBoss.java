package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.level.Level;
public class GoldBoss extends Enemy
{
	private int facing = 0;
	private int jumpTimer = 140;
	public GoldBoss(int x, int y)
	{
		this.x = x;
		this.y = y;
		w = h = 30;
	}
	public void tick()
	{
		if (level.player.x >= x + w / 2) facing = 0;
		else facing = 1;
		if (jumpTimer-- == 0)
		{
			Sound.jumpenemy.play();
			ya = -7;
			jumpTimer = 140;
		}
		move(0, ya);
		if (ya < 16) ya += 0.2;
		super.tick();
	}
	public void die()
	{
		for (int xx = -1; xx < 2; xx += 2)
		{
			for (int yy = -1; yy < 2; yy += 2)
			{
				level.addEntity(new Explosion(x + (xx << 4), y + (yy << 4)));
			}
		}
		Level.bossesKilled = 1;
		removed = true;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_big[facing][0], (int) x, (int) y, null);
	}
}