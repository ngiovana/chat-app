/*
- Aceita múltiplos clientes
- Guarda a lista de clientes conectados (nome + IP + output stream)
- Deve fazer apenas o roteamento das mensagens e arquivos entre os clientes
- Mantém um log de conexões com IP e horário
*/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String args[]) throws IOException {
        ServerSocket server = new ServerSocket(12345);
        System.out.println("Servidor iniciado na 12345!");

        while (true) {
            Socket socket = server.accept();
            new Thread(() -> ConnectClient(socket)).start();
        }
    }

    public static void ConnectClient(Socket socket) {
        System.out.println("Cliente conectado!");
    }
}
