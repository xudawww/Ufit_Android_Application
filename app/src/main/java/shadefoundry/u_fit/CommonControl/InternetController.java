package shadefoundry.u_fit.CommonControl;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetController {

    public boolean chechConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }}
