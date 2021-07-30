package com.sky.gz.mytessdatademo.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsFileUtils2 {

    public static void copyAssetsAllToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(Environment.getExternalStorageDirectory(), dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsAllToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsAllToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                copyAssetFileToSD(context, srcPath, dstPath);
            }
        } catch (Exception e) {
            Log.e("eeeeeeeeeeee", "copyAssetsAllToDst=>" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void copyAssetFileToSD(Context context, String fileName, String dstPath) throws IOException {
        File outFile = new File(Environment.getExternalStorageDirectory(), dstPath);
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        try {
            InputStream is = context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void assets2SDCard(Context context, String assetsFileName, String destFilePath) throws IOException {
        File trainFile = new File(destFilePath);
        if (!trainFile.exists()) {
            trainFile.createNewFile();
            FileOutputStream fos = null;
            InputStream is = null;
            try {
                is = context.getAssets().open(assetsFileName);
                byte[] bytes = new byte[1024];
                int length = 0;
                fos = new FileOutputStream(trainFile);
                while ((length = is.read(bytes)) != -1) {

                    fos.write(bytes, 0, length);

                }
                fos.flush();
            } catch (IOException e) {
                throw e;
            } finally {
                try {
                    if (is != null)
                        is.close();
                    if (fos != null) {

                        fos.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
