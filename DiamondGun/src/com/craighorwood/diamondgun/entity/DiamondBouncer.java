package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.Images;
public class DiamondBouncer extends Enemy
{
	private double bounceDuration = 40, bounceLength = 64;
	private int bounceTimer;
	private double bouncingDir = 1;
	public DiamondBouncer(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public void tick()
	{
		xa = 0;
		if (onGround)
		{
			if (bounceTimer-- == 0)
			{
				double xd = level.player.x - x;
				double yd = level.player.y - y;
				if (xd * xd < 112 * 112 && yd * yd < 112 * 112)
				{
					if (xd < 0) bouncingDir = -1;
					else bouncingDir = 1;
					bounceLength = ((int) (xd * bouncingDir / 16)) << 4;
					ya -= 10;
					double xxd = x + bouncingDir * bounceLength;
					if (bouncingDir == 1 && xxd > 288) bouncingDir = -1;
					if (bouncingDir == -1 && xxd < 16) bouncingDir = 1;
				}
				bounceTimer = 60;
			}
		}
		else xa += bouncingDir * (bounceLength / bounceDuration);
		move(xa, ya);
		if (ya < 6) ya += 0.5;
		super.tick();
	}
	public boolean damage(Entity damager)
	{
		if (damager instanceof Bullet) damager.removed = true;
		return false;
	}
	public void hitSpikes()
	{
		die();
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_small[onGround ? 1 : 2][3], (int) x, (int) y, null);
	}
}