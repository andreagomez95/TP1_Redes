package actors;

import java.util.Date;

public class Frame
{

	private boolean recibido;
	
	private long timeout;
	
	private int idFrame;
	
	private char data;
	
	public Frame(int id, long time) 
	{
		recibido = false;
		timeout = time;
		idFrame = id;
		data = ' ';
	}

}
