package actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;


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
		public static int num1eroVentana=0; //Usuario debe poder meterlo
		public static int totalFrames=0; //Programa debe cambiarlo al leer los datos
		
		public static LinkedList <Frame> colaPendientes = new LinkedList<Frame>();
		public static LinkedList <Frame> colaVentana = new LinkedList<Frame>();
		
		
		public static void ponerDatosEnFrames(String datos){
			totalFrames=datos.length();
			for(int i=0; i<totalFrames; i++){
	              Frame a = new Frame(i);
	              char d=datos.charAt(i);
	              a.setData(d);
	              colaPendientes.add(a);
	         }
		}
		
		
		public static void creandoEstrucNec(String datos){
			ponerDatosEnFrames(datos);
			int numElemPasar=0;

			if(totalFrames>numElemVentana){
				numElemPasar=numElemVentana;
			}else {
				numElemPasar=totalFrames;
			}
			for(int i=0; i<numElemPasar; i++){
				Frame a = colaPendientes.pop();
				colaVentana.add(a);
			}
		}
		
		public static int calcVentanaPendientes(){
			return totalFrames-num1eroVentana;
		}
		//{}
		public static void moverVentana(){
			if (colaVentana.getFirst().getRecibido()==true){
				colaVentana.pop();
				Frame a = colaPendientes.pop();
				colaVentana.add(a);
				num1eroVentana=num1eroVentana+1;
				moverVentana();
			}
		}
		
		
		
		

		
		public static void main(String[] args) throws IOException {
			String datosEntrada = fh.readUsingBuffer(filePath);
	        
			String serverAddress = "localhost";//Mejor mantenerlo en local host para que funcione bien.


			
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

