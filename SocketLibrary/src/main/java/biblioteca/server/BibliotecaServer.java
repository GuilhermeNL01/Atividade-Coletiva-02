package biblioteca.server;

import biblioteca.model.Livro;
import biblioteca.utils.JsonUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class BibliotecaServer {
    private static List<Livro> livros;

    public static void main(String[] args) {
        livros = JsonUtils.carregarLivros();
        final int PORTA = 1336;

        try (ServerSocket servidorSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor pronto para receber conexões...");

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket);
                new Thread(new ClienteHandler(clienteSocket, livros)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
