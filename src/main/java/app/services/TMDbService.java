package app.services;

import app.dtos.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class TMDbService {

    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    private String accessToken = System.getenv("TMDB_API_READ_ACCESS_TOKEN");
    private ObjectMapper mapper = new ObjectMapper();


    public List<GenreDTO> fetchGenres() {
        try {
            String url = API_BASE_URL + "/genre/movie/list?language=en";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                GenreListDTO genreList = mapper.readValue(response.body(), GenreListDTO.class);
                return genreList.getGenres();
            } else System.out.println("GET genres failed. Status code: " + response.statusCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public List<MovieDTO> fetchMovies(LocalDate from, LocalDate to) {
        List<MovieDTO> movies = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            for (int i = 1; ; i++) {
                String url = String.format("%s/discover/movie?with_origin_country=DK&primary_release_date.gte=%s&primary_release_date.lte=%s&page=%d", API_BASE_URL, from, to, i);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .header("accept", "application/json")
                        .header("Authorization", "Bearer " + accessToken)
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) break;

                TmdbResponseDTO result = mapper.readValue(response.body(), TmdbResponseDTO.class);
                if (result.getResults() == null || result.getResults().isEmpty()) break;

                movies.addAll(result.getResults());
                if (i >= result.getTotalPages()) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    public CreditsDTO fetchMovieCredits(Long movieId) {
        try {
            String url = API_BASE_URL + "/movie/" + movieId + "/credits";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) return mapper.readValue(response.body(), CreditsDTO.class);
            else System.out.println("GET credits failed. Status code: " + response.statusCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}