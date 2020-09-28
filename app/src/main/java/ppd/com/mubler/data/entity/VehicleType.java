package ppd.com.mubler.data.entity;

import com.google.gson.annotations.SerializedName;

public class VehicleType {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("width")
    private Float width;

    @SerializedName("height")
    private Float height;

    @SerializedName("depth")
    private Float depth;

    @SerializedName("weight")
    private Float weight;

    public VehicleType(Integer id, String name, Float width, Float height, Float depth, Float weight) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getDepth() {
        return depth;
    }

    public void setDepth(Float depth) {
        this.depth = depth;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "VehicleType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", depth=" + depth +
                ", weight=" + weight +
                '}';
    }
}
