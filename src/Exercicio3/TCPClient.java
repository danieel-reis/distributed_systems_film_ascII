package Exercicio3;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class TCPClient {

    public static void main(String args[]) {
        // arguments supply message and hostname
        Socket s = null;
        try {
            int serverPort = 7896;
            String address = "localhost";
            int idMessage = 0;
            int qtdMessage = 20000;
            int sizePackage = 2500;
            String message = "";
            
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
                s = new Socket(address, serverPort);
                message = transmited.get(i);

                long timeI = System.currentTimeMillis();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                out.writeUTF(message);          // UTF is a string encoding see Sn. 4.4
                String data = in.readUTF();	    // read a line of data from the stream
                long timeF = System.currentTimeMillis();

                System.out.println("Received: " + data);
                received.add(data);
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

        } catch (UnknownHostException e) {
            System.out.println("Socket:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }
            }
        }
    }
}
