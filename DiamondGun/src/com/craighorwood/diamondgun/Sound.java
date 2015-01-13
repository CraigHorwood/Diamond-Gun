package com.craighorwood.diamondgun;
import java.io.*;
import javax.sound.sampled.*;
public class Sound
{
	public static class Clips
	{
		public Clip[] clips;
		private int p;
		private int count;
		public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException
		{
			if (buffer == null) return;
			clips = new Clip[count];
			this.count = count;
			for (int i = 0; i < count; i++)
			{
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
			}
		}
		public void play()
		{
			if (clips == null) return;
			clips[p].stop();
			clips[p].setFramePosition(0);
			clips[p].start();
			p++;
			if (p >= count) p = 0;
		}
	}
	public static Clips absorb = load("/snd/absorb.wav", 1);
	public static Clips bosskill1 = load("/snd/diamondboss_kill1.wav", 1);
	public static Clips bosskill2 = load("/snd/diamondboss_kill2.wav", 1);
	public static Clips charge = load("/snd/charge.wav", 1);
	public static Clips dink = load("/snd/dink.wav", 1);
	public static Clips explosion = load("/snd/explosion.wav", 4);
	public static Clips getgun = load("/snd/getgun.wav", 1);
	public static Clips hurt = load("/snd/hurt.wav", 1);
	public static Clips jump = load("/snd/jump.wav", 1);
	public static Clips jumpenemy = load("/snd/enemyjump.wav", 1);
	public static Clips place = load("/snd/place.wav", 1);
	public static Clips shootenemy = load("/snd/enemyshoot.wav", 4);
	public static Clips shootplayer = load("/snd/playershoot.wav", 4);
	public static Clips start = load("/snd/start.wav", 1);
	public static Clips step = load("/snd/step.wav", 1);
	public static Clips switchgun = load("/snd/switchgun.wav", 1);
	private static Clips load(String name, int count)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(name));
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = dis.read(buffer)) >= 0)
			{
				baos.write(buffer, 0, read);
			}
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(data, count);
		}
		catch (Exception e)
		{
			try
			{
				return new Clips(null, 0);
			}
			catch (Exception ee)
			{
				return null;
			}
		}
	}
	
}