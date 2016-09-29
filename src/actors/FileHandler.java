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


    
    public void writeUsingBuffer() throws IOException
    {
    	BufferedWriter bw = null;
        try
        {
		  	 String mycontent = "This String would be written" +
		  	    " to the specified File";
		  	 
		      //Specify the file name and path here
		  	  File file = new File("C:/Users/USUARIO/Documents/sourceFile.txt");
		
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
    
    public void readUsingBuffer(String filePatch) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(filePatch));
        String line = br.readLine();
        while(line != null)
        {
            System.out.println(line);
            line = br.readLine();
        }
        br.close();
    }
}
