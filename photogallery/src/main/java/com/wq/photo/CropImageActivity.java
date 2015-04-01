package com.wq.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wq.photo.widget.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CropImageActivity extends ActionBarActivity {
    private String filePath;
    private String outFilePath;

    /**上传图片大小*/
    public int crop_image_w=0;
    public int crop_image_h=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        getSupportActionBar().setTitle("裁剪图片");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            crop_image_w=getIntent().getIntExtra("crop_image_w",720);
            crop_image_h=getIntent().getIntExtra("crop_image_h",720);
            Uri uri = getIntent().getData();
            if("file".equals(uri.getScheme())) {
                filePath = uri.getPath();
                outFilePath = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);
                cropImage();
            }
        } catch (Exception e) {
            finish();
        }
    }

    private void cropImage()
    {
        final CropImageView mCropImage=(CropImageView)findViewById(R.id.cropImg);
        mCropImage.setDrawable(Drawable.createFromPath(filePath), crop_image_w, crop_image_h);
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        File f= save2Storage(mCropImage.getCropImage(),getCropFile().getAbsolutePath());
                        Intent intent=new Intent();
                        intent.putExtra("crop_path",f.getAbsolutePath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).start();
            }
        });
    }

    public static File save2Storage(Bitmap bitmap, String path) {
        try {
            File filename = new File(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            try {
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filename;
        } catch (Exception e) {}
        return null;
    }
    public File getCropFile() {
        File f= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "crop.jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  f;
    }

}
