package ppd.com.mubler.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import ppd.com.mubler.R;
import ppd.com.mubler.ui.BaseActivity;
import ppd.com.mubler.ui.login.LoginActivity;
import ppd.com.mubler.ui.user.UserSession;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private Button logout;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setDrawer(mDrawerLayout, navigationView,toolbar);

        //userSession = userSession.getSession();

        /*if (!userSession.isConnected()) {
            userSession.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }*/

        logout = findViewById(R.id.button_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSession.logout();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
