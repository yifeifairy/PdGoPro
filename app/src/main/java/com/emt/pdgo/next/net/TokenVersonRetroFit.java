package com.emt.pdgo.next.net;

import com.emt.pdgo.next.net.util.MyLogInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 文件说明
 *
 * @author chenjh
 * @date 2019/3/26 11:23
 */
public class TokenVersonRetroFit implements IRetroFit {


    public static Retrofit retrofit;


    @Override
    public Retrofit getRetrofit(final String token) {

        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    //设置缓存
                    //.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024))
                    //log请求参数
                    .addInterceptor(new MyLogInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() { //头部信息
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request().newBuilder()
                                    .addHeader("apiVersion", "1.0.0")
                                    .addHeader("Authorization", token)
                                    .build();
                            return chain.proceed(originalRequest);
                        }
                    })
                    //网络请求缓存，未实现
                    //.addInterceptor(cacheInterceptor)
                    .build();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(RetrofitUtil.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
