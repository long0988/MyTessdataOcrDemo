package com.sky.gz.mytessdatademo.download;

/**
 * @author shiqilong
 * @date 2020/7/29
 * Description:
 */
public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength ,boolean done);
}
