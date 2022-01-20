package com.sky.gz.mytessdatademo.retrofit.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //从chain对象中可以获取到request和response，想要的数据都可以从这里获取
        Request request = chain.request();
        //增加头部信息
        request = addHeaders(request);
        //获取response对象
        Response response = chain.proceed(request);
        return response;
    }

    private Request addHeaders(Request request) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "12345");
        params.put("time", System.currentTimeMillis() + "");
        Headers headers = Headers.of(params);
        return request.newBuilder().headers(headers).build();

    }
}
