package com.wq.photo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wq.photo.MediaChoseActivity;
import com.wq.photo.R;
import com.wq.photo.widget.PickConfig;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wangqiong on 15/3/27.
 */
public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public int max_chose_count = 9;
    LayoutInflater inflater;
    List<String> imageses;
    Context context;
    public static RecyclerView.LayoutParams params;
    LinkedHashMap hashmap;
    public int currentChoseMode;
    int sWidthPix;
    int spancount;

    public PhotoAdapter(Context context, List<String> imageses, int spancount, int chosemode) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.spancount = spancount;
        this.imageses = imageses;
        sWidthPix = context.getResources().getDisplayMetrics().widthPixels;
        params = new RecyclerView.LayoutParams(sWidthPix / spancount, sWidthPix / spancount);
        currentChoseMode = chosemode;
        hashmap = ((MediaChoseActivity) context).getImageChoseMap();
    }

    public LinkedHashMap getCHoseImages() {
        return hashmap;
    }

    public void setmax_chose_count(int max_chose_count) {
        this.max_chose_count = max_chose_count;
    }

    String imgdir;

    public void setDir(String dir) {
        imgdir = dir;
    }


    public String getDir() {
        if (imgdir.equals("")) {
            return "";
        } else {
            return imgdir + "/";
        }
    }

    boolean isNeedCamera = true;

    /**
     * 是否在第一个item现实相机
     *
     * @param isNeedCamera
     */
    public void setNeedCamera(boolean isNeedCamera) {
        this.isNeedCamera = isNeedCamera;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_CAMERA) {
            holder = new CameraViewHolder(inflater.inflate(R.layout.item_photo_camera_layout, parent, false));
        } else {
            holder = new ImageViewHolder(inflater.inflate(R.layout.item_photo_image_layout, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_IMAGE) {
            final ImageViewHolder ivholder = (ImageViewHolder) holder;
            final String images = getDir() + getItem(position);
            displayImage(images, ivholder.iv_image);
            if (currentChoseMode == PickConfig.MODE_MULTIP_PICK) {
                ivholder.checkBox.setVisibility(View.VISIBLE);
                ivholder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hashmap.containsKey(images)) {
                            hashmap.remove(images);
                            ivholder.alpha_view.setVisibility(View.GONE);
                            ivholder.checkBox.setSelected(false);
                        } else {
                            if (hashmap.size() >= max_chose_count) {
                                Toast.makeText(context, "你最多只能选择" + max_chose_count + "张照片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            hashmap.put(images, images);
                            ivholder.alpha_view.setVisibility(View.VISIBLE);
                            ivholder.checkBox.setSelected(true);
                        }
                        ((Activity) context).invalidateOptionsMenu();
                    }
                });
                if (hashmap.containsKey(images)) {
                    ivholder.alpha_view.setVisibility(View.VISIBLE);
                    ivholder.checkBox.setSelected(true);
                } else {
                    ivholder.alpha_view.setVisibility(View.GONE);
                    ivholder.checkBox.setSelected(false);
                }
                ivholder.alpha_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MediaChoseActivity) context).starPriview(getCHoseImages(), images);
                    }
                });
            } else {
                ivholder.checkBox.setVisibility(View.GONE);
                ivholder.alpha_view.setVisibility(View.GONE);
                ivholder.iv_image.setClickable(true);
                ivholder.iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCHoseImages().clear();
                        getCHoseImages().put(images, images);
                        ((MediaChoseActivity) context).sendImages();
                    }
                });
            }
        } else {
            CameraViewHolder holder1 = (CameraViewHolder) holder;
            holder1.camera_lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentChoseMode == PickConfig.MODE_MULTIP_PICK) {
                        if (getCHoseImages().size() < max_chose_count) {
                            ((MediaChoseActivity) context).sendStarCamera();
                        } else {
                            Toast.makeText(context, "你最多只能选择" + max_chose_count + "张照片", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (getCHoseImages().size() > 0) {
                            getCHoseImages().clear();
                        }
                        ((MediaChoseActivity) context).sendStarCamera();
                    }
                }
            });
        }
    }

    /**
     * imageScaleType:
     * EXACTLY :图像将完全按比例缩小的目标大小
     * EXACTLY_STRETCHED:图片会缩放到目标大小完全
     * IN_SAMPLE_INT:图像将被二次采样的整数倍
     * IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
     * NONE:图片不会调整
     *
     * @param url
     * @param view
     */
    public void displayImage(String url, ImageView view) {
        Glide.with(context).load(url).
                centerCrop()
                .crossFade()
                .override(sWidthPix / spancount, sWidthPix / spancount)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.loadfaild)
                .into(view);
    }

    public static int TYPE_IMAGE = 10;
    public static int TYPE_CAMERA = 11;

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isNeedCamera) {
            return TYPE_CAMERA;
        }
        return TYPE_IMAGE;
    }

    public String getItem(int postion) {
        int realPos = postion;
        if (isNeedCamera){
            realPos = postion -1;
        }
        if (realPos >= imageses.size()) return "";
        return imageses.get(realPos);
    }

    @Override
    public int getItemCount() {
        if (isNeedCamera) {
            return imageses.size() + 1;
        }
        return imageses.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        ImageButton checkBox;
        View alpha_view;

        public ImageViewHolder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(params);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            alpha_view = itemView.findViewById(R.id.alpha_view);
            checkBox = (ImageButton) itemView.findViewById(R.id.checkimages);
        }
    }

    public static class CameraViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout camera_lin;

        public CameraViewHolder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(params);
            camera_lin = (LinearLayout) itemView.findViewById(R.id.camera_lin);
        }
    }


}
