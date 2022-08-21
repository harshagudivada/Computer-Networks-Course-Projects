/**************************************************
* Title: EmailSender.java
* Author: Gudivada Harsha Vardhan
* Date: 13/02/2012
* Purpose/Description: Demonstration of Email App
* Compilation/Usage: Compile as given below
* javac EmailSender
* Then Execute it on command prompt as given below
* java EmailSender
*****************************************************/

import java.io.*;
import java.net.*;

/**
* EmailSender Class contains the main function of this file. It establishes a 
* TCP connection with the specified mail server.It asks you the Sender email Id,
* receiver's email Id, subject and content in the message.
*/

public class EmailSender
{
   public static void main(String[] args) throws Exception
   {
   
      // Establish a TCP connection with the mail server.
      Socket socket = new Socket("alt4.aspmx.l.google.com",25);

      // Create a BufferedReader to read a line at a time.
      InputStream is = socket.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);

      // Read greeting from the server.
      String response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("220")) {
         throw new Exception("220 reply not received from server.");
      }

      // Get a reference to the socket's output stream.
      OutputStream os = socket.getOutputStream();
      
      InputStreamReader isr2 = new InputStreamReader(System.in);
      BufferedReader br2 = new BufferedReader(isr2);      
      System.out.print("HELO: ");
      String input = br2.readLine();
      
      // Send HELO command and get server response.
      String command = "HELO " + input + "\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("250")) {
         throw new Exception("250 reply not received from server.");
      }

      // Send MAIL FROM command.
      System.out.print("MAIL FROM: ");
      String fromId = br2.readLine();
      command = "MAIL FROM: <"+ fromId +">\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("250")) {
         throw new Exception("250 reply not received from server.");
      }

      // Send RCPT TO command.
      System.out.print("RCPT TO: ");
      String toId = br2.readLine();
      command = "RCPT TO: <"+ toId +">\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("250")) {
         throw new Exception("250 reply not received from server.");
      }

      
      // Send DATA command.
      command = "DATA\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("354")) {
         throw new Exception("354 reply not received from server.");
      }

      // Send message data.
      System.out.print("From: ");
      input = br2.readLine();
      
      String message = "From: "+ input + "  " + fromId +"\n";
      
      System.out.print("To: ");
      input = br2.readLine();
      
      message = message + "To: " + input + "  " + toId + "\n";
      
      message = message + "Date: Mon,13 Feb 2012 22.09.23 -0500\n";
      
      System.out.print("Subject: ");
      input = br2.readLine(); 
                       
      message =  message + "SUBJECT: " + input + "\n";
      
      System.out.println("Enter the message: ");
      while(  !( (input = br2.readLine()).equals(".") ) ){
         message = message + input + "\n";
      }
      System.out.print(message);
      os.write(message.getBytes("US-ASCII"));
      
      
      // End with line with a single period.
      message = "\r\n.\r\n";
      System.out.print(message);
      os.write(message.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("250")) {
         throw new Exception("250 reply not received from server.");
      }
      
      // Send QUIT command.
      command = "QUIT\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("221")) {
         throw new Exception("221 reply not received from server.");
      }
   }
}

