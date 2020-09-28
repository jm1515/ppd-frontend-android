package ppd.com.mubler.ui.user;

import android.util.Log;

import ppd.com.mubler.data.APIClient;
import ppd.com.mubler.data.APIInterface;
import ppd.com.mubler.data.entity.User;
import ppd.com.mubler.ui.login.LoginContract;
import retrofit2.Call;

public class UserUseCaseImpl  implements UserSessionContract.UserUseCase{
    private APIInterface apiInterface;
    private static final String TAG = "UserUseCaseImpl";
    private UserSession userSession;
    private User user;
    private boolean callIsOver = false;

    @Override
    public boolean setUserSession(final String token, LoginContract.LoginView loginView) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        userSession = UserSession.getInstance();

        Call<User> call = apiInterface.getUser(token);
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                Log.i(TAG, "Server response for setUserSession http return code : " + response.code());

                if (response.code()  != 200){
                    call.cancel();
                    Log.e(TAG, "setUserSession call failed ! " + response.code());
                    return;
                }
                Log.i(TAG, "Server response for setUserSession call : " + response.body());
                user = response.body();
                userSession.initSession(response.body(), token);
                loginView.goToMainView();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
                Log.e(TAG, "setUserSession call failed !");
                return;
            }
        });
        return true;
    }
}
