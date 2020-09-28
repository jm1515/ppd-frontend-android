package ppd.com.mubler.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Review {

    @SerializedName("id")
    private Integer id;

    @SerializedName("idConcerned")
    private long idConcerned;

    @SerializedName("idWriter")
    private long idWriter;

    @SerializedName("rating")
    private Integer rating;

    @SerializedName("comment")
    private String comment;

    @SerializedName("timestamp")
    private String timestamp;

    public Review(long idConcerned, long idWriter, Integer rating, String comment) {
        this.idConcerned = idConcerned;
        this.idWriter = idWriter;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = String.valueOf(new Date().getTime());
    }

    public long getIdConcerned() {
        return idConcerned;
    }

    public void setIdConcerned(Integer idConcerned) {
        this.idConcerned = idConcerned;
    }

    public long getIdWriter() {
        return idWriter;
    }

    public void setIdWriter(Integer idWriter) {
        this.idWriter = idWriter;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
