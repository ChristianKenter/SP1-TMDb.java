package app.services;

import app.dtos.GenreListDTO;
import app.dtos.MovieDTO;
import app.dtos.TmdbResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TMDbServiceTest {

    private TMDbService tmdbService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        tmdbService = new TMDbService();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testServiceInitialization() {
        assertThat(tmdbService, notNullValue());
        assertThat(tmdbService.getMapper(), notNullValue());
    }

    @Test
    void testGenreListJsonMapping() throws Exception {
        String json = "{\"genres\":[{\"id\":18,\"name\":\"Drama\"},{\"id\":35,\"name\":\"Comedy\"}]}";
        GenreListDTO genreList = objectMapper.readValue(json, GenreListDTO.class);

        assertThat(genreList, notNullValue());
        assertThat(genreList.getGenres(), hasSize(2));
        assertThat(genreList.getGenres().get(0).getName(), equalTo("Drama"));
        assertThat(genreList.getGenres().get(1).getId(), equalTo(35L));
    }

    @Test
    void testTmdbResponseJsonMapping() throws Exception {
        String json = "{\"page\":1,\"total_pages\":10,\"total_results\":200,\"results\":[{\"id\":999L,\"title\":\"Danish Masterpiece\",\"vote_average\":8.2,\"popularity\":45.0,\"genre_ids\":[18, 35]}]}";
        String cleanedJson = "{\"page\":1,\"total_pages\":10,\"total_results\":200,\"results\":[{\"id\":999,\"title\":\"Danish Masterpiece\",\"vote_average\":8.2,\"popularity\":45.0,\"genre_ids\":[18, 35]}]}";

        TmdbResponseDTO response = objectMapper.readValue(cleanedJson, TmdbResponseDTO.class);

        assertThat(response, notNullValue());
        assertThat(response.getPage(), equalTo(1));
        assertThat(response.getTotalPages(), equalTo(10));
        assertThat(response.getResults(), hasSize(1));

        MovieDTO movie = response.getResults().get(0);
        assertThat(movie.getId(), equalTo(999L));
        assertThat(movie.getTitle(), equalTo("Danish Masterpiece"));
        assertThat(movie.getVoteAverage(), equalTo(8.2));
        assertThat(movie.getGenreIds(), contains(18L, 35L));
    }
}