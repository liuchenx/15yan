package org.liuyichen.fifteenyan.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by root on 15-3-12.
 * and ...
 */
@SuppressWarnings("unused")
public class Toast  {

    private static android.widget.Toast single = null;

    public static final int LENGTH_SHORT = 0;

    public static final int LENGTH_LONG = 1;

    @SuppressWarnings("WeakerAccess")
    @SuppressLint("ShowToast")
    public static android.widget.Toast makeText(Context context, CharSequence text, int duration) {

        single = android.widget.Toast.makeText(context.getApplicationContext(), text, duration);
        return single;
    }

    public static android.widget.Toast makeText(Context context, @SuppressWarnings("SameParameterValue") @StringRes()int resId, @SuppressWarnings("SameParameterValue") int duration) {
        return makeText(context, context.getString(resId), duration);
    }

    public static void cancel() {

        if (single != null) {
            single.cancel();
        }
    }
}
