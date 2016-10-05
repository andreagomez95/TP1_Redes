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
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */






public class Client {
		//Variables globales
		public  boolean hayACKnuevo=false;
		public  boolean debugMode=false;
		public  FileHandler fh = new FileHandler();
		public  String filePath = "C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt";
		
		private  PrintWriter out;// = new PrintWriter(clientSocket.getOutputStream(), true);
		

		public  String serverAddress="";
		
		public  Socket serverSocket; // = new Socket(serverAddress, port);
		
		public  int port;//=9093; 
		public  int windowSize;
		public  int num1eroVentana=0; 
		public  int totalFrames=0; //Programa debe cambiarlo al leer los datos
		public  long timeOut;//=1000; 
		public  long inicializarTimeOut=Long.MIN_VALUE; //De este modo siempre sale que vencio el timeout si no se ha enviado el dato antes.
		
		public static LinkedList <Frame> colaPendientes = new LinkedList<Frame>();
		public static LinkedList <Frame> colaVentana = new LinkedList<Frame>();
		
		
		//public static LinkedList <Frame> colaPendientes;// = new LinkedList<Frame>();
		//public static LinkedList <Frame> colaVentana;// = new LinkedList<Frame>();
		
		public  BufferedReader in; 
		
		public Client() throws IOException{
			setVariables();
			String datosEntrada = fh.readUsingBuffer(filePath);
	        
			//colaPendientes= new LinkedList<Frame>();
			//colaVentana=new LinkedList<Frame>();
			
	        creandoEstrucNec(datosEntrada);
	        
	        
	        
	        //"C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt";
	        serverAddress = "localhost"; //Mejor mantenerlo en local host para que funcione bien.
	        System.out.println("Client connecting to server at " + serverAddress + " in port "+port);
	        serverSocket = new Socket(serverAddress, port);
	        out = new PrintWriter(serverSocket.getOutputStream(), true);
	        
	        
	        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
	        
	        hiloACK.start();
	        hiloEnvia.start();
	        
	        
	        serverSocket.close();
			
			
		}
		
