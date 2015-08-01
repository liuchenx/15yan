package org.liuyichen.fifteenyan;

import com.facebook.stetho.Stetho;

/**
 * Created by liuchen on 15/7/4.
 * and ...
 */
public class DebugApp extends App {


    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

    }
}
