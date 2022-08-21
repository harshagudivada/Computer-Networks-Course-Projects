/***************************************************
* Title: proxyThread.java
*
* Author: N.Hemanth (CS09B024), G.Harsha Vardhan (CS09B011), IITH
*
* Date: 09/03/2012
*
* Purpose/Description: Demonstration of relaying between clients and the remote
* proxy server
*
* Compilation/Usage: Compile as given below
* javac proxyThread.java
*
****************************************************/

import java.net.* ;
import java.io.* ;
import java.lang.* ;
import java.util.* ;

/**
* proxyThread class is used to relay between clients and the remote proxy server
* @version 9:00PM 9th Mar 2012
* @author Gudivada Harsha Vardhan, Narayanam Hemanth
*/


class proxyThread extends Thread {

    Socket incoming, outgoing;
    
    //Thread constructor
    proxyThread(Socket in, Socket out){
        incoming = in;
        outgoing = out;
    }
    
    //Overwritten run() method of thread -- does the data transfers
    
    public void run(){

        byte[] buffer = new byte[60];
        int numberRead = 0;
        OutputStream ToClient;
        InputStream FromClient;
      
        //Relay from input stream to output stream using intermediate buffer
        try{
            ToClient = outgoing.getOutputStream();      
            FromClient = incoming.getInputStream();
          
            while( true){
		        numberRead = FromClient.read(buffer, 0, 50);	              
		       
		 		//close the connections at the end of input read from input stream
			    if(numberRead == -1){
			        incoming.close();
			        outgoing.close();
			    }

			    ToClient.write(buffer, 0, numberRead);
	        }

        }
      catch(IOException e) {}
      catch(ArrayIndexOutOfBoundsException e) {}
      
    }
    
}
