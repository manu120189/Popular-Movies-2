package demo.popularmoviesi.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import demo.popularmoviesi.MainActivity;
import demo.popularmoviesi.R;
import demo.popularmoviesi.adapters.MovieAdapter;
import demo.popularmoviesi.api.objects.MovieResponse;
import demo.popularmoviesi.database.PopularMoviesDB;

public class MovieListFragment extends Fragment {

    private List<MovieResponse.Movie> movieList;
    private MovieAdapter movieAdapter;
    private PopularMoviesDB moviesDB;

   public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        return fragment;
    }

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onStart() {
        super.onStart();
        GridView gridview = (GridView) getActivity().findViewById(R.id.gridView1);
        movieAdapter = new MovieAdapter(getActivity(), null);
        refreshMovieList(null);
        gridview.setAdapter(movieAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(getActivity() instanceof MainActivity){
                    ((MainActivity)getActivity()).loadMovieDetailFragment(movieList.get(position));
                }
                //Toast.makeText(getActivity(), movieList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshMovieList(MainActivity.getMoviesInMemory());
    }
    public void refreshMovieList(List<MovieResponse.Movie> movieList){
        if(movieList != null){
            this.movieList = movieList;
        }else {
            this.movieList = new ArrayList<>();
        }
        movieAdapter.setMovies(this.movieList);
        movieAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void loadMovies(String sort);
    }

}
