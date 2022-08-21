/***************************************************
* Title: WebProxy.java
*
* Author: N.Hemanth (CS09B024), G.Harsha Vardhan (CS09B011), IITH
*
* Date: 09/03/2012
*
* Purpose/Description: Demonstrartion of a webProxy
*
* Compilation/Usage: Compile as given below
* javac WebProxy.java

* then execute it on command prompt as given below
* java WebProxy
*
****************************************************/


import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.lang.* ;

/**
* WebProxy class is used to create a Server socket listening for 
* connections from clients. This web proxy handles 2 cases.
* 1.It can directly parse,understand and forward to the web server if this proxy
* has access to internet.
* 2. Else, if it did not have internet and under another proxy which has access
* to internet, it just relays the data between the clients and the proxy with 
* internet access.
* The main thread creates worker threads for each connection request. Hence it 
* is a "Multi threaded Web Proxy Server"
* @version 9:00PM 9th Mar 2012
* @author Gudivada Harsha Vardhan, Narayanam Hemanth
*/

// Class WebProxy
public final class WebProxy
{
	public static void main(String argv[]) throws Exception
	{
	    String proxy = null;
	    int proxyPort = -1;
	    boolean  proxySet  = false; //default false
		boolean error = false;

		// if conecting to remote proxy 
	    if(argv.length > 0){

	        proxySet = true;

			try
		    {
		        proxy = argv[0];
		        proxyPort = Integer.parseInt(argv[1]);
		    }
		    
		    catch(Exception e)
		    {
		        System.err.println("Error: " + e.getMessage() + "\n");
		        error = true;
		    }

			// Check for valid local and remote port, hostname not null

			//System.out.println("Checking: Port" + " 6789" + " to " + proxy + " Port " + proxyPort);

		    if(proxyPort <=0){
		        System.err.println("Error: Invalid Proxy Port Specification " + "\n");
		        error = true;
		    }
		    if(proxy == null){
		        System.err.println("Error: Invalid Host Specification " + "\n");
		        error = true;
		    }

			//If any errors so far, exit program
        
		    if(error)
		        System.exit(-1);

	    }

		// Set the port number
		int port = 6789;
      
		ServerSocket server = null;
		Socket sSocket, cSocket = null;

		// Establish the listen socket
		try{
			server = new ServerSocket( port );
		}
		catch(IOException e) {
	    	e.printStackTrace();
        }

		// Process HTTP service requests in an infinite loop
		while(true) {

			try{
				sSocket = server.accept();

				if(proxySet){
					//Create the 2 threads for the incoming and outgoing traffic of proxy server
					cSocket = new Socket(proxy, proxyPort); 

					proxyThread thread1 = new proxyThread(sSocket, cSocket);
					thread1.start();
				
					proxyThread thread2 = new proxyThread(cSocket, sSocket);
					thread2.start();
				}
				else{
					// Construct an object to process the HTTP request message.
					HttpRequest request = null;

					request = new HttpRequest( sSocket );

					// Create a new thread to process the request.
					Thread thread = new Thread(request);

					// Start the thread.
					thread.start();
				}
			}

			catch (UnknownHostException e) {
				//Test and make connection to remote host
				System.err.println("Error: Unknown Host " + proxy);
				System.exit(-1);
			} 

			catch(IOException e){
				System.exit(-2);//continue;
			}
		}
	}
}


