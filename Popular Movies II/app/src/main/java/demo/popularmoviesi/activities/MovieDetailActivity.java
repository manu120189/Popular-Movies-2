package demo.popularmoviesi.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import demo.popularmoviesi.MainActivity;
import demo.popularmoviesi.R;
import demo.popularmoviesi.api.objects.MovieResponse;
import demo.popularmoviesi.fragments.MovieDetailFragment;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MainActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity implements MovieDetailFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            int position = getIntent().getIntExtra("MOVIE_POS", 0);
            MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(MainActivity.getMoviesInMemory().get(position));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, movieDetailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void loadMovieDetailFragment(MovieResponse.Movie movie) {

    }

    @Override
    public void refreshMoviesAfterDeletion() {

    }

    public boolean isInternetAvailable() {
        return ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
