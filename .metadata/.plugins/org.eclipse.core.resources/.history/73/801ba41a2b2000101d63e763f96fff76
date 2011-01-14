import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class WalkServer
{
	static final int PORT = 2010;
	Player[] serverPlayerList;
	final int MAXCLIENTS = 40;
	ServerSocket s;
	protected ObjectInputStream[] in;
	private ObjectOutputStream[] out;
	private boolean[] activeStreams;
	
	public WalkServer(ObjectInputStream i, ObjectOutputStream o)
	{
		
	}
	
	public void run()
	{
		
	}
	
	private void addPlayer(Socket s)
	{
		
	}
	
	private int getFirstInactiveStream()
	        {
	            int i = 0;
	            while (i < activeStreams.length &&
	                    activeStreams[i])
	                i++;
	            return i;
	        }
	public boolean isActive()
	        {
	            for (boolean activity : activeStreams)
	                if (activity)
	                    return true;
	           return false;
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
}
