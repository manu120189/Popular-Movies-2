package demo.popularmoviesi.api.objects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import demo.popularmoviesi.database.models.Movie;

/**
 * Created by emma on 01/11/2015.
 */
public class MovieResponse {
    public int page;
    public List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getPopularMovies() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public class Movie {

        @SerializedName("id") public String id;
        @SerializedName("poster_path") public String movieImage;
        @SerializedName("original_title") public String title;
        @SerializedName("overview") public String overview;
        @SerializedName("release_date") public String releaseDate;
        @SerializedName("vote_average") public Double rate;


        public String getMovieImage() {
            return movieImage;
        }

        public String getTitle() {
            return title;
        }

        public String getOverview() {
            return overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public Double getRate() {
            return rate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setMovieImage(String movieImage) {
            this.movieImage = movieImage;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public demo.popularmoviesi.database.models.Movie toDBMovie(){
            demo.popularmoviesi.database.models.Movie movieDb = new demo.popularmoviesi.database.models.Movie();
            movieDb.setMovieId(this.getId());
            movieDb.setMovieImage(this.getMovieImage());
            movieDb.setOverview(this.getOverview());
            movieDb.setRate(this.getRate());
            movieDb.setReleaseDate(this.getReleaseDate());
            movieDb.setTitle(this.getTitle());
            return movieDb;
        }


    }

}
