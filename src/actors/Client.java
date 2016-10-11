package actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.net.UnknownHostException;
//import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
//import java.net.ServerSocket;
import java.util.Scanner;
//import javax.swing.JOptionPane;

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
		
		
		public  BufferedReader in; 
		
		public Client() throws IOException{
			setVariables(true);
			//setVariables(false);
			String datosEntrada = fh.readUsingBuffer(filePath);
	        
	        creandoEstrucNec(datosEntrada);
	        
	        
	        
	        //"C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt";
	        serverAddress = "localhost"; //Mejor mantenerlo en local host para que funcione bien.
	        System.out.println("Client connecting to server at " + serverAddress + " in port "+port);
	        serverSocket = new Socket(serverAddress, port);
	        System.out.println("Client conneted to server");
	        out = new PrintWriter(serverSocket.getOutputStream(), true);
	        
	        
	        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
	        System.out.println("About to start both threads");

	        hiloRecibeACKEnviaDatos();
	        serverSocket.close();
			
			
		}
		
		//Pide al usuario datos de como desea correr la simulacion
		public  void setVariables(boolean prueba){
			
			Scanner scan = new Scanner(System.in);
			if(!prueba){
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
			} else {

				windowSize = 4;

				//filePath = "C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt";
				
				filePath = "C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt";//"C:/Users/USUARIO/git/TP1_Redes/datosTP1.txt";
				
				port = 9093;
				
				debugMode= true;
				
				timeOut = 10;
			}
			
			scan.close();
		}
		
		
		//C:/Users/DELL/Documents/GitHub/TP1_Redes/datosTP1.txt
		//Mete los datos del archivo indicado en los diferentes frames
		public  void ponerDatosEnFrames(String datos){
			mostrarPendientes();
			mostrarVentana();
			totalFrames=datos.length()-4;//////////////////////////////////Revisar esto
			Frame a;
			System.out.println("Datos en frames");
			for(int i=0; i<totalFrames; i++){
	              a = new Frame(i,inicializarTimeOut);
	              char d=datos.charAt(i);
	              a.setData(d);
	              a.setIdFrame(i);
	              colaPendientes.add(a);
	              System.out.println("Frame "+a.getData()+" : "+a.getIdFrame());
	         }
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
				a = colaPendientes.pop();
				b=new Frame(a.getIdFrame(),inicializarTimeOut);
				b.setData(a.getData());
				System.out.print(b.getIdFrame()+": "+b.getData()+", ");
				colaVentana.addLast(b);
			}
			System.out.println( " ");
			mostrarPendientes();
			mostrarVentana();
		}
		
		
		public  void mostrarVentana(){
			int j=colaVentana.size();
			if(j>0){
				System.out.println("Ventana: ");
				for(int i=0; i<j; i++){
					System.out.print(colaVentana.get(i).getIdFrame()+":"+colaVentana.get(i).getData()+", ");
				}
				System.out.println( " ");
			} else {
				System.out.print("Ventana vacia");
			}
		}
		
		public  void mostrarPendientes(){
			int j=colaPendientes.size();
			if(j>0){
				System.out.println("Pendientes: ");
				for(int i=0; i<j; i++){
					System.out.print(colaPendientes.get(i).getIdFrame()+":"+colaPendientes.get(i).getData()+", ");
				}
				System.out.println( " ");
			} else {
				System.out.print("Cola Pendientes vacia");
			}
		}
		
		
	/*	//{}
		public  void moverVentana(){
			Frame a;
			Frame b;
			if(colaVentana.size()>0){
				while ((colaPendientes.size()!=0)&&(colaVentana.getFirst()!=null) && (colaVentana.getFirst().getRecibido()==true)){
					colaVentana.pop();
					a = colaPendientes.pop();
					b=new Frame(a.getIdFrame(),inicializarTimeOut);
					b.setData(a.getData());
					System.out.print(b.getIdFrame()+": "+b.getData()+", ");
					colaVentana.addLast(b);
					num1eroVentana=num1eroVentana+1;
					mostrarVentana();
				}
				while ((colaVentana.getFirst()!=null && colaVentana.getFirst().getRecibido()==true)){
					a = colaVentana.pop();
					num1eroVentana=num1eroVentana+1;
					mostrarVentana();
				}
			}
			
		}*/
		//{}
				public  void moverVentana(){
					Frame a;
					Frame b;
					if(colaVentana.size()>0){
						while ((colaPendientes.size()!=0)&&(colaVentana.getFirst()!=null) && (colaVentana.getFirst().getRecibido()==true)){
							colaVentana.pop();
							a = colaPendientes.pop();
							b=new Frame(a.getIdFrame(),inicializarTimeOut);
							b.setData(a.getData());
							System.out.print(b.getIdFrame()+": "+b.getData()+", ");
							colaVentana.addLast(b);
							num1eroVentana=num1eroVentana+1;
							mostrarVentana();
						}
						//while ((num1eroVentana!=totalFrames)&&(colaVentana.getFirst()!=null && colaVentana.getFirst().getRecibido()==true)){
						while (colaVentana.size()!=0){
							if(colaVentana.getFirst().getRecibido()==true){
								a = colaVentana.pop();
								num1eroVentana=num1eroVentana+1;
								mostrarVentana();
							}else {
								break;
							}
						}
					}
					
				}
		
		public  void enviarDatos(String datos) {//Hay que cambiarlo pero funciona temporalmente, creo
			
			System.out.println("Sending segment.");
			System.out.println("Cliente enviando "+datos );
			out.println(datos);
			out.flush();
			System.out.println("Segment sent.");

		}

		
		public boolean recibirDatos(){

			int ack=-1;
			int index=0;
			Frame a;
			String input="";
			int i=0;
	        if (colaPendientes.size()!=0||colaVentana.size()!=0){

	        	try {
		        		input="";

		        		if (in.ready()){

		        			input = in.readLine();
		        		
			        		System.out.println("After reading next ack.");
							ack=ACKnumb(input);
							index=ack-num1eroVentana;
							if(index>-1){
								a=colaVentana.getFirst();
								int q=a.getIdFrame();
								index=ack-q;
								/*if (q<ack){
									
								}
								else */
								if (index>=0){
									a=colaVentana.get(index);
									if(a.getIdFrame()==ack){
										a.setRecibido(true);
										hayACKnuevo=true;
									}
								}
								//else if (q==ack){}
								
							}
							return true;
		        		} else {

		        			return false;
		        		}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Problema while recibeACK"+i);
					e.printStackTrace();
					System.out.println("Problema while recibeACK");
				}
	        	i=i+1;
	        	return false;
	        }
	        return false;
		}
		
		public boolean CicloRevisarTimeOut2(){

			moverVentana();

			String segmento="";
			Frame a;
			long time;
			for(int i=0; i<colaVentana.size(); i++){
				if(recibirDatos()){
					return false;
				}
				else {
					a=colaVentana.get(i);
					time=TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
					if((a.getTimeout()<time)&&(!a.getRecibido())){
						System.out.println("Vencio time out "+a.getIdFrame()+":"+a.getData());

						segmento=Integer.toString(a.getIdFrame())+":"+a.getData();
						enviarDatos(segmento);
						a.setTimeout((TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis())+timeOut));
						mostrarPendientes();
						mostrarVentana();
					}
		         }
			}
			return true;
		}
		
		
		public void hiloRecibeACKEnviaDatos(){
			System.out.println("Entro a hiloEnviaFrames");
			//int pendientes=calcVentanaPendientes();
	        while (colaPendientes.size()!=0||colaVentana.size()!=0){
	        	CicloRevisarTimeOut2();
	        }
		}
		
		public int ACKnumb(String ack){
			int trash=4;
			System.out.println("ACK que entro: "+ack);
			String result="";
			int numb=ack.length();
			for(int e=trash;e<numb;++e){
				result=result+ack.charAt(e);
			}
			System.out.println("Despues del for "+result );
			return Integer.parseInt(result);
		}
		
		
		public static void main(String[] args) throws IOException {
			Client cliente= new Client();
			
	        System.exit(0);

	    }
		
	}
