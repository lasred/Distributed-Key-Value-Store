import java.net.*;
import java.io.*;
//Refactor out redudancy later
public class TcpClient {
 
   //How is this thing supposed to look?
   //Each java to send a request?
   public static void main(String [] args) {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      try {
         String sCurrentLine;
         System.out.println("Connecting to " + serverName + " on port " + port);
         //Socket - provides communication mechanism between two computers over TCP
         //TCP - two way communicatio protocold
         //Socket constructor tries to connect to client via server name and port 
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         //This is what im talking about. Nobody fucks with the printwriter
         PrintWriter out = new PrintWriter(outToServer);
         //The client should read commands from a script that contains 
         //at least 50 distinct GET/PUT/DELETE transactions on key-value pairs.
         BufferedReader transactionReader = 
             new BufferedReader(new FileReader(new File("transactions.txt")));
         while ((sCurrentLine = transactionReader.readLine()) != null) {
            System.out.println("Requested Operation to Server: " + sCurrentLine);
            out.println(sCurrentLine);
         }
         //working it out. ITs all in a cache, and we need to flush its ass out.
         out.flush();
         BufferedReader in = new BufferedReader(
                   new InputStreamReader(new DataInputStream(client.getInputStream())));
         String cLine = null;
         while((cLine = in.readLine()) != null) {
            System.out.println(cLine);
         }
         client.close();
      }catch(IOException e) {
         e.printStackTrace();
      }
   }
}