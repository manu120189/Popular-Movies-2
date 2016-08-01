package demo.popularmoviesi.database;

import java.util.List;

import demo.popularmoviesi.database.models.Movie;

/**
 * Created by predator on 1/10/2016.
 */
public interface PopularMoviesDB {
    void saveMovieToDatabase(Movie movie);
    boolean deleteMovieFromDatabase(String movieId);
    boolean movieExistInDatabase(String movieId);
    Movie getMovieInDb(String movieId);
    List<Movie> getFavoriteMovies();
    void clearMovieTable();

}
