package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.Images;
public class GoldCube extends Entity
{
	public GoldCube(int x, int y)
	{
		this.x = x;
		this.y = y;
		ya = 0;
	}
	public void tick()
	{
		if (ya < 16) ya++;
		y += ya;
		java.util.List<Entity> entities = level.getEntities((int) x, (int) y, 16, 16);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if (!(e instanceof GoldCube)) e.die();
		}
		int xx = (int) (x / 16);
		int yy = (int) (y / 16) + 1;
		if (level.isBlock(xx, yy, (byte) 1) || level.isBlock(xx, yy, (byte) 9) || level.isBlock(xx, yy, (byte) 10))
		{
			level.placeWall(xx, (int) (y / 16), (byte) 10);
			removed = true;
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.cubes[1][0], (int) x, (int) y, null);
	}
}