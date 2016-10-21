//Network programming - write programs that execute across multiple devices connected via ntwork
//These Java classes abstract the low level communication details
import java.net.*;
import java.io.*;
import java.util.*;

public class TcpServer extends Thread {
   private ServerSocket serverSocket;jpojpojp
   //move this out to own class?
   private Map<String, String> keyValueVault;

   private static final String PUT = "PUT";
   private static final String GET = "GET";
   private static final String DELETE = "DELETE";

   public TcpServer(int port) throws IOException {
      //Step to establishing a TCP connection. Denotes which port number communication is to occur on 
      serverSocket = new ServerSocket(port);
      keyValueVault = new HashMap<String, String>();
   }

   public void run() {
      while(true) {
         try {

           //For this project you will set up your server to respond to requests
          System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
          //When connection is made, server creates a socket object on its end. Talk through that socket
          //waits till a client connects to server 
          Socket server = serverSocket.accept();
          //server - new socket on serer's side that is connected to the client's socket
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            //Makes no sense to use datainputstream which is designed to read bytes. We want abstraction. Lines
            //important to know about this though. Play around with it though.s
            BufferedReader in = new BufferedReader(
                   new InputStreamReader(new DataInputStream(server.getInputStream())));
            
            PrintWriter out = new PrintWriter(server.getOutputStream());
            //available 
            String cLine = null;
            while((cLine = in.readLine()) != null) {
                final String command = cLine;
                final String[] commandComponents = command.split(" ");
                final String operation = commandComponents[0];
                final String operand = commandComponents[1]; 
                 //PUT 4,5
                //GET 4
                if(operation.equals(PUT)) {
                  int delimiter = operand.indexOf(',');             
                  final String key =  operand.substring(0, delimiter);
                  final String value = operand.substring(delimiter + 1, operand.length());
                  keyValueVault.put(key, value);
                  out.println("Key: " + key + " Value: " + value + " was successfully put in key value vault");
                } else if(operation.equals(GET)) {
                  String value = keyValueVault.get(operand);
                  out.println("GET " + operand + " Result: " + value);
                } else { 
                  keyValueVault.remove(operand);
                  out.println("Value associated with key " + operand + " was successfully deleted");
                }
                //Gotta put it here or else this client tries to read this shit too quickly
                out.flush();
            }
            System.out.println("got here2");

            out.flush();
            System.out.println("got here");
            server.close();
         }catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e) {
            e.printStackTrace();
            break;
         } catch(Exception e) {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public static void main(String [] args) {
      int port = Integer.parseInt(args[0]);
      try {
         Thread t = new TcpServer(port);
         t.start();
      }catch(IOException e) {
         e.printStackTrace();
      }
   }
}
