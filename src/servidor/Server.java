/*
 GRUPO: GISELE, GIOVANA E YASMIN

 - Aceita múltiplos clientes
 - Guarda a lista de clientes conectados (nome + IP + output stream)
 - Deve fazer apenas o roteamento das mensagens e arquivos entre os clientes
 - Mantém um log de conexões com IP e horário
*/

package servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import utils.EscreveArquivo;

public class Server {
    private static final Map<String, ClienteConectado> clientes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket servidor = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado na porta 12345.");

            File logArquivo = new File("nomeArquivo.txt");
            logArquivo.createNewFile();

            while (true) {
                Socket socketCliente = servidor.accept();

                BufferedReader leitor = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                PrintStream transmissor = new PrintStream(socketCliente.getOutputStream());

                transmissor.println("Digite seu nome de usuário:");
                String nome = leitor.readLine();

                synchronized (clientes) {
                    while (clientes.containsKey(nome)) {
                        transmissor.println("Nome já em uso. Escolha outro:");
                        nome = leitor.readLine();
                    }

                    ClienteConectado cliente = new ClienteConectado(nome, socketCliente, transmissor);
                    clientes.put(nome, cliente);
                }

                System.out.println(
                        "Cliente " + nome + " conectado (" + socketCliente.getInetAddress().getHostAddress() + ")");
                EscreveArquivo.registrarConexaoServidor(logArquivo.getName(), socketCliente);

                // Correção aplicada aqui
                final String nomeFinal = nome;
                final BufferedReader leitorFinal = leitor;

                new Thread(() -> tratarCliente(nomeFinal, leitorFinal)).start();
            }
        }
    }

    private static void tratarCliente(String nome, BufferedReader leitor) {
        try {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.startsWith("/users")) {
                    listarUsuarios(nome);
                } else if (linha.startsWith("/send message")) {
                    enviarMensagem(nome, linha);
                } else if (linha.startsWith("/send file")) {
                    receberERepassarArquivo(nome, linha, leitor);
                } else if (linha.equals("/sair")) {
                    sair(nome);
                    break;
                } else {
                    clientes.get(nome).getTransmissor().println("Comando inválido.");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro na conexão com " + nome);
        } finally {
            sair(nome);
        }
    }

    private static void listarUsuarios(String remetente) {
        StringBuilder lista = new StringBuilder("Usuários conectados:");
        synchronized (clientes) {
            for (String nome : clientes.keySet()) {
                lista.append("\n - ").append(nome);
            }
        }
        clientes.get(remetente).getTransmissor().println(lista.toString());
    }

    private static void enviarMensagem(String remetente, String comando) {
        try {
            String[] partes = comando.split(" ", 4);
            String destinatario = partes[2];
            String mensagem = partes[3];

            ClienteConectado destino = clientes.get(destinatario);
            if (destino != null) {
                destino.getTransmissor().println("[Mensagem de " + remetente + "]: " + mensagem);
            } else {
                clientes.get(remetente).getTransmissor().println("Usuário não encontrado.");
            }
        } catch (Exception e) {
            clientes.get(remetente).getTransmissor()
                    .println("Formato inválido. Use: /send message <destinatario> <mensagem>");
        }
    }

    private static void receberERepassarArquivo(String remetente, String comando, BufferedReader leitor)
            throws IOException {
        try {
            String[] partes = comando.split(" ", 4);
            String destinatario = partes[2];
            String nomeArquivo = partes[3];

            ClienteConectado destino = clientes.get(destinatario);
            if (destino != null) {
                destino.getTransmissor().println("/receber arquivo " + remetente + " " + nomeArquivo);
                InputStream entrada = clientes.get(remetente).getSocket().getInputStream();
                OutputStream saida = destino.getSocket().getOutputStream();

                byte[] buffer = new byte[4096];
                int bytesLidos;
                while ((bytesLidos = entrada.read(buffer)) != -1) {
                    saida.write(buffer, 0, bytesLidos);
                    if (bytesLidos < 4096)
                        break;
                }
                saida.flush();

            } else {
                clientes.get(remetente).getTransmissor().println("Usuário não encontrado.");
            }
        } catch (Exception e) {
            clientes.get(remetente).getTransmissor().println("Erro ao enviar arquivo.");
        }
    }

    private static void sair(String nome) {
        synchronized (clientes) {
            ClienteConectado cliente = clientes.remove(nome);
            if (cliente != null) {
                try {
                    cliente.getSocket().close();
                } catch (IOException ignored) {
                }
                System.out.println("Cliente " + nome + " desconectado.");
            }
        }
    }
}
