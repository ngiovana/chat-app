package conexao;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ImprimeDadosSocket implements Runnable {

    private Socket socket;

    private static final String SAIR = "sair!";

    public ImprimeDadosSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner;
        try {
            scanner = new Scanner(socket.getInputStream());
            while (scanner.hasNextLine()) {
                String mensagem = scanner.nextLine();
                System.out.println(socket.getInetAddress().getHostAddress() + " disse: " + mensagem);

                if (SAIR.equals(mensagem)) {
                    socket.close();
                    System.out.println(socket.getInetAddress().getHostAddress() + " desconectado");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
