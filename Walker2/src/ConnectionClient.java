//by Alan Davis

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;

public class ConnectionClient implements Runnable
{
	Socket s;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;

	PrimaryGameDisplay app;
	private String status;
	private Player[] playerList;
	private int myID;
	private int mostRecentTime;
	private final int PORT = 2010;
	private String hostName;
	boolean connected;
	KeyboardInput keyboard = new KeyboardInput();

	public ConnectionClient(Socket serverIP)
	{
		status = "none";
		s = serverIP;
		hostName = s.getInetAddress().getHostName();
		mostRecentTime = -1;
	}

	public void run()
	{
		setupConnection();

		makeHandshake();
		startDisplay();
		startInputThread();
		// constant loop inside, so no runtime processes afterward, only
		// closing
		startOutputLoop();
	}

	public void setupConnection()
	{
		// connectToHost();
		System.out.println("Connected to server " + hostName + " on port "
				+ PORT);
		initializeInputAndOutputStreams();
	}

	public void connectToHost()
	{
		try
		{
			s = new Socket(hostName, PORT);
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void initializeInputAndOutputStreams()
	{
		try
		{
			out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void makeHandshake()
	{
		startClientWithID();
		try
		{
			Thread.sleep(100);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
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

	public void startInputThread()
	{
		Thread i = new Thread(new ReceiverThread(in));
		i.start();
	}

	public void startOutputLoop()
	{

		while (connected)
		{
			try
			{
				Thread.sleep(100);

				giveMeFeedback(playerList[0]);
				ClientMessage outgoing = new ClientMessage(
						playerList[myID].sendToServer());
				out.writeObject(outgoing);
				out.flush();

			} catch (InterruptedException e)
			{

				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// receives new clientLists from the server
	public class ReceiverThread implements Runnable
	{
		ObjectInputStream input;

		Player[] tempPlayerList;

		ReceiverThread(ObjectInputStream i)
		{
			input = i;
			connected = true;
		}

		@Override
		public void run()
		{
			while (connected)
			{
				receiveAndUpdateList();
				updateDisplay();
				try
				{
					Thread.sleep(50);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		protected void receiveAndUpdateList()
		{
			try
			{
				StampedServerMessage tempPlayers = new StampedServerMessage(
						(StampedServerMessage) input.readObject());

				tempPlayerList = tempPlayers.getMessages();
				for (int i = 0; i < tempPlayerList.length; i++)
				{
					if (tempPlayerList[i] != null)
					{
						playerList[i].updatePlayer(tempPlayerList[i]);
					}
				}
			} catch (IOException e)
			{
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			} finally
			{
				// updatePlayerList(tempPlayers);
			}

		}

		protected void updatePlayerList(Player[] p)
		{
			for (int i = 0; i < p.length; i++)
			{
				if (p[i] != null)
				{
					playerList[i].updatePlayer(p[i]);
				}
			}
		}
	}

	protected void updateDisplay()
	{
		app.setPlayers(playerList);
		playerList[myID].setState(app.getState(myID));
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
		ConnectionClient c;
		try
		{
			c = new ConnectionClient(new Socket("localhost", 2010));
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