		public  Thread hiloACK = new Thread () {
			  public void run () {
				  hiloRecibeACK();
				  try {
					this.finalize();
				  } catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }
			  }
		};
		public  Thread hiloEnvia = new Thread () {
			  public void run () {
				  hiloEnviaFrames();
				  try {
					  this.finalize();
				  } catch (Throwable e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
			  }
		};
		
		//Pide al usuario datos de como desea correr la simulacion
		public  void setVariables(){
			Scanner scan = new Scanner(System.in);
			System.out.println("Please introduce the desired window size: ");
			windowSize = scan.nextInt();
			scan.nextLine();
			
			System.out.println("Please introduce the file path: ");
			filePath = scan.nextLine();
			
			System.out.println("Please introduce the port you want to connect to: ");
			port = scan.nextInt();
			scan.nextLine();
			System.out.println("Do you want to run the simulation in debug mode? (y/n): ");
			String i = scan.nextLine();
			if((i.charAt(0)=='y')||(i.charAt(0)=='Y')){
				debugMode= true;
			}
			
			System.out.println("Please introduce the desired timeout: (in ms)");
			timeOut = scan.nextInt();
			
			scan.close();
		}
		
		
		//C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt
		//Mete los datos del archivo indicado en los diferentes frames
		public  void ponerDatosEnFrames(String datos){
			mostrarPendientes();
			mostrarVentana();
			totalFrames=datos.length()-4;//////////////////////////////////Revisar esto
			Frame a;
			for(int i=0; i<totalFrames; i++){
	              a = new Frame(i,inicializarTimeOut);
	              char d=datos.charAt(i);
	              a.setData(d);
	              a.setIdFrame(i);
	              colaPendientes.add(a);
	              System.out.println("Creando frame: " +i +":"+d);
	              mostrarPendientes();
	  			  mostrarVentana();
	              /*System.out.println("Creando frame: " +a.getIdFrame() +":"+a.getData());
	              System.out.println("Creando frame: " +colaPendientes.get(i).getIdFrame() +":"+colaPendientes.get(i).getData());
	              if(i!=0){
	              System.out.println("Creando frame: " +colaPendientes.get(i-1).getIdFrame() +":"+colaPendientes.get(i-1).getData());
	              }*/
	         }
			mostrarPendientes();
			mostrarVentana();
			System.out.println("Sale crear frames: ");
		}
		
		
		public  void creandoEstrucNec(String datos){
			ponerDatosEnFrames(datos);
			int numElemPasar=0;

			if(totalFrames>windowSize){
				numElemPasar=windowSize;
			}else {
				numElemPasar=totalFrames;
			}
			Frame a;
			Frame b;
			System.out.println("Metiendo frames en la ventana: ");
			for(int i=0; i<numElemPasar; i++){
				a = colaPendientes.pop();//.pollFirst();
				b=new Frame(a.getIdFrame(),inicializarTimeOut);
				System.out.print(b.getIdFrame()+": "+b.getData()+", ");
				colaVentana.addLast(b);
			}
			System.out.println( " ");
			mostrarPendientes();
			mostrarVentana();
		}
		
		public  int calcVentanaPendientes(){
			int numElemV=totalFrames-num1eroVentana;
			if(numElemV<windowSize){
				return numElemV;
			}
			else{
				return windowSize;
			}	
		}
		
		public  void mostrarVentana(){
			int j=colaVentana.size();
			
			System.out.println("Ventana: ");
			for(int i=0; i<j; i++){
				System.out.print(colaVentana.get(i).getIdFrame()+":"+colaVentana.get(i).getData()+", ");
			}
			System.out.println( " ");
		}
		
		public  void mostrarPendientes(){
			int j=colaVentana.size();
			System.out.println("Pendientes: ");
			for(int i=0; i<j; i++){
				System.out.print(colaPendientes.get(i).getIdFrame()+":"+colaPendientes.get(i).getData()+", ");
			}
			System.out.println( " ");
		}
		
		
		//{}
		public  void moverVentana(){
			Frame a;
			while (colaVentana.getFirst().getRecibido()==true){
				colaVentana.pop();
				a = colaPendientes.pop();//.pop();
				colaVentana.addLast(a);
				num1eroVentana=num1eroVentana+1;
			}
			mostrarVentana();
		}
		
		
		
		public  void CicloRevisarTimeOut(int pendientes){
			//if(hayACKnuevo){
				moverVentana();
			//}
			//int r=pendientes;//calcVentanaPendientes();
			String segmento="";
			Frame a;
			long time;
			for(int i=0; i<pendientes; i++){
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
		
		public  void enviarDatos(String datos)/*  throws IOException  */{//Hay que cambiarlo pero funciona temporalmente, creo
			
			System.out.println("Sending segment.");
			out.println(datos);
			out.flush();
			System.out.println("Segment sent.");
		}
		
		//{}
		public  void hiloEnviaFrames(){
			int pendientes=calcVentanaPendientes();
	        while (pendientes!=0){
	        	CicloRevisarTimeOut(pendientes);
	        }
		}

		public  void hiloRecibeACK(){
			//int pendientes=calcVentanaPendientes();
			int ack=-1;
			int index=0;
			Frame a;
	        while (calcVentanaPendientes()!=0){
	        	//CicloRevisarTimeOut(pendientes);
	        	try {
					String input = in.readLine();
					ack=Integer.parseInt(input);
					index=ack-num1eroVentana;
					if(index>0){
						a=colaVentana.get(index);
						if(a.getIdFrame()==ack){
							a.setRecibido(true);
							hayACKnuevo=true;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
		
		public static void main(String[] args) throws IOException {
			Client cliente= new Client();
			
	        System.exit(0);

	    }
		
	}
