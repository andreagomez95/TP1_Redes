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
import java.util.LinkedList;
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

	
	private FileHandler fileHandler = new FileHandler();
	
	//private String filePath = "C:/Users/DELL/Documents/GitHub/TP1_Redes/outputTP1.txt";
	private String filePath = "C:/Users/USUARIO/git/TP1_Redes/outputTP1.txt";
	
	private Queue<Frame> receivedQueue = new LinkedList<Frame>();
	
	private Queue<Frame> window = new LinkedList<Frame>();
	
	private int windowSize;
	
	private int port; //9093
	
	private int ID = 0;
	
	private boolean debug;
	
	private int sec;
	
	private char charac;
    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException 
    {
    	Server server = new Server();
    	
    	System.exit(0);
    }
    
    public Server() throws IOException 
    {
    	//On true, asks the user to input the necessary data for the program to run
    	//On false, sets the values by default
    	if(getInput(true))
    	{
	        ServerSocket listener = new ServerSocket(port);
	        if(debug)
	        {
	        	System.out.println("Starting server at " + listener.getInetAddress() + " in port " + port);
	        }
	        
	        //Adds the initial empty frames to the window of the server
	        addEmptyFrames();
	        
	        try 
	        {
	        	if(debug)
	            {
	        		System.out.println("Server waiting to connect...");
	            }
	        	
	            if(true) 
	            {
	                Socket clientSocket = listener.accept();
	                if(debug)
	                {
	                	System.out.println("Server connected to " + clientSocket.getInetAddress());
	                }
	                
	                try 
	                {
	                	//in will receive the frames from the client
	                	BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
	                	//out will send the acknowledges to the client
	                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	                    
	                    String input = " ";
	                    //The main loop of the server that receives the frames from the client and returns acknowledges to it.
	                    while (true) 
	                    {
	                        input = in.readLine();
	                        
	                        //If the there are no more packages to send, the client will send a "." to message EOF.
	                        if (input == null || input.equals(".")) 
	                        {
	                        	
	                        	if(debug)
	                            {
	                        		System.out.println("EOF frame received.");
	                            }
	                            break;
	                        }
	                        
	                        //Splits the input string to separate the ID from the data in the frame
	                        splitFrameData(input);
	                        
	                        
	                        if(debug)
	                        {
	                        	System.out.println("Frame number " + sec + " received by server.");
	                        }
	                        
	                        
	                        Frame frame = checkIDs(sec);
	                        //If the ID is not found, then it means that the previous ACK was lost
	                        
	                        if(frame != null)
	                        {
	                        	//If the ID is found, get the data to said frame and change its state to "received".
	                        	if(debug)
	                            {
	                        		System.out.println("Writing " + charac + " into frame " + sec + ".");
	                            }
	                        	frame.setData(charac);
	                        	frame.setRecibido(true);
	                        }
	                        else
	                        {
	                        	if(debug)
	                            {
	                        		System.out.println("Frame ID not found.");
	                            }
	                        }
	                        moveWindow();
	                        
	                        //Either way, send an ACK for the section
	                        out.println("ACK:"+sec);
	                        //out.flush();
	                        
	                        if(debug)
	                        {
	                        	System.out.println("Server sending ACK ID: " + sec + " to client.");
	                        }
	                        
	                    }
	                    
	                    //After all the frames have been received, write the data into a new file
	                    //writeFile();
	                    
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
	        	System.out.println("Server closing.");
	            listener.close();
	            writeFile();
	        }
    	}
    }
    
    public void splitFrameData(String input)//Para separar los datos del numero de frame
    {
    	String[] frameElements;
        String delimeter = ":";
        frameElements = input.split(delimeter);
        
        String section = frameElements[0];
        String character = frameElements[1];
        
        sec = Integer.parseInt(section);
        charac = character.charAt(0);
    }
    
    public void addEmptyFrames()//Añadir frames vacios
    {
    	//Adds the initial empty frames to the window
        for(int i = 0; i < windowSize; i++)
        {
        	Frame e = new Frame(ID,0);
        	window.add(e);
        	ID++;
        }
    }
    
    public boolean getInput(boolean test)//Obtener del usuario el modo en el que se va a correr el programa.
    {
    	if(test)
    	{
    		Scanner scan = new Scanner(System.in);
            System.out.println("Input window size: ");
            windowSize = Integer.parseInt(scan.nextLine());
            System.out.println("Input port to connect to: ");
            port = Integer.parseInt(scan.nextLine());
            System.out.println("Input file path to write the output: ");
            filePath = scan.nextLine();
            System.out.println("Input debug of display debug?(y/n): ");
            String mode = scan.nextLine();
            
            
            if(mode.equalsIgnoreCase("y"))
            {
            	debug = true;
            }
            else
            {
            	debug = false;
            }
            
            scan.close();
    	}
    	else
    	{
    		windowSize = 4;
    		port = 9090;
    		debug = false;
    		//filePath = "C:/Users/USUARIO/git/TP1_Redes/outputTP1.txt";
    		filePath = "C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt";
    	}
    	
        if(windowSize != 0 && port != 0)
        {
        	
        	return true;
        }
        return false;
    }
    
    public void moveWindow()//Correr la ventana
    {
    	while(window.peek().getRecibido())
        {
    		Frame oldFrame = window.poll();
    		Frame newFrame = new Frame(ID,0);
    		//Add an empty frame to the window
        	window.add(newFrame);
        	if(debug)
            {
        		System.out.println("Moving window one position.");
            }
        	//Add the old frame to the receivedQueue. 
        	receivedQueue.add(oldFrame);
        	if(debug)
            {
        		System.out.println("Adding frame data " + oldFrame.getData() + " to receivedQueue.");
            }
        	ID++;
        }
    	
    }
    //Checks if the given id of the received frame is found in the window
    public Frame checkIDs(int id)//Revisar los id de los frames
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
    
    public void writeFile()//Escribir en el archivo
    {
    	System.out.println("Server beginning writing data to file...");
    	Frame a;
    	String t="";
    	int totalFrames=receivedQueue.size();
    	for(int i=0; i<totalFrames; i++){
    		a=receivedQueue.poll();
    		t=t+a.getData();
       }
    	/*for(Frame f : receivedQueue)
    	{*/
    		try 
    		{
				fileHandler.writeUsingBuffer(t, filePath);
			} catch (IOException e) 
    		{
				e.printStackTrace();
			}
    //	}
    	System.out.println("All data written to file successfully.");
    }
}