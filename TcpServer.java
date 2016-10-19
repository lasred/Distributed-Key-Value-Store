//Network programming - write programs that execute across multiple devices

import java.net.*;
import java.io.*;
import java.util.*;

public class TcpServer extends Thread {
   private ServerSocket serverSocket;
   //move this out to own class?
   private Map<String, String> keyValueVault;

   private static final String PUT = "PUT";
   private static final String GET = "GET";
   private static final String DELETE = "DELETE";
   public TcpServer(int port) throws IOException {
      serverSocket = new ServerSocket(port);
      keyValueVault = new HashMap<String, String>();
   }

   public void run() {
      while(true) {
         try {
            System.out.println("Waiting for client on port " + 
               serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            System.out.println(in.available());
            while(in.available() > 0) {
                final String command = in.readUTF();
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
                } else if(operation.equals(GET)) {
                     String value = keyValueVault.get(operand);
                       System.out.println("GET " + operand + " Result: " + value);
                } else { 
                       keyValueVault.remove(operand);
                }
            }
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
               + "\nGoodbye!");
            server.close();
            
         }catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e) {
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
//Compile the client and the serve