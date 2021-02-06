package com.example.runtimepermission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import static com.example.runtimepermission.MainActivity.PREFERENCES;

public class Permission {
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void setShouldShowStatus(Context context, String... permissions) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String aPermissions : permissions) {
            editor.putBoolean(aPermissions, true);
        }
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(Activity activity, String permission) {
        boolean prevShouldShowStatus = getRationaleDisplayStatus(activity, permission);
        boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
        return prevShouldShowStatus != currShouldShowStatus;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(Activity activity, String... permission) {
        int count = 0;
        for (String aPermission : permission) {
            if (neverAskAgainSelected(activity, aPermission)) {
                count++;
            }
        }
        return count == permission.length;
    }

    private static boolean getRationaleDisplayStatus(Context context, String permission) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(permission, false);
    }
}