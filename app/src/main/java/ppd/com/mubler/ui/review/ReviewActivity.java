package ppd.com.mubler.ui.review;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import ppd.com.mubler.R;
import ppd.com.mubler.data.APIClient;
import ppd.com.mubler.data.APIInterface;
import ppd.com.mubler.data.entity.Request;
import ppd.com.mubler.data.entity.Review;
import ppd.com.mubler.ui.BaseActivity;
import ppd.com.mubler.ui.map.MapActivity;
import ppd.com.mubler.ui.user.UserSession;
import retrofit2.Call;

public class ReviewActivity extends BaseActivity {

    private RatingBar ratingBar;
    private TextView review_comment;
    private Button send_review;
    private long idAccepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        idAccepter = getIntent().getLongExtra("idAccepter", 1);

        ratingBar = findViewById(R.id.review_rating);
        ratingBar.setNumStars(5);

        review_comment = findViewById(R.id.review_comment);

        send_review = findViewById(R.id.send_review);

        send_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (review_comment.getText() == null){
                    review_comment.setText("");
                }
                createRequest();
            }
        });
    }

    private void createRequest(){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        UserSession userSession = UserSession.getInstance();
        Call<Review> call = apiInterface.postReview(userSession.getToken(), new Review(idAccepter, userSession.getCurrentUser().getId(), (int) ratingBar.getRating(), userSession.getCurrentUser().getFirstName() + "/" + review_comment.getText().toString()));
        call.enqueue(new retrofit2.Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, retrofit2.Response<Review> response) {
                Log.i("TAG", "Server response for create review http return code : " + response.code());

                if (response.code()  != 201){
                    call.cancel();
                    Log.e("TAG", "post request call failed ! " + response.code());
                    return;
                }
                goToMap();
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                call.cancel();
                Log.e("TAG", "post request fail !" + t.getMessage());
                return;
            }
        });
    }

    private void goToMap(){
        Intent intent = new Intent(this, MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
