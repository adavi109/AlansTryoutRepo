import java.io.Serializable;

import javax.swing.ImageIcon;

class Player implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id, state, frame, x, y, delay;
	ImageIcon currentImg;

	public Player(int id)
	{
		this.id = id;
		state = 0;
		frame = 0;
		x = 0;
		y = 0;
		delay = 0;

		currentImg = null;
	}
	
	public Player(Player p)
	{
		this.id = p.id;
		state = p.getState();
		frame = p.getFrame();
		x = p.getX();
		y = p.getY();
		delay = p.delay;

		currentImg = null;
	}
	
	public void updatePlayer(Player in)
	{
		x = in.getX();
		y = in.getY();
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int s)
	{
		state = s;
	}

	public int getFrame()
	{
		return frame;
	}

	public void setFrame(int frame)
	{
		this.frame = frame;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public ImageIcon getCurrentImg()
	{
		return currentImg;
	}

	public void setCurrentImg(ImageIcon currentImg)
	{
		this.currentImg = currentImg;
	}

	public void animateCharacter(int timePassed)
	{
		delay = +timePassed;

		if (delay < 200)
		{
			delay += timePassed;
		} else
		{
			delay = 0;
			if (frame <= 2)
			{
				frame++;
			} else
				frame = 0;
		}
	}
}
