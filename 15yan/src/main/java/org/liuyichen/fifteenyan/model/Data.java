package org.liuyichen.fifteenyan.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * By liuyichen on 15-3-3 下午5:25.
 */
@SuppressWarnings("unused")
public class Data {


    @Expose
    public String now;
    @Expose
    public boolean ok;
    @Expose
    public int limit;
    @Expose
    public int offset;
    @Expose
    public int total;
    @Expose
    public ArrayList<Story> result;
}
