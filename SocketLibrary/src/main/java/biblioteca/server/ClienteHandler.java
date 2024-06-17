package biblioteca.server;

import biblioteca.model.Livro;
import biblioteca.utils.JsonUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClienteHandler implements Runnable {
    private Socket clienteSocket;
    private List<Livro> livros;

    public ClienteHandler(Socket clienteSocket, List<Livro> livros) {
        this.clienteSocket = clienteSocket;
        this.livros = livros;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true)) {

            String requisicaoCliente;
            while ((requisicaoCliente = entrada.readLine()) != null) {
                String resposta = processarRequisicao(requisicaoCliente);
                saida.println(resposta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                    JsonUtils.salvarLivros(livros);
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
                JsonUtils.salvarLivros(livros);
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
        JsonUtils.salvarLivros(livros);
        return "Livro cadastrado com sucesso.";
    }
}
