import java.io.*;
import java.net.*;


public class ServerConnector implements Runnable
{
	private ServerSocket serverSocket;
	
	
	public void connect()
	        {
	    		boolean connected = true;
	    		try {
					serverSocket = new ServerSocket(1234);
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		
	            while (connected)
	            {
	                Socket socket = null;
	                try
	                {
	                    socket = serverSocket.accept();
	                }
	                catch (IOException ioe)
	                {
	                    ioe.printStackTrace();
	                    break;
	                }
	                try
	                {
	                    ObjectOutputStream out = new ObjectOutputStream(socket
	                                             .getOutputStream());
	                    ObjectInputStream in = new ObjectInputStream(
	                                               new BufferedInputStream(socket.getInputStream(), 100));
	                    //TODO send to server
	                }
	                catch (IOException ioe)
	                {
	                    System.err.println("Problems connecting!");
	                    ioe.printStackTrace(System.err);
	                }
	            }
	        }
	
	public getCurrentConnection()
	{
		
	}


	@Override
	public void run() {
		connect();
	}
}
