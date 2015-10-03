package yandex;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ivansemenov on 30.09.15.
 */
public interface YandexAPI {


    @GET("/getLangs")
    YandexResponseLanguage langList(@Query("key") String key);

    @GET("/translate")
    YandexResponseTranslated translate(@Query("key") String key, @Query("text") String text, @Query("lang") String lang);
}
