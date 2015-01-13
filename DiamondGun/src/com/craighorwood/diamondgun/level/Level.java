package com.craighorwood.diamondgun.level;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.craighorwood.diamondgun.*;
import com.craighorwood.diamondgun.entity.*;
import com.craighorwood.diamondgun.screen.*;
public class Level
{
	public static byte bossesKilled = 0;
	public static int gunLevel = 0;
	public static boolean endBossMusic = false;
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Entity>[] entityMap;
	public int xSpawn, ySpawn;
	public Player player;
	private byte[] walls;
	private String gotGunText = "", gotGunSubtext = "";
	private int gotGunCounter = 0;
	private Screen screen;
	@SuppressWarnings("unchecked")
	public Level(Screen screen, int xLevel, int yLevel, int xSpawn, int ySpawn)
	{
		this.screen = screen;
		int[] pixels = new int[20 * 15];
		this.xSpawn = xSpawn;
		this.ySpawn = ySpawn;
		Images.levels.getRGB(xLevel * 20, yLevel * 15, 20, 15, pixels, 0, 20);
		walls = new byte[20 * 15];
		entityMap = new ArrayList[20 * 15];
		for (int y = 0; y < 15; y++)
		{
			for (int x = 0; x < 20; x++)
			{
				entityMap[x + y * 20] = new ArrayList<Entity>();
				int color = pixels[x + y * 20] & 0xFFFFFF;
				byte wall = 0;
				if (color == 0xFFFFFF) wall = 1;
				else if (color == 0xFFFF00) wall = 2;
				else if (color == 0x7F7F7F) wall = 3;
				else if (color == 0xFF8000) wall = 4;
				else if (color == 0xFF0000) wall = 5;
				else if (color == 0x8000 && (bossesKilled != 3 || xLevel != 6 || yLevel != 8)) wall = 6;
				else if (color == 0xFF && (bossesKilled != 4 || xLevel != 7 || yLevel != 0)) wall = 7;
				else if (color == 0xFFAEC9) wall = 8;
				else if (color == 0x800000) wall = 9;
				else if ((color & 0xFF) == 0xFF && (color & 0xFF0000) == 0xFF0000)
				{
					byte w = (byte) (((color >> 8) & 0xFF) + 10);
					if (w == 14 && bossesKilled == 5) continue;
					wall = (byte) (((color >> 8) & 0xFF) + 10);
				}
				else if ((color & 0xFFFF) == 0xFF00 && (color & 0xFF0000) > 0)
				{
					int id = (color >> 16) & 0xFF;
					if (gunLevel < id) addEntity(new Gun(x << 4, y << 4, id));
				}
				else if (color == 0xC8BFE7 && bossesKilled < 5) wall = 15;
				else if (color == 0xFFFFFE) wall = 16;
				else if ((color & 0xFFFF) == 0xFFFF)
				{
					Enemy e = Enemy.getByType(x << 4, y << 4, color >> 16);
					if (e != null) addEntity(e);
				}
				walls[x + y * 20] = wall;
			}
		}
		player = new Player(this.xSpawn, this.ySpawn);
		player.gunLevel = player.equippedGun = gunLevel;
		addEntity(player);
	}
	public void addEntity(Entity e)
	{
		entities.add(e);
		e.init(this);
		e.xSlot = (int) ((e.x + e.w / 2.0) / 16);
		e.ySlot = (int) ((e.y + e.h / 2.0) / 16);
		if (e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < 20 && e.ySlot < 15)
		{
			entityMap[e.xSlot + e.ySlot * 20].add(e);
		}
	}
	public void tick(Input input)
	{
		if (!player.removed) player.tick(input);
		else
		{
			if (input.isButtonPressed(Input.GUNS))
			{
				((GameplayScreen)screen).respawn();
			}
		}
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			int xSlotOld = e.xSlot;
			int ySlotOld = e.ySlot;
			if (!e.removed) e.tick();
			e.xSlot = (int) ((e.x + e.w / 2.0) / 16);
			e.ySlot = (int) ((e.y + e.h / 2.0) / 16);
			if (e.removed)
			{
				if (xSlotOld >= 0 && ySlotOld >= 0 && xSlotOld < 20 && ySlotOld < 15)
				{
					entityMap[xSlotOld + ySlotOld * 20].remove(e);
				}
				entities.remove(i--);
			}
			else
			{
				if (e.xSlot != xSlotOld || e.ySlot != ySlotOld)
				{
					if (xSlotOld >= 0 && ySlotOld >= 0 && xSlotOld < 20 && ySlotOld < 15)
					{
						entityMap[xSlotOld + ySlotOld * 20].remove(e);
					}
					if (e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < 20 && e.ySlot < 15)
					{
						entityMap[e.xSlot + e.ySlot * 20].add(e);
					}
					else e.removed = true;
				}
			}
		}
		if (gotGunCounter > 0)
		{
			if (--gotGunCounter == 0) gotGunText = gotGunSubtext = "";
		}
	}
	private List<Entity> hits = new ArrayList<Entity>();
	public List<Entity> getEntities(double xc, double yc, double w, double h)
	{
		hits.clear();
		int r = 32;
		int x0 = (int) ((xc - r) / 16);
		int y0 = (int) ((yc - r) / 16);
		int x1 = (int) ((xc + w + r) / 16);
		int y1 = (int) ((yc + h + r) / 16);
		for (int x = x0; x <= x1; x++)
		{
			for (int y = y0; y <= y1; y++)
			{
				if (x >= 0 && y >= 0 && x < 20 && y < 15)
				{
					List<Entity> es = entityMap[x + y * 20];
					for (int i = 0; i < es.size(); i++)
					{
						Entity e = es.get(i);
						double xx0 = e.x;
						double yy0 = e.y;
						double xx1 = e.x + e.w;
						double yy1 = e.y + e.h;
						if (xx0 > xc + w || yy0 > yc + h || xx1 < xc || yy1 < yc) continue;
						hits.add(e);
					}
				}
			}
		}
		return hits;
	}
	public void render(Graphics g)
	{
		for (int y = 0; y < 15; y++)
		{
			for (int x = 0; x < 20; x++)
			{
				byte w = walls[x + y * 20];
				int ximg = 0;
				int yimg = 0;
				if (w < 10)
				{
					if (w == 2 || w == 5 || w == 8) ximg = 1;
					if (w == 3 || w == 6 || w == 9) ximg = 2;
					if (w == 4 || w == 5 || w == 6) yimg = 1;
					if (w == 7 || w == 8 || w == 9) yimg = 2;
					if (w > 0) g.drawImage(Images.walls[ximg][yimg], x << 4, y << 4, null);
				}
				else if (w < 15)
				{
					if (w == 10 || w == 13) ximg = 1;
					if (w == 11 || w == 14) ximg = 2;
					if (w == 12 || w == 13 || w == 14) yimg = 1;
					g.drawImage(Images.cubes[ximg][yimg], x << 4, y << 4, null);
				}
				else g.drawImage(Images.walls[0][0], x << 4, y << 4, null);
			}
		}
		for (int i = 0; i < entities.size(); i++)
		{
			entities.get(i).render(g);
		}
		if (!gotGunText.isEmpty()) screen.drawBigString(gotGunText, g, 160 - gotGunText.length() * 8, 80);
		if (!gotGunSubtext.isEmpty()) screen.drawString(gotGunSubtext, g, 160 - gotGunSubtext.length() * 4, 100);
	}
	public boolean isFree(Entity e, double xc, double yc, int w, int h, double xa, double ya)
	{
		if (e.destroyer) return isBulletFree(e, xc, yc, w, h);
		double pixel = 1 / 16;
		int xx0 = (int) (xc / 16);
		int yy0 = (int) (yc / 16);
		int xx1 = (int) ((xc + w - pixel) / 16);
		int yy1 = (int) ((yc + h - pixel) / 16);
		int x0 = xx0;
		int y0 = yy0;
		int x1 = xx1;
		int y1 = yy1;
		if (xa < 0)
		{
			x0 = xx1;
			x1 = xx0;
		}
		if (ya < 0)
		{
			y0 = yy1;
			y1 = yy0;
		}
		int xi = xa < 0 ? -1 : 1;
		int yi = ya < 0 ? -1 : 1;
		boolean ok = true;
		for (int x = x0; x != x1 + xi; x += xi)
		{
			for (int y = y0; y != y1 + yi; y += yi)
			{
				if (x >= 0 && y >= 0 && x < 20 && y < 15)
				{
					byte ww = walls[x + y * 20];
					if (ww > 0 && ww < 16 && ww != 2) ok = false;
					if (ww == 1)
					{
						if (e == player && ya > 0)
						{
							if (player.climbing) player.climbing = false;
						}
					}
					else if (ww == 2)
					{
						if (isBlock(x, y - 1, (byte) 2))
						{
							if (e == player) player.climbUp = true;
						}
						if (isBlock(x, y + 1, (byte) 2))
						{
							if (isBlock(x, y - 1, (byte) 0) && y1 < y + 1) ok = false;
							if (e == player) player.climbDown = true;
						}
						if (e == player)
						{
							if (player.climbing)
							{
								ok = true;
								player.x = x << 4;
							}
						}
					}
					else if (ww == 9 && ya != 0 && !e.removed) e.hitSpikes();
				}
			}
		}
		return ok;
	}
	public boolean isBulletFree(Entity e, double xc, double yc, int w, int h)
	{
		double pixel = 1 / 16;
		int x0 = (int) (xc / 16);
		int y0 = (int) (yc / 16);
		int x1 = (int) ((xc + w - pixel) / 16);
		int y1 = (int) ((yc + h - pixel) / 16);
		if (e instanceof Bullet)
		{
			if (((Bullet)e).gunLevel == 5)
			{
				x1 = x0;
				y1 = y0;
			}
		}
		boolean ok = true;
		for (int x = x0; x <= x1; x++)
		{
			for (int y = y0; y <= y1; y++)
			{
				if (x >= 0 && y >= 0 && x < 20 && y < 15)
				{
					byte ww = walls[x + y * 20];
					if (ww > 0 && e instanceof Bullet) ok = false;
					if (ww == 2) ok = true;
					if (e instanceof Bullet)
					{
						Bullet b = (Bullet) e;
						if (b.player)
						{
							if (ww > 2 && ww <= 2 + b.gunLevel) breakWall(x, y);
							else if (ww > 9 && ww == 8 + b.gunLevel) doCubeEffect(x, y, b);
						}
					}
					else if (e instanceof Explosion && ((ww > 2 && ww < 9) || ww == 15)) breakWall(x, y);
				}
			}
		}
		return ok;
	}
	public boolean isBlock(int x, int y, byte w)
	{
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		if (x > 19) x = 19;
		if (y > 14) y = 14;
		return walls[x + y * 20] == w;
	}
	private void doCubeEffect(int x, int y, Bullet bullet)
	{
		switch (bullet.gunLevel)
		{
		case 2:
			if (walls[x + (y + 1) * 20] == 0)
			{
				placeWall(x, y, (byte) 0);
				addEntity(new GoldCube(x << 4, y << 4));
			}
			break;
		case 3:
			int nudge = (int) (bullet.xa / 6);
			if (walls[(x + nudge) + y * 20] == 0)
			{
				placeWall(x, y, (byte) 0);
				placeWall(x + nudge, y, (byte) 11);
			}
			break;
		case 4:
			Sound.dink.play();
			bullet.bounced = true;
			if (bullet.ya == 0) bullet.ya = bullet.xa;
			if (isBlock(x + 1, y, (byte) 0) || isBlock(x - 1, y, (byte) 0)) bullet.xa = -bullet.xa;
			if (isBlock(x, y + 1, (byte) 0) || isBlock(x, y - 1, (byte) 0)) bullet.ya = -bullet.ya;
			break;
		case 5:
			if (!player.carryingBlock)
			{
				Sound.absorb.play();
				placeWall(x, y, (byte) 0);
				player.carryingBlock = true;
			}
			break;
		case 6:
			placeWall(x, y, (byte) 0);
			addEntity(new DiamondCube(x << 4, y << 4));
			break;
		default:
			break;
		}
	}
	public void getGun(int id)
	{
		Sound.getgun.play();
		if (id < 6) Music.startMusic(id, Music.pauseMusic());
		gunLevel = id;
		player.gunLevel = player.equippedGun = id;
		gotGunText = "GOT SILVER GUN!";
		switch (id)
		{
		case 1:
			gotGunSubtext = "PRESS X TO FIRE GUN";
			break;
		case 2:
			gotGunText = "GOT GOLD GUN!";
			gotGunSubtext = "PRESS CTRL TO CHANGE GUN";
			break;
		case 3:
			gotGunText = "GOT RUBY GUN!";
			break;
		case 4:
			gotGunText = "GOT EMERALD GUN!";
			break;
		case 5:
			gotGunText = "GOT SAPPHIRE GUN!";
			break;
		case 6:
			gotGunText = "GOT DIAMOND GUN!";
			break;
		}
		gotGunCounter = 120;
	}
	public void placeWall(int x, int y, byte wall)
	{
		walls[x + y * 20] = wall;
	}
	public void breakWall(int x, int y)
	{
		walls[x + y * 20] = 0;
		addEntity(new Explosion(x << 4, y << 4));
	}
	public void transition(int xby, int yby)
	{
		((GameplayScreen)screen).transition(xby, yby);
	}
	public boolean saveGame(byte xLevel, byte yLevel)
	{
		return InputOutput.saveGame(xLevel, yLevel, (byte) gunLevel, (short) xSpawn, (short) ySpawn, bossesKilled, Stats.time, Stats.deaths, endBossMusic ? (byte) 1 : (byte) 0);
	}
}