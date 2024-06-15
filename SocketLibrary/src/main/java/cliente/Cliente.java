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

    // menu de exibição do usuário
    private static void exibirMenu() {
        System.out.println("Biblioteca:");
        System.out.println("1. Listar livros");
        System.out.println("2. Alugar livro");
        System.out.println("3. Devolver livro");
        System.out.println("4. Cadastrar livro");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    //metodo de listagem
    private static void listarLivros(PrintWriter saida, BufferedReader entrada) throws IOException {
        saida.println("listar");
        String resposta;
        while ((resposta = entrada.readLine()) != null) {
            if (resposta.isEmpty()) break;
            System.out.println(resposta);
        }
    }

    //método de aluguel
    private static void alugarLivro(PrintWriter saida, BufferedReader entrada, Scanner scanner) throws IOException {
        System.out.print("Nome do livro: ");
        String nomeLivro = scanner.nextLine();
        saida.println("alugar#" + nomeLivro);
        String resposta = entrada.readLine();
        System.out.println(resposta);
    }

    //Método de devolução
    private static void devolverLivro(PrintWriter saida, BufferedReader entrada, Scanner scanner) throws IOException {
        System.out.print("Nome do livro: ");
        String nomeLivro = scanner.nextLine();
        saida.println("devolver#" + nomeLivro);
        String resposta = entrada.readLine();
        System.out.println(resposta);
    }


}
