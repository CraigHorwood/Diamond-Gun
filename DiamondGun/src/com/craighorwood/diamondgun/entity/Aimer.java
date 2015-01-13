package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
public class Aimer extends Enemy
{
	private int aimingDir = 2;
	private int shootTimer = 110;
	public Aimer(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public void tick()
	{
		Player player = level.player;
		double xd = (x + 8) - (player.x + 8);
		double yd = (y + 4) - (player.y + 12);
		double angle = Math.atan2(yd, xd) * 180 / Math.PI;
		if (angle < 10) angle = 10;
		else if (angle > 170) angle = 170;
		if (angle < 42) aimingDir = 0;
		else if (angle < 74) aimingDir = 1;
		else if (angle < 106) aimingDir = 2;
		else if (angle < 138) aimingDir = 3;
		else aimingDir = 4;
		if (--shootTimer == 0)
		{
			Sound.shootenemy.play();
			shootTimer = 110;
			angle *= Math.PI / 180;
			int xa = (int) (-Math.cos(angle) * 8);
			int ya = (int) (-Math.sin(angle) * 8);
			Bullet bullet = new Bullet(x + 8, y + 4, xa, ya, 0, false);
			level.addEntity(bullet);
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_small[aimingDir % 4][aimingDir / 4 + 1], (int) x, (int) y, null);
	}
}