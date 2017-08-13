package com.abdulaziz.egytronica.data.remote;

import com.abdulaziz.egytronica.data.model.Request;
import com.abdulaziz.egytronica.data.model.Response;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by abdulaziz on 10/24/16.
 */
public interface Service {

    @Headers("Content-Type: application/json")
    @POST("api/auth/login")
    Observable<Response> login(@Body Request r);

    @Headers("Content-Type: application/json")
    @POST("api/auth/signup")
    Observable<Response> register(@Body Request r);

    @Headers("Content-Type: application/json")
    @GET("api/mobile/resend")
    Observable<Response> resendPin(@Query("token") String auth);
//    Observable<Response> resendPin(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("api/mobile/verify")
    Observable<Response> verifyMobile(@Query("token") String auth, @Body Request r);
//    Observable<Response> verifyMobile(@Header("Authorization") String auth, @Body Request r);

    @Headers("Content-Type: application/json")
    @PUT("api/users/registration_id")
    Observable<Response> sendRegistrationId(@Query("token") String auth, @Body Request r);
//    Observable<Response> sendRegistrationId(@Header("Authorization") String auth, @Body Request r);

    @Headers("Content-Type: application/json")
    @POST("api/boards")
    Observable<Response> createBoard(@Query("token") String auth, @Body Request r);
//    Observable<Response> createBoard(@Header("Authorization") String auth, @Body Request r);

    @Headers("Content-Type: application/json")
    @GET("api/boards")
    Observable<Response> getBoards(@Query("token") String auth);
//    Observable<Response> getBoards(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @GET("api/member/boards")
    Observable<Response> getMemberBoards(@Query("token") String auth);
//    Observable<Response> getMemberBoards(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @GET("api/boards/{id}")
    Observable<Response> getBoard(@Path("id") String id, @Query("token") String auth);
//    Observable<Response> getBoard(@Header("Authorization") String auth, @Path("id") String id);

    @Headers("Content-Type: application/json")
    @DELETE("api/boards/{board_id}/members/{member_id}")
    Observable<Response> removeMember(@Path("board_id") String boardId, @Path("member_id") String memberId, @Query("token") String auth);
//    Observable<Response> removeMember(@Header("Authorization") String auth, @Path("board_id") String boardId, @Path("member_id") String memberId);

    @Headers("Content-Type: application/json")
    @POST("api/boards/{id}/members")
    Observable<Response> addMember(@Path("id") String id, @Query("token") String auth, @Body Request r);
//    Observable<Response> addMember(@Header("Authorization") String auth, @Body Request r);

    @Headers("Content-Type: application/json")
    @GET("api/boards/{id}/members")
    Observable<Response> getMembers(@Path("id") String boardId, @Query("token") String auth);
//    Observable<Response> getMembers(@Header("Authorization") String auth, @Path("id") String boardId);

    @Headers("Content-Type: application/json")
    @POST("api/boards/{id}/components")
    Observable<Response> addComponent(@Path("id") String boardId, @Query("token") String auth, @Body Request r);
//    Observable<Response> addComponent(@Header("Authorization") String auth, @Path("id") String boardId, @Body Request r);

    @Headers("Content-Type: application/json")
    @GET("api/boards/{id}/components")
    Observable<Response> getComponents(@Path("id") String boardId, @Query("token") String auth);
//    Observable<Response> getComponents(@Header("Authorization") String auth, @Path("id") String boardId);

    @Headers("Content-Type: application/json")
    @PUT("api/boards/{board_id}/components/{component_id}/control")
    Observable<Response> sendComponentControl(@Path("board_id") String boardId, @Path("component_id") String componentId, @Query("token") String auth, @Body Request r);
//    Observable<Response> sendComponentControl(@Header("Authorization") String auth, @Path("board_id") String boardId, @Path("component_id") String componentId, @Body Request r);

    @Headers("Content-Type: application/json")
    @GET("api/events")
    Observable<Response> getEvents();

    @Headers("Content-Type: application/json")
    @GET("api/news")
    Observable<Response> getNewsList();

    @Headers("Content-Type: application/json")
    @GET("api/news/{id}")
    Observable<Response> getNews(@Path("id") String newsId);


    class Creator{
        public static Retrofit retrofit;

        public static Service getService(){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalEntities.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();

            return retrofit.create(Service.class);
        }
    }
}
