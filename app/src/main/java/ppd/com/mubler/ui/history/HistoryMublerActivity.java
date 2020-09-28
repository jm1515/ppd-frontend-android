package ppd.com.mubler.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import ppd.com.mubler.R;
import ppd.com.mubler.data.APIClient;
import ppd.com.mubler.data.APIInterface;
import ppd.com.mubler.data.entity.Request;
import ppd.com.mubler.ui.BaseActivity;
import ppd.com.mubler.ui.user.UserSession;
import retrofit2.Call;

public class HistoryMublerActivity extends BaseActivity {

    private ListView listView;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private HistoryMublerActivity ref;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getHistory();

        ref = this;

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Courses");
        toolbar.setTitleTextColor(getResources().getColor(R.color.mapbox_plugins_white));

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setDrawer(drawerLayout, navigationView, toolbar);

        listView = findViewById(R.id.history_list);

        title = findViewById(R.id.history_title);
        title.setText("Historique de vos courses");

    }

    private void getHistory(){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        UserSession userSession = UserSession.getInstance();
        Call<List<Request>> call = apiInterface.getHistoryDone(userSession.getToken(), userSession.getCurrentUser().getId());
        call.enqueue(new retrofit2.Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, retrofit2.Response<List<Request>> response) {
                Log.i("TAG", "Server response http return code : " + response.code());

                if (response.code()  != 200){
                    call.cancel();
                    Log.e("TAG", "get request call failed ! " + response.code());
                    return;
                }
                List<Request> requestList = response.body();
                Log.i("TAG", "REQUEST HISTORY LIST = " + requestList.size());
                HistoryAdapter historyAdapter = new HistoryAdapter(ref, requestList);
                listView.setAdapter(historyAdapter);
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {
                call.cancel();
                Log.e("TAG", "post request fail !" + t.getMessage());
                return;
            }
        });
    }

    private class HistoryAdapter extends ArrayAdapter<Request> {

        public HistoryAdapter(@NonNull Context context, List<Request> requestList) {
            super(context,0, requestList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_list_item,parent, false);
            }

            HistoryAdapter.RequestViewHolder viewHolder = (HistoryAdapter.RequestViewHolder) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new HistoryAdapter.RequestViewHolder();
                viewHolder.request_date = convertView.findViewById(R.id.item_mubler_date);
                viewHolder.request_price = convertView.findViewById(R.id.item_mubler_price);
                viewHolder.request_size = convertView.findViewById(R.id.item_mubler_size);
                convertView.setTag(viewHolder);
            }

            Request request = getItem(position);

            viewHolder.request_date.setText(request.getTimestamp());
            viewHolder.request_size.setText("Mubler " + request.getVehiculeTypeName());
            viewHolder.request_price.setText("+ " + request.getPrice() + " €");
            viewHolder.request_price.setTextColor(getResources().getColor(R.color.mapbox_plugins_green));

            return convertView;
        }

        private class RequestViewHolder{
            public TextView request_size;
            public TextView request_date;
            public TextView request_accepter;
            public TextView request_price;
        }
    }

}
