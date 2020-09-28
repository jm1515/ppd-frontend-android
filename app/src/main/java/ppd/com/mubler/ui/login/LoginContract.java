package ppd.com.mubler.ui.login;

import ppd.com.mubler.arch.BasePresenter;
import ppd.com.mubler.arch.BaseView;

public interface LoginContract {
    interface LoginView extends BaseView {
        void showLoginSuccess();

        void showLoginError(LoginError error);

        void showProgress();

        void hideProgress();

        void goToRegisterView();

        void goToPasswordRecoveryView();

        void goToMainView();
    }

    interface LoginPresenter extends BasePresenter<LoginView> {
        void onLoginClick(String username, String password);

        void onPasswordRecoveryClick();

        void onRegisterClick();

    }

    interface LoginUseCase {
        void doLogin(String username, String password, Callback callback, LoginView loginView);

        interface Callback {
            void onLoginSuccess();

            void onLoginError(LoginError error);
        }
    }
}
