package actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.net.ServerSocket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class Client {

		public static boolean hayACKnuevo=false;
		public static boolean debugMode=false;
		public static FileHandler fh = new FileHandler();
		public static String filePath = "C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt";
		
		private static PrintWriter out;// = new PrintWriter(clientSocket.getOutputStream(), true);
		

		public static String serverAddress="";
		
		public static Socket serverSocket; // = new Socket(serverAddress, port);
		
		public static int port;//=9093; //Usuario debe poder meterlo
		public static int windowSize; //Usuario debe poder meterlo
		public static int num1eroVentana=0; //Usuario debe poder meterlo
		public static int totalFrames=0; //Programa debe cambiarlo al leer los datos
		public static long timeOut;//=1000; //Usuario debe poder meterlo
		public static long inicializarTimeOut=Long.MIN_VALUE; //De este modo siempre sale que vencio el timeout si no se ha enviado el dato antes.
		
		public static LinkedList <Frame> colaPendientes = new LinkedList<Frame>();
		public static LinkedList <Frame> colaVentana = new LinkedList<Frame>();
		
		
		public static void ponerDatosEnFrames(String datos){
			totalFrames=datos.length();
			Frame a;
			for(int i=0; i<totalFrames; i++){
	              a = new Frame(i,inicializarTimeOut);
	              char d=datos.charAt(i);
	              a.setData(d);
	              colaPendientes.add(a);
	         }
		}
		
		
		public static void creandoEstrucNec(String datos){
			ponerDatosEnFrames(datos);
			int numElemPasar=0;

			if(totalFrames>windowSize){
				numElemPasar=windowSize;
			}else {
				numElemPasar=totalFrames;
			}
			Frame a;
			for(int i=0; i<numElemPasar; i++){
				a = colaPendientes.pop();
				colaVentana.add(a);
			}
		}
		
		public static int calcVentanaPendientes(){
			int numElemV=totalFrames-num1eroVentana;
			if(numElemV<windowSize){
				return numElemV;
			}
			else{
				return windowSize;
			}	
		}
		
		
		
		
		//{}
		public static void moverVentana(){
			Frame a;
			while (colaVentana.getFirst().getRecibido()==true){
				colaVentana.pop();
				a = colaPendientes.pop();
				colaVentana.add(a);
				num1eroVentana=num1eroVentana+1;
			}
		}
		
		
		
		public static void CicloRevisarTimeOut(){
			//if(hayACKnuevo){
				moverVentana();
			//}
			int r=calcVentanaPendientes();
			String segmento="";
			Frame a;
			long time;
			for(int i=0; i<r; i++){
				a=colaVentana.get(i);
				time=TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
				if((a.getTimeout()<time)&&(!a.getRecibido())){
					//segmento=""; //---
					segmento=Integer.toString(a.getIdFrame())+":"+a.getData();
					enviarDatos(segmento);
					a.setTimeout((TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis())+timeOut));
				}
	              
	         }
		}
		
		public static void enviarDatos(String datos)/*  throws IOException  */{//Hay que cambiarlo pero funciona temporalmente, creo
			
			System.out.println("Sending segment.");
			out.println(datos);
			out.flush();
			System.out.println("Segment sent.");
		}
		
		public static void setVariables(){
			Scanner scan = new Scanner(System.in);
			System.out.println("Please introduce the desired window size: ");
			windowSize = scan.nextInt();
			
			System.out.println("Please introduce the file path: ");
			filePath = scan.nextLine();
			
			System.out.println("Please introduce the port you want to connect to: ");
			port = scan.nextInt();
			
			System.out.println("Do you want to run the simulation in debug mode? (y/n): ");
			String i = scan.nextLine();
			if((i.charAt(0)=='y')||(i.charAt(0)=='Y')){
				debugMode= true;
			}
			
			System.out.println("Please introduce the desired timeout: (in ms)");
			timeOut = scan.nextInt();
			
			scan.close();
		}
		

		
		public static void main(String[] args) throws IOException {
			setVariables();
			String datosEntrada = fh.readUsingBuffer(filePath);
	        
	        creandoEstrucNec(datosEntrada);
	        
	        serverAddress = "localhost"; //Mejor mantenerlo en local host para que funcione bien.
	        System.out.println("Client connecting to server at " + serverAddress + " in port "+port);
	        serverSocket = new Socket(serverAddress, port);
	        out = new PrintWriter(serverSocket.getOutputStream(), true);
	        //enviarDatos("13:2");// no fun bien bien. Creo que es porque no hay un listener del otro lado
	        
	        serverSocket.close();
	        System.exit(0);

	    }
		
	}


	/*int p=e.cola.poll();
       if(e.esperando==p){
            e.esperando++;
            e.ack=e.esperando;
            e.ultimFramReci.add(p);
            e.bRecibioBien=e.bRecibioBien+1;
            if(e.ultimFramReci.size()>20){
                e.ultimFramReci.poll();
            }
        }
        */
//http://www.dreamincode.net/forums/topic/113638-sending-a-string-through-a-socket/
/*
 * ponerDatosEnFrames(datosEntrada);
	        System.out.println("Client connecting to server at " + serverAddress + " in port 9091");
	        serverSocket = new Socket(serverAddress, port);
	        System.out.println("Client receiving package from server.");
	        BufferedReader input =
	            new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
	        String answer = input.readLine();
	        JOptionPane.showMessageDialog(null, answer);
	        serverSocket.close();
	        System.exit(0);*/
 

/*public static void enviarDatos(String datos)/*  throws IOException  * /{//Hay que cambiarlo pero funciona temporalmente, creo

System.out.println("Sending segment.");
PrintWriter out;
try {
	out = new PrintWriter(serverSocket.getOutputStream(), true);
	out.println(datos);
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
    */


/*
System.out.println("Client connecting to server at " + serverAddress + " in port 9091");

	serverSocket = new Socket(serverAddress, port);

System.out.println("Client receiving package from server.");


	BufferedReader input = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

	String answer = input.readLine();

JOptionPane.showMessageDialog(null, answer);

	serverSocket.close();

System.exit(0);
}*/