package actors;

import java.util.Date;

public class Frame
{
	//public static long infinito=Long.MAX_VALUE;
	private static boolean recibido;
	
	private static long timeout;
	
	private static int idFrame;
	
	private static char data;
	
	public Frame(int id, long time) 
	{
		idFrame = id;
		data = ' ';
		recibido = false;
		timeout = time;
	}
	
	public void setIdFrame(int dato){
		idFrame= dato;
	}
	public int getIdFrame(){
		return idFrame;
	}
	
	public void setData(char dato){
		data= dato;
	}
	public char getData(){
		return data;
	}
	
	public void setRecibido(boolean dato){
		recibido= dato;
	}
	public boolean getRecibido(){
		return recibido;
	}
	
	public void setTimeout(long dato){
		timeout= dato;
	}
	public long getTimeout(){
		return timeout;
	}

}
