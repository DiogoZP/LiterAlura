package models;

import java.util.List;

public class Livro {
    private int id;
    private String title;
    private List<Autor> authors;
    private List<String> languages;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public List<Autor> getAuthors() { return authors; }
    public List<String> getLanguages() { return languages; }

    @Override
    public String toString() {
        String autores = authors != null ? authors.stream().map(Autor::getName).reduce((a,b)->a+", "+b).orElse("Nenhum") : "Nenhum";
        return "Livro{id=" + id + ", title='" + title + "', authors=" + autores + "}";
    }
}