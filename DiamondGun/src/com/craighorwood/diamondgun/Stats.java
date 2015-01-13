package com.craighorwood.diamondgun;
public class Stats
{
	public static int time = 0;
	public static int deaths = 0;
	public static boolean highScoreFailed = false;
	public static int getTotalScore()
	{
		int score = 0;
		int timeScore = (1500 - (time / 60)) * 100;
		int deathScore = 10000 - deaths * 100;
		if (timeScore < 0) timeScore = 0;
		if (deathScore < 0) deathScore = 0;
		score += timeScore;
		score += deathScore;
		return score;
	}
	public static String getTimeString()
	{
		int seconds = time / 60;
		int minutes = seconds / 60;
		seconds %= 60;
		String str = minutes + ":";
		if (seconds < 10) str += "0";
		str += seconds;
		return str;
	}
}