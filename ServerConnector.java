import java.io.*;
import java.net.*;


public class ServerConnector
{
	private ServerSocket serverSocket;
	
	
	public void connect()
	        {
	    
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
	                    new Dispatcher(out, in).start();
	                }
	                catch (IOException ioe)
	                {
	                    System.err.println("Problems connecting!");
	                    ioe.printStackTrace(System.err);
	                }
	            }
	        }
}
