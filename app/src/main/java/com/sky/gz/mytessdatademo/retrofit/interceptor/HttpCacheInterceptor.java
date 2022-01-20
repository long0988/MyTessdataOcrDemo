package com.sky.gz.mytessdatademo.retrofit.interceptor;



import com.sky.gz.mytessdatademo.utils.NetUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class HttpCacheInterceptor implements Interceptor {
    //  配置缓存的拦截器
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //request=repaceBaseUrl(request);//多端口未处理完，临时调试
        request = addHeaders(request);
//        if (!NetUtils.isConnected(this) {  //没网强制从缓存读取
//            request = request.newBuilder()
//                    .cacheControl(CacheControl.FORCE_CACHE)
//                    .build();
//        }

        Response originalResponse = chain.proceed(request);
        String cacheControl = request.cacheControl().toString();

        return originalResponse.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .build();
//        if (NetUtils.isConnected(Global.getInstance().getContext())) {
//            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
//            String cacheControl = request.cacheControl().toString();
//
//            return originalResponse.newBuilder()
//                    .header("Cache-Control", cacheControl)
//                    .removeHeader("Pragma")
//                    .build();
//        } else {
//            return originalResponse.newBuilder()
//                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
//                    .removeHeader("Pragma")
//                    .build();
//        }
    }

    private Request addHeaders(Request request) {
        Map<String, String> params = new HashMap<>();
        String token = "";
        if (token != null && !token.equals("")) {
            params.put("token", token);
        }
        params.put("Content-Type", "application/json;charset=UTF-8");
        Headers headers = Headers.of(params);
        return request.newBuilder().headers(headers).build();
//        return request.newBuilder().addHeader(Keys.KEY_AUTHORIZATION, token).addHeader("Content-Type", "application/json;charset=UTF-8").build();

    }
}
