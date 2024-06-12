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
    private static List<Livro> livros;

    public static void main(String[] args) {
        carregarLivros();

        final int PORTA = 12344;

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
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(ARQUIVO_JSON);
        if (file.exists()) {
            try {
                JsonNode rootNode = mapper.readTree(file);
                JsonNode livrosNode = rootNode.path("livros");
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Livro.class);
                livros = mapper.convertValue(livrosNode, listType);
            } catch (IOException e) {
                e.printStackTrace();
                livros = new ArrayList<>();
            }
        } else {
            System.out.println("Arquivo JSON não encontrado, criando novo arquivo...");
            livros = new ArrayList<>();
            salvarLivros(); // Save an empty list to create the file
        }
    }

    private static synchronized void salvarLivros() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Wrap the list in an object with a "livros" key
            mapper.writeValue(new File(ARQUIVO_JSON), new LivrosWrapper(livros));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClienteHandler implements Runnable {
        private Socket clienteSocket;

        public ClienteHandler(Socket clienteSocket) {
            this.clienteSocket = clienteSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true);

                String requisicaoCliente;
                while ((requisicaoCliente = entrada.readLine()) != null) {
                    String resposta = processarRequisicao(requisicaoCliente);
                    saida.println(resposta);
                }

                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        private String listarLivros() {
            StringBuilder resposta = new StringBuilder();
            for (Livro livro : livros) {
                resposta.append(livro.toString()).append("\n");
            }
            return resposta.toString();
        }

        private String alugarLivro(String nomeLivro) {
            for (Livro livro : livros) {
                if (livro.getTitulo().equals(nomeLivro)) {
                    if (livro.getExemplares() > 0) {
                        livro.setExemplares(livro.getExemplares() - 1);
                        salvarLivros();
                        return "Livro alugado com sucesso.";
                    } else {
                        return "Não há exemplares disponíveis para este livro.";
                    }
                }
            }
            return "Livro não encontrado.";
        }

        private String devolverLivro(String nomeLivro) {
            for (Livro livro : livros) {
                if (livro.getTitulo().equals(nomeLivro)) {
                    livro.setExemplares(livro.getExemplares() + 1);
                    salvarLivros();
                    return "Livro devolvido com sucesso.";
                }
            }
            return "Livro não encontrado.";
        }

        private String cadastrarLivro(String livroJson) {
            String[] atributos = livroJson.split(",");
            String autor = atributos[0].trim();
            String titulo = atributos[1].trim();
            String genero = atributos[2].trim();
            int exemplares = Integer.parseInt(atributos[3].trim());
            Livro novoLivro = new Livro(autor, titulo, genero, exemplares);
            livros.add(novoLivro);
            salvarLivros();
            return "Livro cadastrado com sucesso.";
        }
    }

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
