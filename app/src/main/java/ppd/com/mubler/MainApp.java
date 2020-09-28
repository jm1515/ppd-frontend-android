package ppd.com.mubler;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;


public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        // TODO set User session here
    }


}
