package app.daos.impl;

import app.daos.IDAO;
import app.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ActorDAO implements IDAO<Actor, Long> {

    private final EntityManagerFactory emf;

    public ActorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Actor create(Actor actor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(actor);
            em.getTransaction().commit();
            return actor;
        }
    }

    @Override
    public Actor read(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Actor.class, id);
        }
    }

    @Override
    public List<Actor> readAll() {
        try (EntityManager em = emf.createEntityManager()) {

            //getResultList() gets the query results and puts them into a List
            return em.createQuery("SELECT a FROM Actor a", Actor.class).getResultList();
        }
    }

    @Override
    public Actor update(Actor actor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Actor merged = em.merge(actor);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Actor actor = em.find(Actor.class, id);
            if (actor != null) em.remove(actor);
            em.getTransaction().commit();
        }
    }
}