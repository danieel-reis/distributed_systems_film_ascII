package Exercicio1;

import java.net.*;
import java.io.*;

public class UDPClient {
    
    private static boolean flag_break = false;
    private static int sizePackage = 2500;

    private static void executableAll(int sizePackage, int serverPort, String address) throws InterruptedException {
        int qtdPackage;
        for (qtdPackage = 1; qtdPackage < 1000; qtdPackage += 50) {
            System.out.println("SizePackage: " + sizePackage + " - qtdPackage: " + qtdPackage);
            executable(sizePackage, address, serverPort, qtdPackage);
            Thread.sleep(1000);
        }
        flag_break = true;
        executable(sizePackage, address, serverPort, 1); // Envia um pacote indicando que já acabou a transmissão
    }

    private static void executable(int sizePackage, String address, int serverPort, int qtdPackage) {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] m = new byte[sizePackage];
            // Preenche a mensagem toda com zeros (todos bytes serão zeros na mensagem)
            for (int i = 0; i < sizePackage; i++) {
                m[i] = '0';
            }
            InetAddress aHost = InetAddress.getByName(address);
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

            for (int i = 0; i < qtdPackage; i++) {
                if (i == 0 && flag_break == false) {
                    m[0] = '1';
                } else if (i == 0 && flag_break == true) {
                    m[0] = '2';
                } else {
                    m[0] = '0';
                }
//                System.out.println("    Send Package:" + (i + 1));
                aSocket.send(request);
            }

            // Não quero essa parte porque não quero receber a mensagem de volta
//            byte[] buffer = new byte[1000];
//            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
//            aSocket.receive(reply);
//            System.out.println("Reply: " + new String(reply.getData()));
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

    public static void main(String args[]) throws InterruptedException {
        int serverPort = 6789;
        String address = "localhost";
        executableAll(sizePackage, serverPort, address);
    }
}
