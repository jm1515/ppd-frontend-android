package ppd.com.mubler.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ActivityUtils {

    private ActivityUtils() {

    }

    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 4;

    public static void closeKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null &&
                activity.getCurrentFocus().getWindowToken() != null) {

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !target.equals("") && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void checkPermissionsState(Activity activity) {
        int internetPermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.INTERNET);

        int networkStatePermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_NETWORK_STATE);

        int coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        int fineLocationPermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int wifiStatePermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_WIFI_STATE);

        if (internetPermissionCheck == PackageManager.PERMISSION_GRANTED &&
                networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED &&
                fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED &&
                wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE},
                    MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }
}
