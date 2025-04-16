package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

public class EscreveArquivo {

    public static void registrarConexaoServidor(String nomeArquivo, Socket socket) {
        GregorianCalendar calendario = new GregorianCalendar();
        try {
            FileWriter escritor = new FileWriter(nomeArquivo, true);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime agora = LocalDateTime.now();

            String mensagem = "Host: " + socket.getInetAddress().getHostAddress()
                    + " - Data e Hora da Conexão: " + agora.format(formato) + "\n";

            escritor.write(mensagem);
            escritor.close();

            System.out.println("Conexão registrada --- " + mensagem);
            System.out.println("Arquivo salvo em: " + new File(nomeArquivo).getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
