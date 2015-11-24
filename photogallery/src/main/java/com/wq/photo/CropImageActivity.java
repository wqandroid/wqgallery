package com.wq.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;


public class CropImageActivity extends ActionBarActivity {
    private String filePath;
    private String outFilePath;

    /**
     * 上传图片大小
     */
    public int crop_image_w = 0;
    public int crop_image_h = 0;

    CropImageView cropImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        cropImageView = (CropImageView) findViewById(R.id.cropImg);

        crop_image_w = getIntent().getIntExtra("crop_image_w", 720);
        crop_image_h = getIntent().getIntExtra("crop_image_h", 720);

        cropImageView.setAspectRatio(10, 10);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setGuidelines(1);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("裁剪图片");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Uri uri = getIntent().getData();
        if ("file".equals(uri.getScheme())) {
            filePath = uri.getPath();
            outFilePath = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);
        }
        try {
            Log.i("path", "剪裁图片路径" + filePath);
            if (new File(filePath) != null && new File(filePath).exists()) {
                cropImage();
            } else {
                Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_crop_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_rotion) {
            if (new File(filePath) != null && new File(filePath).exists()) {
                cropImageView.rotateImage(90);
                return true;
            } else return false;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void cropImage() {


        Bitmap bitmap=null;
        BitmapFactory.Options opts=new BitmapFactory.Options();
        try {
            opts.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(filePath, opts);
            int nw=opts.outWidth;
            int nh=opts.outHeight;
            opts.inJustDecodeBounds=false;
            if (nw > 1920){
//                opts.outHeight=1920;
//                opts.outWidth=nw*1920/nh;
                opts.inSampleSize=2;
            }
             bitmap =  BitmapFactory.decodeFile(filePath, opts);
        }catch (Exception e){
            e.printStackTrace();
            opts.inJustDecodeBounds=false;
            opts.inSampleSize=2;
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        }
        cropImageView.setImageBitmap(bitmap);
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        save2Storage(cropImageView.getCroppedImage(), outFilePath);
                        Intent intent = new Intent();
                        intent.putExtra("crop_path", outFilePath);
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
            if (!filename.exists()) {
                filename.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return filename;
        } catch (Exception e) {
            return null;
        }
    }

}
