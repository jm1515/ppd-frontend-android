package ppd.com.mubler.ui.register;

import android.util.Log;

import ppd.com.mubler.data.APIClient;
import ppd.com.mubler.data.APIInterface;
import ppd.com.mubler.data.entity.User;
import retrofit2.Call;

public class RegisterUseCaseImpl implements RegisterContract.RegisterUseCase {
    private APIInterface apiInterface;
    private static final String TAG = "RegisterUseCaseImpl";

    @Override
    public void registerUser(String email, String lastname, String firstname, String phonenumber, String password, String confirmPassword, final Callback callback) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        if (email == null || lastname == null || firstname == null || phonenumber == null){
            callback.onRegisterError(RegisterError.UNKNOWN);
        } else {
            User user = new User(firstname, lastname, email, password, phonenumber);
            Log.i(TAG, "Creating user : " + user.toString());

            Call<User> call = apiInterface.createUser(user);
            call.enqueue(new retrofit2.Callback<User>() {
                @Override
                public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                    Log.i(TAG, "Server response for createUser, http return code : " + response.code());

                    if (response.code() != 201) {
                        callback.onRegisterError(RegisterError.UNKNOWN);
                        call.cancel();
                        Log.e(TAG, "Register failed !" + response.code());
                        return;
                    }

                    Log.i(TAG, "Server response for createUser : " + response.body().toString());
                    callback.onRegisterSuccess();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "Register failed !");
                    callback.onRegisterError(RegisterError.UNKNOWN);
                    call.cancel();
                }
            });
        }
    }
}
