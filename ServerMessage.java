import java.io.Serializable;


public class ServerMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x, y;
	
	public ServerMessage(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public ServerMessage(Player p)
	{
		x = p.getX();
		y = p.getY();
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


}
