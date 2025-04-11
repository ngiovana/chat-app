/*
- Aceita múltiplos clientes
- Guarda a lista de clientes conectados (nome + IP + output stream)
- Deve fazer apenas o roteamento das mensagens e arquivos entre os clientes
- Mantém um log de conexões com IP e horário
*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread{
    private Socket socket;
    public Server(final Socket socket) { this.socket = socket; }
    public String clientName = "";

    public void run() {
        Scanner input = null;

        try {
            input = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clientName = input.nextLine();

        String line;
        while (input.hasNextLine()) {
            line = input.nextLine();

            if (line.equals("/sair")) {
                System.out.println("Cliente desconectado: " + socket.getInetAddress().getHostAddress());
                break;
            }

            System.out.println(clientName + ": " + input.nextLine());
        }

        if (input.nextLine() == "/sair") {
            System.out.println("Cliente desconectado: " + socket.getInetAddress().getHostAddress());
        }
    }

    public static void main(String args[]) throws IOException {
        ServerSocket server = new ServerSocket(12345);
        System.out.println("Servidor iniciado na 12345!");

        while (true) {
            Socket client = server.accept();
            System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());
            new Server(client).start();
        }
    }

    public static void reader(String path) throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(path));
        String linha = "";
        while (true) {
            if (linha != null) {
                System.out.println(linha);

            } else
                break;
            linha = buffRead.readLine();
        }
        buffRead.close();
    }

    public static void writer(String path, String clientInfo) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));

        buffWrite.append(clientInfo + "\n");
        buffWrite.close();
    }
}
