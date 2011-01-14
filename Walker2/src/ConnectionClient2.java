//by Alan Davis

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;

public class ConnectionClient2 implements Runnable
{
	Socket s;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;

	PrimaryGameDisplay app;
	private String status;
	private Player[] playerList;
	private int myID;
	private final int PORT = 2010;
	private String hostName;
	boolean connected;
	KeyboardInput keyboard = new KeyboardInput();
	
	// for debugging
	private final int TESTING_PORT = 3010;

	public ConnectionClient2(Socket serverIP)
	{
		status = "none";
		s = serverIP;
		hostName = s.getInetAddress().getHostName();
	}

	public void run()
	{
		setupConnection();

		makeHandshake();
		startDisplay();
		startLoop();
	}

	public void setupConnection()
	{
		System.out.println("Connected to server " + hostName + " on port "
				+ PORT);
		initializeInputAndOutputStreams();
	}

	public void initializeInputAndOutputStreams()
	{
		try
		{
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void makeHandshake()
	{
		startClientWithID();
		pause();
		getPlayerList();
	}

	public void startClientWithID()
	{
		try
		{
			myID = in.read();

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			System.out.println("ID is " + myID);
		}
	}

	protected void getPlayerList()
	{
		try
		{
			playerList = (Player[]) in.readObject();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	protected void startDisplay()
	{

		Thread gDisplay = new Thread(app = new PrimaryGameDisplay(playerList));
		app.setTitle("WalkVille");
		app.setVisible(true);
		gDisplay.start();
	}

	public void startLoop()
	{

		while (true)
		{
			try
			{
				doLoopTasks();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void doLoopTasks() throws InterruptedException, IOException,
			ClassNotFoundException
	{
		Thread.sleep(100);
		recieveNewList();
		updateDisplay();
		giveMeFeedback(playerList[0]);
		writeOutputs();
	}

	protected void recieveNewList() throws IOException, ClassNotFoundException
	{
		playerList = null;
		//playerList = (Player[]) in.readObject();
		// for debugging *** of course this should be changed accordingly
		Socket ts = new Socket("localhost", TESTING_PORT);
		ObjectInputStream o = new ObjectInputStream(ts.getInputStream());
		playerList = (Player[]) o.readObject();
		ts.close();
	}

	public void updateList()
	{
		Player[] tempPlayers = null;
		for (int i = 0; i < tempPlayers.length; i++)
		{
			if (tempPlayers[i] != null)
			{
				playerList[i].updatePlayer(tempPlayers[i]);
			}
		}
	}
	
	protected void pause()
	{
		try
		{
			Thread.sleep(100);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	protected void updateDisplay()
	{
		app.setPlayers(playerList);
		playerList[myID].setState(app.getState(myID));
	}

	public void writeOutputs() throws IOException
	{
		ClientMessage outgoing = new ClientMessage(
				playerList[myID].sendToServer());
		out.writeObject(outgoing);
		out.flush();
	}

	public void setClientStatus(String s)
	{
		status = s;
	}

	public void giveMeFeedback(Player p)
	{
		System.out.println("X: " + p.getX() + "\nY: " + p.getY() + "\nState: "
				+ p.getState());
	}

	public String getClientStatus()
	{
		return status;
	}

	public static void main(String args[])
	{
		ConnectionClient2 c;
		try
		{
			c = new ConnectionClient2(new Socket("localhost", 2010));
			c.run();
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
