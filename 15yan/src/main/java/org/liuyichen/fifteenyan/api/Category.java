package org.liuyichen.fifteenyan.api;

/**
 * Created by root on 15-3-12.
 * and ...
 */
public enum Category {

    LATEST("latest"), HOT("trending");

    private final String v;

    Category(String v) {
        this.v = v;
    }

    public String value() {
        return v;
    }
}
