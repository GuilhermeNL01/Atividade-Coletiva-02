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

public class JsonUtils {
    private static final String ARQUIVO_JSON = "src/main/resources/livros.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static synchronized List<Livro> carregarLivros() {
        File file = new File(ARQUIVO_JSON);
        if (file.exists()) {
            try {
                JsonNode rootNode = mapper.readTree(file);
                JsonNode livrosNode = rootNode.path("livros");
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Livro.class);
                return mapper.convertValue(livrosNode, listType);
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } else {
            System.out.println("Arquivo JSON não encontrado, criando novo arquivo...");
            List<Livro> livros = new ArrayList<>();
            salvarLivros(livros);
            return livros;
        }
    }

    public static synchronized void salvarLivros(List<Livro> livros) {
        try {
            mapper.writeValue(new File(ARQUIVO_JSON), new LivrosWrapper(livros));
        } catch (IOException e) {
            e.printStackTrace();
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
