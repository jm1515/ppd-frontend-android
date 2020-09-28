package ppd.com.mubler.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Request {

    @SerializedName("id")
    private Integer id;

    @SerializedName("startCoordX")
    private Float startCoordX;

    @SerializedName("startCoordY")
    private Float startCoordY;

    @SerializedName("endCoordX")
    private Float endCoordX;

    @SerializedName("endCoordY")
    private Float endCoordY;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("vehiculeTypeName")
    private String vehiculeTypeName;

    @SerializedName("price")
    private float price;

    public Request(Float startCoordX, Float startCoordY, Float endCoordX, Float endCoordY, String vehiculeTypeName) {
        this.startCoordX = startCoordX;
        this.startCoordY = startCoordY;
        this.endCoordX = endCoordX;
        this.endCoordY = endCoordY;
        this.vehiculeTypeName = vehiculeTypeName;
        this.timestamp = String.valueOf(new Date().getTime());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getStartCoordX() {
        return startCoordX;
    }

    public void setStartCoordX(Float startCoordX) {
        this.startCoordX = startCoordX;
    }

    public Float getStartCoordY() {
        return startCoordY;
    }

    public void setStartCoordY(Float startCoordY) {
        this.startCoordY = startCoordY;
    }

    public Float getEndCoordX() {
        return endCoordX;
    }

    public void setEndCoordX(Float endCoordX) {
        this.endCoordX = endCoordX;
    }

    public Float getEndCoordY() {
        return endCoordY;
    }

    public void setEndCoordY(Float endCoordY) {
        this.endCoordY = endCoordY;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVehiculeTypeName() {
        return vehiculeTypeName;
    }

    public void setVehiculeTypeName(String vehiculeTypeName) {
        this.vehiculeTypeName = vehiculeTypeName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", startCoordX=" + startCoordX +
                ", startCoordY=" + startCoordY +
                ", endCoordX=" + endCoordX +
                ", endCoordY=" + endCoordY +
                ", timestamp=" + timestamp +
                ", vehiculeTypeName='" + vehiculeTypeName + '\'' +
                ", price=" + price +
                '}';
    }
}
