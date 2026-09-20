package app.services;

import app.config.EntityRegistry;
import app.daos.impl.MovieDAO;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static EntityManagerFactory emf;
    private MovieService movieService;
    private MovieDAO movieDAO;

    @BeforeAll
    static void setUpAll() {
        postgres.start();

        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.connection.url", postgres.getJdbcUrl());
        props.put("hibernate.connection.username", postgres.getUsername());
        props.put("hibernate.connection.password", postgres.getPassword());
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.show_sql", "false");

        Configuration configuration = new Configuration();
        configuration.setProperties(props);
        EntityRegistry.registerEntities(configuration);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        emf = configuration.buildSessionFactory(serviceRegistry).unwrap(EntityManagerFactory.class);
    }

    @BeforeEach
    void setUp() {
        movieService = new MovieService(emf);
        movieDAO = new MovieDAO(emf);
        clearDatabase();
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
        postgres.stop();
    }

    private void clearDatabase() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM movie_directors").executeUpdate();
            em.createNativeQuery("DELETE FROM movie_actors").executeUpdate();
            em.createNativeQuery("DELETE FROM movie_genres").executeUpdate();
            em.createNativeQuery("DELETE FROM movies").executeUpdate();
            em.createNativeQuery("DELETE FROM actors").executeUpdate();
            em.createNativeQuery("DELETE FROM directors").executeUpdate();
            em.createNativeQuery("DELETE FROM genres").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    void testSearchMoviesByTitle() {
        Movie m1 = new Movie();
        m1.setMovieId(101L);
        m1.setMovieTitle("Another Round");
        m1.setMovieRating(7.8);
        movieDAO.create(m1);

        Movie m2 = new Movie();
        m2.setMovieId(102L);
        m2.setMovieTitle("The Promised Land");
        m2.setMovieRating(7.7);
        movieDAO.create(m2);

        List<Movie> results = movieService.searchMoviesByTitle("round");
        assertThat(results, hasSize(1));
        assertThat(results.get(0).getMovieTitle(), equalTo("Another Round"));
    }

    @Test
    void testGetAverageRating() {
        Movie m1 = new Movie();
        m1.setMovieId(201L);
        m1.setMovieTitle("Movie A");
        m1.setMovieRating(8.0);
        movieDAO.create(m1);

        Movie m2 = new Movie();
        m2.setMovieId(202L);
        m2.setMovieTitle("Movie B");
        m2.setMovieRating(6.0);
        movieDAO.create(m2);

        Double avg = movieService.getAverageRating();
        assertThat(avg, closeTo(7.0, 0.001));
    }

    @Test
    void testGetHighestAndLowestRated() {
        Movie m1 = new Movie();
        m1.setMovieId(301L);
        m1.setMovieTitle("Bad Movie");
        m1.setMovieRating(3.0);
        movieDAO.create(m1);

        Movie m2 = new Movie();
        m2.setMovieId(302L);
        m2.setMovieTitle("Masterpiece");
        m2.setMovieRating(9.5);
        movieDAO.create(m2);

        List<Movie> highest = movieService.getHighestRated(1);
        assertThat(highest, hasSize(1));
        assertThat(highest.get(0).getMovieTitle(), equalTo("Masterpiece"));

        List<Movie> lowest = movieService.getLowestRated(1);
        assertThat(lowest, hasSize(1));
        assertThat(lowest.get(0).getMovieTitle(), equalTo("Bad Movie"));
    }

    @Test
    void testUpdateMovieTitleAndDate() {
        Movie m = new Movie();
        m.setMovieId(401L);
        m.setMovieTitle("Old Title");
        m.setMovieReleaseDate("2020-01-01");
        movieDAO.create(m);

        movieService.updateMovieTitleAndDate(401L, "New Title", "2024-05-05");

        Movie updated = movieDAO.read(401L);
        assertThat(updated, notNullValue());
        assertThat(updated.getMovieTitle(), equalTo("New Title"));
        assertThat(updated.getMovieReleaseDate(), equalTo("2024-05-05"));
    }

    @Test
    void testDeleteMovie() {
        Movie m = new Movie();
        m.setMovieId(501L);
        m.setMovieTitle("To Be Deleted");
        m.setMovieRating(5.0);
        movieDAO.create(m);

        movieService.deleteMovie(501L);

        assertThat(movieDAO.read(501L), nullValue());
    }

    @Test
    void testCreateAndReadMovie() {
        Movie movie = new Movie();
        movie.setMovieId(9999L);
        movie.setMovieTitle("Test Danish Movie");
        movie.setMovieRating(8.5);
        movie.setMovieReleaseDate("2024-01-01");
        movie.setMoviePopularity(50.0);

        movieDAO.create(movie);

        Movie retrieved = movieDAO.read(9999L);
        assertThat(retrieved, notNullValue());
        assertThat(retrieved.getMovieTitle(), equalTo("Test Danish Movie"));
    }
}