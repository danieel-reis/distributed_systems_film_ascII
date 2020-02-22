package Exercicio2;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class UDPClient {

    private static int sizePackage = 2500;
    private static int qtdMessage = 20000;
    private static Scanner in;

    private static void executable(int sizePackage, String address, int serverPort, int qtdMessage) {
        in = new Scanner(System.in);
        String message = "";

        System.out.print("Digite o timeout (ms): ");
        int timeOut = in.nextInt();
        in.nextLine(); // Limpar o buffer
        System.out.println();

        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            aSocket.setSoTimeout(timeOut); // Define timeout
            int idMessage = 0;

            ArrayList<String> transmited = new ArrayList<String>();
            ArrayList<String> received = new ArrayList<String>();
            String alphabeto[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " "};
            Random r = new Random();

            System.out.println("Generating messages...");
            for (int k = 0; k < qtdMessage; k++) {
                idMessage++;
                String messagee = "";
                for (int i = 0; i < sizePackage; i++) {
                    messagee += alphabeto[r.nextInt(alphabeto.length)];
                }
                transmited.add(idMessage + "-" + messagee);
            }
            transmited.add("exit");

            for (int i = 0; i < transmited.size(); i++) {

                byte[] m = new byte[sizePackage];
                message = transmited.get(i);
                m = message.getBytes();

                long timeI = System.currentTimeMillis();
                InetAddress aHost = InetAddress.getByName(address);
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                aSocket.send(request);

                byte[] buffer = new byte[2500];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);

                long timeF = System.currentTimeMillis();
                String receivedMessage = new String(reply.getData());
                received.add(receivedMessage);
                System.out.println("    Reply: " + receivedMessage);
                System.out.println("    Time: " + (timeF - timeI));
            }

            // Comparação
            int equalsYes = 0;
            int equalsNo = 0;
            for (int i = 0; i < (transmited.size() - 1) && i < received.size(); i++) {
//                System.out.println("Transmited: " + transmited.get(i));
//                System.out.println("Received: " + received.get(i));
                
                String tr[] = transmited.get(i).substring(0,10).split("-");
                String re[] = received.get(i).substring(0,10).split("-");

                if (tr[0].equals(re[0])) {
                    equalsYes++;
                } else {
                    equalsNo++;
                }
            }

            // Resultados
            System.out.println("Pacotes recebidos em ordem igual: " + equalsYes);
            System.out.println("Pacotes recebidos em ordem diferente: " + equalsNo);

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
        executable(sizePackage, address, serverPort, qtdMessage);
    }
}
