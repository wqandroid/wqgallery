package com.wq.photo.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.wq.photo.MediaChoseActivity;
import com.yalantis.ucrop.UCrop;

/**
 *
 */
public class PickConfig {

    public static int DEFAULT_SPANCOUNT = 3;
    public static int DEFAULT_PICKSIZE = 1;

    public static int MODE_SINGLE_PICK = 1;
    public static int MODE_MULTIP_PICK = 2;


    public final static int PICK_REQUEST_CODE = 10607;


    public final static String EXTRA_PICK_BUNDLE = "extra_pick_bundle";
    public final static String EXTRA_SPAN_COUNT = "extra_span_count";
    public final static String EXTRA_PICK_MODE = "extra_pick_mode";
    public final static String EXTRA_MAX_SIZE = "extra_max_size";
    public final static String EXTRA_UCROP_OPTIONS = "extra_ucrop_options";
    public final static String EXTRA_IS_NEED_CAMERA = "extra_isneed_camera";
    public final static String EXTRA_IS_NEED_CROP = "extra_isneed_crop";
    public final static String EXTRA_IS_SQUARE_CROP = "extra_issquare_crop";
    public final static String EXTRA_ACTION_BAR_COLOR = "extra_actionbar_color";
    public final static String EXTRA_STATUS_BAR_COLOR = "extra_status_bar_color";


    private int spanCount;
    private int pickMode;
    private int maxPickSize;
    private boolean isneedcrop;
    private boolean isneedcamera;
    private int actionBarcolor;
    private int statusBarcolor;
    private UCrop.Options options;
    private boolean isSqureCrop;

    private PickConfig(Activity context, PickConfig.Builder builder) {
        this.spanCount = builder.spanCount;
        this.pickMode = builder.pickMode;
        this.maxPickSize = builder.maxPickSize;
        this.isneedcrop = builder.isneed_crop;
        this.isneedcamera = builder.isneed_camera;
        this.statusBarcolor = builder.statusBarcolor;
        this.actionBarcolor = builder.actionBarcolor;
        this.options=builder.options;
        this.isSqureCrop=builder.isSqureCrop;


        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATUS_BAR_COLOR, this.statusBarcolor);
        bundle.putInt(EXTRA_ACTION_BAR_COLOR, this.actionBarcolor);
        bundle.putInt(EXTRA_SPAN_COUNT, this.spanCount);
        bundle.putParcelable(EXTRA_UCROP_OPTIONS,this.options);
        bundle.putInt(EXTRA_PICK_MODE, this.pickMode);
        bundle.putInt(EXTRA_MAX_SIZE, this.maxPickSize);
        bundle.putBoolean(EXTRA_IS_NEED_CAMERA, this.isneedcamera);
        bundle.putBoolean(EXTRA_IS_NEED_CROP, this.isneedcrop);
        bundle.putBoolean(EXTRA_IS_SQUARE_CROP,this.isSqureCrop);

        if (this.pickMode == MODE_MULTIP_PICK) {
            this.isneedcrop = false;
        } else {
            this.maxPickSize = 1;
        }
        startPick(context, bundle);
    }

    private void startPick(Activity context, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PICK_BUNDLE, bundle);
        intent.setClass(context, MediaChoseActivity.class);
        context.startActivityForResult(intent, PICK_REQUEST_CODE);
    }


    public static class Builder {

        private Activity context;
        private int spanCount = DEFAULT_SPANCOUNT;
        private int pickMode = MODE_SINGLE_PICK;
        private int maxPickSize = DEFAULT_PICKSIZE;
        private boolean isneed_crop = false;
        private boolean isneed_camera = true;
        private boolean isSqureCrop=false;
        private int actionBarcolor = Color.parseColor("#03A9F4");
        private int statusBarcolor = Color.parseColor("#0288D1");

        private UCrop.Options options=null;





        public Builder(Activity context) {
            if (context == null) {
                throw new IllegalArgumentException("A non-null Context must be provided");
            }
            this.context = context;
        }

        public PickConfig.Builder spanCount(int spanCount) {
            this.spanCount = spanCount;
            if (this.spanCount == 0) {
                this.spanCount = DEFAULT_SPANCOUNT;
            }
            return this;
        }


        /**
         * this methord must be last call
         *
         * @param pickMode
         * @return
         */
        public PickConfig.Builder pickMode(int pickMode) {
            this.pickMode = pickMode;
            return this;
        }

        public PickConfig.Builder maxPickSize(int maxPickSize) {
            this.maxPickSize = maxPickSize;
            if (this.maxPickSize == 0) {
                this.maxPickSize = DEFAULT_PICKSIZE;
            }
            return this;
        }

        public PickConfig.Builder statusBarcolor(int statusBarcolor) {
            this.statusBarcolor = statusBarcolor;
            return this;
        }

        public PickConfig.Builder setUropOptions(UCrop.Options options) {
            this.options = options;
            return this;
        }


        public PickConfig.Builder actionBarcolor(int actionBarcolor) {
            this.actionBarcolor = actionBarcolor;
            return this;
        }

        public PickConfig.Builder isneedcrop(boolean isneed_crop) {
            this.isneed_crop = isneed_crop;
            return this;
        }

        public PickConfig.Builder isSqureCrop(boolean isSqureCrop) {
            this.isSqureCrop = isSqureCrop;
            return this;
        }

        public PickConfig.Builder isneedcamera(boolean isneed_camera) {
            this.isneed_camera = isneed_camera;
            return this;
        }


        public PickConfig build() {
            return new PickConfig(context, this);
        }
    }




}
