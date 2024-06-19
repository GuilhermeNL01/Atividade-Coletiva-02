package biblioteca.utils;

import biblioteca.model.Livro;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * A classe JsonUtils fornece métodos para carregar e salvar dados de livros em formato JSON.
 * Ela utiliza a biblioteca Jackson para realizar operações de serialização e desserialização.
 */
public class JsonUtils {
    private static final String ARQUIVO_JSON = "src/main/resources/livros.json"; // Caminho do arquivo JSON
    private static final ObjectMapper mapper = new ObjectMapper(); // Objeto ObjectMapper para operações JSON

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Configura para indentar a saída JSON
    }

    /**
     * Método para carregar a lista de livros a partir do arquivo JSON.
     *
     * @return uma lista de livros carregada do arquivo JSON, ou uma lista vazia se o arquivo não existir
     */
    public static synchronized List<Livro> carregarLivros() {
        File file = new File(ARQUIVO_JSON);
        if (file.exists()) {
            try {
                JsonNode rootNode = mapper.readTree(file); // Lê o JSON e obtém o nó raiz
                JsonNode livrosNode = rootNode.path("livros"); // Obtém o nó "livros"
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Livro.class);
                return mapper.convertValue(livrosNode, listType); // Converte o nó "livros" para lista de Livro
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>(); // Retorna lista vazia em caso de erro de leitura
            }
        } else {
            System.out.println("Arquivo JSON não encontrado, criando novo arquivo...");
            List<Livro> livros = new ArrayList<>();
            salvarLivros(livros); // Cria um novo arquivo JSON com lista vazia
            return livros;
        }
    }

    /**
     * Método para salvar a lista de livros no arquivo JSON.
     *
     * @param livros a lista de livros a ser salva no arquivo JSON
     */
    public static synchronized void salvarLivros(List<Livro> livros) {
        try {
            mapper.writeValue(new File(ARQUIVO_JSON), new LivrosWrapper(livros)); // Escreve a lista de livros no arquivo JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Classe interna LivrosWrapper utilizada para envolver a lista de livros para serialização.
     */
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
