package org.liuyichen.fifteenyan.api;

import org.liuyichen.fifteenyan.model.Data;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * By liuyichen on 15-3-3 下午4:47.
 */
public interface ApiService {

    String BASE_URL = "http://www.15yan.com/";

    @GET("apis/story.json?retrieve_type=by_topic&topic_id=bMMGZQUx50l&limit=20")
    Observable<Data>  getStorys(@Query("offset") int offset, @Query("order_by") String orderBy);

    @GET("topic/shi-wu-yan-chuang-kou-wen-zhang-ku/{id}/")
    Observable<String> getDetailStory(@Path("id") String storyId);
}
