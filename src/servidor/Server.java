package servidor;/*
- Aceita múltiplos clientes
- Guarda a lista de clientes conectados (nome + IP + output stream)
- Deve fazer apenas o roteamento das mensagens e arquivos entre os clientes
- Mantém um log de conexões com IP e horário
*/

import conexao.ImprimeDadosSocket;
import conexao.EscreveDadosSocket;
import utils.EscreveArquivo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Socket socketCliente;

    public Server() {
    }

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(12345);
        System.out.println("Conectado ao servidor!");
        File arquivo = new File("nomeArquivo.txt");
        arquivo.createNewFile();

        while (true) {
            socketCliente = servidor.accept();
            System.out.println("Client " + socketCliente.getInetAddress().getHostAddress() + " conectado.");

            EscreveArquivo.registrarConexaoServidor(arquivo.getName(), socketCliente);

            Thread threadEscrita = new Thread(new EscreveDadosSocket(socketCliente));
            Thread threadLeitura = new Thread(new ImprimeDadosSocket(socketCliente));
            threadEscrita.start();
            threadLeitura.start();

            try {
                threadEscrita.join();
                threadLeitura.join();
            } catch (InterruptedException excecao) {
                throw new RuntimeException(excecao);
            }
        }
    }
}

