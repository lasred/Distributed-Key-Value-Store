/*
     Datagram Sockets - Java's mechanism for network communication via UDP
     Difference between  UDP and TCP - UDP. you just send datagrams to some IP address
        on the network. No guarantee if packets arrivve or in what order they arrive 
*/
public class UdpClient {
 
   //How is this thing supposed to look?
   //Each java to send a request?
   public static void main(String [] args) {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      try {
         String sCurrentLine;
         System.out.println("Connecting to " + serverName + " on port " + port " over UDP");
         DatagramSocket client = new DatagramSocket();

         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         //Represents IP address
         InetAddress receiverAddress = InetAddress.getLocalHost();
         //The client should read commands from a script that contains 
         //at least 50 distinct GET/PUT/DELETE transactions on key-value pairs.
         BufferedReader transactionReader = 
             new BufferedReader(new FileReader(new File("transactions.txt")));
         while ((sCurrentLine = transactionReader.readLine()) != null) {
            byte[] buffer = sCurrentLine.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress, 80);
            client.send(packet);
         }

         InputStream inFromServer = client.getInputStream();
         while()
         DataInputStream in = new DataInputStream(inFromServer);         
         System.out.println("Server says " + in.readUTF());
         client.close();
      }catch(IOException e) {
         e.printStackTrace();
      }
   }
}