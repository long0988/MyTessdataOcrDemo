package com.sky.gz.mytessdatademo.listener;

/**
 * @author shiqilong
 * @date 2020/7/28
 * Description:
 */
public interface DownloadFileListener {
    void onStart(String tag);//下载开始

    void onProgress(String tag, int progress);//下载进度

    void onDownloadSuccess(String tag, String path);//下载完成

    void onDownloadFail(String tag, String errorInfo);//下载失败
}
