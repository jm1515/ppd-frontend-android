package ppd.com.mubler.ui.request_end;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import ppd.com.mubler.R;
import ppd.com.mubler.data.APIClient;
import ppd.com.mubler.data.APIInterface;
import ppd.com.mubler.data.entity.User;
import ppd.com.mubler.ui.BaseActivity;
import ppd.com.mubler.ui.user.UserSession;
import retrofit2.Call;

public class RequestEndActivity extends BaseActivity {

    private TextView requester_name, requester_phone, requester_email, request_price;
    private Button end_request, cancel_request;
    private Toolbar toolbar;
    private OkHttpClient client;
    private WebSocket webSocket;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private int idRequester;
    private int idRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_end);

        Intent intent = getIntent();

        idRequester = Integer.valueOf(intent.getStringExtra("idRequester"));
        idRequest = Integer.valueOf(intent.getStringExtra("idRequest"));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Recap");
        toolbar.setTitleTextColor(getResources().getColor(R.color.mapbox_plugins_white));

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setDrawer(drawerLayout, navigationView, toolbar);

        requester_name = findViewById(R.id.request_end_name);
        requester_phone = findViewById(R.id.request_end_phone);
        requester_email = findViewById(R.id.request_end_email);
        request_price = findViewById(R.id.request_end_price);
        request_price.setText(intent.getStringExtra("price"));

        end_request = findViewById(R.id.button_end_request);
        cancel_request = findViewById(R.id.button_cancel_request);

        client = new OkHttpClient();
        openWebSocket();
        getUserInfos();

        end_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webSocket.send("END/" + idRequest);
                Toast.makeText(getBaseContext(), "Merci pour votre course !", Toast.LENGTH_SHORT).show();
                endActivity();
            }
        });

        cancel_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webSocket.send("CANCEL/" + idRequest);
                Toast.makeText(getBaseContext(), "Vous avez annulé cette requête", Toast.LENGTH_SHORT).show();
                endActivity();
            }
        });
    }

    private void endActivity(){
        this.finish();
    }

    private void getUserInfos(){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        UserSession userSession = UserSession.getInstance();
        Call<User> call = apiInterface.getUserDetails(userSession.getToken(), idRequester);
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                Log.i("TAG", "Server response http return code : " + response.code());

                if (response.code()  != 200){
                    call.cancel();
                    Log.e("TAG", "get request call failed ! " + response.code());
                    return;
                }

                User user = response.body();
                requester_name.setText(user.getLastName() +  " " + user.getFirstName());
                requester_phone.setText(user.getPhoneNumber());
                requester_email.setText(user.getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
                Log.e("TAG", "post request fail !" + t.getMessage());
                return;
            }
        });
    }

    private void openWebSocket(){
        Request request = new Request.Builder().url("https://mubler-ws.herokuapp.com/requests").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        this.webSocket = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
        }

        @Override
        public void onMessage(final WebSocket webSocket, final String text) {
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        }
    }

}
