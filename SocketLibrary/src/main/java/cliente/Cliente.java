package cliente;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    // portas
    private static final String HOST = "localhost";
    private static final int PORTA = 12344;

    //opcoes
    private static final String LISTAR_LIVROS = "1";
    private static final String ALUGAR_LIVRO = "2";
    private static final String DEVOLVER_LIVRO = "3";
    private static final String CADASTRAR_LIVRO = "4";
    private static final String SAIR = "5";

    // principal
    public static void main(String[] args) {
        try {
            //inicializando socket
            Socket socket = new Socket(HOST, PORTA);

           // Leitores pro sistema

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            //use cases
            String opcao;
            do {
                exibirMenu();
                opcao = scanner.nextLine();

                switch (opcao) {
                    case LISTAR_LIVROS:
                        listarLivros(saida, entrada);
                        break;
                    case ALUGAR_LIVRO:
                        alugarLivro(saida, entrada, scanner);
                        break;
                    case DEVOLVER_LIVRO:
                        devolverLivro(saida, entrada, scanner);
                        break;
                    case CADASTRAR_LIVRO:
                        cadastrarLivro(saida, entrada, scanner);
                        break;
                    case SAIR:
                        saida.println("sair");
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } while (!opcao.equals(SAIR));

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
