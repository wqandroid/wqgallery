package com.eq.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wq.photo.MediaChoseActivity;
import com.wq.photo.PhotoGalleryFragment;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    LinearLayout llcontent;
    int scw,sch;
    public  static  final int REQUEST_IMAGE=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scw=getResources().getDisplayMetrics().widthPixels-40;
        llcontent= (LinearLayout) findViewById(R.id.llcontent);
        sch=getResources().getDisplayMetrics().heightPixels-156;
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MediaChoseActivity.class);
                intent.putExtra("chose_mode",0);
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }
    public void onclick(){
        Intent intent=new Intent(this, MediaChoseActivity.class);
        intent.putExtra("chose_mode",1);
        intent.putExtra("max_chose_count",6);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            //在data中返回 选择的图片列表
            ArrayList<String>paths=data.getStringArrayListExtra("data");
            llcontent.removeAllViews();
           for (String path:paths){
               BitmapFactory.Options options=new BitmapFactory.Options();
               options.inJustDecodeBounds=true;
               BitmapFactory.decodeFile(path,options);
               int w=options.outWidth;
               int h=options.outHeight;
               ImageView imageView1=new ImageView(MainActivity.this);
               int parw=0;
               int parh=0;
               if(w>scw){
                   parw=scw;
                   parh=sch*w/h;
               }else{
                   parh=h;
                   parw=w;
               }
               LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(parw,parh);
               params.setMargins(20,20,20,20);
               imageView1.setLayoutParams(params);
               options.inJustDecodeBounds=false;
               options.outWidth=parw;
               options.outHeight=parh;
               Bitmap bitmap=BitmapFactory.decodeFile(path,options);
               imageView1.setImageBitmap(bitmap);
               llcontent.addView(imageView1);
           }
        }
    }
}
