package demo.popularmoviesi.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import demo.popularmoviesi.database.models.Movie;

/**
 * Created by predator on 1/10/2016.
 */
public class PopularMoviesDBImpl implements PopularMoviesDB{
    private Context context;

    public PopularMoviesDBImpl(Context context) {
        this.context = context;
    }

    public void saveMovieToDatabase(Movie movie){
        // get our dao
        getMovieDAO().createOrUpdate(movie);
    }

    private RuntimeExceptionDao<Movie, Integer> getMovieDAO() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getMovieDataDao();
    }

    public boolean deleteMovieFromDatabase(String movieId){
        try {
            // get our dao
            Movie movieToDelete = getMovieInDb(movieId);
            getMovieDAO().delete(movieToDelete);
            return true;
        } catch (Exception e) {
            Log.e("", "Error deleting movie");
            return false;
        }

    }

    public boolean movieExistInDatabase(String movieId){
        return getMovieInDb(movieId) != null;
    }

    public Movie getMovieInDb(String movieId){
        try {
            return getMovieDAO().queryBuilder().where().eq(Movie.Keys.MOVIE_ID, movieId).queryForFirst();
        } catch (SQLException e) {
            Log.e("","Error getting movie");
            return null;
        }
    }

    public List<Movie> getFavoriteMovies(){
        try {
            return getMovieDAO().queryBuilder().where().isNotNull(Movie.Keys.MOVIE_ID).query();
        } catch (SQLException e) {
            Log.e("","Error getting movie");
            return null;
        }
    }

    public void clearMovieTable(){
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }
}
