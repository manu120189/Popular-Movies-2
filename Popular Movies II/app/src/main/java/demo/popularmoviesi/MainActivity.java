package demo.popularmoviesi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import demo.popularmoviesi.activities.MovieDetailActivity;
import demo.popularmoviesi.adapters.MovieAdapter;
import demo.popularmoviesi.api.MovieService;
import demo.popularmoviesi.api.objects.MovieResponse;
import demo.popularmoviesi.database.PopularMoviesDBImpl;
import demo.popularmoviesi.database.models.Movie;
import demo.popularmoviesi.fragments.MovieDetailFragment;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MainActivity extends AppCompatActivity implements MovieDetailFragment.OnFragmentInteractionListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static final String TAG_MOVIE_LIST_FRAGMENT = "TAG_MOVIE_LIST_FRAGMENT";
    public static final String MOVIE_DETAIL_FRAGMENT = "MOVIE_DETAIL_FRAGMENT";
    private MovieService movieService;
    //API KEY GOES HERE
    public static final String MOVIE_API_KEY = "";
    private static List<MovieResponse.Movie> moviesInMemory;
    private static final String SORT_MOST_POPULAR = "popularity.desc";
    private static final String SORT_HIGHEST_RATED = "vote_average.desc";
    private static final String SORT_FAVORITES = "favorites";
    private PopularMoviesDBImpl moviesDB;
    private String currentSort;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        moviesDB = new PopularMoviesDBImpl(this);
        //moviesDB.clearMovieTable();
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout

        loadMovies(SORT_MOST_POPULAR);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        loadMovies(currentSort);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_favorites) {
            loadMovies(SORT_FAVORITES);
        }else if(id == R.id.sort_highest_rated){
            loadMovies(SORT_HIGHEST_RATED);
        }else if(id == R.id.sort_most_popular){
            loadMovies(SORT_MOST_POPULAR);
        }

        return super.onOptionsItemSelected(item);
    }
    public void loadMovies(String sort) {
        currentSort = sort;
        if (sort.equals(SORT_FAVORITES)) {
            //search in database
            List<Movie> favoriteMovies = moviesDB.getFavoriteMovies();
            if(favoriteMovies != null && favoriteMovies.size() > 0){
                List<MovieResponse.Movie> movies = toResponseMovieList(favoriteMovies);
                setMoviesInMemory(movies);
                refreshMovieList();
            }else{
                //show empty message
                setMoviesInMemory(new ArrayList<MovieResponse.Movie>());
                refreshMovieList();
                Toast.makeText(getApplicationContext(), "Favorite list empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!isInternetAvailable()){
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
            //search in api, validate network connection
            movieService = new MovieService();
            final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading. Please wait...", true);
            movieService.getMovieAPI()
                    .getPopularMovies(MOVIE_API_KEY, sort)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(), "Error loading movies", Toast.LENGTH_SHORT).show();
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(MovieResponse response) {
                            if (response != null) {
                                setMoviesInMemory(response.getPopularMovies());
                                refreshMovieList();
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        }
                    });
        }
    }

    public List<MovieResponse.Movie> toResponseMovieList(List<Movie> movies) {
        if (movies == null)
            return null;
        List<MovieResponse.Movie> responseMovies = new ArrayList<>();
        for (Movie movie : movies) {
            MovieResponse.Movie responseMovie = new MovieResponse().new Movie();
            responseMovie.setTitle(movie.getTitle());
            responseMovie.setReleaseDate(movie.getReleaseDate());
            responseMovie.setRate(movie.getRate());
            responseMovie.setOverview(movie.getOverview());
            responseMovie.setId(movie.getMovieId());
            responseMovie.setMovieImage(movie.getMovieImage());
            responseMovies.add(responseMovie);
        }
        return responseMovies;
    }

    public boolean isInternetAvailable() {
        return ((ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public void refreshMoviesAfterDeletion(){
        loadMovies(currentSort);
    }
    public void refreshMovieList() {
        View gridView = findViewById(R.id.item_list);
        assert gridView != null;
        setupRecyclerView((GridView) gridView);
    }

    public static List<MovieResponse.Movie> getMoviesInMemory() {
        return moviesInMemory;
    }

    public static void setMoviesInMemory(List<MovieResponse.Movie> moviesInMemory) {
        MainActivity.moviesInMemory = moviesInMemory;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void loadMovieDetailFragment(MovieResponse.Movie movie) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MovieDetailFragment mdf = MovieDetailFragment.newInstance(movie);
        ft.replace(R.id.fragment_container, mdf);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(MOVIE_DETAIL_FRAGMENT);
        ft.commit();
    }

    private void setupRecyclerView(@NonNull GridView gridView) {
        gridView.setAdapter(new MovieAdapter(this, getMoviesInMemory()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (mTwoPane) {
                    MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(getMoviesInMemory().get(position));
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, movieDetailFragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("MOVIE_POS", position);
                    context.startActivity(intent);
                }
            }
        });
    }


}
