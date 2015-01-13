package com.craighorwood.diamondgun;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class Images
{
	public static BufferedImage[][] cubes = chop(load("/img/cubes.png"), 16, 16);
	public static BufferedImage[][] enemies_big = chop(load("/img/enemies_big.png"), 32, 32);
	public static BufferedImage[][] enemies_small = chop(load("/img/enemies_small.png"), 16, 16);
	public static BufferedImage[][] font_big = chop(load("/img/font_big.png"), 16, 16);
	public static BufferedImage[][] font_small = chop(load("/img/font_small.png"), 8, 8);
	public static BufferedImage[][] guns = chop(load("/img/guns.png"), 16, 12);
	public static BufferedImage[][] player = chop(load("/img/player.png"), 16, 24);
	public static BufferedImage[][] walls = chop(load("/img/walls.png"), 16, 16);
	public static BufferedImage credits = load("/img/credits.png");
	public static BufferedImage levels = load("/data/levels.png");
	public static BufferedImage sky = load("/img/sky.png");
	public static BufferedImage title = load("/img/titlescreen.png");
	public static BufferedImage load(String name)
	{
		try
		{
			BufferedImage org = ImageIO.read(Images.class.getResource(name));
			BufferedImage res = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = res.getGraphics();
			g.drawImage(org, 0, 0, null, null);
			g.dispose();
			return res;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	private static BufferedImage[][] chop(BufferedImage src, int xs, int ys)
	{
		int xSlices = src.getWidth() / xs;
		int ySlices = src.getHeight() / ys;
		BufferedImage[][] res = new BufferedImage[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++)
		{
			for (int y = 0; y < ySlices; y++)
			{
				res[x][y] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
				Graphics g = res[x][y].getGraphics();
				g.drawImage(src, -x * xs, -y * ys, null);
				g.dispose();
			}
		}
		return res;
	}
}