package com.craighorwood.diamondgun;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.craighorwood.diamondgun.screen.*;
public class DiamondGun extends Canvas implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 320;
	private static final int HEIGHT = 240;
	private static final int SCALE = 2;
	private boolean running = false;
	private Screen screen;
	private Thread thread;
	private Input input = new Input();
	private boolean started = false;
	public DiamondGun()
	{
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		this.addKeyListener(this);
		this.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e)
			{
			}
			public void focusLost(FocusEvent e)
			{
				input.releaseAll();
			}
		});
	}
	public void start()
	{
		running = true;
		thread = new Thread(this);
		thread.setName("DiamondGun-MainThread");
		thread.start();
	}
	public void stop()
	{
		running = false;
		try
		{
			thread.join();
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}
	public void setScreen(Screen newScreen)
	{
		if (screen != null) screen.removed();
		screen = newScreen;
		if (screen != null) screen.init(this);
	}
	private Image splashImage;
	public void update(Graphics g)
	{
		paint(g);
	}
	public void paint(Graphics g)
	{
		if (started) return;
		if (splashImage == null)
		{
			try
			{
				splashImage = ImageIO.read(DiamondGun.class.getResource("/logo.png"));
				splashImage = splashImage.getScaledInstance(640, 480, BufferedImage.SCALE_AREA_AVERAGING);
			}
			catch (IOException ioe)
			{
			}
		}
		g.drawImage(splashImage, 0, 0, null);
	}
	public void run()
	{
		requestFocus();
		Image image = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
		setScreen(new TitleScreen());
		long then = System.nanoTime();
		long unprocessed = 0;
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
		while (running)
		{
			Graphics g = image.getGraphics();
			long now = System.nanoTime();
			unprocessed += now - then;
			then = now;
			int max = 10;
			while (unprocessed > 0)
			{
				unprocessed -= 1000000000 / 60;
				screen.tick(input);
				input.tick();
				if (max-- == 0)
				{
					unprocessed = 0;
					break;
				}
			}
			screen.render(g);
			if (!hasFocus())
			{
				String msg = "CLICK TO REFOCUS";
				g.setColor(Color.RED);
				g.fillRect(28, 108, 264, 24);
				screen.drawBigString(msg, g, 160 - msg.length() * 8, 120 - 8);
			}
			g.dispose();
			try
			{
				started = true;
				g = getGraphics();
				g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0, 0, WIDTH, HEIGHT, null);
				g.dispose();
			}
			catch (Throwable e)
			{
			}
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
	}
	public void keyPressed(KeyEvent e)
	{
		input.set(e.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent e)
	{
		input.set(e.getKeyCode(), false);
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Diamond Gun");
		DiamondGun diamondGun = new DiamondGun();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("dat/ic.png"));
		frame.setLayout(new BorderLayout());
		frame.add(diamondGun, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		diamondGun.start();
	}
}