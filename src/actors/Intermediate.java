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
	   
	   private  static PrintWriter outServer;//Es lo que viene del servidor y va hacia el cliente
	   
	   private  static BufferedReader inServer;//Es lo que entra al servidor desde el cliente
	   
	   private  static PrintWriter outClient;//Es lo que viene del cliente y va hacia el servidor
	   
	   private  static BufferedReader inClient;//Es lo que entra al cliente desde el servidor
	   
	   static String serverAddress = "localhost"; //Mejor mantenerlo en local host para que funcione bien.
	   public  String serverAddress1="";
		
		public static Socket serverSocket;
		public static ServerSocket listener;
		public static Socket clientSocket;

	   
	   
	   
	
	   public static void main(String args[])  throws IOException
	   {
		   getInput();
		   
		   serverSocket = new Socket(serverAddress, portServer);//Para conectarse con el servidor
		      listener = new ServerSocket(portClient);
		      
		      
	          //Lo que sale del servidor hacia el cliente
		        
		        
		        
		   System.out.println("Hasta aqui llegp");
		   clientSocket = listener.accept();
           outClient = new PrintWriter(clientSocket.getOutputStream(), true);
	          //Lo que sale del cliente hacia el servidor
           inServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));//
	      	//Lo que le entra al servidor desde el cliente
           inClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		      //Lo que le entra al cliente desde el server
	          outServer = new PrintWriter(serverSocket.getOutputStream(), true);
		  Intermediate T1 = new Intermediate( "Thread-ServerLink");
	      T1.start();
	      
	      Intermediate T2 = new Intermediate( "Thread-ClientLink");
	      T2.start();
	     // System.exit(0);
	   }   
	   
	   
	   
	   
	   
	   
	   
	   
	   public void clienteAServidor() throws IOException
	   {
		 //Asks the user to input the necessary data for the program to run

		   /*
		    * Recibe el input del cliente y lo envia al servidor.
		    * Deberia tomar las cosas del inClient creado con la direccion del cliente
		    * y enviarlo al out con la direccion de modo que le llegue al servidor.
		    * 
		    * 
		    * */
		        if(debug)
		        {
		        	System.out.println("Starting server at " + listener.getInetAddress() + " in port " + portClient);
		        }
		        
		        //Adds the initial empty frames to the window of the server
		        try 
		        {
		        	if(debug)
		            {
		        		System.out.println("Intermediate waiting to connect...");
		            }
		        	
		            if(true) 
		            {

		                //Acepta la conexion con el cliente (por eso usa el listener creado con la direccion del cliente)
		                if(debug)
		                {
		                	System.out.println("Intermediate connected to " + clientSocket.getInetAddress());
		                }
		                
		                try 
		                { 
		                    String input = " ";
		                    //The main loop of the server that receives the frames from the client and returns acknowledges to it.
		                    while (true) 
		                    {
		                        input = inServer.readLine();//Lo que le deberia entrar al servidor
		                        System.out.println("Servidor recibio " + input);
		                        //If the there are no more packages to send, the client will send a "." to message EOF.
		                        if (input == null || ( input.compareTo(".")==0) )
		                        {
		                        	
		                        	if(debug)
		                            {
		                        		System.out.println("EOF frame received.");
		                            }
		                            break;
		                        }

		                        if(debug)
		                        {
		                        	System.out.println("Frame received by Intermediate from server: " + input);
		                        }
		                        if(!isMissing()){
			                        outClient.println(input);
			                        outClient.flush();
		                        }
		                        if(debug)
		                        {
		                        	System.out.println("Intermediate sending: " + input + "from client to server.");
		                        }
		                        
		                    }
		                }catch (IOException e) 
		                {
		                	//System.out.println("Error handling client : " + e);
		                }    
		                finally 
		                {
		                	
		                }
		                if(debug)
                        {
	                		System.out.println("Server closing connection to client.");
                        }
	                	clientSocket.close();
		            }
		        }
		        finally 
		        {
		        	
		        }
		        if(debug)
                {
	        		System.out.println("Server closing.");
                }
	            listener.close();
	    
	   }
	     
	   
	   public void servidorACliente() throws IOException
	   {
		 //Asks the user to input the necessary data for the program to run

		   /*
		    * Recibe el input del servidor y lo envia al cliente.
		    * Deberia tomar las cosas del inServer creado con la direccion del servidor
		    * y enviarlo al out con la direccion de modo que le llegue al cliente.
		    * 
		    * 
		    * */
		   	try {
					String input="";
					int i=0;
					while (true) 
	                {
	                    input = inClient.readLine();//Lo que le deberia entrar al servidor
	                    System.out.println("Servidor recibio " + input);
	                    //If the there are no more packages to send, the client will send a "." to message EOF.
	                    if (input == null || input.equals(".")) 
	                    {
	                    	
	                    	if(debug)
	                        {
	                    		System.out.println("EOF frame received.");
	                        }
	                        break;
	                    } else {
	
		                    if(debug)
		                    {
		                    	System.out.println("Frame received by Intermediate from server: " + input);
		                    }
		                    if(!isMissing()){
					            outServer.println(input);
			                    outServer.flush();
			                }
		                    
		                    if(debug)
		                    {
		                    	System.out.println("Intermediate sending: " + input + "from server to client.");
		                    }
	                    }
	                    
	                }

			} catch (IOException e) {
				// TODO Auto-generated catch block
				//System.out.println("Problema while recibeACK"+i);
				//e.printStackTrace();
				//System.out.println("Problema while recibeACK");
			}
			
	    
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
	    		clienteAServidor();

	    	}
	    	else
	    	{
	    		
	    		servidorACliente();
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