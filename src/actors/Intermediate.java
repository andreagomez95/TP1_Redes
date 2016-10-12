package actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Intermediate extends Thread
{

	   private Thread t;
	   
	   private String threadName;
	   
	   private static int portServer;
	   
	   private static int portClient;
	   
	   private static int p;
	   
	   private static boolean debug;
	   
	   private PrintWriter out;
	   
	   private BufferedReader in;
	   
	   String serverAddress = "localhost"; //Mejor mantenerlo en local host para que funcione bien.
	
	   public static void main(String args[])  throws IOException
	   {
		  getInput();
		  Intermediate T1 = new Intermediate( "Thread-ServerLink");
	      T1.start();
	      
	      Intermediate T2 = new Intermediate( "Thread-ClientLink");
	      T2.start();
	   }   
	   
	     
	   Intermediate(String name)  throws IOException
	   {
	      threadName = name;
	      System.out.println("Creating " +  threadName + ".");
	   }
	   
	   public void runIntermediate()   throws IOException
	   {
		 
	    	if(threadName.equals("Thread-ServerLink"))
	    	{
	    		if(debug)
	            {
	    			System.out.println("Intermediate client connecting to server at " + serverAddress + " in port " + portServer);
	            }
		        
		        Socket serverSocket = new Socket(serverAddress, portServer);
		        
		        if(debug)
	            {
	    			System.out.println("Intermediate client connecting to client at " + serverAddress + " in port " + portClient);
	            }
		        
		        Socket clientSocket = new Socket(serverAddress, portClient);
		        if(debug)
	            {
		        	System.out.println("Intermediate client connected to server");
	            }
		        
		        //out will send the ACKs to the client
		        out = new PrintWriter(clientSocket.getOutputStream(), true);
		        
		        //in will receive the ACKs from the server
		        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

		        String input = in.readLine();
                if(debug)
                {
                	System.out.println("Acknowledge " + input + " received from server.");
                }
                
                if(isMissing())
                {
                	if(debug)
	                {
	                	System.out.println("Acknowledge " + input + " lost.");
	                }
                }
                else
                {
                	if(debug)
	                {
	                	System.out.println("Sending Acknowledge " + input + " to server.");
	                }
                	out.println(input);
                }
	    	}
	    	else
	    	{
	    		ServerSocket clientConnection = new ServerSocket(portClient);
		        if(debug)
		        {
		        	System.out.println("Starting intermediate server connection at " + clientConnection.getInetAddress() + " in port " + portClient);
		        }
		        try 
		        {
		        	if(debug)
		            {
		        		System.out.println("Intermediate server waiting to connect...");
		            }
		        	
		            if(true) 
		            {
		                Socket clientSocket = clientConnection.accept();
		                if(debug)
		                {
		                	System.out.println("Intermediate server connected to " + clientSocket.getInetAddress());
		                }
		                
		                Socket serverSocket = new Socket(serverAddress, portServer);
		                
		                if(debug)
		                {
		                	System.out.println("Intermediate server connected to " + serverSocket.getInetAddress());
		                }
		                
		                try 
		                {
		                	//in will receive the frames from the client
		                	in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		                	//out will send the frames to the server
		                    out = new PrintWriter(serverSocket.getOutputStream(), true);
		                    
		                    String input = " ";
		                    //The main loop of the server that receives the frames from the client and returns acknowledges to it.
		                    while (true) 
		                    {
		                        input = in.readLine();
		                        if(debug)
	    		                {
	    		                	System.out.println("Packet " + input + " received from client.");
	    		                }
		                        
		                        if(isMissing())
		                        {
		                        	if(debug)
		    		                {
		    		                	System.out.println("Packet " + input + " lost.");
		    		                }
		                        }
		                        else
		                        {
		                        	if(debug)
		    		                {
		    		                	System.out.println("Sending packet " + input + " to server.");
		    		                }
		                        	out.println(input);
		                        }
		                    }
		                }
		                catch (IOException e) 
		                {
		                	System.out.println("Error handling client : " + e);
		                }    
		                finally 
		                {
		                	System.out.println("Intermediate closing its connection to client.");
		                	clientSocket.close();
		                }
		            }
		        }
		        finally 
		        {
		        	System.out.println("Intermediate closing.");
		        	clientConnection.close();
		        }
	    	}
	   }
	   
	   public void run() 
	   {
	      System.out.println("Running " +  threadName );
	      try 
	      {
	         try {
				runIntermediate();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         Thread.sleep(50);
	      }catch (InterruptedException e) 
	      {
	         System.out.println("Thread " +  threadName + " interrupted.");
	      }
	      System.out.println("Thread " +  threadName + " exiting.");
	   }
	   
	   public void start () 
	   {
	      System.out.println("Starting " +  threadName );
	      if (t == null) 
	      {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }
	   
	   public static void getInput()
	    {
	    	Scanner scan = new Scanner(System.in);
	        System.out.println("Input the port to connect to the server: ");
	        portServer = Integer.parseInt(scan.nextLine());
	        System.out.println("Input the port to connect to the client: ");
	        portClient = Integer.parseInt(scan.nextLine());
	        System.out.println("Input the probability p of missing frames: ");
	        p = Integer.parseInt(scan.nextLine());
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
	   
	   public boolean isMissing()
	   {
		   Random randomNumber = new Random();
		   
		   int randomP = randomNumber.nextInt(100);
		   
		   if(randomP <= p)
		   {
			   return true;
		   }
		   return false;
	   }
}