/*
public boolean CicloRevisarTimeOut(){
	System.out.println("Revisar time out.");
	moverVentana();
	//int r=pendientes;//calcVentanaPendientes();
	String segmento="";
	Frame a;
	long time;
	for(int i=0; i<colaVentana.size(); i++){
		if(hayACKnuevo){
			return false;
		}
		else {
			a=colaVentana.get(i);
			time=TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
			if((a.getTimeout()<time)&&(!a.getRecibido())){
				System.out.println("Vencio time out "+a.getIdFrame()+":"+a.getData());
				//System.out.println("Enviando frame "+a.getIdFrame());
				segmento=Integer.toString(a.getIdFrame())+":"+a.getData();
				enviarDatos(segmento);
				a.setTimeout((TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis())+timeOut));
			}
         }
	}
	return true;
}
*/
/*
public  int calcVentanaPendientes(){
	int numElemV=totalFrames-num1eroVentana;
	if(numElemV<windowSize){
		return numElemV;
	}
	else{
		return windowSize;
	}	
}*/
/*
public  Thread hiloACK = new Thread () {
	  public void run () {
		  System.out.println("hiloRecibeACK() started");
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
		  System.out.println("hiloEnviaFrames() started");
		  hiloEnviaFrames();
		  try {
			  this.finalize();
		  } catch (Throwable e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
	  }
};
*/
/*
//{}
public  void hiloEnviaFrames(){
	System.out.println("Entro a hiloEnviaFrames");
	//int pendientes=calcVentanaPendientes();
    while (colaPendientes.size()!=0||colaVentana.size()!=0) {
    	CicloRevisarTimeOut();
    }
}

public  void hiloRecibeACK(){
	System.out.println("Entro a hiloRecibeACK");
	//int pendientes=calcVentanaPendientes();
	int ack=-1;
	int index=0;
	Frame a;
	String input="";
	int i=0;
    while (colaPendientes.size()!=0||colaVentana.size()!=0){
    	System.out.println("While pendientes"+i);
    	//CicloRevisarTimeOut(pendientes);
    	try {
        		input="";
        		System.out.println("Before reading next ack.");
        		while (input.length()==0){
        			//in.readLine()
        			input = in.readLine();
        		}
        		System.out.println("After reading next ack.");
				ack=Integer.parseInt(input);
				index=ack-num1eroVentana;
				if(index>-1){
					a=colaVentana.get(index);
					if(a.getIdFrame()==ack){
						a.setRecibido(true);
						hayACKnuevo=true;
					}
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Problema while recibeACK"+i);
			e.printStackTrace();
			System.out.println("Problema while recibeACK");
		}
    	i=i+1;
    }
}
*/