package Exercicio1;

import Graficos.SerieDuasLinhas;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import org.jfree.ui.RefineryUtilities;

public class UDPServer {

    private static ArrayList<Long> packagesTransmited = new ArrayList<Long>();
    private static ArrayList<Long> packagesReceived = new ArrayList<Long>();
    private static int sizePackage = 2500;

    private static void createGraphic() throws IOException {
        // Gera grafico de relatorio
        String name_img = "graphic_transmitedXreceived_packages-" + sizePackage + "bytes_UTP.png";
        int dimension_x = 925;
        int dimension_y = 400;
        String title = "Transmissão x Recepção de pacotes com " + sizePackage + " bytes";
        String nome_linha1 = "Transmited";
        String nome_linha2 = "Received";
        String nome_x = "Transmissão";
        String nome_y = "Recepção";
        ArrayList<String> names = new ArrayList<String>();
        for (int g = 1; g <= 1000; g += 50) {
            String s = "";
            s += g;
            names.add(s);
        }
        final SerieDuasLinhas demo = new SerieDuasLinhas(nome_linha1, nome_linha2, name_img,
                title, nome_x, nome_y, names, packagesTransmited, packagesReceived, dimension_x, dimension_y);

        // Mostra o gráfico na tela
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

    public static void main(String args[]) throws IOException {
        int qtdMaxPackage = 1000;
        int qtdMinPackage = 1;
        int qtdPackage = qtdMinPackage;
        int qtdPackageReceived = 0;

        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(6789);
            // create socket at agreed port
            byte[] buffer = new byte[10000];

            while (true) {

                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                qtdPackageReceived++;

                //Nao preciso dele porque não preciso responder
//                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
//                        request.getAddress(), request.getPort());
                String received = new String(request.getData());
//                System.out.println("    Received: " + received);
                // Se o pacote começar com 1, refere-se a primeiro pacote, então voltar o contador
                if (received.charAt(0) == '1') {
                    packagesTransmited.add(Long.parseLong(String.valueOf(qtdPackage)));
                    packagesReceived.add(Long.parseLong(String.valueOf(qtdPackageReceived)));
                    System.out.println("Package size: " + sizePackage + " - Packages transmited: " + qtdPackage + " - Packages received: " + qtdPackageReceived);
                    qtdPackageReceived = 50;
                    qtdPackage += 50; // Serão enviados 50 pacotes a mais do que o anterior
                }
                if (received.charAt(0) == '2') {
                    break;
                }
//                System.out.println("NumPacotes: " + qtdPackageReceived);

                //aSocket.send(reply);
                if (qtdPackage == qtdMaxPackage) {
                    qtdPackage = qtdMinPackage;
                }
            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            createGraphic();
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }
}
