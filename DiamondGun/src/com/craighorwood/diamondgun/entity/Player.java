package com.craighorwood.diamondgun.entity;
import java.awt.Graphics;

import com.craighorwood.diamondgun.*;
public class Player extends Entity
{
	public boolean isWinScreen = false;
	public boolean climbing = false;
	public boolean climbUp = false, climbDown = false;
	public int gunLevel = 0, equippedGun = 0;
	public boolean damaged = false;
	public boolean carryingBlock = false;
	public int frame, dir;
	private int invincibleTime = 0;
	private final int gravity = 1;
	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
		h = 22;
		frame = 1;
		dir = 1;
	}
	private int walkTimer = 0;
	public void tick()
	{
	}
	public void tick(Input input)
	{
		if (isWinScreen)
		{
			boolean walking = false;
			if (xa > 0 && onGround)
			{
				walking = true;
				if (++walkTimer > 7) walkTimer = 0;
			}
			if (walking && walkTimer < 2) frame = 2;
			else frame = 1;
			move(xa, ya);
			if (ya < 16) ya += gravity;
			return;
		}
		if (invincibleTime > 0) invincibleTime--;
		if (!climbing)
		{
			boolean walking = false;
			if (input.isButtonDown(Input.LEFT))
			{
				xa -= 2;
				dir = -1;
				walking = true;
				walkTimer++;
			}
			if (input.isButtonDown(Input.RIGHT))
			{
				xa += 2;
				dir = 1;
				walking = true;
				walkTimer++;
			}
			if (walkTimer > 7) walkTimer = 0;
			if (walking && walkTimer < 2) frame = 2;
			else frame = 1;
			if (input.isButtonPressed(Input.JUMP) && onGround)
			{
				Sound.jump.play();
				ya -= 12;
			}
			if (input.isButtonPressed(Input.FIRE) && gunLevel > 0)
			{
				if (!carryingBlock)
				{
					Sound.shootplayer.play();
					Bullet bullet = new Bullet(x + 4, y + 12, equippedGun, dir, true);
					level.addEntity(bullet);
				}
				else
				{
					if (level.isBlock(xSlot + dir, ySlot, (byte) 0))
					{
						Sound.place.play();
						int blocksFallen = 0;
						while (level.isBlock(xSlot + dir, ySlot + blocksFallen + 1, (byte) 0))
						{
							blocksFallen++;
						}
						level.placeWall(xSlot + dir, ySlot + blocksFallen, (byte) 13);
						carryingBlock = false;
					}
				}
			}
		}
		else
		{
			if (input.isButtonPressed(Input.JUMP) || !climbUp || !climbDown) climbing = false;
			if (!climbUp) y -= 16;
		}
		if (climbUp && input.isButtonDown(Input.UP))
		{
			climbing = true;
			ya -= 2;
		}
		else if (climbDown && input.isButtonDown(Input.DOWN))
		{
			if (!climbing && level.isBlock(xSlot, ySlot + 1, (byte) 2)) y += 16;
			climbing = true;
			ya += 2;
		}
		climbUp = false;
		climbDown = false;
		move(xa, ya);
		if (!climbing && ya < 16) ya += gravity;
		xa = 0;
		if (climbing) ya = 0;
		if (input.isButtonPressed(Input.GUNS) && gunLevel > 1)
		{
			Sound.switchgun.play();
			if (++equippedGun > gunLevel) equippedGun = 1;
		}
		if (y < -12) level.transition(0, -1);
		if (y > 216) level.transition(0, 1);
		if (x < 8) level.transition(-1, 0);
		if (x > 312) level.transition(1, 0);
	}
	public void render(Graphics g)
	{
		if (invincibleTime % 4 == 0) g.drawImage(Images.player[climbing ? 4 : -dir + frame][equippedGun], (int) x, (int) y, null);
	}
	public boolean damage(Entity damager)
	{
		if (invincibleTime > 0) return false;
		if (damager instanceof Enemy)
		{
			if (x < damager.x + damager.w / 2) xa = -16;
			else xa = 16;
		}
		else
		{
			xa += damager.xa / 2;
			ya += damager.ya / 2;
		}
		Sound.hurt.play();
		if (!damaged)
		{
			invincibleTime = 120;
			damaged = true;
		}
		else die();
		return true;
	}
	public void hitSpikes()
	{
		die();
	}
	public void die()
	{
		if (!removed) Stats.deaths++;
		super.die();
	}
}