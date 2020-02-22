package TP;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SServidor {

    public static void main(String[] args) throws IOException {
        // inicia o servidor
        new SServidor(6789).executa();
    }

    private int porta;
    private List<PrintStream> clientes;

    public SServidor(int porta) {
        this.porta = porta;
        this.clientes = new ArrayList<PrintStream>();
    }

    public void executa() throws IOException {
        ServerSocket servidor = new ServerSocket(this.porta);

        while (true) {
            Socket cliente = servidor.accept(); // aceita um cliente
            System.out.println("Nova conexão com o cliente "  + cliente.getInetAddress().getHostAddress());

            PrintStream ps = new PrintStream(cliente.getOutputStream()); // adiciona saida do cliente à lista
            this.clientes.add(ps);

            TrataCliente tc = new TrataCliente(cliente.getInputStream(), this);  // cria tratador de cliente numa nova thread
            new Thread(tc).start();
        }

    }

    public void distribuiMensagem(String msg) {
        // envia msg para todo mundo
        for (PrintStream cliente : this.clientes) {
            cliente.println(msg);
        }
    }
}

class TrataCliente implements Runnable {

    private InputStream cliente;
    private SServidor servidor;

    public TrataCliente(InputStream cliente, SServidor servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
    }

    public void run() {
        // quando chegar uma msg, distribui pra todos
        Scanner s = new Scanner(this.cliente);
        while (s.hasNextLine()) {
            servidor.distribuiMensagem(s.nextLine());
        }
        s.close();
    }
}
