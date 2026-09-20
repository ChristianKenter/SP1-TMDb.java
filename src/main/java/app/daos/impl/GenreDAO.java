package app.daos.impl;

import app.daos.IDAO;
import app.entities.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class GenreDAO implements IDAO<Genre, Long> {
    private final EntityManagerFactory emf;

    public GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Genre create(Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();
            return genre;
        }
    }

    @Override
    public Genre read(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Genre.class, id);
        }
    }

    @Override
    public List<Genre> readAll() {
        try (EntityManager em = emf.createEntityManager()) {

            //getResultList() gets the query results and puts them into a List
            return em.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
        }
    }

    @Override
    public Genre update(Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Genre merged = em.merge(genre);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Genre genre = em.find(Genre.class, id);
            if (genre != null) em.remove(genre);
            em.getTransaction().commit();
        }
    }
}