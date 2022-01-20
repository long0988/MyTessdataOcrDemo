package com.sky.gz.mytessdatademo.retrofit;


import com.sky.gz.mytessdatademo.download.DownloadResponseBody;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Qvechar on 2020/7/27.
 */
public interface RetrofitService {

    /**
     * panel获取音乐列表
     *
     * @return
     */
    @GET("panel/getMusicList")
    Observable<BaseResponse> getMusicList();

    /**
     * panel获取图片/视频
     *
     * @return
     */
    @GET("panel/getVideoList")
    Observable<BaseResponse> getVideoList();

    /**
     * panel获取成果文件
     *
     * @return
     */
    @GET("panel/getFileList")
    Observable<BaseResponse> getFileList();

    /**
     * 下载
     *
     * @return
     */
    @Streaming
    @GET
    Observable<DownloadResponseBody> download(@Url String fileUrl);
}
