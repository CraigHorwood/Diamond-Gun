package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.level.Level;
public class RubyBoss extends Enemy
{
	private int shootCounter = 100;
	private int shootPhase = 0;
	private boolean shouldJump = false;
	public RubyBoss(int x, int y)
	{
		this.x = x;
		this.y = y;
		w = h = 30;
	}
	public void tick()
	{
		if (--shootCounter == 0)
		{
			Bullet bullet = new Bullet(x, y + (shootPhase << 4), -6, 0, 0, false);
			level.addEntity(bullet);
			if (shootPhase++ < 2)
			{
				Sound.shootenemy.play();
				shootCounter = 40;
			}
			else shouldJump = true;
		}
		if (shouldJump)
		{
			Sound.jumpenemy.play();
			shootPhase = 0;
			shootCounter = 100;
			ya = -8;
			shouldJump = false;
		}
		if (ya < 16) ya += 0.5;
		move(0, ya);
		super.tick();
	}
	public boolean damage(Entity damager)
	{
		if (damager instanceof Bullet) damager.removed = true;
		return false;
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
		Level.bossesKilled = 2;
		removed = true;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_big[1][1], (int) x, (int) y, null);
	}
}