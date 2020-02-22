package Exercicio2;

import java.net.*;
import java.io.*;
import sun.rmi.transport.DGCAckHandler;

public class UDPServer {

    private static int sizePackage = 2500;

    public static void main(String args[]) throws IOException {

        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(6789);
            // create socket at agreed port
            
            String received = "";
            
            do {
                byte[] buffer = new byte[2500];
                
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
                received = new String(request.getData());
                
                System.out.println("    Received: " + received);
//                System.out.println("NumPacotes: " + qtdPackageReceived);

                aSocket.send(reply);
            } while(!received.substring(0,4).equals("exit"));

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }
}
