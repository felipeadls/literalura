package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;


@Component
@AllArgsConstructor
public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private String endereco = "https://gutendex.com/books/?search=";
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private AutorRepository autorRepository;

    public Principal() {
    }


    public void exibeMenu() {
        var opcao = -1;

        while(opcao!= 9) {
            var menu = """
                    ***Literalura Livros ***
                    
                    
                    1 - Buscar livros por título.
                    2 - Buscar livros por autores.
                    3 - Listar livros.
                    4 - Listar autores.
                    5 - Listar autores vivos em determinado ano.
                    
                    9 - Sair
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
           leitura.nextLine();


            switch (opcao) {
                case 1 -> buscarLivros();
                case 2 -> buscarAutores();
                case 3 -> listarLivros();
                case 4 -> listarAutores();
                case 5 -> pesquisarDadosDeAutor();
                case 9 -> System.out.println("Saindo da pesquisa!");
                default -> System.out.println("Opção inválida.");
            }

        }
    }


    private void buscarLivros() {
        System.out.println("Qual o nome do livro para busca");
        var nomeLivro = leitura.nextLine();
        var json = consumoApi.obterDados(endereco + nomeLivro.replace(" ", "+"));
        DadosResultado dadosResultado = conversor.obterDados(json, DadosResultado.class);
        exibirInformacoesAutor(dadosResultado);



       /* for (DadosLivro dadosLivro : dadosResultado.livro()) {
            Livro livro = new Livro(dadosLivro);
            for (DadosAutor dadosAutor : dadosLivro.autores()){
                Autor autor = new Autor(dadosAutor);
                if (dadosAutor.anoNascimento() == null) {
                    autor.setDataNascimento(0);
                }

                if (dadosAutor.anoFalecimento() == null) {
                    autor.setDataFalecimento(0);
                }
                livro.getAutores().add(autor);
                autorRepository.save(autor);

            }
            livroRepository.save(livro);
        }*/

    }

    private void exibirInformacoesAutor(DadosResultado dadosResultado) {
        dadosResultado.livro().forEach(livro -> {
            System.out.println("O livro de nome: " + livro.titulo());
            livro.autores().forEach(autor ->
                    System.out.println("O nome do autor é: " + autor.nomeAutor()));
        });

    }

    private void buscarAutores() {
        System.out.println("Digite o nome do autor desejado: ");
        String nomeAutor = leitura.nextLine();
        List<Livro> livrosEncontrados = livroRepository.findAll();
        if (livrosEncontrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o autor " + nomeAutor);
        } else {
            System.out.println("Livros encontrados para o autor " + nomeAutor + ":");
            livrosEncontrados.forEach(livro -> {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Idioma: " + livro.getLinguagem());
                System.out.println("Total de Downloads: " + livro.getTotalDownloads());
                System.out.println("-----------");
            });

        }

    }

    private void listarLivros() {
        List<Livro> livros = livroRepository.findAll();

        if (!livros.isEmpty()) {
            livros.stream().forEach(livro -> {
                System.out.println("O nome do livro é: " + livro.getTitulo() +
                        " O autor é: " + livro.getAutores().getFirst());
            });
        } else {
            System.out.println("Nenhum livro encontrado!");
        }
    }


    private void listarAutores() {
        System.out.println("Qual o nome do autor desejado? ");
        var nomeAutor = leitura.nextLine();
        List<Livro> autoresEncontrados = livroRepository.findAll();
        System.out.println("Livros que: " + nomeAutor + "escreveu");
        autoresEncontrados.forEach(livro ->
                System.out.println(livro.getAutores()));
        }

    private void pesquisarDadosDeAutor() {
    }
}




