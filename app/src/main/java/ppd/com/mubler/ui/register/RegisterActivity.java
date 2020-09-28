package ppd.com.mubler.ui.register;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ppd.com.mubler.R;
import ppd.com.mubler.ui.BaseActivity;

public class RegisterActivity extends BaseActivity {
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.login_placeholder, new RegisterStep1Fragment());
        transaction.commit();
    }
}
