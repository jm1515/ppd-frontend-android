package ppd.com.mubler.ui.register;

import android.app.DatePickerDialog;

import java.util.Calendar;

import androidx.fragment.app.Fragment;
import ppd.com.mubler.arch.BasePresenter;
import ppd.com.mubler.arch.BaseView;

public interface RegisterContract {
    interface RegisterView extends BaseView {
        void showRegisterSuccess();

        void showRegisterError(RegisterError error);

        void showProgress();

        void hideProgress();

        void goToLoginView();

        void goToMainView();

        void setDate(String text);

        void showDatePicker(DatePickerDialog.OnDateSetListener date, Calendar calendar);

    }

    interface RegisterPresenter extends BasePresenter<RegisterView> {
        void onStep1Clicked(String email,
                            String lastname,
                            String firstname,
                            String phoneNumber);

        void onStep2Clicked(String email,
                            String lastname,
                            String firstname,
                            String phoneNumber,
                            String password,
                            String confirmPassword);

        void onLoginClick();

        void onDateInputClick();

        void onNextButtonClicked(String email,
                                 String lastname,
                                 String firstname,
                                 String phoneNumber);

        void goToStep1(String email,
                       String lastname,
                       String firstname,
                       String phoneNumber);

    }

    interface RegisterUseCase {

        void registerUser(String email,
                           String lastname,
                           String firstname,
                           String phonenumber,
                           String password,
                           String confirmPassword,
                           RegisterContract.RegisterUseCase.Callback callback);

        interface Callback {
            void onRegisterSuccess();
            void onRegisterError(RegisterError error);
        }
    }

}
