package org.liuyichen.fifteenyan.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.liuyichen.fifteenyan.http.Network;
import org.liuyichen.fifteenyan.model.Data;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


/**
 * Created by liuchen on 15/8/1.
 * and ...
 */
public class Api {

    static ApiService service;
    static {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(Network.getOkClient())
                .build();

        service = retrofit.create(ApiService.class);
    }

    public static Call<Data> getStorys(int offset, String orderBy) {
        return service.getStorys(offset, orderBy);
    }

    public static Call<String> getDetailStory(String storyId) {
        return service.getDetailStory(storyId);
    }
}
