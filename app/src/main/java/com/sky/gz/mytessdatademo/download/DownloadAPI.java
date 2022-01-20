package com.sky.gz.mytessdatademo.download;

import android.util.Log;

import androidx.annotation.NonNull;

import com.sky.gz.mytessdatademo.retrofit.RetrofitService;
import com.sky.gz.mytessdatademo.utils.FileDownLoadUtils;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author shiqilong
 * @date 2020/7/29
 * Description:
 */
public class DownloadAPI {
    private static final String TAG = DownloadAPI.class.getName();
    private static final int DEFAULT_TIMEOUT = 15;
    public Retrofit retrofit;

    public DownloadAPI(String url, DownloadProgressListener listener) {

        DownloadInterceptor interceptor = new DownloadInterceptor(listener);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void downloadFile(@NonNull String url, File file, Observer subscriber) {
        Log.e(TAG, "downloadURL: " + url);
        retrofit.create(RetrofitService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        FileDownLoadUtils.writeFile(inputStream, file);

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
