package com.craighorwood.diamondgun;
import java.io.*;
public class InputOutput
{
	private static final int SAVE_HEADER = 0x53415645;
	private static final int HIGH_SCORE_HEADER = 0x48534352;
	public static int highScore = -1;
	private static File gameDir = getGameDir();
	private static File getGameDir()
	{
		String home = System.getProperty("user.home", ".");
		File file = new File(home, "DiamondGun/");
		switch (getOperatingSystem())
		{
		case 0:
			String appData = System.getenv("APPDATA");
			if (appData != null) file = new File(appData, "DiamondGun/");
			else file = new File(home, "DiamondGun/");
			break;
		case 1:
			file = new File(home, "Library/Application Support/DiamondGun");
			break;
		case 2:
			file = new File(home, "DiamondGun/");
			break;
		case 3:
		case 4:
		default:
			break;
		}
		if (!file.exists() && !file.mkdirs())
		{
			throw new RuntimeException("The working directory could not be created: " + file);
		}
		return file;
	}
	private static int getOperatingSystem()
	{
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) return 0;
		if (os.contains("mac")) return 1;
		if (os.contains("solaris") || os.contains("sunos")) return 2;
		if (os.contains("linux") || os.contains("unix")) return 3;
		return 4;
	}
	public static boolean saveGame(byte xLevel, byte yLevel, byte gunLevel, short xSpawn, short ySpawn, byte bossesKilled, int time, int deaths, byte endBossMusic)
	{
		try
		{
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(gameDir + "/.save"));
			dos.writeInt(SAVE_HEADER);
			dos.writeByte(xLevel);
			dos.writeByte(yLevel);
			dos.writeByte(gunLevel);
			dos.writeShort(xSpawn);
			dos.writeShort(ySpawn);
			dos.writeByte(bossesKilled);
			dos.writeInt(time);
			dos.writeInt(deaths);
			dos.writeByte(endBossMusic);
			dos.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	public static int[] loadGame()
	{
		try
		{
			DataInputStream dis = new DataInputStream(new FileInputStream(gameDir + "/.save"));
			dis.skip(4);
			int[] saved = new int[9];
			saved[0] = dis.readByte();
			saved[1] = dis.readByte();
			saved[2] = dis.readByte();
			saved[3] = dis.readShort();
			saved[4] = dis.readShort();
			saved[5] = dis.readByte();
			saved[6] = dis.readInt();
			saved[7] = dis.readInt();
			saved[8] = dis.readByte();
			dis.close();
			return saved;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public static boolean saveHighScore(int highScore)
	{
		try
		{
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(gameDir + "/.hs"));
			dos.writeInt(HIGH_SCORE_HEADER);
			dos.writeInt(highScore);
			dos.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	public static int loadHighScore()
	{
		try
		{
			DataInputStream dis = new DataInputStream(new FileInputStream(gameDir + "/.hs"));
			dis.skip(4);
			highScore = dis.readInt();
			dis.close();
			return highScore;
		}
		catch (Exception e)
		{
			return -1;
		}
	}
	public static boolean saveExists()
	{
		File file = new File(gameDir + "/.save");
		return file.exists();
	}
	public static boolean highScoreExists()
	{
		File file = new File(gameDir + "/.hs");
		return file.exists();
	}
}