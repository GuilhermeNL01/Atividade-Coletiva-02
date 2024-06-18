# Projeto Biblioteca Java

## Descrição
Este projeto implementa um sistema simples de gerenciamento de uma biblioteca utilizando sockets em Java. O servidor da biblioteca permite o registro de clientes e a adição de livros, além de listar os livros disponíveis e os clientes registrados.

## Funcionalidades
- Listar livros
- Alugar livro
- Devolver livro
- Cadastrar livro

## Arquivos do Projeto
- `Cliente.java`: Define a classe Cliente com atributos nome e id, além de métodos para acessar essas informações. Localizado em `src/main/java/biblioteca/client/`.
- `Livro.java`: Define a classe Livro com atributos título, autor, ano de publicação e disponibilidade, além de métodos para acessar e modificar essas informações. Localizado em `src/main/java/biblioteca/model/`.
- `BibliotecaServer.java`: Implementa o servidor da biblioteca, permitindo adicionar livros, registrar clientes e listar livros e clientes registrados. Localizado em `src/main/java/biblioteca/server/`.
- `ClienteHandler.java`: Gerencia a comunicação com os clientes conectados ao servidor. Localizado em `src/main/java/biblioteca/server/`.
- `JsonUtils.java`: Utilitários para manipulação de JSON, como leitura e escrita de arquivos JSON. Localizado em `src/main/java/biblioteca/utils/`.
- `livros.json`: Arquivo JSON contendo os dados dos livros. Localizado em `src/main/resources/`.

## Participantes do Projeto
- Guilherme Nunes Lobo
- Felipe Vasconcelos Cardoso

## Instruções para Execução
1. Inicie o servidor de biblioteca
2. Inicie o cliente
3. Agora será possível executar as ações do Cliente


6. O servidor estará pronto para receber registros de clientes e adição de livros.

## Requisitos
- Java JDK 8 ou superior.
