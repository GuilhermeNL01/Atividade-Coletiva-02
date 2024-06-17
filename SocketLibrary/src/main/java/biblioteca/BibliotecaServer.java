package biblioteca;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaServer {
    private static final String ARQUIVO_JSON = "src/main/resources/livros.json"; // Caminho do arquivo JSON com os dados dos livros
    private static List<Livro> livros; // Lista de livros

    public static void main(String[] args) {
        carregarLivros(); // Carrega os livros do arquivo JSON

        final int PORTA = 1336; // Porta do servidor

        try {
            ServerSocket servidorSocket = new ServerSocket(PORTA); // Cria um ServerSocket na porta especificada
            System.out.println("Servidor pronto para receber conexões...");

            while (true) {
                Socket clienteSocket = servidorSocket.accept(); // Aceita uma conexão de um cliente
                System.out.println("Cliente conectado: " + clienteSocket);

                // Cria uma nova thread para lidar com o cliente
                Thread clienteThread = new Thread(new ClienteHandler(clienteSocket));
                clienteThread.start(); // Inicia a thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método sincronizado para carregar os livros do arquivo JSON
    private static synchronized void carregarLivros() {
        ObjectMapper mapper = new ObjectMapper(); // Objeto para mapear JSON
        File file = new File(ARQUIVO_JSON);
        if (file.exists()) {
            try {
                JsonNode rootNode = mapper.readTree(file); // Lê o arquivo JSON
                JsonNode livrosNode = rootNode.path("livros"); // Extrai o nó "livros"
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Livro.class);
                livros = mapper.convertValue(livrosNode, listType); // Converte o nó JSON em uma lista de objetos Livro
            } catch (IOException e) {
                e.printStackTrace();
                livros = new ArrayList<>(); // Se ocorrer um erro, inicializa a lista de livros como vazia
            }
        } else {
            System.out.println("Arquivo JSON não encontrado, criando novo arquivo...");
            livros = new ArrayList<>(); // Inicializa a lista de livros como vazia
            salvarLivros(); // Salva a lista vazia no arquivo JSON
        }
    }

    // Método sincronizado para salvar os livros no arquivo JSON
    private static synchronized void salvarLivros() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Configura o ObjectMapper para usar a saída indentada
        try {
            // Envolve a lista de livros em um objeto com a chave "livros"
            mapper.writeValue(new File(ARQUIVO_JSON), new LivrosWrapper(livros));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe interna para lidar com as conexões dos clientes
    static class ClienteHandler implements Runnable {
        private Socket clienteSocket;

        public ClienteHandler(Socket clienteSocket) {
            this.clienteSocket = clienteSocket;
        }

        @Override
        public void run() {
            try {
                // Streams para entrada e saída de dados
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true);

                String requisicaoCliente;
                while ((requisicaoCliente = entrada.readLine()) != null) {
                    String resposta = processarRequisicao(requisicaoCliente); // Processa a requisição do cliente
                    saida.println(resposta); // Envia a resposta para o cliente
                }

                clienteSocket.close(); // Fecha a conexão com o cliente
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Método para processar a requisição do cliente
        private String processarRequisicao(String requisicao) {
            String[] partes = requisicao.split("#"); // Divide a requisição em partes
            String operacao = partes[0];

            switch (operacao) {
                case "listar":
                    return listarLivros(); // Lista todos os livros
                case "alugar":
                    return alugarLivro(partes[1]); // Aluga um livro pelo título
                case "devolver":
                    return devolverLivro(partes[1]); // Devolve um livro pelo título
                case "cadastrar":
                    return cadastrarLivro(partes[1]); // Cadastra um novo livro
                case "sair":
                    return "Saindo..."; // Finaliza a conexão
                default:
                    return "Operação inválida."; // Operação não reconhecida
            }
        }

        // Método para listar todos os livros
        private String listarLivros() {
            StringBuilder resposta = new StringBuilder();
            for (Livro livro : livros) {
                resposta.append(livro.toString()).append("\n");
            }
            return resposta.toString();
        }

        // Método para alugar um livro pelo título
        private String alugarLivro(String nomeLivro) {
            for (Livro livro : livros) {
                if (livro.getTitulo().equals(nomeLivro)) {
                    if (livro.getExemplares() > 0) {
                        livro.setExemplares(livro.getExemplares() - 1); // Decrementa o número de exemplares
                        salvarLivros(); // Salva a lista de livros atualizada
                        return "Livro alugado com sucesso.";
                    } else {
                        return "Não há exemplares disponíveis para este livro.";
                    }
                }
            }
            return "Livro não encontrado.";
        }

        // Método para devolver um livro pelo título
        private String devolverLivro(String nomeLivro) {
            for (Livro livro : livros) {
                if (livro.getTitulo().equals(nomeLivro)) {
                    livro.setExemplares(livro.getExemplares() + 1); // Incrementa o número de exemplares
                    salvarLivros(); // Salva a lista de livros atualizada
                    return "Livro devolvido com sucesso.";
                }
            }
            return "Livro não encontrado.";
        }

        // Método para cadastrar um novo livro a partir de uma string JSON
        private String cadastrarLivro(String livroJson) {
            String[] atributos = livroJson.split(",");
            String autor = atributos[0].trim();
            String titulo = atributos[1].trim();
            String genero = atributos[2].trim();
            int exemplares = Integer.parseInt(atributos[3].trim());
            Livro novoLivro = new Livro(autor, titulo, genero, exemplares);
            livros.add(novoLivro); // Adiciona o novo livro à lista
            salvarLivros(); // Salva a lista de livros atualizada
            return "Livro cadastrado com sucesso.";
        }
    }

    // Classe wrapper para envolver a lista de livros em um objeto JSON
    static class LivrosWrapper {
        private List<Livro> livros;

        public LivrosWrapper(List<Livro> livros) {
            this.livros = livros;
        }

        public List<Livro> getLivros() {
            return livros;
        }

        public void setLivros(List<Livro> livros) {
            this.livros = livros;
        }
    }
}
