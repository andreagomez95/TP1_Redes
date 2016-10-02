package actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
//import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class Client {

		public static FileHandler fh = new FileHandler();
		public static String filePath = "C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt";

		public static Socket serverSocket; // = new Socket(serverAddress, numServerSocket);
		public static int numServerSocket=9093; //Usuario debe poder meterlo
		public static int numElemVentana; //Usuario debe poder meterlo
		public static int num1eroVentana; //Usuario debe poder meterlo
		
		public Queue<Frame> colaPendientes = new ArrayDeque<Frame>();
		public Queue<Frame> colaVentana = new ArrayDeque<Frame>();
	    /**
	     * Runs the client as an application.  First it displays a dialog
	     * box asking for the IP address or hostname of a host running
	     * the date server, then connects to it and displays the date that
	     * it serves.
	     */
		public static void ponerDatosEnFrames(String datos){
			
		}
		
		public static void main(String[] args) throws IOException {
			String datosEntrada = fh.readUsingBuffer(filePath);
	        
			String serverAddress = "localhost";//Mejor mantenerlo en local host para que funcione bien.
			//=JOptionPane.showInputDialog("Enter IP Address of a machine that is\n" + "running the date service on port 9090:");
			//String serverAddress = "hola";
			
			ponerDatosEnFrames(datosEntrada);
	        System.out.println("Client connecting to server at " + serverAddress + " in port 9091");
	        /*Socket*/ serverSocket = new Socket(serverAddress, numServerSocket);
	        System.out.println("Client receiving package from server.");
	        BufferedReader input =
	            new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
	        String answer = input.readLine();
	        JOptionPane.showMessageDialog(null, answer);
	        serverSocket.close();
	        System.exit(0);
	        
	    }
		
	}
    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
	/*
    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog(
            "Enter IP Address of a machine that is\n" +
            "running the date service on port 9090:");
        System.out.println("Client connecting to server at " + serverAddress + " in port 9091");
        Socket serverSocket = new Socket(serverAddress, 9093);
        System.out.println("Client receiving package from server.");
        BufferedReader input =
            new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        String answer = input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        serverSocket.close();
        System.exit(0);
        
    }*/
	
	
	
	/* v2
    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog(
            "Enter IP Address of a machine that is\n" +
            "running the date service on port 9090:");
        System.out.println("Client connecting to server at " + serverAddress + " in port 9091");
        Socket serverSocket = new Socket(serverAddress, numServerSocket);
        System.out.println("Client receiving package from server.");
        BufferedReader input =
            new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        String answer = input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        serverSocket.close();
        System.exit(0);
        
    }
    */
