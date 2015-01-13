package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.level.Level;
public class SapphireBoss extends Enemy
{
	private boolean dir = false;
	private int moveCounter = 0;
	private int shotCounter = 128;
	public SapphireBoss(int x, int y)
	{
		this.x = x;
		this.y = y;
		w = h = 30;
	}
	public void tick()
	{
		if (moveCounter > 0)
		{
			if (dir) x--;
			else x++;
			if (--moveCounter == 0 && shotCounter == 0) shotCounter = 128;
		}
		else if (shotCounter > 0)
		{
			shotCounter--;
			if (shotCounter % 16 == 0)
			{
				Sound.shootenemy.play();
				level.addEntity(new Bullet(x, y + 8, -4, dir ? 2 : 0, 0, false));
			}
			if (shotCounter == 0 && level.isBlock(14, 4, (byte) 7))
			{
				moveCounter = 64;
				dir = !dir;
			}
		}
		if (ya < 16) ya += 0.5;
		move(xa, ya);
		super.tick();
	}
	public void hitSpikes()
	{
		for (int xx = -1; xx < 2; xx += 2)
		{
			for (int yy = -1; yy < 2; yy += 2)
			{
				level.addEntity(new Explosion(x + (xx << 4), y + (yy << 4)));
			}
		}
		Level.bossesKilled = 4;
		removed = true;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_big[dir || shotCounter > 0 ? 1 : 0][3], (int) x, (int) y, null);
	}
}