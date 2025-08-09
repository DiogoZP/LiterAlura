package models;
import java.util.List;

public class GutendexResponse {
    private int count;
    private String next;
    private String previous;
    private List<Livro> results;

    public List<Livro> getResults() {
        return results;
    }
}
