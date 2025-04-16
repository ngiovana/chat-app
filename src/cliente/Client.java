package cliente;/*
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

import conexao.ImprimeDadosSocket;
import conexao.EscreveDadosSocket;

import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

    public Client() {
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        System.out.println("Conectado ao servidor!");

        Thread escrita = new Thread(new EscreveDadosSocket(socket));
        Thread leitura = new Thread(new ImprimeDadosSocket(socket));
        escrita.start();
        leitura.start();

        try {
            escrita.join();
            leitura.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
