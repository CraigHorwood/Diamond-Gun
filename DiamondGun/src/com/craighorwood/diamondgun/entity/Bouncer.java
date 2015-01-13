package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.Images;
public class Bouncer extends Enemy
{
	private final int bounceDuration = 24, bounceLength = 64;
	private int bounceTimer;
	private int bouncingDir = 1;
	private EmeraldBoss boss = null;
	public Bouncer(int x, int y)
	{
		this.x = x;
		this.y = y;
		bounceTimer = 60;
	}
	public Bouncer(int x, int y, EmeraldBoss boss)
	{
		this(x, y);
		this.boss = boss;
	}
	public void tick()
	{
		xa = 0;
		if (onGround)
		{
			if (bounceTimer-- == 0)
			{
				ya -= 6;
				bounceTimer = 60;
				bouncingDir = Math.random() < 0.5 ? -1 : 1;
				double xd = x + bouncingDir * bounceLength;
				if (bouncingDir == 1 && xd > 288) bouncingDir = -1;
				if (bouncingDir == -1 && xd < 16) bouncingDir = 1;
			}
		}
		else xa += bouncingDir * (bounceLength / bounceDuration);
		move(xa, ya);
		if (ya < 6) ya += 0.5;
		super.tick();
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_small[onGround ? 0 : 1][0], (int) x, (int) y, null);
	}
	public boolean damage(Entity damager)
	{
		die();
		if (boss != null)
		{
			if (boss.bouncerCount > 0) boss.bouncerCount--;
		}
		return true;
	}
}