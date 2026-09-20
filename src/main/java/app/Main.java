package app;

import app.config.HibernateConfig;
import app.services.MovieService;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieService movieService = new MovieService(emf);
        movieService.insertDataToDatabase(LocalDate.now(),LocalDate.of(2021, 1, 1));

        System.out.println("Total Movies in DB: " + movieService.getAllMovies().size());
        System.out.println("Overall Average Rating: " + movieService.getAverageRating());

    }
}