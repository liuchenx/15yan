package org.liuyichen.fifteenyan.provider;

import org.liuyichen.fifteenyan.Const;

import ollie.OllieProvider;

/**
 * By liuyichen on 15-3-4 上午9:30.
 */
public class StoryProvider extends OllieProvider {


    @Override
    protected String getDatabaseName() {
        return Const.DB_NAME;
    }

    @Override
    protected int getDatabaseVersion() {
        return Const.DB_VERSION;
    }
}
