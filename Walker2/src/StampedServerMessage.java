import java.io.Serializable;


public class StampedServerMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Player[] messagesToBeSent;
	
	public StampedServerMessage(StampedServerMessage stpd)
	{
		messagesToBeSent = stpd.getMessages();
	}
	
	public StampedServerMessage(Player[] p)
	{
		messagesToBeSent = new Player[p.length];
		for (int i = 0; i < p.length; i++)
		{
			messagesToBeSent[i] =p[i];
		}
	}

	public Player[] getMessages()
	{
		return messagesToBeSent;
	}

	public void setMessages(Player[] messagesToBeSent)
	{
		this.messagesToBeSent = null;
		this.messagesToBeSent = messagesToBeSent;
	}
	
	
}