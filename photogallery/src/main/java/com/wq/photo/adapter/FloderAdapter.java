package com.wq.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wq.photo.R;
import com.wq.photo.mode.ImageFloder;

import java.util.List;

/**
 * Created by wangqiong on 15/3/30.
 */
public class FloderAdapter extends BaseAdapter {


    List<ImageFloder> floders;
    LayoutInflater inflater;
    Context context;
    int imgsize=0;
    public FloderAdapter(List<ImageFloder>floders,Context context){
        this.floders=floders;
        this.context=context;
        inflater=LayoutInflater.from(context);
        imgsize=dip2px(context,96);
    }
    int ckpos=0;
    public void setCheck(int pos){
        ckpos=pos;
        notifyDataSetChanged();
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
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(imgsize,imgsize)
                .into(view);

    }
    /**
     * DIP转换成PX
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
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
        holderFoler.tv_flodername.setText(floder.getName().substring(1));
        holderFoler.tv_flodercount.setText(floder.getCount()+"");
        if(position==ckpos){
            holderFoler.is_checked.setVisibility(View.VISIBLE);
        }else {
            holderFoler.is_checked.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public static  class  ViewHolderFoler {
        ImageView iv_floderimage,is_checked;
        TextView tv_flodername,tv_flodercount;
        public ViewHolderFoler(View convertView){
            is_checked= (ImageView) convertView.findViewById(R.id.is_checked);
            iv_floderimage= (ImageView) convertView.findViewById(R.id.id_dir_item_image);
            tv_flodercount= (TextView) convertView.findViewById(R.id.id_dir_item_count);
            tv_flodername= (TextView) convertView.findViewById(R.id.id_dir_item_name);
        }
    }



}
