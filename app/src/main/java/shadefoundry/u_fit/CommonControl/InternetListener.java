package shadefoundry.u_fit.CommonControl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.net.ConnectivityManager.TYPE_MOBILE;

public class InternetListener  {

    public static String getConnectivityStatus(Context context) { {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return "wify";

            if(activeNetwork.getType() == TYPE_MOBILE)
                return "data";

        }
        return "No Internet";
    }

}}
