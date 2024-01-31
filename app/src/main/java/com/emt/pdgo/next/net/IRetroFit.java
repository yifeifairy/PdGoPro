package com.emt.pdgo.next.net;

import retrofit2.Retrofit;

/**
 * 文件说明
 *
 * @author chenjh
 * @date 2019/3/26 11:22
 */
public interface IRetroFit {
    Retrofit getRetrofit(String token);
}
