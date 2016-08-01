package demo.popularmoviesi.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.GridView;

import com.squareup.picasso.Picasso;

import java.util.List;

import demo.popularmoviesi.R;
import demo.popularmoviesi.api.objects.MovieResponse;

public class MovieAdapter extends BaseAdapter {
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    public static final int W = 360;
    public static final int H = 500;
    private Context mContext;
    private List<MovieResponse.Movie> movieList;
    public MovieAdapter(Context c, List<MovieResponse.Movie> movieList) {
        this.mContext = c;
        this.movieList = movieList;
    }

    public int getCount() {
        return movieList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(W, H));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(IMAGE_URL + movieList.get(position).getMovieImage()).placeholder(R.drawable.placeholder).into(imageView);
        return imageView;
    }

    public void setMovies(List<MovieResponse.Movie> movieList){
        this.movieList = movieList;
    }

}
