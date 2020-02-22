package TP;

import java.net.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    private String filmes[] = {"fly.txt", "stage.txt", "sw.txt", "train.txt"};
    private String comands[] = {"[CATALOGO]", "[ASSISTE]", "[SAIR]"};
    private static Scanner in;

    /**
     * Este método verifica se um valor lido é realmente do tipo inteiro. Senão,
     * indica erro.
     *
     * @param in Variável do tipo Scanner
     * @return Realiza um loop até que possa enviar um número que foi lido
     */
    private int readInteger(Scanner in) {
        int r = 0;
        boolean continua = false;
        do {
            try {
                r = in.nextInt();
                continua = false;
            } catch (InputMismatchException e) {
                System.out.println("Erro ao ler número! Digite novamente:");
                in.nextLine();
                continua = true;
            }
        } while (continua);
        return r;
    }

    /**
     * Método criado para imprimir os dados do menu.
     */
    private void printMenu() {
        System.out.println("-------------------------- MENU --------------------------");
        for (int i = 0; i < comands.length; i++) {
            System.out.println(comands[i]);
        }
        System.out.println("----------------------------------------------------------");
    }

    /**
     * Método criado para limpar o console no terminal
     */
    private void clearScreen() {
        System.out.printf("\33[2J"); // Codigo ansi para limpar a tela
    }

    /**
     * Este método verifica se uma string lida é do tipo inteiro. Senão, indica
     * erro.
     *
     * @param data String de dados a ser verificada
     * @return Valor lido ou -1
     */
    private int checkInteger(String data) {
        /* Remove '\n' */
        data = data.replace("\n", "").replace("\r", "");

        try {
            int r = Integer.parseInt(data);
            return r;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Método criado para realizar a execução do software.
     *
     * @param address Endereço
     * @param serverPort Porta
     * @throws InterruptedException Essa exceção é gerada pelo sleep, que por
     * sua vez é um método estático, ou seja cuja chamada afeta todas as
     * instâncias da classe. O resultado da sua chamada é que todas as linhas de
     * execução "dormem" durante o intervalo especificado pelo argumento, em
     * milisegundos.
     */
    private void executable(String address, int serverPort) throws InterruptedException {
        clearScreen();
        in = new Scanner(System.in);
        String message = "";
        int timeOut = 3000;
        int sleep = 100;

        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            aSocket.setSoTimeout(timeOut); // Define timeout

            do {
                byte[] m = new byte[2500];
                while (message.equals("")) {
                    printMenu();
                    message = in.nextLine();

                    // Test message
                    boolean flag = false;
                    for (int i = 0; i < comands.length; i++) {
                        if (message.trim().equals(comands[i])) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag == false) {
                        message = "";
                    }

                    // ASSISTE
                    if (message.trim().equals("[ASSISTE]")) {
                        System.out.println("Digite o código do filme: ");
                        int filme = readInteger(in);
                        message = message.trim();
                        message += " ";
                        message += filme;
                        in.nextLine();
                    }
                }

                if (!message.trim().equals("[SAIR]")) {
                    m = message.getBytes();

                    InetAddress aHost = InetAddress.getByName(address);
                    DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                    aSocket.send(request);

                    byte[] buffer = new byte[2500];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    aSocket.receive(reply);
                    String receivedMessage = new String(reply.getData());

                    if (message.trim().length() >= 9) {
                        if (message.trim().substring(0, 9).equals("[ASSISTE]")) {
                            if (receivedMessage.trim().equals("[BEGIN]")) {
                                clearScreen();
                                int idFrame = 0;
                                int idAntFrame = -1;
                                do {
                                    buffer = new byte[2500];
                                    reply = new DatagramPacket(buffer, buffer.length);
                                    aSocket.receive(reply);
                                    receivedMessage = new String(reply.getData());
                                    if (!receivedMessage.trim().equals("[END]")) {
                                        // TESTE ID FRAME -> PACOTES PODEM CHEGAR FORA DE ORDEM (DESCONSIDERA OS ATRASADOS E/OU PERDIDOS)
                                        if (receivedMessage.trim().length() >= 10) {
                                            String teste[] = receivedMessage.trim().substring(0, 10).split("#");
                                            idFrame = checkInteger(teste[0]);
                                            if (idFrame > 0 && idFrame > idAntFrame) {
                                                // DETECTA TAMANHO OCUPADO PELO NUMERO
                                                int size = String.valueOf(idFrame).length() + 1; // + 1 PELO #

                                                // IMPRIME
                                                System.out.println(receivedMessage.substring(size, receivedMessage.length()));
                                                Thread.sleep(sleep);
                                                clearScreen();
                                                
                                                // ATUALIZA ID ANTERIOR
                                                idAntFrame = idFrame;
                                            }
                                        }

                                    }
                                } while (!receivedMessage.trim().equals("[END]"));
                                message = "";
                            }
                        } else if (receivedMessage.trim().equals("[CATALOGO]")) {
                            clearScreen();
                            buffer = new byte[2500];
                            reply = new DatagramPacket(buffer, buffer.length);
                            aSocket.receive(reply);
                            receivedMessage = new String(reply.getData());
                            System.out.println(receivedMessage);
                            message = "";
                        }
                    }
                }
            } while (!message.trim().equals("[SAIR]"));

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
        Client client = new Client();
        int serverPort = 6789;
        String address = "localhost";
        client.executable(address, serverPort);
    }
}
