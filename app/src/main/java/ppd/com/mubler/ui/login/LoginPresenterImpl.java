package ppd.com.mubler.ui.login;

public class LoginPresenterImpl implements LoginContract.LoginPresenter, LoginContract.LoginUseCase.Callback {

    private LoginContract.LoginUseCase loginUseCase;
    private LoginContract.LoginView loginView;

    public LoginPresenterImpl(LoginContract.LoginUseCase loginUseCase){
        this.loginUseCase = loginUseCase;
    }

    @Override
    public void onViewAttach(LoginContract.LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void onViewDetach() {
        this.loginView = null;
    }

    @Override
    public void onDestroy() {
        onViewDetach();
    }

    @Override
    public void onLoginClick(String username, String password) {
        loginView.showProgress();

        if (username.isEmpty()) {
            username = "test@test.fr";
//            loginView.showLoginError(LoginError.EMPTY_USERNAME);
//            loginView.hideProgress();
//            return;
        }
        if (password.isEmpty()) {
            password = "test";
//            loginView.showLoginError(LoginError.EMPTY_PASSWORD);
//            loginView.hideProgress();
//            return;
        }

        loginUseCase.doLogin(username, password, this, loginView);
    }

    @Override
    public void onPasswordRecoveryClick() {
        loginView.goToPasswordRecoveryView();
    }

    @Override
    public void onRegisterClick() {
        loginView.goToRegisterView();
    }

    /**
     * LoginUseCase Callback
     */

    @Override
    public void onLoginSuccess() {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.showLoginSuccess();
        }
    }

    @Override
    public void onLoginError(LoginError error) {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.showLoginError(error);
        }
    }
}
