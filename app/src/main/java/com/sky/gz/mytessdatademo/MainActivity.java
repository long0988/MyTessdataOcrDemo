package com.sky.gz.mytessdatademo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.sky.gz.mytessdatademo.utils.AssetsFileUtils2;
import com.sky.gz.mytessdatademo.utils.FileUtil;
import com.sky.gz.mytessdatademo.utils.ThreadManager;

public class MainActivity extends AppCompatActivity {
    static final String DEST_PATH = "tessdata";
    static final String TESSBASE_PATH = Environment.getExternalStorageDirectory().toString() + "/sky/";
    //识别语言英文
    static final String DEFAULT_LANGUAGE = "eng";
    //识别语言简体中文
    static final String CHINESE_LANGUAGE = "chi_sim";

    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1007;
    private TextView mTvInfo;
    private ImageView mImageview;
    private int mPsmType = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvInfo = findViewById(R.id.info);
        mImageview = findViewById(R.id.imageview);
        Spinner spinner = findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mPsmType = pos;
                String[] languages = getResources().getStringArray(R.array.PSM_TYPE);
                Toast.makeText(MainActivity.this, "你点击的是:" + languages[pos], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spinner.setSelection(mPsmType);
        requestPermission();

    }

    private void copyTessdata() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                AssetsFileUtils2.copyAssetsAllToDst(MainActivity.this, "tessdata", DEST_PATH);
            }
        });
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_CONTACTS)) {
//
//            } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//            }
        } else {
            copyTessdata();
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，通用文字识别
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            String absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
            mImageview.setImageBitmap(bitmap);
            getText(bitmap);
        }
    }

    /**
     * 获取图片上的文字
     *
     * @param bitmap
     * @return
     */
    private String getText(Bitmap bitmap) {
        String retStr = "No result";
        try {
            TessBaseAPI tessBaseAPI = new TessBaseAPI();
            //初始化OCR的训练数据路径与语言
            tessBaseAPI.init(TESSBASE_PATH, CHINESE_LANGUAGE);
            // 识别黑名单
//            tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=÷-[]}{;:'\"\\|~`," +
//                    "./<>?" + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
//            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            //设置识别模式
            tessBaseAPI.setPageSegMode(mPsmType);
            //设置要识别的图片
            tessBaseAPI.setImage(bitmap);
            retStr = tessBaseAPI.getUTF8Text();
            mTvInfo.setText(retStr);
            tessBaseAPI.clear();
            tessBaseAPI.end();
        } catch (Exception e) {
            Log.e("eeeeeeeeeee", e.getMessage());
        }
        return retStr;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    copyTessdata();
                } else {
                }
                return;
            }
        }
    }

//    TessBaseAPI.PageSegMode
//    PSM_OSD_ONLY, ///< Orientation and script detection only. 定位和脚本检测
//    PSM_AUTO_OSD, ///< Automatic page segmentation with orientation and < script detection. (OSD) 自动定位、检测、分割
//    PSM_AUTO_ONLY, ///< Automatic page segmentation, but no OSD, or OCR. 自动(仅)分割
//    PSM_AUTO, ///< Fully automatic page segmentation, but no OSD. 完全自动分割
//    PSM_SINGLE_COLUMN, ///< Assume a single column of text of variable sizes. 仅有一列大小变化的文本
//    PSM_SINGLE_BLOCK_VERT_TEXT, ///< Assume a single uniform block of vertically aligned text. 假设一个统一的垂直对齐的文本块
//    PSM_SINGLE_BLOCK, ///< Assume a single uniform block of text. (Default.) 假设一个统一的文本块
//    PSM_SINGLE_LINE, ///< Treat the image as a single text line. 将图像视为一条单独的文本行
//    PSM_SINGLE_WORD, ///< Treat the image as a single word. 单独的词
//    PSM_CIRCLE_WORD, ///< Treat the image as a single word in a circle. 将图像看作一个圆圈中的单个单词
//    PSM_SINGLE_CHAR, ///< Treat the image as a single character. 一个字符
//    PSM_SPARSE_TEXT, ///< Find as much text as possible in no particular order. 找到尽可能多的文本，没有特别的顺序
//    PSM_SPARSE_TEXT_OSD, ///< Sparse text with orientation and script det. 带定向和脚本依据的稀疏文本
//    PSM_RAW_LINE, ///< Treat the image as a single text line, bypassing hacks that are Tesseract-specific. 将图像看作一个单独的文本行，绕过Tesseract-specific的检测
//    PSM_COUNT ///< Number of enum entries. 枚举的条目数
}