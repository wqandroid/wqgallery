package com.wq.photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wq.photo.mode.ImageFloder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqiong on 15/3/30.
 */
public class FloderAdapter extends BaseAdapter {


    List<ImageFloder> floders;
    LayoutInflater inflater;
    public FloderAdapter(List<ImageFloder>floders,Context context){
        this.floders=floders;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return floders.size();
    }

    @Override
    public Object getItem(int position) {
        return floders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void displayImage(String url, ImageView view) {
        com.wq.photo.ImageLoader.getInstance(3, com.wq.photo.ImageLoader.Type.LIFO)
                .loadImage(url,view);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderFoler holderFoler=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.list_dir_item,null);
            holderFoler=new ViewHolderFoler(convertView);
            convertView.setTag(holderFoler);
        }else{
            holderFoler= (ViewHolderFoler) convertView.getTag();
        }
        ImageFloder floder=floders.get(position);
        displayImage(floder.getFirstImagePath(),holderFoler.iv_floderimage);
        holderFoler.tv_flodername.setText(floder.getName());
        holderFoler.tv_flodercount.setText(floder.getCount()+"");
        return convertView;
    }

    public static  class  ViewHolderFoler {

        ImageView iv_floderimage;
        TextView tv_flodername,tv_flodercount;
        public ViewHolderFoler(View convertView){
            iv_floderimage= (ImageView) convertView.findViewById(R.id.id_dir_item_image);
            tv_flodercount= (TextView) convertView.findViewById(R.id.id_dir_item_count);
            tv_flodername= (TextView) convertView.findViewById(R.id.id_dir_item_name);
        }
    }



}
