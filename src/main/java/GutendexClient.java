import com.google.gson.Gson;
import models.GutendexResponse;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class GutendexClient {
    private static final String BASE_URL = "https://gutendex.com/books";

    private final HttpClient httpClient;
    private final Gson gson;

    public GutendexClient() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        this.gson = new Gson();
    }

    public GutendexResponse buscarLivrosPorTitulo(String titulo) throws Exception {
        String query = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = BASE_URL + "?search=" + query;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status code: " + response.statusCode());
        System.out.println("Body: " + response.body());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), GutendexResponse.class);
        } else {
            System.err.println("Erro HTTP na requisição");
            return null;
        }
    }
}
