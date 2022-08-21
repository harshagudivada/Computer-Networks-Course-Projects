/*
* %W% %E% Gudivada Harsha Vardhan
*
* Copyright (c) 2012-2013 Gudivada Harsha Vardhan All Rights Reserved.
*
* This software is the confidential and proprietary information of 
* Gudivada Harsha Vardhan ("Confidential Information"). You shall not
* disclose such Confidential Information and shall use it only in
* accordance with the terms of the license agreement you entered into
* with Gudivada Harsha Vardhan.
*
* Gudivada Harsha Vardhan MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
* THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSOR IMPLIED, INCLUDING
* BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
* FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. Gudivada 
* Harsha Vardhan SHALL NOT BE LIABLEFOR ANY DAMAGES SUFFERED BY 
* LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS 
* SOFTWARE OR ITS DERIVATIVES.
*/

import java.io.* ;
import java.net.* ;
import java.util.* ;

/**
* WebServer class is used to create a Server socket listening for 
* connections. The main thread creates worker threads for each 
* connection request.
* @version 9.30 17 Jan 2012
* @author Gudivada Harsha Vardhan
*/


public final class WebServer {
    public static void main(String argv[]) throws Exception {	
    
        //Set the port number    
	int port = 6789;
	    
	// Establish the listen socket                             	    
        ServerSocket servSocket = new ServerSocket(port);
            
        // Process HTTP service requests in an infinite loop.
        while (true) {
            
        // Listen for a TCP connection request.
        Socket socket=servSocket.accept();
            
        // Construct an object to process the HTTP request message.
        HttpRequest request = new HttpRequest(socket);
            
        // Create a new thread to process the request.
        Thread thread = new Thread(request);
            
        // Start the thread.
        thread.start(); 	        
	
	}	
    }
}

