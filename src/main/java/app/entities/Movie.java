package app.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "movie_title", nullable = false)
    private String movieTitle;

    @Column(name = "movie_rating")
    private Double movieRating;

    @Column(name = "movie_release_date")
    private String movieReleaseDate;

    @Column(name = "movie_popularity")
    private Double moviePopularity;

    @ManyToMany
    @JoinTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "movie_actors", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "actor_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "movie_directors", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "director_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Director> directors = new HashSet<>();
}