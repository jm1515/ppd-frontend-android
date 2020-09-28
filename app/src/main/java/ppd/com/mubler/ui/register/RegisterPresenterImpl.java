package ppd.com.mubler.ui.register;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.fragment.app.FragmentManager;
import ppd.com.mubler.R;
import ppd.com.mubler.ui.utils.ActivityUtils;

public class RegisterPresenterImpl implements RegisterContract.RegisterPresenter, RegisterContract.RegisterUseCase.Callback{

    private RegisterContract.RegisterUseCase registerUseCase;
    private RegisterContract.RegisterView view;
    private Calendar calendar;
    private FragmentManager fragmentManager;

    public RegisterPresenterImpl(RegisterContract.RegisterUseCase registerUseCase, FragmentManager fragmentManager) {
        this.registerUseCase = registerUseCase;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onViewAttach(RegisterContract.RegisterView registerView) {
        this.view = registerView;
    }

    @Override
    public void onViewDetach() {
        view = null;
    }

    @Override
    public void onDestroy() {
        onViewDetach();
    }

    @Override
    public void onStep1Clicked(String email, String lastname, String firstname, String phoneNumber) {
        view.showProgress();

        if (lastname.isEmpty()) {
            view.showRegisterError(RegisterError.EMPTY_LASTNAME);
            view.hideProgress();
            return;
        }
        if (firstname.isEmpty()) {
            view.showRegisterError(RegisterError.EMPTY_FIRSTNAME);
            view.hideProgress();
            return;
        }
        if (email.isEmpty()) {
            view.showRegisterError(RegisterError.EMPTY_EMAIL);
            view.hideProgress();
            return;
        }
        if (!ActivityUtils.isValidEmail(email)) {
            view.showRegisterError(RegisterError.INVALID_EMAIL);
            view.hideProgress();
            return;
        }

        onNextButtonClicked(email, lastname, firstname, phoneNumber);
    }

    @Override
    public void onStep2Clicked(String email, String firstname, String lastname, String phonenumber, String password, String confirmPassword) {
        if (password.isEmpty()) {
            view.showRegisterError(RegisterError.EMPTY_PASSWORD);
            view.hideProgress();
            return;
        }
        if (!password.equals(confirmPassword)) {
            view.showRegisterError(RegisterError.PASSWORD_CONFIRMATION_FAILED);
            view.hideProgress();
            return;
        }
        registerUseCase.registerUser(email, firstname, lastname, phonenumber, password, confirmPassword, this);
    }

    @Override
    public void onLoginClick() {
        view.goToLoginView();
    }

    @Override
    public void onDateInputClick() {
        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker DateView, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat typeFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date_t =  typeFormat.format(calendar.getTime());
                view.setDate(date_t);
            }
        };

        view.showDatePicker(date, calendar);
    }

    @Override
    public void onNextButtonClicked(String email, String lastname, String firstname, String phoneNumber) {
        RegisterStep2Fragment step2 = new RegisterStep2Fragment();
        step2.setData(email, lastname, firstname, phoneNumber);
        this.fragmentManager.beginTransaction().replace(R.id.login_placeholder,step2).commit();
    }

    @Override
    public void goToStep1(String email, String lastname, String firstname, String phoneNumber) {
        RegisterStep1Fragment step1 = new RegisterStep1Fragment();
        Log.i("TAG", "setting data");
        step1.setData(email, lastname, firstname, phoneNumber);
        this.fragmentManager.beginTransaction().replace(R.id.login_placeholder, step1).commit();
    }

    @Override
    public void onRegisterSuccess() {
        Log.i("TAG", "REGISTER SUCESS");
        if (view != null) {
            Log.i("TAG", "view not null");
            view.hideProgress();
            view.showRegisterSuccess();
            view.goToMainView();
        }
    }

    @Override
    public void onRegisterError(RegisterError error) {
        if (view != null) {
            view.hideProgress();
            view.showRegisterError(error);
        }
    }
}
