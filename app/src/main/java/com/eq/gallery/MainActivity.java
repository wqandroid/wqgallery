package com.eq.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wq.photo.MediaChoseActivity;
import com.wq.photo.PhotoGalleryFragment;
import com.wq.photo.widget.PickConfig;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    LinearLayout ll_max_chose_layout;
    ListView listview;
    AppCompatCheckBox cb_is_single_mode,cb_is_need_camera,cb_is_need_crop,cb_is_need_actionbar;
    EditText ed_span_count,ed_maxchose_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview= (ListView) findViewById(R.id.listview);
        cb_is_single_mode=(AppCompatCheckBox)findViewById(R.id.cb_is_single_mode);
        cb_is_need_camera=(AppCompatCheckBox)findViewById(R.id.cb_is_need_camera);
        cb_is_need_crop=(AppCompatCheckBox)findViewById(R.id.cb_is_need_crop);
        cb_is_need_actionbar=(AppCompatCheckBox)findViewById(R.id.cb_is_need_actionbar);
        ed_span_count= (EditText) findViewById(R.id.ed_span_count);
        ed_maxchose_count= (EditText) findViewById(R.id.ed_maxchose_count);
        ll_max_chose_layout= (LinearLayout) findViewById(R.id.ll_max_chose_layout);

        cb_is_single_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_is_need_crop.setEnabled(true);
                    cb_is_need_actionbar.setEnabled(true);
                    ll_max_chose_layout.setVisibility(View.GONE);
                }else {
                    cb_is_need_actionbar.setChecked(false);
                    cb_is_need_crop.setChecked(false);
                    cb_is_need_crop.setEnabled(false);
                    cb_is_need_actionbar.setEnabled(false);
                    ll_max_chose_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.bt_start_chose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chose_mode=cb_is_single_mode.isChecked()?PickConfig.MODE_SINGLE_PICK:PickConfig.MODE_MULTIP_PICK;
                new  PickConfig.Builder(MainActivity.this)
                        .isneedcrop(cb_is_need_crop.isChecked())
                        .isneedcamera(cb_is_need_camera.isChecked())
                        .maxPickSize(Integer.parseInt(ed_maxchose_count.getText().toString()))
                        .spanCount(Integer.parseInt(ed_span_count.getText().toString()))
                        .pickMode(chose_mode).build();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE){
            //在data中返回 选择的图片列表
            ArrayList<String>paths=data.getStringArrayListExtra("data");
            ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,paths);
            listview.setAdapter(adapter);
        }
    }
}
