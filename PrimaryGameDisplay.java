import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class PrimaryGameDisplay extends JFrame implements Runnable
{

	static final int WIDTH = 640;
	static final int HEIGHT = 480;
	private final int JUMP = 10; // increment for movement

	static final int SPEED = 10;// the speed that the game runs

	Player[] tempList;
	ImageIcon[] anim;

	Graphics graphics;
	Graphics2D g2d;
	GraphicsDevice gd;
	GraphicsConfiguration gc;
	BufferStrategy buffer;
	GraphicsEnvironment ge;
	BufferedImage bufImage;
	Color background;
	Canvas canvas;
	KeyboardInput keyboard = new KeyboardInput();

	public PrimaryGameDisplay(Player[] p)
	{

		configurePanel();
		loadAnimationRecources();
		tempList = p;
	}

	public void configurePanel()
	{
		setIgnoreRepaint(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addCanvas();
		pack();
		
		addKeyListener( keyboard );
	    canvas.addKeyListener( keyboard );
	}

	public void addCanvas()
	{
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setSize(WIDTH, HEIGHT);
		add(canvas);
	}

	public void loadAnimationRecources()
	{
		anim = new ImageIcon[3];
		anim[0] = new ImageIcon("dots.png");
		anim[1] = new ImageIcon("dots1.png");
		anim[2] = new ImageIcon("dots2.png");
	}

	public void run()
	{
		configureBuffer();

		graphics = null;
		g2d = null;

		background = Color.BLACK;

		while (true)
		{
			try
			{
				keyboard.poll();
				clearBackBuffer();
				
				anim[1].paintIcon(this, g2d,
						100, 100);
				processInput();
				drawObjects();
				blitAndFlip();
				sleep();

			} finally
			{
				// Release resources
				if (graphics != null)
					graphics.dispose();
				if (g2d != null)
					g2d.dispose();
			}
		}
	}

	public void configureBuffer()
	{
		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();
		bufImage = gc.createCompatibleImage(WIDTH, HEIGHT);
	}

	public void clearBackBuffer()
	{
		g2d = bufImage.createGraphics();
		g2d.setColor(background);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	protected void processInput()
	{
		if (keyboard.keyDown(KeyEvent.VK_A))
		{
			tempList[0].setState(1);
		}else if (keyboard.keyDown(KeyEvent.VK_D))
		{
			tempList[0].setState(2);

		}else 
			tempList[0].setState(0);
	}

	public void resetList()
	{
		// TODO
	}

	public void drawObjects()
	{
		drawAllCharacters();
		displayClientStatus();
	}

	public void drawAllCharacters()
	{
		if (tempList != null)
		{
			for (int playernum = 0; playernum < tempList.length; playernum++)
			{
				drawOneCharacter(playernum);
			}
		}
	}

	public void drawOneCharacter(int playerNum)
	{
		if (tempList[playerNum] != null)
		{
			tempList[playerNum].setCurrentImg(anim[0]);
			tempList[playerNum].currentImg.paintIcon(this, g2d,
					tempList[playerNum].x, tempList[playerNum].y);
		}
	}

	public void displayClientStatus()
	{
		// TODO
	}

	public void blitAndFlip()
	{
		graphics = buffer.getDrawGraphics();
		graphics.drawImage(bufImage, 0, 0, null);
		if (!buffer.contentsLost())
			buffer.show();
	}

	protected void sleep()
	{
		try
		{
			Thread.sleep(SPEED);
		} catch (InterruptedException e)
		{
		}
	}

	public void setPlayers(Player[] p)
	{
		tempList = null;
		tempList = p;
	}
	
	public int getState(int playernum)
	{
		return tempList[playernum].getState();
	}

	public static void main(String[] args) throws IOException
	{
		Player[] players = new Player[40];
		players[0] = new Player(0);
		PrimaryGameDisplay app = new PrimaryGameDisplay(players);
		app.setTitle("Test Display");
		app.setVisible(true);
		app.run();

		System.exit(0);

	}
}
