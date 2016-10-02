package actors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class FileHandler
{
	
	public FileHandler()
	{
		// TODO Auto-generated constructor stub
		
	}


    
    public void writeUsingBuffer(String mycontent, String filePath) throws IOException
    {
    	BufferedWriter bw = null;
        try
        {
		  	 
		      //Specify the file name and path here
		  	  File file = new File(filePath);
		
		  	  if (!file.exists()) 
		  	  {
		  	     file.createNewFile();
		  	  }
		
		  	  FileWriter fw = new FileWriter(file);
		  	  bw = new BufferedWriter(fw);
		  	  bw.write(mycontent);
		            System.out.println("File written Successfully");

        } catch (IOException ioe) 
        {
        	ioe.printStackTrace();
  	    }
	  	finally
	  	{ 
	  	   try
	  	   {
	  	      if(bw!=null)
	  	    	  bw.close();
	  	   }catch(Exception ex){
	  	       System.out.println("Error in closing the BufferedWriter"+ex);
	  	    }
	  	}
    }
    
    public String readUsingBuffer(String filePatch) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(filePatch));
        
        String line = br.readLine();
        String result=line;
        //como se supone que todos los datos vienen juntos talvez no sea necesario el ciclo
        while(line != null)
        {
            System.out.println(line);
            line = br.readLine();
            result = result + line;
        }
        br.close();
        return result;
    }
}
