package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.level.Level;
public abstract class Enemy extends Entity
{
	public int gunLevel = 1;
	public static Enemy getByType(int x, int y, int type)
	{
		Enemy e = null;
		switch (type)
		{
		case 0:
			e = new Bouncer(x, y - 2);
			break;
		case 1:
			if (Level.bossesKilled < 1) e = new GoldBoss(x - 18, y - 16);
			break;
		case 2:
			e = new Turret(x, y, 1);
			break;
		case 3:
			e = new Turret(x, y, -1);
			break;
		case 4:
			e = new Aimer(x, y);
			break;
		case 5:
			if (Level.bossesKilled < 2) e = new RubyBoss(x - 17, y - 17);
			break;
		case 6:
			e = new EmeraldBouncer(x, y);
			break;
		case 7:
			if (Level.bossesKilled < 3) e = new EmeraldBoss(x - 17, y - 17);
			break;
		case 8:
			e = new LadderChaser(x, y);
			break;
		case 9:
			if (Level.bossesKilled < 4) e = new SapphireBoss(x - 17, y - 17);
			break;
		case 10:
			e = new DiamondBouncer(x, y);
			break;
		case 11:
			if (Level.bossesKilled < 5) e = new DiamondBoss(x - 48, y - 48);
			break;
		default:
			break;
		}
		return e;
	}
	public void tick()
	{
		java.util.List<Entity> entities = level.getEntities((int) x, (int) y, w, h);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if (e instanceof Player) e.damage(this);
		}
	}
	public void render(Graphics g)
	{
	}
	public boolean damage(Entity damager)
	{
		return false;
	}
}