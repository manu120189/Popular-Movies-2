package demo.popularmoviesi.api.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by emma on 01/11/2015.
 */
public class MovieReviewResponse {
    public int id;
    public List<Review> results;

    public int getId() {
        return id;
    }

    public List<Review> getReviews() {
        return results;
    }

    public class Review {

        @SerializedName("id") public String id;
        @SerializedName("author") public String author;
        @SerializedName("content") public String content;
        @SerializedName("url") public String url;

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "Review{" +
                    "id='" + id + '\'' +
                    ", author='" + author + '\'' +
                    ", content='" + content + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
