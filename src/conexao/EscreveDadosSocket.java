package conexao;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class EscreveDadosSocket implements Runnable {

    private Socket socket;

    public EscreveDadosSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            PrintStream transmissor = new PrintStream(socket.getOutputStream());
            while (scanner.hasNextLine()) {
                transmissor.println(scanner.nextLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
