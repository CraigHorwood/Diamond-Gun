package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.level.Level;
public class DiamondCube extends Entity
{
	private DiamondBoss boss;
	private int soundTime = 150;
	private int bulletTime = 240;
	private double xo, yo;
	public DiamondCube(int x, int y)
	{
		this.x = x;
		this.y = y;
		Music.stopMusic();
	}
	public void init(Level level)
	{
		super.init(level);
		for (int i = 0; i < level.entities.size(); i++)
		{
			Entity e = level.entities.get(i);
			if (e instanceof Bullet) e.removed = true;
			else if (e instanceof DiamondBoss) boss = (DiamondBoss) e;
		}
		if (boss != null)
		{
			boss.frozen = true;
			boss.attackTime = 0;
			xo = boss.x;
			yo = boss.y;
		}
	}
	public void tick()
	{
		if (soundTime > 0)
		{
			if (--soundTime == 100) Sound.charge.play();
		}
		else if (bulletTime > 0)
		{
			Sound.shootplayer.play();
			boss.x = xo + random.nextInt(4) - 2;
			boss.y = yo + random.nextInt(4) - 2;
			if (random.nextDouble() < 0.2) boss.attackTime = boss.attackTime == 1 ? 0 : 1;
			for (int i = 0; i < 4; i++)
			{
				level.addEntity(new Bullet(x + (i << 2), y + 16, random.nextDouble() - random.nextDouble(), random.nextInt(8) + 4, random.nextInt(5) + 1, false));
			}
			if (--bulletTime == 0)
			{
				boss.die();
				removed = true;
			}
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.cubes[2][1], (int) x, (int) y, null);
	}
}