package com.wq.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wq.photo.mode.Images;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * 调用媒体选择库
 * 需要在inten中传递2个参数
 * 1. 选择模式 chose_mode  0  //单选 1多选
 * 2. 选择张数 max_chose_count  多选模式默认 9 张
 */
public class MediaChoseActivity extends ActionBarActivity {

    public static final int CHOSE_MODE_SINGLE = 0;
    public static final int CHOSE_MODE_MULTIPLE = 1;
    public int max_chose_count = 1;
    public LinkedHashMap imasgemap = new LinkedHashMap();
    public LinkedHashSet imagesChose = new LinkedHashSet();
    PhotoGalleryFragment photoGalleryFragment;
    int chosemode = CHOSE_MODE_MULTIPLE;

    boolean isneedCrop = false;
    int crop_image_w, crop_image_h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_chose);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        chosemode = getIntent().getIntExtra("chose_mode", CHOSE_MODE_MULTIPLE);
        if (chosemode == 1) {
            max_chose_count = getIntent().getIntExtra("max_chose_count", 9);
        }
        isneedCrop = getIntent().getBooleanExtra("crop", false);
        if (isneedCrop) {
            chosemode = CHOSE_MODE_SINGLE;
            max_chose_count=1;
            crop_image_w = getIntent().getIntExtra("crop_image_w", 720);
            crop_image_h = getIntent().getIntExtra("crop_image_h", 720);
        }
        photoGalleryFragment = PhotoGalleryFragment.newInstance(chosemode, max_chose_count);
        fragmentTransaction.add(R.id.container, photoGalleryFragment, PhotoGalleryFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
    boolean isPriview = false;
    public void starPriview(LinkedHashMap map, String currentimage) {
        if(isneedCrop&&!isCropOver){
            sendStarCrop(currentimage);
        }else{
            Set<String> keys = map.keySet();
            ArrayList<String> ims = new ArrayList<>();
            int pos = 0;
            int i = 0;
            for (String s : keys) {
                ims.add((String) map.get(s));
                if (map.get(s).equals(currentimage)) {
                    pos = i;
                }
                i++;
            }
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, ImagePreviewFragemnt.newInstance(ims, pos), ImagePreviewFragemnt.class.getSimpleName());
            fragmentTransaction.addToBackStack("con");
            fragmentTransaction.commit();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            isPriview = true;
            invalidateOptionsMenu();
        }
    }

    public Fragment getCurrentFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public LinkedHashMap getImageChoseMap() {
        return imasgemap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_gallery_menu, menu);
        if (isPriview && (chosemode == CHOSE_MODE_MULTIPLE)) {
            menu.findItem(R.id.menu_photo_delete).setVisible(true);
        } else {
            menu.findItem(R.id.menu_photo_delete).setVisible(false);
        }
        if (imasgemap.size() < 1) {
            menu.findItem(R.id.menu_photo_count).setEnabled(false);
            menu.findItem(R.id.menu_photo_count).setVisible(false);
        } else {
            menu.findItem(R.id.menu_photo_count).setEnabled(true);
            menu.findItem(R.id.menu_photo_count).setVisible(true);
            if (chosemode == CHOSE_MODE_MULTIPLE) {
                menu.findItem(R.id.menu_photo_count).setTitle("发送(" + imasgemap.size() + "/" + max_chose_count + ")");
            } else {
                menu.findItem(R.id.menu_photo_count).setTitle("发送(1)");
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            popFragment();
        } else if (item.getItemId() == R.id.menu_photo_delete) {
            ImagePreviewFragemnt fragemnt = (ImagePreviewFragemnt) getCurrentFragment(ImagePreviewFragemnt.class.getSimpleName());
            if (fragemnt != null) {
                String img = fragemnt.delete();
                Iterator iterator = imasgemap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if (imasgemap.get(key).equals(img)) {
                        iterator.remove();
                    }
                }
                invalidateOptionsMenu();
            }
        } else if (item.getItemId() == R.id.menu_photo_count) {
            sendImages();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        if (keyCode == KeyEvent.KEYCODE_BACK && fm.getBackStackEntryCount() > 0) {
            popFragment();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void log(String msg) {
        Log.i("gallery", msg);
    }

    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        isPriview = false;
        invalidateOptionsMenu();
        if (photoGalleryFragment != null && chosemode == CHOSE_MODE_MULTIPLE) {
            photoGalleryFragment.notifyDataSetChanged();
        }
    }


    boolean isCropOver = false;

    public void sendImages() {
        if (isneedCrop && !isCropOver) {
            Intent intent = new Intent(this, CropImageActivity.class);
            Iterator iterator = imasgemap.keySet().iterator();
            File file = new File(iterator.next().toString());
            if (!file.exists()) {
                Toast.makeText(this, "获取文件失败", Toast.LENGTH_SHORT).show();
            }
            intent.setData(Uri.fromFile(file));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempFile().getAbsolutePath());
            startActivityForResult(intent, REQUEST_CODE_CROP);
        } else {
            Intent intent = new Intent();
            ArrayList<String> img = new ArrayList<>();
            Iterator iterator = imasgemap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                img.add((String) imasgemap.get(key));
            }
            intent.putExtra("data", img);
            setResult(RESULT_OK, intent);
            finish();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CROP && (chosemode == CHOSE_MODE_SINGLE)) {
            Intent intent = new Intent();
            ArrayList<String> img = new ArrayList<>();
            String crop_path=data.getStringExtra("crop_path");
            isCropOver=true;
            Bitmap bitmap= BitmapFactory.decodeFile(crop_path);
            if(bitmap==null){
                Toast.makeText(this, "截取图片失败", Toast.LENGTH_SHORT).show();
            }else{
                img.add(crop_path);
                intent.putExtra("data", img);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA && (chosemode == CHOSE_MODE_SINGLE)) {
            if(isneedCrop&&!isCropOver){
                sendStarCrop(currentfile.getAbsolutePath());
            }else{
                Intent intent = new Intent();
                ArrayList<String> img = new ArrayList<>();
                img.add(currentfile.getAbsolutePath());
                intent.putExtra("data", img);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA && (chosemode == CHOSE_MODE_MULTIPLE)) {
            getImageChoseMap().put(currentfile.getAbsolutePath(), currentfile.getAbsolutePath());
            invalidateOptionsMenu();
        }
    }

    public static final int REQUEST_CODE_CAMERA = 2001;
    public static final int REQUEST_CODE_CROP = 2002;
    File currentfile;
    public void sendStarCamera() {
        currentfile = getTempFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentfile));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
    public void sendStarCrop(String path) {
        Intent intent = new Intent(this, CropImageActivity.class);
        intent.setData(Uri.fromFile(new File(path)));
        intent.putExtra("crop_image_w",crop_image_w);
        intent.putExtra("crop_image_h",crop_image_h);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempFile().getAbsolutePath());
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }
    public File getTempFile() {
        return  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".tmp.jpg");
    }

}
