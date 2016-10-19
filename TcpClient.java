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
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         //The client should read commands from a script that contains 
         //at least 50 distinct GET/PUT/DELETE transactions on key-value pairs.
         BufferedReader transactionReader = 
             new BufferedReader(new FileReader(new File("transactions.txt")));
         while ((sCurrentLine = transactionReader.readLine()) != null) {
            System.out.println(sCurrentLine);
            out.writeUTF(sCurrentLine);
         }

         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         
         System.out.println("Server says " + in.readUTF());
         client.close();
      }catch(IOException e) {
         e.printStackTrace();
      }
   }
}