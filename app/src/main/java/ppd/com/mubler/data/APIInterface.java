package ppd.com.mubler.data;

import com.google.gson.JsonObject;

import java.util.List;

import ppd.com.mubler.data.entity.Request;
import ppd.com.mubler.data.entity.Review;
import ppd.com.mubler.data.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @POST("/users")
    Call<User> createUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<JsonObject> doLogin(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @GET("/user")
    Call<User> getUser(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("/request")
    Call<Request> postRequest(@Header("Authorization") String token, @Body Request body);

    @Headers("Content-Type: application/json")
    @GET("/users/{idUser}/history")
    Call<List<Request>> getHistory(@Header("Authorization") String token, @Path("idUser") int idUser);

    @Headers("Content-Type: application/json")
    @GET("/users/{idUser}/historydone")
    Call<List<Request>> getHistoryDone(@Header("Authorization") String token, @Path("idUser") int idUser);

    @Headers("Content-Type: application/json")
    @GET("/users/{id}")
    Call<User> getUserDetails(@Header("Authorization") String token, @Path("id") int id);

    @Headers("Content-Type: application/json")
    @POST("/reviews")
    Call<Review> postReview(@Header("Authorization") String token, @Body Review body);

    @Headers("Content-Type: application/json")
    @GET("/users/{id}/reviewsconcerned")
    Call<List<Review>> getUserReviews(@Header("Authorization") String token, @Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("/users/{id}/reviewsof")
    Call<List<Review>> getUserReviewsOf(@Header("Authorization") String token, @Path("id") int id);
}
