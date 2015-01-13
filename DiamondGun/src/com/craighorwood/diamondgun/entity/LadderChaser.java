package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
public class LadderChaser extends Enemy
{
	private int wait = 0;
	public LadderChaser(int x, int y)
	{
		this.x = x;
		this.y = y;
		ya = Math.random() < 0.5 ? 1 : -1;
	}
	public void tick()
	{
		y += ya;
		if (y < 0 || y > 224) ya = -ya;
		if (wait == 0)
		{
			double yd = level.player.y - y;
			if (yd * yd <= 16 * 16)
			{
				boolean xd = level.player.x - x > 0;
				Sound.shootenemy.play();
				level.addEntity(new Bullet(x + (xd ? 0 : 16), level.player.y + 12 , 0, xd ? 1 : -1, false));
				wait = 64;
			}
		}
		else wait--;
		super.tick();
	}
	public boolean damage(Entity damager)
	{
		die();
		return true;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_small[0][3], (int) x, (int) y, null);
	}
}