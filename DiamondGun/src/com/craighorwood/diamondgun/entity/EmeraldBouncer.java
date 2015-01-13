package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.Images;
public class EmeraldBouncer extends Enemy
{
	public EmeraldBouncer(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public void tick()
	{
		if (onGround)
		{
			xa = 0;
			java.util.List<Entity> entities = level.getEntities((int) x - 64, (int) y, w + 128, h);
			for (int i = 0; i < entities.size(); i++)
			{
				Entity e = entities.get(i);
				if (e instanceof Bullet)
				{
					Bullet b = (Bullet) e;
					if ((b.x <= x && b.xa < 0) || (b.x > x && b.xa > 0)) break;
					bounce();
					break;
				}
			}
		}
		move(0, ya);
		if (ya < 6) ya += 0.5;
		super.tick();
	}
	private void bounce()
	{
		if (!onGround) return;
		ya = -6;
	}
	public boolean damage(Entity damager)
	{
		die();
		return true;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.enemies_small[onGround ? 2 : 3][2], (int) x, (int) y, null);
	}
}