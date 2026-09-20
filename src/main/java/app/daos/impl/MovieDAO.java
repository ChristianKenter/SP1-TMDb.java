package app.daos.impl;

import app.daos.IDAO;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MovieDAO implements IDAO<Movie, Long> {
    private final EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Movie create(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        }
    }

    @Override
    public Movie read(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Movie.class, id);
        }
    }

    @Override
    public List<Movie> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        }
    }

    @Override
    public Movie update(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie merged = em.merge(movie);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            if (movie != null) em.remove(movie);
            em.getTransaction().commit();
        }
    }

    public List<Movie> findByTitle(String titleQuery) {
        try (EntityManager em = emf.createEntityManager()) {

            //LOWER() converts text to lowercase.
            //LIKE/Matches.
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE LOWER(m.movieTitle) LIKE LOWER(:title)", Movie.class);
            query.setParameter("title", "%" + titleQuery + "%");
            return query.getResultList();
        }
    }

    public Double getTotalAverageRating() {
        try (EntityManager em = emf.createEntityManager()) {

            //getSingleResult() gets a single result from the query.
            //AVG() gets the average.
            Double avg = em.createQuery("SELECT AVG(m.movieRating) FROM Movie m", Double.class).getSingleResult();
            return avg != null ? avg : 0.0;
        }
    }

    public List<Movie> getTopHighestRated(int limit) {
        try (EntityManager em = emf.createEntityManager()) {

            //DESC descending order. Sorts from highest to lowest.
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.movieRating DESC", Movie.class).setMaxResults(limit).getResultList();
        }
    }

    public List<Movie> getTopLowestRated(int limit) {

        //ASC ascending order. Sorts from lowest to highest.
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m WHERE m.movieRating IS NOT NULL ORDER BY m.movieRating ASC", Movie.class).setMaxResults(limit).getResultList();
        }
    }

    public List<Movie> getTopPopular(int limit) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.moviePopularity DESC", Movie.class).setMaxResults(limit).getResultList();
        }
    }
}