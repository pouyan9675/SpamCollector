package com.nahed.pouyan.spam_collector.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.nahed.pouyan.spam_collector.Core;
import com.nahed.pouyan.spam_collector.activities.MainActivity;

/**
 * Created by pouyan on 3/19/18.
 */

public class PermissionHelper {

    public static final int REQUEST_CODE = 101;

    public static boolean hasPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int permission = Core.getInstance().getContext().checkSelfPermission(Manifest.permission.READ_SMS);
            return permission == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestPermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS},
                101);
    }

}
