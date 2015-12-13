package com.eq.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wq.photo.MediaChoseActivity;
import com.wq.photo.PhotoGalleryFragment;
import com.wq.photo.widget.PickConfig;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    LinearLayout llcontent;
    int scw,sch;
    public  static  final int REQUEST_IMAGE=1000;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview= (ListView) findViewById(R.id.listview);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  PickConfig.Builder(MainActivity.this)
                        .spanCount(2)
                        .pickMode(PickConfig.MODE_SINGLE_PICK).build();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new  PickConfig.Builder(MainActivity.this)
                        .isneedcrop(true)
                        .isneedcamera(false)
                        .spanCount(1)
                        .pickMode(PickConfig.MODE_SINGLE_PICK).build();

            }
        });
    }
    public void onclick(){
        new  PickConfig.Builder(MainActivity.this)
                .maxPickSize(9)
                .isneedcamera(true)
                .spanCount(4)
                .pickMode(PickConfig.MODE_MULTIP_PICK).build();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            //在data中返回 选择的图片列表
            ArrayList<String>paths=data.getStringArrayListExtra("data");
            ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,paths);
            listview.setAdapter(adapter);
        }
    }
}
