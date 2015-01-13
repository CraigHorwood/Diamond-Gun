package com.craighorwood.diamondgun;
import java.io.*;
import javazoom.jl.player.*;
public class Music
{
	private static boolean canPlay = false;
	private static int song = -1;
	private static long from;
	public static boolean shouldLoop = true;
	private static Player player;
	private static InputStream input;
	public static long pausedAt = 0;
	private static long currentSongLength = 0;
	public static void startMusic(int song, long from)
	{
		if (from == 0 && player != null) player.close();
		Music.song = song;
		Music.from = from;
		(new Thread()
		{
			public void run()
			{
				try
				{
					String path = "/msc/gun" + Music.song + ".mp3";
					if (Music.song == 0) path = "/msc/credits.mp3";
					if (Music.song == 6) path = "/msc/endboss.mp3";
					input = Music.class.getResource(path).openStream();
					currentSongLength = input.available();
					input.skip(Music.from);
					player = new Player(input);
					canPlay = true;
					Thread thread = new Thread()
					{
						public void run()
						{
							try
							{
								player.play();
								if (player.isComplete())
								{
									if (shouldLoop) startMusic(Music.song, 0);
									else stopMusic();
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					};
					thread.setName("DiamondGun-MusicThread");
					thread.setDaemon(true);
					thread.start();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static long pauseMusic()
	{
		if (player != null && canPlay)
		{
			try
			{
				pausedAt = currentSongLength - input.available();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			player.close();
		}
		return pausedAt;
	}
	public static void resumeMusic()
	{
		if (player != null && canPlay) startMusic(song, pausedAt);
	}
	public static void stopMusic()
	{
		if (player != null)
		{
			player.close();
			canPlay = false;
		}
		currentSongLength = 0;
	}
}