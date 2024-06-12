package biblioteca;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaServer {
    private static final String ARQUIVO_JSON = "src/main/resources/livros.json";

    public static void main(String[] args) {
        carregarLivros();

        final int PORTA = 1337;

        try {
            ServerSocket servidorSocket = new ServerSocket(PORTA);
            System.out.println("Servidor pronto para receber conexões...");

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket);

                Thread clienteThread = new Thread(new ClienteHandler(clienteSocket));
                clienteThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void carregarLivros() {
        // Método para carregar os livros do arquivo JSON
    }
}

