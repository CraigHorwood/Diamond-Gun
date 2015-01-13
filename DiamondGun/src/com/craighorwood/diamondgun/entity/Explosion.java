package com.craighorwood.diamondgun.entity;
import java.awt.Color;
import java.awt.Graphics;

import com.craighorwood.diamondgun.Sound;
public class Explosion extends Entity
{
	private double xo, yo;
	public double gravity = 0;
	private boolean sound;
	public Explosion(double x, double y)
	{
		this(x, y, true);
	}
	public Explosion(double x, double y, boolean sound)
	{
		this.x = xo = x + 8;
		this.y = yo = y + 8;
		this.sound = sound;
		w = h = 0;
		destroyer = true;
		if (sound) Sound.explosion.play();
	}
	public void tick()
	{
		w += 4;
		h += 4;
		xo += xa;
		yo += ya;
		x = xo - w / 2;
		y = yo - h / 2;
		if (w > 24)
		{
			if (sound) removed = true;
			else w = h = 0;
		}
		move(xa, ya);
		if (gravity > 0) ya += gravity;
	}
	public void render(Graphics g)
	{
		g.setColor(Color.WHITE);
		if (w < 20) g.fillOval((int) x, (int) y, w, h);
		else g.drawOval((int) x, (int) y, w, h);
	}
}