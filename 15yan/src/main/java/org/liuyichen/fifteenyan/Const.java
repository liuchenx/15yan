package org.liuyichen.fifteenyan;

/**
 * By liuyichen on 15-3-4 上午9:32.
 */
public class Const {

    public static final String DB_NAME = "FifteenYan.db";

    public static final int DB_VERSION = 1;

    public static final String SETTINGS_PREFERENCES_NAME = "Settings";
    public static final String SETTINGS_PREFERENCES_ONLY_WIFI = "only_wifi";


    public static final boolean isDebug() {

        return BuildConfig.DEBUG;
    }
}
