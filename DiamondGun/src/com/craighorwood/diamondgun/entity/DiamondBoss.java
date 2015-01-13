package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.level.Level;
public class DiamondBoss extends Enemy
{
	public boolean frozen = false;
	public int attackTime = 0;
	private int fireTime = 15;
	public DiamondBoss(int x, int y)
	{
		this.x = x;
		this.y = y;
		w = h = 62;
	}
	public void tick()
	{
		if (frozen) return;
		if (--attackTime == -120) attackTime = 120;
		if (attackTime > 0)
		{
			if (attackTime % fireTime == 0)
			{
				double time = attackTime * 1 / 60.0 + 1.5;
				double xxa = Math.cos(time) * 2.0;
				double yya = Math.sin(time) * 2.0;
				Sound.shootenemy.play();
				Bullet bullet = new Bullet(x, y + 32, xxa, yya, 0, false);
				level.addEntity(bullet);
			}
		}
		if (attackTime == 0 && Level.gunLevel == 6) fireTime = fireTime == 15 ? 3 : 15;
		super.tick();
	}
	public void die()
	{
		for (int i = 0; i < 16; i++)
		{
			double r = 2 * i * Math.PI / 16;
			double sin = -Math.sin(r);
			double cos = Math.cos(r);
			Explosion ex = new Explosion(x + 16 + cos, y + 16 + sin, false);
			ex.xa = 3 * cos;
			ex.ya = 3 * sin;
			ex.gravity = 0.05;
			level.addEntity(ex);
		}
		Sound.bosskill1.play();
		Sound.bosskill2.play();
		Level.bossesKilled = 5;
		removed = true;
	}
	public void render(Graphics g)
	{
		g.drawImage(attackTime > 0 ? Images.enemies_big[0][6] : Images.enemies_big[0][4], (int) x, (int) y, null);
		g.drawImage(Images.enemies_big[1][4], (int) (x + 32), (int) y, null);
		g.drawImage(attackTime > 0 ? Images.enemies_big[1][6] : Images.enemies_big[0][5], (int) x, (int) (y + 32), null);
		g.drawImage(Images.enemies_big[1][5], (int) (x + 32), (int) (y + 32), null);
	}
}