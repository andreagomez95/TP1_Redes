package actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.Scanner;

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class Server 
{

	
	private static FileHandler fileHandler = new FileHandler();
	private static String filePath = "C:/Users/USUARIO/Documents/outputTP1.txt";
	
	private static Queue<Frame> receivedQueue = new ArrayDeque<Frame>();
	private static Queue<Frame> window = new ArrayDeque<Frame>();
	
	private static int windowSize;
	
	private static int port; //9093
	
	private static int ID = 0;
	
	private static String mode;
    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException 
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Input window size: ");
        windowSize = scan.nextInt();
        System.out.println("Input port to connect to: ");
        port = scan.nextInt();
        System.out.println("Input mode of display: ");
        mode = scan.nextLine();
        
        ServerSocket listener = new ServerSocket(port);
        System.out.println("Starting server at " + listener.getInetAddress() + " in port " + port);
        
        for(int i = 0; i < windowSize; i++)
        {
        	Frame e = new Frame(ID,0);
        	window.add(e);
        	ID++;
        }
        
        
        
        try 
        {
        	System.out.println("Server waiting to connect...");
        	//TODO: change while condition to stop loop
            while (true) 
            {
                Socket clientSocket = listener.accept();
                System.out.println("Server connected to " + clientSocket.getInetAddress());
                try 
                {
                	//in will receive the frames from the client
                	BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                	//out will send the acknowledges to the client
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    
                    //The main loop of the server that receives the frames from the client and returns acknowledges to it.
                    while (true) 
                    {
                        String input = in.readLine();
                        
                        //If the there are no more packages to send, the client will send a "." to message EOF.
                        if (input == null || input.equals(".")) 
                        {
                            break;
                        }
                        
                        //Splits the input string to separate the ID from the data in the frame
                        String[] frameElements;
                        String delimeter = ":";
                        frameElements = input.split(delimeter);
                        
                        String section = frameElements[0];
                        String character = frameElements[1];
                        
                        int sec = Integer.parseInt(section);
                        char charac = character.charAt(0);
                        
                        
                        System.out.println("Frame number " + section + " received by server.");
                        
                        Frame frame = checkIDs(sec);
                        //If the ID is not found, then it means that the previous ACK was lost
                        
                        if(frame != null)
                        {
                        	//If the ID is found, get the data to said frame and change its state to "received".
                        	frame.setData(charac);
                        	frame.setRecibido(true);
                        }
                        moveWindow();
                        
                        //Either way, send an ACK for the section
                        out.println("ACK:"+section);
                        System.out.println("Server sending ACK ID: " + section + " to client.");
                        
                        
                    }
                    
                    //After all the frames have been received, write the data into a new file
                    writeFile();
                    
                }catch (IOException e) 
                {
                	System.out.println("Error handling client : " + e);
                }    
                finally 
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
    
    public static void moveWindow()
    {
    	while(window.peek().getRecibido())
        {
    		Frame oldFrame = window.poll();
    		Frame newFrame = new Frame(ID,0);
    		//Add an empty frame to the window
        	window.add(newFrame);
        	//Add the old frame to the receivedQueue. 
        	receivedQueue.add(oldFrame);
        	ID++;
        }
    	
    }
    //Checks if the given id of the received frame is found in the window
    public static Frame checkIDs(int id)
    {
    	for(Frame f : window)
    	{
    		if(f.getIdFrame() == id)
    		{
    			return f;
    		}
    	}
    	return null;
    }
    
    public static void writeFile()
    {
    	System.out.println("Beginning writing data to file...");
    	for(Frame f : receivedQueue)
    	{
    		try 
    		{
				fileHandler.writeUsingBuffer(Character.toString(f.getData()), filePath);
			} catch (IOException e) 
    		{
				e.printStackTrace();
			}
    	}
    	System.out.println("All data written to file successfully.");
    }
}