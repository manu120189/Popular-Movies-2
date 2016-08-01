package demo.popularmoviesi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import demo.popularmoviesi.R;
import demo.popularmoviesi.api.objects.MovieTrailerResponse;

public class TrailerAdapter extends BaseAdapter {
    private Context mContext;
    private List<MovieTrailerResponse.Trailer> trailers;
    public TrailerAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        if (trailers == null)
            return 0;
        return trailers.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.list_trailer_item, null);

        }

        v.setVisibility(View.VISIBLE);
        TextView textView = (TextView)v.findViewById(R.id.trailer_name);
        textView.setText(trailers.get(position).getName());

        return v;
    }

    public void setTrailers(List<MovieTrailerResponse.Trailer> trailers) {
        this.trailers = trailers;
    }
}
