//Network programming - write programs that execute across multiple devices connected via ntwork
//These Java classes abstract the low level communication details
import java.net.*;
import java.io.*;
import java.util.*;

//Using https://docs.oracle.com/javase/tutorial/networking/datagrams/examples/QuoteServerThread.java 
//as a reference 
public class UdpServer extends Thread {
   private ServerSocket serverSocket;
   //move this out to own class?
   private Map<String, String> keyValueVault;

   private static final String PUT = "PUT";
   private static final String GET = "GET";
   private static final String DELETE = "DELETE";
   private static final int BUFFER_SIZE = 256;

   private boolean moreRequests = true;

   public UdpServer(int port) throws IOException {
      //Step to establishing a TCP connection. Denotes which port number communication is to occur on 
      serverSocket = new ServerSocket(port);
      keyValueVault = new HashMap<String, String>();
   }

   public void run() {
       try {
          DatagramSocket serverSocket = new DatagramSocket(80);
           //For this project you will set up your server to respond to requests
          System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
          
          while(moreRequests) {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            //https://www.mkyong.com/java/how-do-convert-byte-array-to-string-in-java/
            final String command = new String(buffer);
            final String[] commandComponents = command.split(" ");
            final String operation = commandComponents[0];
            final String operand = commandComponents[1]; 

            final byte[] response = new byte[BUFFER_SIZE];
            String responseAsString = null;
            if(operation.equals(PUT)) {
              int delimiter = operand.indexOf(',');             
              final String key =  operand.substring(0, delimiter);
              final String value = operand.substring(delimiter + 1, operand.length());
              keyValueVault.put(key, value);
              responseAsString = "Key: " + key + " Value: " + value + " was successfully put in key value vault";
            } else if(operation.equals(GET)) {
              String value = keyValueVault.get(operand);
              responseAsString = "GET " + operand + " Result: " + value;
            } else { 
              keyValueVault.remove(operand);
              responseAsString = "Value associated with key " + operand + " was successfully deleted";
            }
            response = responseAsString.getBytes();
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();   
            packet = new DatagramPacket(response, response.length, clientAddress, clientPort);
            serverSocket.send(packet);
         }
         }catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e) {
            e.printStackTrace();
            moreRequests = false;
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
         Thread t = new UdpServer(port);
         t.start();
      }catch(IOException e) {
         e.printStackTrace();
      }
   }
}
