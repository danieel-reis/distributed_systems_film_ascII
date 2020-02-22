package TP;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Streamer {

    public static void main(String args[]) throws ClassNotFoundException, IOException, InterruptedException {
        int fps = Integer.parseInt(args[0]);
        DatagramSocket aSocket = null;

        try {
            aSocket = new DatagramSocket(6789); // Create socket at agreed port

            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                ConnectionClient connection = new ConnectionClient(aSocket, request, fps);
                Thread threadConnection = new Thread(connection);
                threadConnection.start();
            }

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

class ConnectionClient implements Runnable {

    private String filmes[] = {"fly.txt", "stage.txt", "sw.txt", "train.txt"};
    private String filePath = "/home/daniel/NetBeansProjects/SD/filmes-ASCII/";
    private String comands[] = {"[CATALOGO]", "[ASSISTE]", "[SAIR]"};
    private int fps;
    private DatagramSocket aSocket = null;
    private DatagramPacket request = null;

    public ConnectionClient(DatagramSocket aSocket, DatagramPacket request, int fps) {
        this.fps = fps;
        this.aSocket = aSocket;
        this.request = request;
    }

    /**
     * Este método foi criado para retornar o tamanho do arquivo em bytes.
     *
     * @param filePath Caminho + nome do arquivo
     * @return Tamanho em bytes do arquivo solicitado
     */
    private int getSizeFile(String filePath) {
        File file = new File(filePath);
        return Integer.parseInt(Long.toString(file.length()));
    }

    /**
     * Procedimento que realiza a leitura byte a byte do arquivo e retorna um
     * array de bytes. Para usa-ló, deve-se instanciar uma string, passando como
     * parâmetro como no seguinte exemplo: new String(readFromFile(FILEPATH,
     * BYTEINICIAL, TAMANHO)); onde tamanho refere-se a variação, ou seja,
     * (bytefinal - byteinicial).
     *
     * @param filePath Caminho do arquivo
     * @param byteinitial Valor do byteinicial do arquivo
     * @param size Tamanho total do arquivo no momento
     * @return Array de bytes lidos do arquivo
     * @throws IOException Exceção lançada caso ocorre algum erro no acesso do
     * arquivo
     */
    private byte[] readFromFile(String filePath, int byteinitial, int size) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(byteinitial);
        byte[] bytes = new byte[size];
        file.read(bytes);
        file.close();
        return bytes;
    }

    /**
     * Método criado para realizar a leitura do arquivo que contém registros de
     * tamanho variável. Basicamente, ele varre todo arquivo e transforma as
     * strings encontradas em strings. Precisa receber apenas o caminho+nome do
     * arquivo.
     *
     * @param filePath Caminho do arquivo
     * @return Array de dados obtidos no arquivo que contém registros de tamanho
     * variável
     * @throws ClassNotFoundException Este tipo de exceção é checked e como o
     * próprio nome já sugere, ela ocorre quando alguma classe não é encontrada
     * no seu classpath
     * @throws IOException Essa exceção ocorre quando algum sinal de
     * entrada/saída falha ou é interrompido
     */
    private ArrayList<String> readFile(String filePath)
            throws ClassNotFoundException, IOException {
        ArrayList<String> data = new ArrayList<String>();
        int size_file = getSizeFile(filePath);
        String read = null;
        String line[] = null;
        int byteOffset = 0;

        read = new String(readFromFile(filePath, byteOffset, size_file));
        line = null;
        line = read.split("\n"); // Fragmenta linha a linha
        read = null;
        for (int i = 0; i < line.length; i++) { // Insere linha a linha no array de dados
            data.add(line[i]);
//            System.out.println(line[i]);
        }
        return data;
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
     * Método criado para transmitir uma frame.
     *
     * @param frame Array de dados da frame
     * @param inetAddress Endereço
     * @param port Porta
     * @param aSocket Socket
     * @param fps Frames por segundo
     * @throws InterruptedException Essa exceção é gerada pelo sleep, que por
     * sua vez é um método estático, ou seja cuja chamada afeta todas as
     * instâncias da classe. O resultado da sua chamada é que todas as linhas de
     * execução "dormem" durante o intervalo especificado pelo argumento, em
     * milisegundos.
     * @throws IOException Essa exceção ocorre quando algum sinal de
     * entrada/saída falha ou é interrompido
     */
    private void transmitedFrame(ArrayList<String> frame, InetAddress inetAddress, int port, DatagramSocket aSocket, int fps, int idFrame)
            throws InterruptedException, IOException {
        String data = "";
        data += idFrame;
        data += "#";
        for (int i = 0; i < frame.size(); i++) {
            data += frame.get(i) + "\n";
        }
        DatagramPacket reply = new DatagramPacket(data.getBytes(), data.length(), inetAddress, port);
        aSocket.send(reply);
        Thread.sleep(1000 / fps);
    }

    /**
     * Método criado para transmitir os dados de um catálogo.
     *
     * @param inetAddress Endereço
     * @param port Porta
     * @param aSocket Socket
     * @param fps Frames por segundo
     * @throws InterruptedException Essa exceção é gerada pelo sleep, que por
     * sua vez é um método estático, ou seja cuja chamada afeta todas as
     * instâncias da classe. O resultado da sua chamada é que todas as linhas de
     * execução "dormem" durante o intervalo especificado pelo argumento, em
     * milisegundos.
     * @throws IOException Essa exceção ocorre quando algum sinal de
     * entrada/saída falha ou é interrompido
     */
    private void transmitedCatalogo(InetAddress inetAddress, int port, DatagramSocket aSocket, int fps)
            throws InterruptedException, IOException {
        String data_ = "[CATALOGO]";
        DatagramPacket reply = new DatagramPacket(data_.getBytes(), data_.length(), inetAddress, port);
        aSocket.send(reply);

        String data = "------------------------- FILMES -------------------------\n";
        for (int i = 0; i < filmes.length; i++) {
            data += " " + (i + 1) + " - " + filmes[i] + "\n";
        }
        data += "----------------------------------------------------------\n";

        reply = new DatagramPacket(data.getBytes(), data.length(), inetAddress, port);
        aSocket.send(reply);
        Thread.sleep(1000 / fps);
    }

    /**
     * Método criado para executar a transmissão de um filme.
     *
     * @param filePath Caminho do arquivo
     * @param filme Filme a ser transmitido
     * @param inetAddress Endereço
     * @param port Porta
     * @param aSocket Socket
     * @param fps Frames por segundo
     * @throws ClassNotFoundException Este tipo de exceção é checked e como o
     * próprio nome já sugere, ela ocorre quando alguma classe não é encontrada
     * no seu classpath
     * @throws IOException Essa exceção ocorre quando algum sinal de
     * entrada/saída falha ou é interrompido
     * @throws InterruptedException Essa exceção é gerada pelo sleep, que por
     * sua vez é um método estático, ou seja cuja chamada afeta todas as
     * instâncias da classe. O resultado da sua chamada é que todas as linhas de
     * execução "dormem" durante o intervalo especificado pelo argumento, em
     * milisegundos.
     */
    private void executable(String filePath, String filme, InetAddress inetAddress, int port, DatagramSocket aSocket, int fps)
            throws ClassNotFoundException, IOException, InterruptedException {
        String data_ = "[BEGIN]";
        DatagramPacket reply = new DatagramPacket(data_.getBytes(), data_.length(), inetAddress, port);
        aSocket.send(reply);

        ArrayList<String> data = readFile(filePath + filme);
        ArrayList<String> frame = new ArrayList<String>();

        int number_detected = checkInteger(data.get(0));
        int idFrame = 1;

        if (number_detected <= 0) { // Number not detected
            for (int i = 0; i < data.size(); i++) {
                frame.add(data.get(i)); // Add line
                if (data.get(i).length() == 0) { // Line is empty
                    transmitedFrame(frame, inetAddress, port, aSocket, fps, idFrame);
                    while (data.get(i).length() == 0 && i < data.size()) {
                        i++;
                    }
                    frame = null;
                    frame = new ArrayList<String>();
                    idFrame++;
                }
            }
        } else { // Number detected
            for (int i = 1; i < (data.size() - 1); i++) {
                while (checkInteger(data.get(i)) <= 0 && i < (data.size() - 1)) { // While not detected (number)
                    frame.add(data.get(i));
                    i++;
                }
                for (int j = 0; j < number_detected; j++) {  // Repeat frame
                    transmitedFrame(frame, inetAddress, port, aSocket, fps, idFrame);
                    idFrame++;
                }
                frame = null;
                frame = new ArrayList<String>();
                if (i < data.size()) {
                    number_detected = checkInteger(data.get(i));
                }
            }
        }

        data_ = "[END]";
        reply = new DatagramPacket(data_.getBytes(), data_.length(), inetAddress, port);
        aSocket.send(reply);
    }

    /**
     * Execução da thread
     */
    public void run() {
        InetAddress inetAddress = request.getAddress();
        int port = request.getPort();
        String received = new String(request.getData());
        System.out.println("    Received: " + received.trim());

        switch (received.trim()) {
            case "[CATALOGO]": {
                try {
                    transmitedCatalogo(inetAddress, port, aSocket, fps);
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "[ASSISTE] 1": {
                try {
                    executable(filePath, filmes[0], inetAddress, port, aSocket, fps);
                } catch (ClassNotFoundException | IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "[ASSISTE] 2": {
                try {
                    executable(filePath, filmes[1], inetAddress, port, aSocket, fps);
                } catch (ClassNotFoundException | IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "[ASSISTE] 3": {
                try {
                    executable(filePath, filmes[2], inetAddress, port, aSocket, fps);
                } catch (ClassNotFoundException | IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "[ASSISTE] 4": {
                try {
                    executable(filePath, filmes[3], inetAddress, port, aSocket, fps);
                } catch (ClassNotFoundException | IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }
}
