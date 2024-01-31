package com.emt.pdgo.next.net.util;

import android.text.TextUtils;


import com.emt.pdgo.next.util.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * 输出网络请求和返回的的日志信息
 *
 * @author chenjh
 * @date 2018/11/27 16:39
 */
public class MyLogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");


    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Logger.d("网络请求request=" + request.toString());
        RequestBody requestBody = request.body();
        long t1 = System.nanoTime();
        okhttp3.Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
        // Log.v(TAG, String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
        //response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        Logger.d("网络请求" + String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));



        try {
            if (request.body() instanceof MultipartBody) {
                //如果是MultipartBody，会log出一大推乱码的东东
            } else {
                if (requestBody != null) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        contentType.charset(UTF8);
                    }
                    Logger.d("网络请求数据=====" + buffer.readString(charset));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        // Log.i(TAG, "response body:" + content);
        if (content.startsWith("{")) {
            Logger.json(content.toString());
        } else {
            if (TextUtils.isEmpty(content)) {
                Logger.d("服务端返回  空数据");
            } else {
                Logger.d("服务端返回======" + content);
            }
        }

        if (TextUtils.isEmpty(content)) {
            content = "{\"message\": \"OK\",\"result\": null,\"httpCode\": 200,\"success\": true}";
        }

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}

