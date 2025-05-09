package cliente;/*
                GRUPO: GISELE, GIOVANA E YASMIN
                
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

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream saida = new PrintStream(socket.getOutputStream());

            Scanner scanner = new Scanner(System.in);
            System.out.println(entrada.readLine()); // "Digite seu nome"
            saida.println(scanner.nextLine()); // nome do cliente

            Thread recebe = new Thread(() -> {
                try {
                    String linha;
                    while ((linha = entrada.readLine()) != null) {
                        if (linha.startsWith("/receber arquivo")) {
                            String[] partes = linha.split(" ", 4);
                            String remetente = partes[2];
                            String nomeArquivo = partes[3];
                            receberArquivo(socket, nomeArquivo, remetente);
                        } else {
                            System.out.println(linha);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada.");
                }
            });

            Thread envia = new Thread(() -> {
                while (true) {
                    String msg = scanner.nextLine();
                    saida.println(msg);

                    if (msg.startsWith("/send file")) {
                        String[] partes = msg.split(" ", 4);
                        if (partes.length == 4) {
                            enviarArquivo(socket, partes[3]);
                        }
                    }
                }
            });

            recebe.start();
            envia.start();
            recebe.join();
            envia.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enviarArquivo(Socket socket, String caminhoArquivo) {
        try {
            FileInputStream fis = new FileInputStream(caminhoArquivo);
            OutputStream out = socket.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytes;
            while ((bytes = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytes);
            }
            fis.close();
        } catch (IOException e) {
            System.out.println("Erro ao enviar o arquivo.");
        }
    }

    private static void receberArquivo(Socket socket, String nomeArquivo, String remetente) {
        try {
            InputStream in = socket.getInputStream();
            FileOutputStream fos = new FileOutputStream(nomeArquivo);

            byte[] buffer = new byte[4096];
            int bytes;
            while ((bytes = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytes);
                if (bytes < 4096)
                    break;
            }

            fos.close();
            System.out.println("Arquivo '" + nomeArquivo + "' recebido de " + remetente);
        } catch (IOException e) {
            System.out.println("Erro ao receber o arquivo.");
        }
    }
}
