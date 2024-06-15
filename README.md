# Projeto Biblioteca Java

## Descrição
Este projeto implementa um sistema simples de gerenciamento de uma biblioteca utilizando sockets em Java. O servidor da biblioteca permite o registro de clientes e a adição de livros, além de listar os livros disponíveis e os clientes registrados.

## Funcionalidades
- Listar livros
- Alugar livro
- Devolver livro
-  Cadastrar livro

## Arquivos do Projeto
- `Cliente.java`: Define a classe Cliente com atributos nome e id, além de métodos para acessar essas informações.
- `Livro.java`: Define a classe Livro com atributos título, autor, ano de publicação e disponibilidade, além de métodos para acessar e modificar essas informações.
- `BibliotecaServer.java`: Implementa o servidor da biblioteca, permitindo adicionar livros, registrar clientes e listar livros e clientes registrados.

## Participantes do Projeto
- Guilherme Nunes Lobo 
- Felipe Vasconcelos Cardoso

## Instruções para Execução
1. Compile os arquivos Java:
    ```sh
    javac Cliente.java Livro.java BibliotecaServer.java
    ```

2. Inicie o servidor de biblioteca:
    ```sh
    java BibliotecaServer
    ```

3. Inicie o servidor do Cliente:
    ```sh
    java Cliente
    ```
4. O servidor estará pronto para receber registros de clientes e adição de livros.

## Requisitos
- Java JDK 8 ou superior.
