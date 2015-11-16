package org.liuyichen.fifteenyan;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import ollie.Ollie;
/**
 * By liuyichen on 15-3-3 下午4:39.
 */
public class App extends Application {

    private static App self;

    private RefWatcher refWatcher;
    public static RefWatcher getRefWatcher() {
        return self.refWatcher;
    }

    @Override
    public void onCreate() {
        self = this;
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        Ollie
            .with(this)
            .setName(Const.DB_NAME)
            .setVersion(Const.DB_VERSION)
            .setLogLevel(Const.isDebug()? Ollie.LogLevel.FULL : Ollie.LogLevel.NONE)
            .setCacheSize(10 * 1024 * 1024)
            .init();

    }

    public static App getSelf() {
        return self;
    }
}
