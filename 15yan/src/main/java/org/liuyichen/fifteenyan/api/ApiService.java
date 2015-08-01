package org.liuyichen.fifteenyan.api;

import org.liuyichen.fifteenyan.model.Data;
import org.liuyichen.fifteenyan.model.Detail;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * By liuyichen on 15-3-3 下午4:47.
 */
public interface ApiService {

    String BASE_URL = "http://www.15yan.com";

    @GET("/apis/story.json?retrieve_type=by_topic&order_by=latest&topic_id=bMMGZQUx50l&limit=20")
    void getStorys(Callback<Data> callback);

    @GET("/apis/story.json?retrieve_type=by_topic&topic_id=bMMGZQUx50l&limit=20")
    void getStorys(@Query("offset") int offset, @Query("order_by") String orderBy, Callback<Data> callback);

    @GET("/apis/story.json?retrieve_type=by_topic&order_by=latest&topic_id=bMMGZQUx50l")
    void getStorys(@Query("limit") int num, @Query("offset") int offset, Callback<Data> callback);


    @GET("/apis/story/web2png.json")
    void getDetailStoryImage(@Query("story_id") String id, Callback<Detail> callback);


    @GET("/topic/shi-wu-yan-chuang-kou-wen-zhang-ku/{id}/")
    void getDetailStory(@Path("id") String storyId, Callback<Response> callback);
}
