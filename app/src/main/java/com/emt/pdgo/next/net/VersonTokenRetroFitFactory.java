package com.emt.pdgo.next.net;

import retrofit2.Retrofit;

/**
 *
 *
 * @author chenjh
 * @date 2019/3/26 11:23
 */
public class VersonTokenRetroFitFactory implements RetrofitAbstractFactory {

    /**
     * 创建版本号和带有token的头部信息
     * @param token
     * @return
     */
    @Override
    public Retrofit createVersionAndTokenRetrofit(String token) {
        return new TokenVersonRetroFit().getRetrofit(token);
    }

}
