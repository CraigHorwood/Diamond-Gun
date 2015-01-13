package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.Images;
public class Gun extends Entity
{
	private int id;
	public Gun(int x, int y, int id)
	{
		this.x = x;
		this.y = y;
		this.w = 16;
		this.h = 12;
		this.id = id - 1;
	}
	public void tick()
	{
		java.util.List<Entity> entities = level.getEntities((int) x, (int) y, 16, 12);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if (e instanceof Player)
			{
				level.getGun(id + 1);
				level.player.damaged = false;
				removed = true;
			}
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.guns[id % 3][id / 3], (int) x, (int) y, null);
	}
}