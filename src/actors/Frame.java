package actors;

import java.util.Date;

public class Frame
{
	//public static long infinito=Long.MAX_VALUE;
	private boolean recibido;
	
	private long timeout;
	
	private int idFrame;
	
	private char data;
	
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
