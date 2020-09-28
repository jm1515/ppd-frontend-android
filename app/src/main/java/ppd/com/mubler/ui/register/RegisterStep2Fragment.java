package ppd.com.mubler.ui.register;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import ppd.com.mubler.R;
import ppd.com.mubler.ui.main.MainActivity;
import ppd.com.mubler.ui.map.MapActivity;

public class RegisterStep2Fragment extends Fragment implements RegisterContract.RegisterView {

    private Toolbar toolbar;
    private RegisterActivity activity;
    private Button btnRegister;
    private RegisterContract.RegisterPresenter presenter;
    private EditText inputPassword, inputConfirmPassword;
    private String email, firstname, lastname, phonenumber;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parentViewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_step2, parentViewGroup, false);

        activity = (RegisterActivity) getActivity();

        presenter = new RegisterPresenterImpl(new RegisterUseCaseImpl(), getFragmentManager());
        presenter.onViewAttach(this);

        inputPassword = rootView.findViewById(R.id.inputPassword);
        inputConfirmPassword = rootView.findViewById(R.id.inputConfirmationPassword);
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.register_button_register);
        toolbar.setTitleTextColor(getResources().getColor(R.color.primaryTextColor));
        toolbar.setNavigationIcon(R.drawable.back_icon_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToStep1(email, firstname, lastname, phonenumber);
            }
        });

        btnRegister = rootView.findViewById(R.id.button_Register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "register clicked");
                presenter.onStep2Clicked(email, firstname, lastname, phonenumber, inputPassword.getText().toString(), inputConfirmPassword.getText().toString());
            }
        });

        return rootView;
    }

    @Override
    public void showRegisterSuccess() {

    }

    @Override
    public void showRegisterError(RegisterError error) {
        switch (error) {
            case EMPTY_PASSWORD:
                inputPassword.setError(getString(R.string.register_weak_password_err));
                break;
            case PASSWORD_CONFIRMATION_FAILED:
                Toast.makeText(activity, getString(R.string.register_password_confirmation_error), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void goToLoginView() {

    }

    @Override
    public void goToMainView() {
        startActivity(new Intent(activity, MapActivity.class));
        activity.finish();
    }

    @Override
    public void setDate(String text) {

    }

    @Override
    public void showDatePicker(DatePickerDialog.OnDateSetListener date, Calendar calendar) {

    }

    public void setData(String email, String lastname, String firstname, String phoneNumber){
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.phonenumber = phoneNumber;
    }
}
