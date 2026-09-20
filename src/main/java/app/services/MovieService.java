package app.services;

import app.daos.impl.ActorDAO;
import app.daos.impl.DirectorDAO;
import app.daos.impl.GenreDAO;
import app.daos.impl.MovieDAO;
import app.dtos.CreditsDTO;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieService {
    private final MovieDAO movieDAO;
    private final ActorDAO actorDAO;
    private final DirectorDAO directorDAO;
    private final GenreDAO genreDAO;
    private final TMDbService tmdbService;

    public MovieService(EntityManagerFactory emf) {
        this.movieDAO = new MovieDAO(emf);
        this.actorDAO = new ActorDAO(emf);
        this.directorDAO = new DirectorDAO(emf);
        this.genreDAO = new GenreDAO(emf);
        this.tmdbService = new TMDbService();
    }

    public void insertDataToDatabase(LocalDate from, LocalDate to) {
        List<GenreDTO> genreDTOs = tmdbService.fetchGenres();
        Map<Long, Genre> genreMap = new HashMap<>();

        for (GenreDTO g : genreDTOs) {
            Genre genre = new Genre();
            genre.setGenreId(g.getId());
            genre.setGenreName(g.getName());
            Genre existingGenre = genreDAO.read(g.getId());

            if (existingGenre == null) existingGenre = genreDAO.create(genre);
            genreMap.put(existingGenre.getGenreId(), existingGenre);
        }

        List<MovieDTO> movieDTOs = tmdbService.fetchMovies(from, to);
        for (MovieDTO dto : movieDTOs) {

            if (movieDAO.read(dto.getId()) != null) continue;
            Movie movie = new Movie();
            movie.setMovieId(dto.getId());
            movie.setMovieTitle(dto.getTitle());
            movie.setMovieRating(dto.getVoteAverage());
            movie.setMovieReleaseDate(dto.getReleaseDate());
            movie.setMoviePopularity(dto.getPopularity());

            if (dto.getGenreIds() != null) {
                for (Long gId : dto.getGenreIds()) {
                    Genre genre = genreMap.get(gId);
                    if (genre != null) movie.getGenres().add(genre);
                }
            }

            CreditsDTO credits = tmdbService.fetchMovieCredits(dto.getId());
            if (credits != null) {
                if (credits.getCast() != null) {
                    int count = 0;

                    for (var c : credits.getCast()) {
                        if (count >= 10) break;
                        Actor actor = actorDAO.read(c.getId());

                        if (actor == null) {
                            actor = new Actor();
                            actor.setActorId(c.getId());
                            actor.setActorName(c.getName());
                            actor = actorDAO.create(actor);
                        }
                        movie.getActors().add(actor);
                        count++;
                    }
                }

                if (credits.getCrew() != null) {
                    for (var cr : credits.getCrew()) {
                        if ("Director".equalsIgnoreCase(cr.getJob())) {

                            Director director = directorDAO.read(cr.getId());
                            if (director == null) {
                                director = new Director();
                                director.setDirectorId(cr.getId());
                                director.setDirectorName(cr.getName());
                                director = directorDAO.create(director);
                            }
                            movie.getDirectors().add(director);
                        }
                    }
                }
            }
            movieDAO.create(movie);
        }
    }

    public List<Movie> getAllMovies() {
        return movieDAO.readAll();
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return movieDAO.findByTitle(title);
    }

    public Double getAverageRating() {
        return movieDAO.getTotalAverageRating();
    }

    public List<Movie> getHighestRated(int limit) {
        return movieDAO.getTopHighestRated(limit);
    }

    public List<Movie> getLowestRated(int limit) {
        return movieDAO.getTopLowestRated(limit);
    }

    public List<Movie> getMostPopular(int limit) {
        return movieDAO.getTopPopular(limit);
    }

    public Movie updateMovieTitleAndDate(Long id, String newTitle, String newDate) {
        Movie movie = movieDAO.read(id);
        if (movie == null) {
            throw new RuntimeException("Movie not found");
        }
        movie.setMovieTitle(newTitle);
        movie.setMovieReleaseDate(newDate);
        return movieDAO.update(movie);
    }

    public void deleteMovie(Long id) {
        movieDAO.delete(id);
    }
}