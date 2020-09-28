package ppd.com.mubler.ui.request_recap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import ppd.com.mubler.R;
import ppd.com.mubler.ui.BaseActivity;
import ppd.com.mubler.ui.review.ReviewActivity;
import ppd.com.mubler.ui.user.UserSession;

public class RequestRecapActivity extends BaseActivity {

    private TextView request_size, request_price, request_status, mubler_name1, mubler_name2, mubler_phone;
    private LinearLayout mubler_details;
    private Button goToReviewButton;
    private OkHttpClient client;
    private UserSession userSession;
    private Toolbar toolbar;
    private long idAccepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_recap);

        Intent intent = getIntent();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Course en cours");
        toolbar.setTitleTextColor(getResources().getColor(R.color.mapbox_plugins_white));

        userSession = UserSession.getSession();
        request_size = findViewById(R.id.request_size);
        request_size.setText(intent.getStringExtra("request_size"));
        request_price = findViewById(R.id.request_price);
        request_price.setText(String.valueOf(intent.getFloatExtra("request_price", 0f)));
        request_status = findViewById(R.id.request_status);

        mubler_details = findViewById(R.id.recap_mubler);

        client = new OkHttpClient();

        mubler_name1 = findViewById(R.id.mubler_name1);
        mubler_name2 = findViewById(R.id.mubler_name2);
        mubler_phone = findViewById(R.id.mubler_phone);

        goToReviewButton = findViewById(R.id.button_review);
        goToReviewButton.setVisibility(View.INVISIBLE);
        goToReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToReview();
            }
        });

        openWebSocket();

    }

    private void goToReview(){
        Intent intentReview = new Intent(this, ReviewActivity.class);
        intentReview.putExtra("idAccepter", idAccepter);
        startActivity(intentReview);
    }

    private void openWebSocket(){
        Request request = new Request.Builder().url("https://mubler-ws.herokuapp.com/dialog").build();
        DialogSocketListener listener = new DialogSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private final class DialogSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("SUB/" + userSession.getCurrentUser().getId());
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.i("SOCKET", text);
            if (text.contains("REQUEST_ACCEPTED")){
                Log.i("SOCKET", "SHOW DISPLAY INFOS");
                idAccepter = Long.valueOf(text.split("/")[1]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mubler_name1.setText(text.split("/")[2]);
                        mubler_name2.setText(text.split("/")[3]);
                        mubler_phone.setText(text.split("/")[4]);
                        request_status.setText("En cours...");
                        mubler_details.setVisibility(View.VISIBLE);
                    }
                });

            }
            if (text.contains("REQUEST_OVER")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        request_status.setText("Terminée");
                        goToReviewButton.setVisibility(View.VISIBLE);
                    }
                });
            }
            if (text.contains("REQUEST_CANCELED")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        request_status.setText("Annulée");
                    }
                });
            }
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.send("UNSUB/" + userSession.getCurrentUser().getId());
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

        }
    }
}
