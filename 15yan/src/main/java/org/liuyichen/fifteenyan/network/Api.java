package org.liuyichen.fifteenyan.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.liuyichen.fifteenyan.network.plugin.StringConverterFactory;
import org.liuyichen.fifteenyan.network.plugin.Network;
import org.liuyichen.fifteenyan.model.Data;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;


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
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(Network.getOkClient())
                .build();

        service = retrofit.create(ApiService.class);
    }

    public static Observable<Data> getStorys(int offset, String orderBy) {
        return service.getStorys(offset, orderBy);
    }

    public static Observable<String> getDetailStory(String storyId) {
        return service.getDetailStory(storyId);
    }
}
