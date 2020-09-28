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
import ppd.com.mubler.ui.login.LoginActivity;
import ppd.com.mubler.ui.main.MainActivity;

public class RegisterStep1Fragment extends Fragment implements RegisterContract.RegisterView {

    private RegisterActivity activity;
    private Button btnNext;
    private EditText inputFirstname, inputLastname, inputEmail, inputPhoneNumber;
    private String email, firstname, lastname, phonenumber;
    private RegisterContract.RegisterPresenter presenter;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_step1, parentViewGroup, false);

        activity = (RegisterActivity) getActivity();

        presenter = new RegisterPresenterImpl(new RegisterUseCaseImpl(), getFragmentManager());
        presenter.onViewAttach(this);

        inputFirstname = rootView.findViewById(R.id.inputFirstName);
        inputLastname = rootView.findViewById(R.id.inputLastName);
        inputEmail = rootView.findViewById(R.id.inputEmail);
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.register_button_register);
        toolbar.setTitleTextColor(getResources().getColor(R.color.primaryTextColor));
        toolbar.setNavigationIcon(R.drawable.back_icon_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginView();
            }
        });
        inputPhoneNumber = rootView.findViewById(R.id.inputPhone);
        btnNext = rootView.findViewById(R.id.buttonNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLastname.hasFocus()) {
                    inputFirstname.requestFocus();
                    return;
                }
                if (inputFirstname.hasFocus()) {
                    inputEmail.requestFocus();
                    return;
                }
                if (inputEmail.hasFocus()) {
                    inputPhoneNumber.requestFocus();
                    return;
                }
                presenter.onStep1Clicked(
                        inputEmail.getText().toString(),
                        inputLastname.getText().toString(), inputFirstname.getText().toString(),
                        inputPhoneNumber.getText().toString());
            }
        });
        Log.i("TAG", this.email + " " + this.firstname + " " + this.lastname + " " + this.phonenumber);
        if (this.firstname != null){
            this.inputFirstname.setText(this.firstname);
        }
        if (this.lastname != null){
            this.inputLastname.setText(this.lastname);
        }
        if (this.phonenumber != null){
            this.inputPhoneNumber.setText(this.phonenumber);
        }
        if (this.email != null){
            this.inputEmail.setText(this.email);
        }


        return rootView;
    }

    @Override
    public void setDate(String text){
    }

    @Override
    public void showDatePicker(DatePickerDialog.OnDateSetListener date, Calendar calendar) {
        new DatePickerDialog(activity, date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showRegisterSuccess() {
        Toast.makeText(activity, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRegisterError(RegisterError error) {

        switch (error) {
            case EMPTY_EMAIL:
                inputEmail.setError(getString(R.string.register_empty_field));
                break;
            case EMPTY_LASTNAME:
                inputLastname.setError(getString(R.string.register_empty_field));
                break;
            case EMPTY_FIRSTNAME:
                inputFirstname.setError(getString(R.string.register_empty_field));
                break;
            case EMAIL_ALREADY_USED:
                Toast.makeText(activity, getString(R.string.register_email_already_used_error), Toast.LENGTH_SHORT).show();
                break;
            case INVALID_EMAIL:
                Toast.makeText(activity, getString(R.string.register_invalid_email), Toast.LENGTH_SHORT).show();
                break;
            case INVALID_INPUT_DATE:
                Toast.makeText(activity, getString(R.string.register_invalid_date_of_birth), Toast.LENGTH_SHORT).show();
                break;
            case UNKNOWN:
                Toast.makeText(activity, getString(R.string.register_unknown), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void showProgress() {
        //   progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        //  progressBar.setVisibility(View.GONE);
    }

    @Override
    public void goToLoginView() {
        startActivity(new Intent(activity, LoginActivity.class));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void goToMainView() {
        startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    public void setData(String email, String firstname, String lastName, String phonenumber){
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastName;
        this.phonenumber = phonenumber;
    }


}
