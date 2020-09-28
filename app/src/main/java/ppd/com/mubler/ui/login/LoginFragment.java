package ppd.com.mubler.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import ppd.com.mubler.R;
import ppd.com.mubler.ui.details.ProfileDetailsActivity;
import ppd.com.mubler.ui.main.MainActivity;
import ppd.com.mubler.ui.map.MapActivity;
import ppd.com.mubler.ui.register.RegisterActivity;
import ppd.com.mubler.ui.review.ReviewActivity;
import ppd.com.mubler.ui.user.UserSession;
import ppd.com.mubler.ui.utils.ActivityUtils;

public class LoginFragment extends Fragment implements LoginContract.LoginView{

    private Toolbar toolbar;
    private LoginActivity activity;
    private Button btnLogin, btnRegister;
    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private LoginContract.LoginPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.connection_layout, parentViewGroup, false);
        activity = (LoginActivity) getActivity();

        ActivityUtils.checkPermissionsState(activity);

        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.login_button_connection);
        toolbar.setTitleTextColor(getResources().getColor(R.color.primaryTextColor));

        presenter = new LoginPresenterImpl(new LoginUseCaseImpl());
        presenter.onViewAttach(this);

        inputEmail = rootView.findViewById(R.id.inputEmail);
        inputPassword = rootView.findViewById(R.id.inputPassword);
        progressBar = rootView.findViewById(R.id.progressBar);
        btnLogin = rootView.findViewById(R.id.buttonLogin);
        btnRegister = rootView.findViewById(R.id.buttonGoRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setEnabled(false);
                presenter.onLoginClick(inputEmail.getText().toString(), inputPassword.getText().toString());
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRegisterClick();
            }
        });

        progressBar.setIndeterminate(true);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showLoginSuccess() {
        btnLogin.setEnabled(true);
        Toast.makeText(activity, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginError(LoginError error) {
        btnLogin.setEnabled(true);

        switch (error) {
            case EMPTY_USERNAME:
                inputEmail.setError(getString(R.string.login_empty_field));
                break;
            case EMPTY_PASSWORD:
                inputPassword.setError(getString(R.string.login_empty_field));
                break;
            case WEAK_PASSWORD:
                Toast.makeText(activity, getString(R.string.login_weak_password_err), Toast.LENGTH_SHORT).show();
                break;
            case INVALID_CREDENTIALS:
                Toast.makeText(activity, getString(R.string.login_invalid_credentials), Toast.LENGTH_SHORT).show();
                break;
            case UNKNOWN:
                Toast.makeText(activity, getString(R.string.login_unknown), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void goToRegisterView() {
        startActivity(new Intent(activity, RegisterActivity.class));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void goToPasswordRecoveryView() {
        // TODO: startActivity(new Intent(activity, PasswordRecoveryActivity.class));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void goToMainView() {
        if (UserSession.getSession().getCurrentUser().isMubler()){
            startActivity(new Intent(activity, ProfileDetailsActivity.class));
            activity.finish();
        }else {
            startActivity(new Intent(activity, MapActivity.class));
            activity.finish();
        }
    }
}
