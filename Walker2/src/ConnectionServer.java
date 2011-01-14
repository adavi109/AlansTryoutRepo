import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/* This is the main server loop that throws the connection to a thread
 * 
 */
public class ConnectionServer implements Runnable
{
	static final int PORT = 2010;
	Player[] serverPlayerList;
	final int MAXCLIENTS = 40;
	ServerSocket s;
	// for debugging
	ServerSocket testing_socket;
	static final int TESTING_PORT = 3010;
	

	ConnectionServer()
	{
		serverPlayerList = new Player[MAXCLIENTS];
		for (int c = 0; c < serverPlayerList.length; c++)
		{
			serverPlayerList[c] = null;
		}
	}

	public void run()
	{
		makeServer();
		System.out.println("made server");
		sendConnectionsToThread();
	}

	public void makeServer()
	{
		try
		{
			s = new ServerSocket(PORT);
			// for debugging
			testing_socket = new ServerSocket(TESTING_PORT);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Server Started");
	}

	private void sendConnectionsToThread()
	{
		try
		{
			while (true)
			{
				Socket socket = s.accept();
				try
				{
					Thread n = new Thread(new ServerRun(socket));
					n.run();
				} catch (IOException e)
				{
					socket.close();
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				s.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException
	{

		ConnectionServer m = new ConnectionServer();
		m.run();

	}

	class ServerRun implements Runnable
	{
		private Socket socket;
		ObjectOutputStream out;
		ObjectInputStream in;
		int clientID;
		int timeoutTick;

		public ServerRun(Socket s) throws IOException
		{
			socket = s;
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			timeoutTick = 0;
		}

		public void run()
		{
			handshake();
			
			while (true)
			{
				pause();
				writeStampedMessage();
				recieveInputs();
				processClientInputs();
				
				
			}
		}

		protected void handshake()
		{
			for (int i = 0; i < serverPlayerList.length; i++)
			{
				if (serverPlayerList[i] == null)
				{
					serverPlayerList[i] = new Player(i);
					try
					{
						out.write(i);
						out.writeObject(serverPlayerList);
						out.flush();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
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
		
		protected void recieveInputs()
		{
			try
			{
				ClientMessage temPlayer = new ClientMessage((ClientMessage)in.readObject());
				serverPlayerList[temPlayer.getId()].setState(temPlayer.getState());
			} catch (IOException e)
			{
				
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		protected void writeStampedMessage()
		{
			try
			{
				giveMeFeedback();
				// for debugging
				Socket ts = testing_socket.accept();
				ObjectOutputStream o = new ObjectOutputStream(ts.getOutputStream());
				o.writeObject(serverPlayerList);
				o.flush();

			} catch (IOException e)
			{
				try
				{
					// for debugging *** of course this should be changed accordingly
					out.close();
				in.close();
				socket.close();
				return;
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}

		protected void processClientInputs()
		{
			for (int i = 0; i < serverPlayerList.length; i++)
			{
				if (serverPlayerList[i] != null)
				{
					switch (serverPlayerList[i].getState())
					{
					case 1:
						serverPlayerList[i]
								.setX(serverPlayerList[i].getX() - 5);
						break;
					case 2:
						serverPlayerList[i]
								.setX(serverPlayerList[i].getX() + 5);
						break;
					}
				}
			}
		}
		public void giveMeFeedback()
		{
			System.out.println("X: " + serverPlayerList[0].getX() + "\nY: "
					+ serverPlayerList[0].getY() + "\nState: "
					+ serverPlayerList[0].getState());
		}
	}
}
