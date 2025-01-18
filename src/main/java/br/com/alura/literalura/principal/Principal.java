package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import java.util.Scanner;


@Component
public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private String endereco = "https://gutendex.com/books/?search=";
    private int contador;
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
                    6 - Buscar quantidade de livro por idioma.
                    
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
                case 6 -> quantidadeDeLivrosPorIdioma();
                case 9 -> { System.out.println("Saindo da pesquisa!");
                    System.exit(0);
                }
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



       for (DadosLivro dadosLivro : dadosResultado.livro()) {
            Livro livro = new Livro(dadosLivro);
            for (DadosAutor dadosAutor : dadosLivro.autores()) {
                Autor autor = new Autor(dadosAutor);
                if (dadosAutor.anoNascimento() == null) {
                    autor.setDataNascimento(0);
                }

                if (dadosAutor.anoFalecimento() == null) {
                    autor.setDataFalecimento(0);
                }

                livro.getAutores().add(autor);

            }
            livroRepository.save(livro);
        }

    }

    private void exibirInformacoesAutor(DadosResultado dadosResultado) {
        DadosLivro livro = dadosResultado.livro().getFirst();

        System.out.println("O livro de nome: " + livro.titulo());
        System.out.println("O autor de nome: " + livro.autores().getFirst().nomeAutor());
        System.out.println("Nascido no ano: " + livro.autores().getFirst().anoNascimento());
        System.out.println("Falecido no ano: " + livro.autores().getFirst().anoFalecimento());


    }

    private void buscarAutores() {
        System.out.println("Digite o nome do autor desejado: ");
        String nomeAutor = leitura.nextLine();
        List<Livro> livrosEncontrados = livroRepository.findByAutorContainingIgnoreCase(nomeAutor);
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
                System.out.println("O nome do livro é: " + livro.getTitulo());
            });
        } else {
            System.out.println("Nenhum livro encontrado!");
        }
    }


    private void listarAutores() {
        List<Autor> autoresEncontrados = autorRepository.findAll();
        autoresEncontrados.forEach(autor -> {
                    System.out.println("Autor encontrado: " + autor.getNomeAutor() + " | ★ Nascido no ano: "
                            + autor.getDataNascimento() + " | ✞ Falecido no ano: " + autor.getDataFalecimento());
                });

        }

    private void pesquisarDadosDeAutor() {
        System.out.println("Digite o ano para busca: ");
        var anoBuscado = leitura.nextInt();
        List<Autor> autoresEncontrados = autorRepository.findAll();
        autoresEncontrados.stream().forEach(data -> {
                    if (anoBuscado >= data.getDataNascimento() && anoBuscado <= data.getDataFalecimento()) {
                        System.out.println("O Autor: " + data.getNomeAutor() + " ★ estava vivo!");
                    }
                }
                );



    }

    private void quantidadeDeLivrosPorIdioma() {

        System.out.println("Digite o idioma para consulta: ");
        var idiomaSelecionado = leitura.nextLine();
        contador = 0;
        List<Livro> livrosEncontrados = livroRepository.findAll();
        livrosEncontrados.stream().forEach(contagem -> {
                if (contagem.getLinguagem().contains(idiomaSelecionado)) {
                    contador++;
                }


        });

        System.out.println("A quantidade de livos nesse idioma é: " + contador);

    }

}




