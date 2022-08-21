/***************************************************
* Title: HttpRequest.java
*
* Author: N.Hemanth (CS09B024), G.Harsha Vardhan (CS09B011), IITH
*
* Date: 09/03/2012
*
* Purpose/Description: Demonstration of parsing and response relaying in proxy
*
* Compilation/Usage: Compile as given below
* javac HttpRequest.java
*
****************************************************/


import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.lang.* ;


/**
* HttpRequest class is used to parse and modify the HTTP request as per 
* requirement and is forwarded to remote server. After getting the reply from 
* the server, it forwards the same to the client without any modification to the 
* client as if it was from the server.
* @version 9:00PM 9th Mar 2012
* @author Gudivada Harsha Vardhan, Narayanam Hemanth
*/


// Class HttpRequest
final class HttpRequest implements Runnable
{
	final static String CRLF = "\r\n";
	Socket socket;

	// Constructor
	public HttpRequest(Socket socket) throws Exception 
	{
		this.socket = socket;
	}
  
	// Implement the run() method of the Runnable interface.
	public void run()
	{
		try {		    
			   processRequest();
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	private void processRequest() throws Exception
	{
		// Get a reference to the socket's input and output streams.
		InputStream is = socket.getInputStream();
		OutputStream soco = socket.getOutputStream();
		DataOutputStream os = new DataOutputStream (soco);

		// Set up input stream filters.
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);

		// Get the request line of the HTTP request message.
		String requestLine = br.readLine();
		if (requestLine == null) {
			return;
		}
	
        String error = null;
        
        try{
		// Extract the filename from the request line.
		StringTokenizer tokens = new StringTokenizer(requestLine);
		if ( tokens.countTokens() != 3){
			sendError(os);			
		}
		else{
            // skip over the method, which should be "GET"
		    error = tokens.nextToken();  
		    if( !(error.equals("GET")) ){
		        sendError(os);
		    }
		    else{
				//token incoming HTTP request
				String fileName = tokens.nextToken();
		        String HttpVer = tokens.nextToken();
		        StringTokenizer chunk = new StringTokenizer(fileName, "/");
		        int l1 = (chunk.nextToken()).length();

		        l1 = l1 + 2;
		        String address = chunk.nextToken();
		        l1 = l1 + address.length();
		        String location = fileName.substring(l1);

		        String hostname = "";
		        String port = "80";      //default HTTP port

		        boolean b = address.indexOf(":") > 0;
		        if(b){
			        chunk = new StringTokenizer(address, ":");
			        hostname = chunk.nextToken();
			        port = chunk.nextToken();
		        }
		        else
			        hostname = address;

		        int portnum = new Integer(port);

                
                try{
					Socket clientSocket = null;
					
					//Establishing connection between proxy and the server
					clientSocket = new Socket(hostname, portnum);		            
		          
		        DataOutputStream outToServer = 
						   new DataOutputStream(clientSocket.getOutputStream());
		        InputStream inFromServer= clientSocket.getInputStream();
         
		        //Modify the HTTP request
     
		        String modReq = "GET " + location + " " + HttpVer + "\r\n";

		        outToServer.writeBytes( modReq );

		        // Get and forward the header lines.
		        String headerLine = null;

		        while ((headerLine = br.readLine()).length() != 0) {
			        if(headerLine.startsWith("Proxy-Connection")){
				        chunk = new StringTokenizer(headerLine, ":");
				        chunk.nextToken();
				        headerLine = ("Connection" + ":" + chunk.nextToken() );
			        }
			        outToServer.writeBytes(headerLine + "\r\n");
		        }

		        outToServer.writeBytes("\r\n");		        

		        sendBytes( inFromServer, os);

		        System.out.println("Data forwarded to Web Browser");

				//Closing the connections on the server side
		        outToServer.close();
        		inFromServer.close();
		        clientSocket.close();
        		}

				catch (UnknownHostException e) {
					//Test and make connection to remote host
					System.err.println("Error: Unknown Host " + proxy);
				}

        		catch (ConnectException e){
		            System.out.println("Error: " + e.getMessage());
					sendError(os);
		        }  
            }
        }
        }
        catch(NoSuchElementException e){
            System.out.println("Error: " + e.getMessage());
            sendError(os);
        }
        finally{
		// Close streams and socket.
		os.close();
		br.close();
		socket.close();
		}		
	}
    
    //send 504 error message when remote server refuses connections
    private static void sendError(DataOutputStream os) throws Exception {
        String CRLF = "\r\n";
        String err = "HTTP/1.1 " +"504 "+"Gateway Time-out"+ CRLF + 
                       "Content-type: "+"text/html"+ CRLF +
						"Connection: close" +CRLF + CRLF +
                       "<html><head>" +  "<title>ERROR: URL can't be retrived</title>" + 
                       "</head><body> <h1>Error</h1>" + 
                       "<p>The requested URL can't be retrieved" +
                       "<br /></p><hr> </body></html>";        
        os.writeBytes(err);               
        os.writeBytes(CRLF);
    }
        
	private static void sendBytes(InputStream isr, OutputStream os) 
	throws Exception
	{
	   // Construct a 1K buffer to hold bytes on their way to the socket.
	   byte[] buffer = new byte[1024];
	   int bytes = 0;

	   // Copy requested file into the socket's output stream.
	   while((bytes = isr.read(buffer,0,1024)) != -1 ) {
		  os.write(buffer, 0, bytes);
	   }
	}

}

