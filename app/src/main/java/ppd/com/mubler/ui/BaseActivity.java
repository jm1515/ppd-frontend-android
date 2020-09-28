package ppd.com.mubler.ui;

import android.graphics.Color;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import ppd.com.mubler.R;
import ppd.com.mubler.ui.details.ProfileDetailsActivity;
import ppd.com.mubler.ui.history.HistoryActivity;
import ppd.com.mubler.ui.history.HistoryMublerActivity;
import ppd.com.mubler.ui.history.ReviewHistoryActivity;
import ppd.com.mubler.ui.history.ReviewHistoryWriterActivity;
import ppd.com.mubler.ui.user.UserSession;

public abstract class BaseActivity extends AppCompatActivity {

    public void setDrawer(DrawerLayout mDrawerLayout, NavigationView navigationView, Toolbar toolbar) {
        setDrawerButton(mDrawerLayout);
        setNavButton(mDrawerLayout, navigationView);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    public void setDrawerButton(DrawerLayout mDrawerLayout) {
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
    }

    public void setNavButton(DrawerLayout mDrawerLayout, NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()){
                        case R.id.nav_profile: {
                            goToProfile();
                            break;
                        }
                        case R.id.nav_hisotry: {
                            goToHistory();
                            break;
                        }
                        case R.id.nav_reviews: {
                            goToReviews();
                            break;
                        }
                    }
                    navigationView.setItemBackgroundResource(R.color.mapbox_plugins_white);

                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected

                    // For example, swap UI fragments here

                    return true;
                });
    }

    private void goToProfile(){
        Intent intent = new Intent(this, ProfileDetailsActivity.class);
        startActivity(intent);
    }

    private void goToHistory(){
        if (UserSession.getSession().getCurrentUser().isMubler()) {
            Intent intent = new Intent(this, HistoryMublerActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        }
    }

    private void goToReviews(){
        if (UserSession.getSession().getCurrentUser().isMubler()) {
            Intent intent = new Intent(this, ReviewHistoryActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, ReviewHistoryWriterActivity.class);
            startActivity(intent);
        }
    }
}

