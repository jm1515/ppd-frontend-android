package ppd.com.mubler.ui.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ppd.com.mubler.R;
import ppd.com.mubler.ui.login.LoginActivity;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen_layout);
        this.changeActivity();
    }

    private void changeActivity(){
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashscreenActivity.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2000);

    }
}
