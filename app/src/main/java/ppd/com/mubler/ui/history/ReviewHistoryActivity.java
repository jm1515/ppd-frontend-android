package ppd.com.mubler.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import ppd.com.mubler.R;
import ppd.com.mubler.data.APIClient;
import ppd.com.mubler.data.APIInterface;
import ppd.com.mubler.data.entity.Review;
import ppd.com.mubler.ui.BaseActivity;
import ppd.com.mubler.ui.user.UserSession;
import retrofit2.Call;

public class ReviewHistoryActivity extends BaseActivity {

    private ListView listView;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ReviewHistoryActivity ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_history);
        getHistory();

        ref = this;

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Avis");
        toolbar.setTitleTextColor(getResources().getColor(R.color.mapbox_plugins_white));

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setDrawer(drawerLayout, navigationView, toolbar);

        listView = findViewById(R.id.review_history_list);

    }

    private void getHistory(){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        UserSession userSession = UserSession.getInstance();
        Call<List<Review>> call = apiInterface.getUserReviews(userSession.getToken(), userSession.getCurrentUser().getId());
        call.enqueue(new retrofit2.Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, retrofit2.Response<List<Review>> response) {
                Log.i("TAG", "Server response http return code : " + response.code());

                if (response.code()  != 200){
                    call.cancel();
                    Log.e("TAG", "get request call failed ! " + response.code());
                    return;
                }
                List<Review> requestList = response.body();
                Log.i("TAG", "REQUEST HISTORY LIST = " + requestList.size());
                ReviewHistoryAdapter reviewHistoryAdapter = new ReviewHistoryAdapter(ref, requestList);
                listView.setAdapter(reviewHistoryAdapter);
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                call.cancel();
                Log.e("TAG", "post request fail !" + t.getMessage());
                return;
            }
        });
    }

    private class ReviewHistoryAdapter extends ArrayAdapter<Review> {

        public ReviewHistoryAdapter(@NonNull Context context, List<Review> requestList) {
            super(context,0, requestList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item,parent, false);
            }

            ReviewHistoryAdapter.ReviewViewHolder viewHolder = (ReviewHistoryAdapter.ReviewViewHolder) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new ReviewViewHolder();
                viewHolder.review_comment = convertView.findViewById(R.id.review_comment_text);
                viewHolder.review_date = convertView.findViewById(R.id.review_date);
                viewHolder.review_from = convertView.findViewById(R.id.review_from);
                viewHolder.review_rating = convertView.findViewById(R.id.review_rating_static);
                convertView.setTag(viewHolder);
            }

            Review review = getItem(position);

            viewHolder.review_comment.setText("'"+review.getComment().split("/")[1]+"'");
            viewHolder.review_date.setText(review.getTimestamp());
            viewHolder.review_rating.setRating(review.getRating());
            viewHolder.review_from.setText("De : " + review.getComment().split("/")[0]);

            return convertView;
        }

        private class ReviewViewHolder{
            public TextView review_comment;
            public TextView review_date;
            public TextView review_from;
            public RatingBar review_rating;
        }
    }
}
