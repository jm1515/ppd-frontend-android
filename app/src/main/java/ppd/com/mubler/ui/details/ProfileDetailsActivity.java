package ppd.com.mubler.ui.details;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import ppd.com.mubler.R;
import ppd.com.mubler.data.entity.User;
import ppd.com.mubler.ui.BaseActivity;
import ppd.com.mubler.ui.request_end.RequestEndActivity;
import ppd.com.mubler.ui.user.UserSession;

public class ProfileDetailsActivity extends BaseActivity {

    private final static String REQUEST_MSG = "REQUEST-AVAILABLE";

    private TextView profile_name, profile_email, profile_phone;
    private TextView notif_price, notif_size;
    private User user;
    private Toolbar toolbar;
    private OkHttpClient client;
    private Animation slideUpAnimation, slideDownAnimation;
    private LinearLayout requestNotifLayout;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ProfileDetailsActivity ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        user = UserSession.getSession().getCurrentUser();

        ref = this;

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profil");
        toolbar.setTitleTextColor(getResources().getColor(R.color.mapbox_plugins_white));

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setDrawer(drawerLayout, navigationView, toolbar);

        profile_email = findViewById(R.id.profile_email);
        profile_email.setText(user.getEmail());
        profile_name = findViewById(R.id.profile_name);
        profile_name.setText(user.getFirstName() + " " + user.getLastName().toUpperCase());
        profile_phone = findViewById(R.id.profile_phone);
        profile_phone.setText(user.getPhoneNumber());

        if (user.isMubler()){
            requestNotifLayout = findViewById(R.id.request_notif);
            slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up_annimation);

            slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down_annimation);
            client = new OkHttpClient();
            openWebSocket();
        }

        notif_price = findViewById(R.id.notif_price);
        notif_size = findViewById(R.id.notif_size);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openWebSocket(){
        Request request = new Request.Builder().url("https://mubler-ws.herokuapp.com/requests").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            //webSocket.send(".");
        }

        @Override
        public void onMessage(final WebSocket webSocket, final String text) {
            Log.i("TAG", text);
            if (text.contains(REQUEST_MSG)){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            v.vibrate(1000);
                        }
                        notif_price.setText(text.split("/")[2]);
                        notif_size.setText(text.split("/")[4]);
                        requestNotifLayout.startAnimation(slideDownAnimation);
                        requestNotifLayout.setVisibility(View.VISIBLE);
                        TextView acceptButton = findViewById(R.id.notif_btn_accept);
                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestNotifLayout.startAnimation(slideUpAnimation);
                                requestNotifLayout.setVisibility(View.GONE);
                                String res = "ACCEPTED/" + user.getId() + "/" + text.split("/")[1];
                                Log.i("TAG", res);
                                webSocket.send(res);
                                Intent intent = new Intent(ref, RequestEndActivity.class);
                                intent.putExtra("price", text.split("/")[2]);
                                intent.putExtra("idRequester", text.split("/")[3]);
                                intent.putExtra("idRequest", text.split("/")[1]);
                                startActivity(intent);
                                webSocket.close(1000, "mdr");
                            }
                        });
                        TextView refuseButton = findViewById(R.id.notif_btn_refuse);
                        refuseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestNotifLayout.startAnimation(slideUpAnimation);
                                requestNotifLayout.setVisibility(View.GONE);
                                webSocket.send("REFUSED");
                            }
                        });
                    }
                });
            }
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
