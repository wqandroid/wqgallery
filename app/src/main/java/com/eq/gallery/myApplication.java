package com.eq.gallery;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by wangqiong on 15/3/27.
 */
public class myApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
    }


    /**
     * 配置imageload 加载库
     * @param context
     */
    public static void initImageLoader(Context context) {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        if(memoryCacheSize > 8*1024*1024 || memoryCacheSize <= 0){
            memoryCacheSize = 8*1024*1024;
        }

        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.loadfaild)
                .showImageForEmptyUri(R.drawable.loadfaild)
                .showImageOnFail(R.drawable.loadfaild)
                .cacheOnDisc(true)
                .cacheInMemory(false)
                .build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY)
                .memoryCacheSize(memoryCacheSize)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultDisplayImageOptions).build();
        ImageLoader.getInstance().init(configuration);

    }

}
