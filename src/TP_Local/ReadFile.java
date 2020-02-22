/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TP_Local;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Esta classe tem a função de tratar todos os procedimentos necessários para se
 * ler um arquivo txt, capturar as informações e convertê-lá a entidade em
 * questão.
 *
 * @author daniel/otavio
 */
public class ReadFile {

    private String filmes[] = {"fly.txt", "stage.txt", "sw.txt", "train.txt"};
    private String comands[] = {"[CATALOGO]", "[ASSISTE]", "[SAIR]"};
    private static Scanner in;

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
     * strings encontradas em atributos da entidade. Precisa receber apenas o
     * caminho+nome do arquivo.
     *
     * @param FILEPATH Caminho do arquivo
     * @return Array de usuários obtidos no arquivo que contém registros de
     * tamanho variável
     * @throws ClassNotFoundException Este tipo de exceção é checked e como o
     * próprio nome já sugere, ela ocorre quando alguma classe não é encontrada
     * no seu classpath
     * @throws IOException Essa exceção ocorre quando algum sinal de
     * entrada/saída falha ou é interrompido
     */
    private ArrayList<String> readFile(String FILEPATH)
            throws ClassNotFoundException, IOException {
        ArrayList<String> data = new ArrayList<String>();
        int size_file = getSizeFile(FILEPATH);
        String read = null;
        String line[] = null;
        int byteOffset = 0;

        read = new String(readFromFile(FILEPATH, byteOffset, size_file));
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
     * Método criado para limpar o console no terminal
     */
    public void clearScreen() {
        System.out.printf("\33[2J"); // Codigo ansi para limpar a tela 
    }

    /**
     * Método criado para imprimir uma frame
     *
     * @param frame
     * @param sleep
     * @throws InterruptedException
     */
    private void printFrame(ArrayList<String> frame, int sleep) throws InterruptedException {
        for (int i = 0; i < frame.size(); i++) {
            System.out.println(frame.get(i));
        }
        Thread.sleep(sleep);
    }

    /**
     * Este método verifica se uma string lida é do tipo inteiro. Senão, indica
     * erro.
     *
     * @param in Variável do tipo Scanner
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
     * Este método verifica se um valor lido é realmente do tipo inteiro. Senão,
     * indica erro.
     *
     * @param in Variável do tipo Scanner
     * @return Realiza um loop até que possa enviar um número que foi lido
     */
    public int readInteger(Scanner in) {
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

    public void printMenu() {
        System.out.println("-------------------------- MENU --------------------------");
        for (int i = 0; i < comands.length; i++) {
            System.out.println(comands[i]);
        }
        System.out.println("----------------------------------------------------------");
    }

    public void printCatalogo() {
        System.out.println("------------------------- FILMES -------------------------");
        for (int i = 0; i < filmes.length; i++) {
            System.out.println(" " + (i + 1) + " - " + filmes[i]);
        }
        System.out.println("----------------------------------------------------------");
    }

    public void executable(String filePath, String filme, int sleep) throws ClassNotFoundException, IOException, InterruptedException {
        ArrayList<String> data = readFile(filePath + filme);
        ArrayList<String> frame = new ArrayList<String>();
        clearScreen();

        int number_detected = checkInteger(data.get(0));

        if (number_detected <= 0) { // Number not detected
            for (int i = 0; i < data.size(); i++) {
                frame.add(data.get(i)); // Add line
                if (data.get(i).length() == 0) { // Line is empty
                    clearScreen();
                    printFrame(frame, sleep);
                    while (data.get(i).length() == 0 && i < data.size()) {
                        i++;
                    }
                    frame = null;
                    frame = new ArrayList<String>();
                }
            }
        } else { // Number detected
            for (int i = 1; i < (data.size() - 1); i++) {
                while (checkInteger(data.get(i)) <= 0 && i < (data.size() - 1)) { // While not detected (number)
                    frame.add(data.get(i));
                    i++;
                }
                for (int j = 0; j < number_detected; j++) {  // Repeat frame
                    clearScreen();
                    printFrame(frame, sleep);
                }
                frame = null;
                frame = new ArrayList<String>();
                if (i < data.size()) {
                    number_detected = checkInteger(data.get(i));
                }
            }
        }
    }

    public static void main(String args[]) throws ClassNotFoundException, IOException, InterruptedException {
        in = new Scanner(System.in);
        ReadFile rf = new ReadFile();
        String filmes[] = {"fly.txt", "stage.txt", "sw.txt", "train.txt"};
        String filePath = "/home/daniel/NetBeansProjects/SD/filmes-ASCII/";
        int sleep = 100;
        int filme;

        rf.clearScreen();
        String read = "";
        do {
            rf.printMenu();
            read = in.nextLine();

            if (read.equals("[CATALOGO]")) {
                rf.clearScreen();
                rf.printCatalogo();
            } else if (read.equals("[ASSISTE]")) {
                System.out.println("Digite o código do filme: ");
                filme = rf.readInteger(in);
                rf.executable(filePath, filmes[filme - 1], sleep);
            } else if (read.equals("[SAIR]")) {
                rf.clearScreen();
                System.out.println("Saindo...");
            } else {
                rf.clearScreen();
            }
        } while (!read.equals("[SAIR]"));
    }
}
