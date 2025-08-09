import models.Autor;
import models.GutendexResponse;
import models.Livro;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final GutendexClient client = new GutendexClient();
    private static final List<Livro> livrosBuscados = new ArrayList<>();

    public static void main(String[] args) {
        boolean executando = true;

        while (executando) {
            mostrarMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> buscarLivros();
                case 2 -> listarLivrosBuscados();
                case 3 -> listarAutores();
                case 4 -> listarLivrosPorIdioma();
                case 5 -> {
                    System.out.println("Saindo...");
                    executando = false;
                }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Catálogo de Livros ===");
        System.out.println("1. Buscar livros por título");
        System.out.println("2. Listar livros buscados");
        System.out.println("3. Listar autores dos livros buscados");
        System.out.println("4. Listar livros por idioma");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // opção inválida
        }
    }

    private static void buscarLivros() {
        System.out.print("Digite o título para buscar: ");
        String titulo = scanner.nextLine();

        try {
            GutendexResponse response = client.buscarLivrosPorTitulo(titulo);
            List<Livro> resultados = response.getResults();

            if (resultados.isEmpty()) {
                System.out.println("Nenhum livro encontrado.");
            } else {
                System.out.println("Livros encontrados:");
                resultados.forEach(System.out::println);

                // Adiciona à lista de buscados (sem duplicatas)
                for (Livro livro : resultados) {
                    if (livrosBuscados.stream().noneMatch(l -> l.getId() == livro.getId())) {
                        livrosBuscados.add(livro);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro na busca: " + e.getMessage());
        }
    }

    private static void listarLivrosBuscados() {
        if (livrosBuscados.isEmpty()) {
            System.out.println("Nenhum livro foi buscado ainda.");
        } else {
            System.out.println("Livros buscados:");
            livrosBuscados.forEach(System.out::println);
        }
    }

    private static void listarAutores() {
        if (livrosBuscados.isEmpty()) {
            System.out.println("Nenhum livro foi buscado ainda.");
            return;
        }
        System.out.println("Autores dos livros buscados:");
        livrosBuscados.stream()
                .flatMap(l -> l.getAuthors().stream())
                .map(Autor::getName)
                .distinct()
                .forEach(System.out::println);
    }

    private static void listarLivrosPorIdioma() {
        if (livrosBuscados.isEmpty()) {
            System.out.println("Nenhum livro foi buscado ainda.");
            return;
        }
        System.out.print("Digite o código do idioma (ex: 'en' para inglês): ");
        String idioma = scanner.nextLine();

        List<Livro> filtrados = livrosBuscados.stream()
                .filter(l -> l.getLanguages() != null && l.getLanguages().contains(idioma))
                .toList();

        if (filtrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado nesse idioma.");
        } else {
            System.out.println("Livros no idioma '" + idioma + "':");
            filtrados.forEach(System.out::println);
        }
    }
}