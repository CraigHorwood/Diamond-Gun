package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
public class Turret extends Enemy
{
	private int dir;
	private int shootTimer = 110;
	public Turret(int x, int y, int dir)
	{
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public void tick()
	{
		if (--shootTimer == 0)
		{
			Sound.shootenemy.play();
			Bullet bullet = new Bullet(x + 8 * (dir + 1), y + 6, 0, dir, false);
			bullet.destroyer = false;
			level.addEntity(bullet);
			shootTimer = 110;
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_small[dir == 1 ? 2 : 3][0], (int) x, (int) y, null);
	}
}