package demo.popularmoviesi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import demo.popularmoviesi.R;
import demo.popularmoviesi.api.objects.MovieReviewResponse;
import demo.popularmoviesi.api.objects.MovieTrailerResponse;

public class ReviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<MovieReviewResponse.Review> reviews;
    public ReviewAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        if (reviews == null)
            return 0;
        return reviews.size();
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
            v = vi.inflate(R.layout.list_review_item, null);

        }
        v.setVisibility(View.VISIBLE);
        TextView textView = (TextView)v.findViewById(R.id.review_content);
        textView.setText(reviews.get(position).getContent());

        TextView author = (TextView)v.findViewById(R.id.review_author);
        author.setText(reviews.get(position).getAuthor());

        return v;
    }

    public void setReviews(List<MovieReviewResponse.Review> reviews) {
        this.reviews = reviews;
    }
}
