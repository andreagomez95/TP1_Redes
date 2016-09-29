package actors;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class Server {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException 
    {
    	
        ServerSocket listener = new ServerSocket(9091);
        System.out.println("Starting server at " + listener.getInetAddress() + " in port 9091");
        
        try 
        {
        	System.out.println("Server waiting to connect...");
            while (true) 
            {
                Socket clientSocket = listener.accept();
                System.out.println("Server connected to " + clientSocket.getInetAddress());
                try 
                {
                	System.out.println("Server sending package to client...");
                    PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println(new Date().toString());
                } finally 
                {
                	System.out.println("Server closing connection to client.");
                	clientSocket.close();
                }
            }
        }
        finally 
        {
        	System.out.println("Server closing connection.");
            listener.close();
        }
    }
}