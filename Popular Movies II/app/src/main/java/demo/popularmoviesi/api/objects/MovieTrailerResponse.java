package demo.popularmoviesi.api.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by emma on 01/11/2015.
 */
public class MovieTrailerResponse {
    public int id;
    public List<Trailer> results;

    public int getId() {
        return id;
    }

    public List<Trailer> getTrailers() {
        return results;
    }

    public class Trailer {

        @SerializedName("id") public String id;
        @SerializedName("iso_639_1") public String iso;
        @SerializedName("key") public String key;
        @SerializedName("name") public String name;
        @SerializedName("site") public String  site;
        @SerializedName("type") public String  type;
        @SerializedName("size") public int  size;

        public String getId() {
            return id;
        }

        public String getIso() {
            return iso;
        }

        public String getName() {
            return name;
        }

        public String getKey() {
            return key;
        }

        public String getSite() {
            return site;
        }

        public int getSize() {
            return size;
        }

        public String getType() {
            return type;
        }
    }
}
