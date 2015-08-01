package org.liuyichen.fifteenyan.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.liuyichen.fifteenyan.App;
import org.liuyichen.fifteenyan.Const;

/**
 * Created by root on 15-3-13.
 * and ...
 */
public class Settings {

    public static boolean isOnlyWifiOpen() {

        SharedPreferences pf = App.getSelf().getSharedPreferences(Const.SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pf.getBoolean(Const.SETTINGS_PREFERENCES_ONLY_WIFI, true);
    }

    public static void switchOnlyWifiMode(boolean is) {

        SharedPreferences pf = App.getSelf().getSharedPreferences(Const.SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pf.edit();
        ed.putBoolean(Const.SETTINGS_PREFERENCES_ONLY_WIFI, is);
        ed.apply();
    }

    public static boolean canLoadImage() {

        SharedPreferences pf = App.getSelf().getSharedPreferences(Const.SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean isOpenOnlyWifi = pf.getBoolean(Const.SETTINGS_PREFERENCES_ONLY_WIFI, true);
        return !(isOpenOnlyWifi && !NetWorkHelper.isWifi(App.getSelf()));

    }

    public static String getAppVersion() {
        Application app = App.getSelf();
        PackageInfo pinfo = null;
        String ver = "1.0.0";
        try {
            pinfo = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
            ver = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ver;
    }
}
