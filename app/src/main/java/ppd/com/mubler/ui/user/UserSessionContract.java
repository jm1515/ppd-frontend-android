package ppd.com.mubler.ui.user;

import ppd.com.mubler.ui.login.LoginContract;

public interface UserSessionContract{

    interface UserUseCase{
        boolean setUserSession(String token, LoginContract.LoginView loginView);
    }
}


