import java.io.Serializable;

class ClientMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id, state;

	public ClientMessage()
	{
		id = -1;
		state = 0;
	}

	public ClientMessage(ClientMessage c)
	{
		id = c.id;
		state = c.state;
	}

	public ClientMessage(int id)
	{
		this.id = id;
		state = 0;
	}
	
	public void set(ClientMessage c)
	{
		id = c.id;
		state = c.state;
	}

	public ClientMessage get()
	{
		return this;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int a)
	{
		state = a;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}
