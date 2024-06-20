package biblioteca.server;

import biblioteca.model.Livro;
import biblioteca.utils.JsonUtils;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * A classe GerenciadorCliente implementa a interface Runnable para lidar com as requisições de um cliente.
 * Ela processa operações relacionadas aos livros da biblioteca, como listar, alugar, devolver e cadastrar livros.
 */
public class GerenciadorCliente implements Runnable {
    private Socket clienteSocket; // Socket para comunicação com o cliente
    private List<Livro> livros; // Lista de livros disponíveis na biblioteca
    private static Map<Socket, List<Livro>> livrosAlugados = new HashMap<>(); // Rastreamento de livros alugados por cliente

    /**
     * Construtor para inicializar ClienteHandler com o socket do cliente e a lista de livros.
     *
     * @param clienteSocket o socket do cliente conectado
     * @param livros        a lista de livros disponíveis na biblioteca
     */
    public GerenciadorCliente(Socket clienteSocket, List<Livro> livros) {
        this.clienteSocket = clienteSocket;
        this.livros = livros;
        livrosAlugados.putIfAbsent(clienteSocket, new ArrayList<>()); // Inicializa a lista de livros alugados para este cliente
    }

    /**
     * Método run() implementado da interface Runnable.
     * Este método é chamado quando a thread é iniciada e lida com as requisições do cliente.
     */
    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true)) {

            String requisicaoCliente;
            while ((requisicaoCliente = entrada.readLine()) != null) {
                String resposta = processarRequisicao(requisicaoCliente);
                saida.println(resposta); // Envia a resposta de volta ao cliente
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clienteSocket.close(); // Fecha o socket do cliente após o término da conexão
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método privado para processar a requisição do cliente e retornar uma resposta.
     *
     * @param requisicao a requisição recebida do cliente
     * @return a resposta à requisição do cliente
     */
    private String processarRequisicao(String requisicao) {
        String[] partes = requisicao.split("#");
        String operacao = partes[0];

        switch (operacao) {
            case "listar":
                return listarLivros();
            case "alugar":
                return alugarLivro(partes[1]);
            case "devolver":
                return devolverLivro(partes[1]);
            case "cadastrar":
                return cadastrarLivro(partes[1]);
            case "sair":
                return "Saindo...";
            default:
                return "Operação inválida.";
        }
    }

    /**
     * Método privado para listar todos os livros disponíveis na biblioteca.
     *
     * @return uma string contendo a lista de livros disponíveis
     */
    private String listarLivros() {
        StringBuilder resposta = new StringBuilder();
        for (Livro livro : livros) {
            resposta.append(livro.toString()).append("\n");
        }
        return resposta.toString();
    }

    /**
     * Método privado para alugar um livro da biblioteca.
     *
     * @param nomeLivro o título do livro a ser alugado
     * @return uma mensagem indicando o sucesso ou falha na operação de aluguel
     */
    private String alugarLivro(String nomeLivro) {
        for (Livro livro : livros) {
            if (livro.getTitulo().equals(nomeLivro)) {
                if (livro.getExemplares() > 0) {
                    livro.setExemplares(livro.getExemplares() - 1);
                    livrosAlugados.get(clienteSocket).add(livro); // Adiciona o livro à lista de livros alugados pelo cliente
                    JsonUtils.salvarLivros(livros); // Salva a lista atualizada de livros
                    return "Livro alugado com sucesso.";
                } else {
                    return "Não há exemplares disponíveis para este livro.";
                }
            }
        }
        return "Livro não encontrado.";
    }

    /**
     * Método privado para devolver um livro à biblioteca.
     *
     * @param nomeLivro o título do livro a ser devolvido
     * @return uma mensagem indicando o sucesso ou falha na operação de devolução
     */
    private String devolverLivro(String nomeLivro) {
        List<Livro> livrosCliente = livrosAlugados.get(clienteSocket);
        for (Livro livro : livrosCliente) {
            if (livro.getTitulo().equals(nomeLivro)) {
                livro.setExemplares(livro.getExemplares() + 1);
                livrosCliente.remove(livro); // Remove o livro da lista de livros alugados pelo cliente
                JsonUtils.salvarLivros(livros); // Salva a lista atualizada de livros
                return "Livro devolvido com sucesso.";
            }
        }
        return "Você não alugou este livro ou já o devolveu.";
    }

    /**
     * Método privado para cadastrar um novo livro na biblioteca.
     *
     * @param livroJson uma string contendo os atributos do livro a ser cadastrado
     * @return uma mensagem indicando o sucesso ou falha na operação de cadastro
     */
    private String cadastrarLivro(String livroJson) {
        String[] atributos = livroJson.split(",");
        String autor = atributos[0].trim();
        String titulo = atributos[1].trim();
        String genero = atributos[2].trim();
        int exemplares = Integer.parseInt(atributos[3].trim());
        Livro novoLivro = new Livro(autor, titulo, genero, exemplares);
        livros.add(novoLivro); // Adiciona o novo livro à lista de livros
        JsonUtils.salvarLivros(livros); // Salva a lista atualizada de livros
        return "Livro cadastrado com sucesso.";
    }
}
