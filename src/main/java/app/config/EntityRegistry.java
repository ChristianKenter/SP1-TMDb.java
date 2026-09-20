package app.config;

import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;
import org.hibernate.cfg.Configuration;

public final class EntityRegistry {

    private EntityRegistry() {}

    public static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(Actor.class);
        configuration.addAnnotatedClass(Director.class);
        configuration.addAnnotatedClass(Genre.class);
        configuration.addAnnotatedClass(Movie.class);
    }
}