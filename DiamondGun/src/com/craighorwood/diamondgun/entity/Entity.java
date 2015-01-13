package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;
import java.util.Random;

import com.craighorwood.diamondgun.level.Level;
public class Entity
{
	protected static Random random = new Random();
	protected Level level;
	public double x, y;
	public double xa, ya;
	public int w = 14, h = 14;
	public int xSlot, ySlot;
	public boolean removed = false;
	public boolean destroyer = false;
	protected boolean onGround = false;
	public void init(Level level)
	{
		this.level = level;
	}
	public void tick()
	{
	}
	public void render(Graphics g)
	{
	}
	public void move(double xa, double ya)
	{
		onGround = false;
		if (level.isFree(this, x + xa, y, w, h, xa, 0))
		{
			x += xa;
		}
		else
		{
			hitWall(xa, 0);
			if (this instanceof Bullet) return;
			if (xa < 0)
			{
				double xx = x / 16;
				xa = -(xx - ((int) xx)) * 16;
			}
			else
			{
				double xx = (x + w) / 16;
				xa = 16 - (xx - ((int) xx)) * 16;
			}
			if (level.isFree(this, x + xa, y, w, h, xa, 0))
			{
				x += xa;
			}
			this.xa = 0;
		}
		if (level.isFree(this, x, y + ya, w, h, 0, ya))
		{
			y += ya;
		}
		else
		{
			if (ya > 0) onGround = true;
			hitWall(0, ya);
			if (this instanceof Bullet) return;
			if (ya < 0)
			{
				double yy = y / 16;
				ya = -(yy - ((int) yy)) * 16;
			}
			else
			{
				double yy = (y + h) / 16;
				ya = 16 - (yy - ((int) yy)) * 16;
			}
			if (level.isFree(this, x, y + ya, w, h, 0, ya))
			{
				y += ya;
			}
			this.ya = 0;
		}
	}
	protected void hitWall(double xa, double ya)
	{
	}
	public void hitSpikes()
	{
	}
	public void die()
	{
		if (removed) return;
		Explosion e = new Explosion(x, y);
		e.destroyer = false;
		level.addEntity(e);
		removed = true;
	}
	public boolean damage(Entity damager)
	{
		return false;
	}
}