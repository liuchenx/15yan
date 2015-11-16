package org.liuyichen.fifteenyan.activity;

import android.support.v7.app.AppCompatActivity;

import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import org.liuyichen.fifteenyan.App;

/**
 * By liuyichen on 15-3-4 上午10:50.
 */
public abstract class BaseActivty extends AppCompatActivity {


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
