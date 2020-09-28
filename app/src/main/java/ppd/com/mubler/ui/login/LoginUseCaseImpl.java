package ppd.com.mubler.ui.login;

import android.util.Log;

import com.google.gson.JsonObject;

import ppd.com.mubler.data.APIClient;
import ppd.com.mubler.data.APIInterface;
import ppd.com.mubler.ui.user.UserUseCaseImpl;
import retrofit2.Call;

public class LoginUseCaseImpl implements LoginContract.LoginUseCase {
    private APIInterface apiInterface;
    private static final String TAG = "LoginUseCaseImpl";
    private JsonObject paramObject;
    private UserUseCaseImpl userSession;
    private String token;

    @Override
    public void doLogin(final String email, final String password, final Callback callback, LoginContract.LoginView loginView) {
        Log.i(TAG,"Login user : " + email + " " + password);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        paramObject = new JsonObject();
        userSession = new UserUseCaseImpl();

        paramObject.addProperty("email", email);
        paramObject.addProperty("password", password);
        Log.i(TAG, "Login JSON param : " + paramObject.toString());

        Call<JsonObject> call = apiInterface.doLogin(paramObject);
        call.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.i(TAG, "Server response for doLogin http return code : " + response.code());

                if (response.code()  != 200){
                    callback.onLoginError(LoginError.UNKNOWN);
                    call.cancel();
                    Log.e(TAG, "Login failed ! " + response.code());
                    return;
                }

                Log.i(TAG, "Server response for doLogin : " + response.body().toString());
                Log.i(TAG, "Token : " + response.body().get("token").toString());

                token = response.body().get("token").toString().replace("\"", "");
                boolean result = userSession.setUserSession(token, loginView);

                if (result){
                    callback.onLoginSuccess();
                }else{
                    callback.onLoginError(LoginError.UNKNOWN);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onLoginError(LoginError.UNKNOWN);
                call.cancel();
                Log.e(TAG, "Login failed ! ");
            }
        });



    }


}
