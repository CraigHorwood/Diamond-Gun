package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.level.Level;
public class EmeraldBoss extends Enemy
{
	private int spawnTime = 100;
	public int bouncerCount = 0;
	public EmeraldBoss(int x, int y)
	{
		this.x = x;
		this.y = y;
		w = h = 30;
		bouncerCount = 0;
	}
	public void tick()
	{
		if (--spawnTime == 0)
		{
			if (bouncerCount < 8)
			{
				Sound.jumpenemy.play();
				ya = -2;
				Bouncer spawned = new Bouncer(160, 96, this);
				level.addEntity(spawned);
				bouncerCount++;
			}
			spawnTime = 100;
		}
		move(0, ya);
		if (ya < 6) ya += 0.5;
		super.tick();
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_big[1][2], (int) x, (int) y, null);
	}
	public boolean damage(Entity damager)
	{
		if (damager instanceof Bullet)
		{
			Bullet b = (Bullet) damager;
			if (b.player)
			{
				die();
				return true;
			}
		}
		return false;
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
		Level.bossesKilled = 3;
		removed = true;
	}
}