package ppd.com.mubler.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit RETROFIT = null;

    private APIClient() {}

    public static Retrofit getClient() {

        if (RETROFIT == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            RETROFIT = new Retrofit.Builder()
                    .baseUrl("")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return RETROFIT;
    }


}