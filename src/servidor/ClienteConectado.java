package servidor;

import java.io.PrintStream;
import java.net.Socket;

public class ClienteConectado {
    private String nome;
    private Socket socket;
    private PrintStream transmissor;

    public ClienteConectado(String nome, Socket socket, PrintStream transmissor) {
        this.nome = nome;
        this.socket = socket;
        this.transmissor = transmissor;
    }

    public String getNome() {
        return nome;
    }

    public PrintStream getTransmissor() {
        return transmissor;
    }

    public Socket getSocket() {
        return socket;
    }
}
