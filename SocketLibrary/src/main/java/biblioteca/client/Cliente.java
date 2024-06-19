package biblioteca.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * A classe Cliente implementa um cliente para interagir com o servidor da biblioteca.
 * Ela permite listar livros, alugar, devolver e cadastrar livros através de interações via console.
 */
public class Cliente {
    private static final String HOST = "localhost"; // Endereço do servidor
    private static final int PORTA = 9090; // Porta do servidor

    // Constantes para as opções do menu
    private static final String LISTAR_LIVROS = "1";
    private static final String ALUGAR_LIVRO = "2";
    private static final String DEVOLVER_LIVRO = "3";
    private static final String CADASTRAR_LIVRO = "4";
    private static final String SAIR = "5";

    /**
     * Método principal para iniciar o cliente da biblioteca.
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORTA);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            String opcao;
            do {
                exibirMenu(); // Exibe o menu de opções
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
                        saida.println("sair"); // Envia mensagem de saída para o servidor
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } while (!opcao.equals(SAIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para exibir o menu de opções disponíveis.
     */
    private static void exibirMenu() {
        System.out.println("Biblioteca:");
        System.out.println("1. Listar livros");
        System.out.println("2. Alugar livro");
        System.out.println("3. Devolver livro");
        System.out.println("4. Cadastrar livro");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Método para listar os livros disponíveis no servidor.
     *
     * @param saida   PrintWriter para enviar a requisição ao servidor
     * @param entrada BufferedReader para ler a resposta do servidor
     * @throws IOException em caso de erro de E/S
     */
    private static void listarLivros(PrintWriter saida, BufferedReader entrada) throws IOException {
        saida.println("listar"); // Envia a requisição para listar livros
        String resposta;
        while ((resposta = entrada.readLine()) != null) {
            if (resposta.isEmpty()) break; // Finaliza a leitura quando não há mais livros para listar
            System.out.println(resposta); // Exibe cada livro recebido do servidor
        }
    }

    /**
     * Método para solicitar o aluguel de um livro ao servidor.
     *
     * @param saida   PrintWriter para enviar a requisição ao servidor
     * @param entrada BufferedReader para ler a resposta do servidor
     * @param scanner Scanner para ler entrada do usuário
     * @throws IOException em caso de erro de E/S
     */
    private static void alugarLivro(PrintWriter saida, BufferedReader entrada, Scanner scanner) throws IOException {
        System.out.print("Nome do livro: ");
        String nomeLivro = scanner.nextLine();
        saida.println("alugar#" + nomeLivro); // Envia a requisição para alugar um livro específico
        String resposta = entrada.readLine(); // Recebe a resposta do servidor
        System.out.println(resposta); // Exibe a resposta do servidor para o usuário
    }

    /**
     * Método para solicitar a devolução de um livro ao servidor.
     *
     * @param saida   PrintWriter para enviar a requisição ao servidor
     * @param entrada BufferedReader para ler a resposta do servidor
     * @param scanner Scanner para ler entrada do usuário
     * @throws IOException em caso de erro de E/S
     */
    private static void devolverLivro(PrintWriter saida, BufferedReader entrada, Scanner scanner) throws IOException {
        System.out.print("Nome do livro: ");
        String nomeLivro = scanner.nextLine();
        saida.println("devolver#" + nomeLivro); // Envia a requisição para devolver um livro específico
        String resposta = entrada.readLine(); // Recebe a resposta do servidor
        System.out.println(resposta); // Exibe a resposta do servidor para o usuário
    }

    /**
     * Método para solicitar o cadastro de um novo livro ao servidor.
     *
     * @param saida   PrintWriter para enviar a requisição ao servidor
     * @param entrada BufferedReader para ler a resposta do servidor
     * @param scanner Scanner para ler entrada do usuário
     * @throws IOException em caso de erro de E/S
     */
    private static void cadastrarLivro(PrintWriter saida, BufferedReader entrada, Scanner scanner) throws IOException {
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Genero: ");
        String genero = scanner.nextLine();
        System.out.print("Número de exemplares: ");
        int numExemplares = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer do scanner
        String livroJson = autor + "," + nome + "," + genero + "," + numExemplares;
        saida.println("cadastrar#" + livroJson); // Envia a requisição para cadastrar um novo livro
        String resposta = entrada.readLine(); // Recebe a resposta do servidor
        System.out.println(resposta); // Exibe a resposta do servidor para o usuário
    }
}
