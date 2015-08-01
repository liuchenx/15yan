package org.liuyichen.fifteenyan.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.liuyichen.fifteenyan.Const;
import org.liuyichen.fifteenyan.http.Network;
import org.liuyichen.fifteenyan.model.Data;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by liuchen on 15/8/1.
 * and ...
 */
public class Api {

    static ApiService service;
    static {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(ApiService.BASE_URL)
                .setLogLevel(Const.isDebug() ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(Network.getOkClient()))
                .build();
        service = restAdapter.create(ApiService.class);
    }

    public static void getStorys(int offset, String orderBy, Callback<Data> callback) {
        service.getStorys(offset, orderBy, callback);
    }

    public static void getDetailStory(String storyId, Callback<Response> callback) {
        service.getDetailStory(storyId, callback);
    }
}
