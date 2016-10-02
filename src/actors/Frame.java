package actors;

import java.util.Date;

public class Frame
{
	//public static long infinito=Long.MAX_VALUE;
	private static boolean recibido;
	
	private static long timeout;
	
	private static int idFrame;
	
	private static char data;
	
	public Frame(int id) 
	{
		idFrame = id;
		data = ' ';
		recibido = false;
		timeout = Long.MIN_VALUE;
	}
	
	public static void setIdFrame(int dato){
		idFrame= dato;
	}
	public static int getIdFrame(){
		return idFrame;
	}
	
	public static void setData(char dato){
		data= dato;
	}
	public static char getData(){
		return data;
	}
	
	public static void setRecibido(boolean dato){
		recibido= dato;
	}
	public static boolean getRecibido(){
		return recibido;
	}
	
	public static void setTimeout(long dato){
		timeout= dato;
	}
	public static long getTimeout(){
		return timeout;
	}

}
