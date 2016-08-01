package demo.popularmoviesi.database.models;

import com.j256.ormlite.field.DatabaseField;

public class Movie {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField(unique = true)
    public String movieId;
    @DatabaseField
    public String movieImage;
    @DatabaseField
    public String title;
    @DatabaseField
    public String overview;
    @DatabaseField
    public String releaseDate;
    @DatabaseField
    public Double rate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public static class Keys{
        public static final String ID = "id";
        public static final String MOVIE_ID = "movieId";
        public static final String MOVIE_IMAGE = "movieImage";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String RATE = "rate";
    }
}
