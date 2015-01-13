package com.craighorwood.diamondgun.entity;
import java.awt.Color;
import java.awt.Graphics;
public class Bullet extends Entity
{
	private int[] colors = {
		0x7F7F7F, 0xFFD800, 0xFF0000, 0x8000, 0xFF, 0xFFFF
	};
	private Color color;
	public int gunLevel;
	public boolean player;
	public boolean bounced = false;
	public Bullet(double x, double y, double xa, double ya, int gunLevel, boolean player)
	{
		this.x = x;
		this.y = y;
		this.w = this.h = 2;
		this.xa = xa;
		this.ya = ya;
		this.gunLevel = gunLevel;
		this.player = player;
		if (player || gunLevel > 0) this.color = new Color(colors[gunLevel - 1]);
		else this.color = Color.WHITE;
		this.destroyer = true;
	}
	public Bullet(double x, double y, int gunLevel, int dir, boolean player)
	{
		this(x, y, dir * 6, 0, gunLevel, player);
	}
	public void tick()
	{
		move(xa, ya);
		java.util.List<Entity> entities = level.getEntities((int) x, (int) y, 2, 2);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if (!player && e instanceof Enemy) continue;
 			else if (player && e instanceof Player) continue;
			if (e.damage(this)) removed = true;
		}
	}
	public void hitWall(double xa, double ya)
	{
		if (!bounced) removed = true;
		else bounced = false;
	}
	public void render(Graphics g)
	{
		g.setColor(color);
		g.fillRect((int) x, (int) y, w, h);
	}
}