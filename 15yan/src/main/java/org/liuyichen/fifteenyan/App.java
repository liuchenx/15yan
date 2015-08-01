package org.liuyichen.fifteenyan;

import android.app.Application;
import ollie.Ollie;
/**
 * By liuyichen on 15-3-3 下午4:39.
 */
public class App extends Application {

    private static App self;


    @Override
    public void onCreate() {
        self = this;
        super.onCreate();

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
