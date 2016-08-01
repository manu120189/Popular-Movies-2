package demo.popularmoviesi.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import demo.popularmoviesi.activities.MovieDetailActivity;
import demo.popularmoviesi.MainActivity;
import demo.popularmoviesi.R;
import demo.popularmoviesi.adapters.ReviewAdapter;
import demo.popularmoviesi.adapters.TrailerAdapter;
import demo.popularmoviesi.api.MovieService;
import demo.popularmoviesi.api.objects.MovieResponse;
import demo.popularmoviesi.api.objects.MovieReviewResponse;
import demo.popularmoviesi.api.objects.MovieTrailerResponse;
import demo.popularmoviesi.database.PopularMoviesDB;
import demo.popularmoviesi.database.PopularMoviesDBImpl;
import demo.popularmoviesi.database.models.Movie;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment{

    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private MovieResponse.Movie movie;
    private OnFragmentInteractionListener mListener;
    private MovieService movieService;

    private TrailerAdapter trailerAdapter;
    private ListView trailerListView;
    private List<MovieTrailerResponse.Trailer> movieTrailers;

    private ReviewAdapter reviewAdapter;
    private ListView reviewListView;
    private List<MovieReviewResponse.Review> reviews;

    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    PopularMoviesDB moviesDB;

    private Switch favoriteSwitch;

    public static MovieDetailFragment newInstance(MovieResponse.Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setMovie(movie);
        return fragment;
    }
    public void setMovie(MovieResponse.Movie movie){
        this.movie = movie;
    }
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }
    @Override
    public void onStart(){
        super.onStart();

        moviesDB = new PopularMoviesDBImpl(getActivity());

        favoriteSwitch = (Switch) getActivity().findViewById(R.id.mySwitch);
        //check for movie in db
        boolean movieExistInDatabase = moviesDB.movieExistInDatabase(movie.getId());
        //check the current state before we display the screen
        if(movieExistInDatabase){
            favoriteSwitch.setText("Unmark as favorite");
            favoriteSwitch.setChecked(true);
        }else {
            favoriteSwitch.setText("Mark as favorite");
        }
        //attach a listener to check for changes in state
        favoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    favoriteSwitch.setText("Unmark as favorite");
                    Movie movi1 = movie.toDBMovie();
                    //save to database
                    moviesDB.saveMovieToDatabase(movi1);
                    //save image of movie
                    //saveImageToSdCard(movi1);
                } else {
                    favoriteSwitch.setText("Mark as favorite");
                    //delete from database
                    moviesDB.deleteMovieFromDatabase(movie.getId());
                    /*MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.refreshMoviesAfterDeletion();*/
                }

            }
        });


        trailerListView = (ListView) getActivity().findViewById(R.id.lvExp);
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String key = movieTrailers.get(position).getKey();
                String uriString = YOUTUBE_URL + key;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uriString)));
            }
        });
        trailerAdapter = new TrailerAdapter(getActivity());
        trailerListView.setAdapter(trailerAdapter);

        reviewListView = (ListView) getActivity().findViewById(R.id.lvExp2);
        reviewAdapter = new ReviewAdapter(getActivity());
        reviewListView.setAdapter(reviewAdapter);

        loadMovieReviews(movie.getId());
        loadMovieTrailers(movie.getId());

        TextView movieTitle = (TextView) getView().findViewById(R.id.movieTitle);
        TextView movieDescription = (TextView) getView().findViewById(R.id.movieDescription);
        TextView releaseDate = (TextView) getView().findViewById(R.id.releaseDate);
        TextView voteAverage = (TextView) getView().findViewById(R.id.averageVote);

        if(movieTitle != null){
            movieTitle.setText(movie.getTitle());
        }
        if(movieDescription != null){
            movieDescription.setText(movie.getOverview());
        }
        if(releaseDate != null){
            releaseDate.setText(movie.getReleaseDate());
        }
        if(voteAverage != null){
            voteAverage.setText(movie.getRate().toString());
        }
        ImageView moviePoster = (ImageView)getView().findViewById(R.id.moviePoster);
        if(moviePoster != null){
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movie.getMovieImage()).placeholder(R.drawable.placeholder).into(moviePoster);
        }

    }


    public void saveImageToSdCard(Movie movie) {
        Observable.just(movie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Error loading movie trailers", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Movie movie) {
                        String filepath = null;
                        try
                        {
                            URL url = new URL(IMAGE_URL + movie.getMovieImage());
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setDoOutput(true);
                            urlConnection.connect();
                            File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
                            String filename="downloadedFile.png";
                            Log.i("Local filename:", "" + filename);
                            File file = new File(SDCardRoot,filename);
                            if(file.createNewFile())
                            {
                                file.createNewFile();
                            }
                            FileOutputStream fileOutput = new FileOutputStream(file);
                            InputStream inputStream = urlConnection.getInputStream();
                            int totalSize = urlConnection.getContentLength();
                            int downloadedSize = 0;
                            byte[] buffer = new byte[1024];
                            int bufferLength = 0;
                            while ( (bufferLength = inputStream.read(buffer)) > 0 )
                            {
                                fileOutput.write(buffer, 0, bufferLength);
                                downloadedSize += bufferLength;
                                Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
                            }
                            fileOutput.close();
                            if(downloadedSize==totalSize) filepath=file.getPath();
                            Log.i("filepath:", " " + filepath) ;
                            movie.setMovieImage(filepath != null ? filepath : null);
                            moviesDB.saveMovieToDatabase(movie);
                        }
                        catch (Exception e)
                        {
                            Log.e("","Error saving image");
                        }

                    }
                });
    }
    public void loadMovieTrailers(String movieId){
        movieService = new MovieService();
        movieService.getMovieAPI()
                .getMovieTrailers(movieId, MainActivity.MOVIE_API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieTrailerResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Error loading movie trailers", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MovieTrailerResponse response) {
                        if (response != null) {
                            showListOfTrailers(response.getTrailers());
                        }
                    }
                });
    }
    public boolean isInternetAvailable() {
        return ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
    public void loadMovieReviews(String movieId){
        if(!isInternetAvailable()){
            checkTitles();
            return;
        }
        movieService = new MovieService();
        movieService.getMovieAPI()
                .getMovieReviews(movieId, MainActivity.MOVIE_API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieReviewResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Error loading movie reviews", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MovieReviewResponse response) {
                        if(response != null){
                            showListOfReviews(response.getReviews());
                        }
                    }
                });
    }

    private void checkTitles() {
        TextView textViewTrailers = (TextView)getActivity().findViewById(R.id.trailers_title);
        if(textViewTrailers != null){
            textViewTrailers.setVisibility(View.GONE);
        }
        TextView textViewReviews = (TextView)getActivity().findViewById(R.id.reviews_title);
        if(textViewReviews != null){
            textViewReviews.setVisibility(View.GONE);
        }
    }


    public void showListOfTrailers(List<MovieTrailerResponse.Trailer> movieTrailers){
        this.movieTrailers = movieTrailers;
        trailerAdapter.setTrailers(movieTrailers);
        trailerAdapter.notifyDataSetChanged();
    }

    public void showListOfReviews(List<MovieReviewResponse.Review> movieReviews){
        this.reviews = movieReviews;
        reviewAdapter.setReviews(movieReviews);
        reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        void loadMovieDetailFragment(MovieResponse.Movie movie);
        void refreshMoviesAfterDeletion();
    }


}
