
# Literalura - Biblioteca Virtual

## Sobre o Projeto
O projeto **Literalura** é uma aplicação Java desenvolvida com Spring Boot que conecta usuários a um vasto catálogo de livros disponibilizados pela API externa [Gutendex](https://gutendex.com/). Ele permite buscar livros por título ou autor, listar obras disponíveis, consultar estatísticas sobre autores e idiomas, além de armazenar os dados coletados em um banco de dados PostgreSQL.

## Funcionalidades
A aplicação oferece as seguintes funcionalidades:

1. **Buscar livros por título:** Pesquisa livros na API e armazena os dados no banco de dados.
2. **Buscar livros por autor:** Encontra livros no banco de dados usando o nome do autor.
3. **Listar livros:** Exibe todos os livros armazenados no banco de dados.
4. **Listar autores:** Mostra a lista de autores disponíveis no banco.
5. **Listar autores vivos em um ano específico:** Filtra autores que estavam vivos em um determinado ano.
6. **Consultar quantidade de livros por idioma:** Conta a quantidade de livros disponíveis em um idioma específico.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **API Gutendex**
- **Maven**

## Estrutura do Projeto

### Pacotes Principais

#### `br.com.alura.literalura.model`
- **`Livro`**: Representa um livro, incluindo título, linguagem, total de downloads e autores relacionados.
- **`Autor`**: Representa um autor com informações como nome, ano de nascimento e falecimento.
- **`DadosLivro` e `DadosAutor`**: Modelos para deserializar dados JSON da API externa.

#### `br.com.alura.literalura.repository`
- **`LivroRepository`**: Interface para operações no banco de dados relacionadas aos livros.
- **`AutorRepository`**: Interface para operações no banco de dados relacionadas aos autores.

#### `br.com.alura.literalura.service`
- **`ConsumoApi`**: Classe para consumir dados da API Gutendex usando o cliente HTTP.
- **`ConverteDados`**: Utilitário para converter dados JSON em objetos Java.

#### `br.com.alura.literalura.principal`
- **`Principal`**: Classe principal da aplicação que gerencia o menu interativo e executa as funcionalidades principais do projeto.

### Banco de Dados: Utilização do PostgreSQL
O **PostgreSQL** é utilizado como banco de dados relacional para persistir as informações obtidas pela API Gutendex, como livros e autores. A integração foi realizada utilizando **Spring Data JPA**.

#### Configuração do Banco de Dados
A conexão com o banco de dados é configurada no arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

#### Estrutura das Tabelas
- **Tabela `livro`:** Armazena informações sobre os livros, como título, idioma, total de downloads e os autores relacionados.
- **Tabela `autor`:** Armazena informações sobre os autores, como nome, ano de nascimento e ano de falecimento.
- Relacionamento: `Livro` e `Autor` possuem uma relação **muitos-para-muitos**, gerenciada pelo JPA.

#### Persistência de Dados
Os dados obtidos da API são processados e armazenados no PostgreSQL. Por exemplo, no método `buscarLivros`, os livros e autores são salvos no banco:
```java
livroRepository.save(livro);
```

#### Consultas Personalizadas
- **Buscar livros por autor:**
```java
List<Livro> livrosEncontrados = livroRepository.findByAutorContainingIgnoreCase(nomeAutor);
```
- **Filtrar autores vivos em um ano específico:**
```java
if (anoBuscado >= autor.getDataNascimento() && anoBuscado <= autor.getDataFalecimento()) {
    System.out.println("O autor estava vivo!");
}
```

## Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/literalura.git
   ```

2. Configure o PostgreSQL:
   - Crie um banco de dados chamado `literalura`.
   - Atualize as credenciais no arquivo `application.properties`.

3. Instale as dependências e execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

4. Interaja com o menu principal no console.

---

Desenvolvido por **Felipe Augusto de Lima Silveira**.
