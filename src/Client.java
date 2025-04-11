/*
- Se conecta ao servidor
- Lê comandos via terminal
- Envia mensagens para outro cliente
- Envia arquivos (em bytes)
- Recebe mensagens e arquivos de outros clientes

Comandos especiais:
/users
/send message <destinatario> <mensagem>
/send file <destinatario> <caminho>
/sair
*/

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) throws IOException {
        var client = new Socket("127.0.0.1", 12345);
        System.out.println("Cliente conectado!");

        var keyboard = new Scanner(System.in);
        var input = new PrintStream(client.getOutputStream());
        while (keyboard.hasNextLine()) {
            input.println(keyboard.nextLine());
        }

        input.close();
        keyboard.close();
        client.close();

    }

}
