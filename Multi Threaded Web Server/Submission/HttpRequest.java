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
* HttpRequest class is used to handle each http request to the server.
* Each object runs in a separate thread and replies respective requests.
* @version 9.30 17 Jan 2012
* @author Gudivada Harsha Vardhan
*/

final class HttpRequest implements Runnable {
	final static String CRLF = "\r\n";	
	Socket socket;

    // Constructor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

    // Implement the run() method of the Runnable interface.
	public void run() {
		try {
		    processRequest();               
	    } catch (Exception e) {	   
		    System.out.println(e);
		    //e.printStackTrace();
	    }
    }
    
    
    /**
    * processRequest function reads the HTTP request message 
    * and replies to it in proper format
    */    
	private void processRequest() throws Exception {
	    
	    // Get a reference to the socket's input and output streams
        InputStream is = socket.getInputStream();
	    OutputStream s1out = socket.getOutputStream();
       	DataOutputStream os = new DataOutputStream (s1out);

	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    
	    // Get the request line of the HTTP request message.        
        String requestLine = br.readLine();
        String fileName = null;
                
        //if (requestLine == null){            
            //System.out.println("requestLine is null");
        //}        
                        
        StringTokenizer tokens = null;
        
        FileInputStream fis = null;
        boolean fileExists = false;
        File file = null;
        
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        
        //handle null request sent by chrome browser
        try {      
            tokens = new StringTokenizer(requestLine);
        } catch(NullPointerException e) {
            return;                 
        }
        
        tokens.nextToken(); 
        fileName = tokens.nextToken();
        
        //If no file is mentioned, return the default home page
        if (fileName.equals("/")) {
            fileName += "index.html";
        }
        
        fileName = "." + fileName;                
        file = new File(fileName);
        
        // Construct the response message.
        //If file can not be read, return forbidden
        if ( file.exists() && ( !file.canRead() || !file.isFile() ) ) {
             statusLine = "HTTP/1.0 " +"403 "+"Forbidden"+CRLF;
             contentTypeLine = "Content-type: "+"text/html"+CRLF;
             entityBody = "<HTML>" + 
		        "<HEAD><TITLE>Forbidden</TITLE></HEAD>" +
		        "<BODY>Forbidden</BODY></HTML>";
        } else {
            if (!file.exists()) {
                statusLine = "HTTP/1.0 " +"404 "+"Not Found"+CRLF;
                contentTypeLine = "Content-type: "+"text/html"+CRLF;
                entityBody = "<HTML>" + 
		        "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
		        "<BODY>Not Found</BODY></HTML>";
            } else {
                fileExists = true;
                statusLine = "HTTP/1.0 " +"200 "+"OK"+CRLF;
	            contentTypeLine = "Content-type: " + 
		        contentType( fileName ) + CRLF;		        
		        fis = new FileInputStream(fileName);
            }
        }
        
        // Send the status line.                      
        os.writeBytes(statusLine);       
        
        // Send the content type line.     
        os.writeBytes(contentTypeLine);

        // Send a blank line to indicate the end of the header lines.
        os.writeBytes(CRLF);
                
        // Send the entity body.        
        if (fileExists)	{
	        sendBytes(fis, os);
	        fis.close();
        } else {
	        os.writeBytes(entityBody);
        }

        // Close streams and socket.
        os.close();
        br.close();
        socket.close();
    }

    /**
    * sendBytes writes the requested file onto the socket's output stream
    */
    private static void sendBytes(FileInputStream fis, OutputStream os) 
    throws Exception
    {
       // Construct a 1K buffer to hold bytes on their way to the socket.
       byte[] buffer = new byte[1024];
       int bytes = 0;

       // Copy requested file into the socket's output stream.
       while ((bytes = fis.read(buffer)) != -1 ) {
          os.write(buffer, 0, bytes);
       }
    }
    
    /**
    * contentType will examine the extension of a file name and return a string
    * that represents it's MIME type.
    */
    private static String contentType(String fileName)
    {
    
	    if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
		    return "text/html";
	    } else if (fileName.endsWith(".gif")) {
	        return "image/gif";
	    } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
		    return "image/jpeg";
	    } else if (fileName.endsWith(".zip") || fileName.endsWith(".ZIP")) {
		    return "application/zip";
	    } else if (fileName.endsWith(".png") || fileName.endsWith(".PNG")) {
		    return "image/png";
	    } else if (fileName.endsWith(".pdf") || fileName.endsWith(".PDF")) {
		    return "application/pdf";
	    } else if (fileName.endsWith(".mp3") || fileName.endsWith(".mpeg")) {
		    return "audio/mpeg";
	    } else if (fileName.endsWith(".txt") || fileName.endsWith(".TXT")) {
		    return "text/plain";
	    } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
		    return "application/msword";
	    } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
		    return "application/vnd.ms-powerpoint";
	    } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
		    return "application/vnd.ms-excel";
	    }
	
	    return "application/octet-stream";
    }

}
    
