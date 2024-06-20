package biblioteca.server;

import biblioteca.model.Livro;
import biblioteca.utils.JsonUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * A classe SrvBib representa o servidor da aplicação de biblioteca.
 * Este servidor aguarda conexões de clientes e delega o processamento para threads separadas.
 */
public class SrvBib {
    private static List<Livro> livros; // Lista de livros carregada a partir de um arquivo JSON

    public static void main(String[] args) {
        // Carrega a lista de livros utilizando a classe utilitária JsonUtils
        livros = JsonUtils.carregarLivros();
        final int PORTA = 9090; // Porta em que o servidor está escutando

        try (ServerSocket servidorSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor pronto para receber conexões...");

            // Loop infinito para aguardar conexões de clientes
            while (true) {
                Socket clienteSocket = servidorSocket.accept(); // Aguarda a conexão de um cliente
                System.out.println("Cliente conectado: " + clienteSocket);
                
                // Inicia uma nova thread para lidar com o cliente conectado
                new Thread(new GerenciadorCliente(clienteSocket, livros)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
