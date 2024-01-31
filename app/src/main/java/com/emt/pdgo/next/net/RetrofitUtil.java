package com.emt.pdgo.next.net;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.util.MyLogInterceptor;
import com.emt.pdgo.next.util.CacheUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 文件说明
 *
 * @author chenjh
 * @date 2018/11/27 16:53
 */
public class RetrofitUtil {


    public static final String HTTP_HEAD = "http://";

//    public static String BASE_URL = "http://192.168.31.38:8765/api/";//http://192.168.31.38:8765/api/auth/jwt/token
    public static String BASE_URL = "https://ejc.ckdcloud.com/api/";
//        PdproHelper.getInstance().getAppServerUrl() == null
//        ? "https://ejc.ckdcloud.com/api/" : PdproHelper.getInstance().getAppServerUrl()+"/api/";//正式线
//    public static String BASE_URL = "https://iot.ckdcloud.com/api/";//测试线

    private static APIService service;
    private static APIService serviceVersion;

    private static APIService serviceVersion11Token;

    private static Retrofit retrofit;
    private static Retrofit retrofitVersion;


    public static APIService getService() {
        if (service == null) {
            service = getRetrofit().create(APIService.class);
        }
//        Log.e("baseUrl","baseUrl--"+BASE_URL);
        return service;
    }

    public static APIService getServiceVersion() {
        if (serviceVersion == null) {
            serviceVersion = getUpversionRetrofit().create(APIService.class);
        }
        return serviceVersion;
    }


    public static APIService getService(RetrofitAbstractFactory retrofitAbstractFactory, String token) {
        if (serviceVersion11Token == null) {
            serviceVersion11Token = getRetrofit(retrofitAbstractFactory, token).create(APIService.class);
        }
        return serviceVersion11Token;
    }


    private static Retrofit getRetrofit(RetrofitAbstractFactory retrofitAbstractFactory, String token) {

        return retrofitAbstractFactory.createVersionAndTokenRetrofit(token);
    }

    private static Retrofit getRetrofit() {
//        if (retrofit == null) {
        //头部信息
        OkHttpClient client = new OkHttpClient.Builder()
                    //设置缓存
                    //.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024))
                    //log请求参数
                    .addInterceptor(new MyLogInterceptor())
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request originalRequest;
                        if (CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.TOKEN) == null) {
                            originalRequest = chain.request().newBuilder()
//                                        .addHeader("apiVersion", "1.0.0")
                                    .build();
                        } else {
                            originalRequest = chain.request().newBuilder()
//                                        .addHeader("apiVersion", "1.0.0")
                                    .addHeader("Authorization", CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.TOKEN))
                                    .build();
                        }
                        return chain.proceed(originalRequest);
                    })
                    .build();
            return retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(PdproHelper.getInstance().getAppServerUrl()==null?BASE_URL:PdproHelper.getInstance().getAppServerUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
    }

    private static Retrofit getUpversionRetrofit() {
        if (retrofitVersion == null) {
            //头部信息
            OkHttpClient client = new OkHttpClient.Builder()
                    //设置缓存
                    //.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024))
                    //log请求参数
                    .addInterceptor(new MyLogInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request().newBuilder()
                                .addHeader("apiVersion", "1.0.0")
                                .build();//增加头部版本号
                        return chain.proceed(originalRequest);
                    })
                    .build();
            retrofitVersion = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofitVersion;
    }


    public void cleanAPIServices() {
        retrofit = null;
        retrofitVersion = null;
        service = null;
        serviceVersion = null;
    }

    /****
     * 对请求的结果，进行统一的规范
     *
     * @param response
     * @param <T>
     * @return
     */
    public <T> Observable<T> flatResponse(final MyResponse<T> response) {

        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (response.isSuccess()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(response.data);
                    }
                } else {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(new APIException(response.status, response.message));
                    }
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }

            }


        });
    }


    public class APIException extends Exception {
        public String code;
        public String message;

        public APIException(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    final Observable.Transformer transformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1() {
                        @Override
                        public Object call(Object response) {
                            return flatResponse((MyResponse<Object>) response);
                        }
                    });
        }
    };


    protected <T> Observable.Transformer<MyResponse<T>, T> applySchedulers() {

        return (Observable.Transformer<MyResponse<T>, T>) transformer;
    }


}
