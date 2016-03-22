package com.wq.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wq.photo.adapter.FloderAdapter;
import com.wq.photo.adapter.PhotoAdapter;
import com.wq.photo.mode.ImageFloder;
import com.wq.photo.widget.PickConfig;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by wangqiong on 15/3/27.
 */

public class PhotoGalleryFragment extends Fragment implements android.os.Handler.Callback {


    public int max_chose_count = 9;
    private int spancount=3;
    public boolean isNeedfcamera = false;
    View rootview;
    RecyclerView my_recycler_view;
    TextView open_gallery;
    ImageView clear;
    PhotoAdapter adapter;
    ArrayList<String> imageses = new ArrayList<>();

    List<String> currentimageses = new ArrayList<>();
    Handler handler;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    int totalCount = 0;
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            initFloderPop();
        } else {
            adapter.notifyDataSetChanged();
        }
        return false;
    }

    ListPopupWindow popupWindow;
    FloderAdapter floderAdapter;

    private void initFloderPop() {
        if(getActivity() == null){return;}
        if(getActivity().isFinishing()){return;}
        popupWindow = new ListPopupWindow(getActivity());
        ImageFloder allimgslist = new ImageFloder();
        allimgslist.setDir("/所有图片");
        allimgslist.setCount(imageses.size());
        if(imageses.size()>0){
            allimgslist.setFirstImagePath(imageses.get(0));
        }
        mImageFloders.add(0, allimgslist);
        floderAdapter = new FloderAdapter(mImageFloders, getActivity());
        popupWindow.setAdapter(floderAdapter);
        int sWidthPix = getResources().getDisplayMetrics().widthPixels;
        popupWindow.setContentWidth(sWidthPix);
        popupWindow.setHeight(sWidthPix + 100);
        popupWindow.setAnchorView(open_gallery);
        open_gallery.setEnabled(true);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageFloder floder = (ImageFloder) parent.getAdapter().getItem(position);
                floderAdapter.setCheck(position);
                if (floder.getName().equals("/所有图片")) {
                    currentimageses.clear();
                    currentimageses.addAll(imageses);
                    adapter = new PhotoAdapter(getActivity(), currentimageses,spancount, chose_mode);
                    adapter.setmax_chose_count(max_chose_count);
                    adapter.setDir("");
                    adapter.setNeedCamera(isNeedfcamera);
                    my_recycler_view.setAdapter(adapter);
                    popupWindow.dismiss();
                    open_gallery.setText("所有图片");
                } else {
                    File mImgDir = new File(floder.getDir());
                    List<String> ims =
                            Arrays.asList(mImgDir.list(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String filename) {
                                    if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                            || filename.endsWith(".jpeg"))
                                        return true;
                                    return false;
                                }
                            }));

                    currentimageses.clear();
                    currentimageses.addAll(ims);
                    /**
                     * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
                     */
                    adapter = new PhotoAdapter(getActivity(), currentimageses,spancount, chose_mode);
                    adapter.setmax_chose_count(max_chose_count);
                    adapter.setDir(floder.getDir());
                    adapter.setNeedCamera(false);
                    my_recycler_view.setAdapter(adapter);
                    open_gallery.setText(floder.getName());
                    popupWindow.dismiss();
                }
            }
        });

    }
    int chose_mode=1;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImagePreviewFragemnt.
     */
    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        return fragment;
    }
    /**
     * 当拍照之后刷新出来拍照的那张照片
     * @param path
     */
    public void addCaptureFile(final String path) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (new File(path)!=null && new File(path).exists() && new File(path).length() >10){
                    currentimageses.add(0, path);
                    imageses.add(0, path);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this);
    }

    public void time(String msg) {
        Log.i("milles", msg + System.currentTimeMillis() + "thread" + Thread.currentThread().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootview == null) {
            rootview = inflater.inflate(R.layout.fragment_photogallery_layout, container, false);
            my_recycler_view = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);
            open_gallery = (TextView) rootview.findViewById(R.id.open_gallery);
            clear = (ImageView) rootview.findViewById(R.id.clear);
            open_gallery.setEnabled(false);
        }
        if (adapter == null) {
            adapter = new PhotoAdapter(getActivity(), currentimageses, spancount,chose_mode);
            adapter.setDir("");
            adapter.setNeedCamera(isNeedfcamera);
            adapter.setmax_chose_count(max_chose_count);
        }
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        my_recycler_view.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spancount);
        my_recycler_view.setLayoutManager(layoutManager);
        my_recycler_view.setAdapter(adapter);
        open_gallery.setText("所有图片");
        loadAllImages();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootview;
    }

    boolean isshowiing = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isshowiing) {
                    isshowiing = false;
                    popupWindow.dismiss();
                } else {
                    isshowiing = true;
                    popupWindow.show();
                }
            }
        });
    }


    public void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void loadAllImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_MODIFIED};
                Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.
                                Media.EXTERNAL_CONTENT_URI, columns,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png", "image/gif"},
                        MediaStore.Images.Media.DATE_ADDED + " DESC");
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                while (cursor.moveToNext()) {
                    String photopath = cursor.getString(dataColumnIndex);
                    if (photopath != null && new File(photopath).exists()) {
                        imageses.add(photopath);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                currentimageses.clear();
                currentimageses.addAll(imageses);
                handler.sendEmptyMessage(0);
                getImages();
            }
        }).start();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getActivity()
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png","image/jpg","image/gif"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path);
                    if (parentFile == null )
                        continue;
                    String dirPath = parentFile.getParentFile().getAbsolutePath();
                    ImageFloder imageFloder = null;
                    File file=new File(dirPath);
                    if(file!=null&&file.isDirectory()&&file.list().length>0){
                        // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                        if (mDirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            mDirPaths.add(dirPath);
                            // 初始化imageFloder
                            imageFloder = new ImageFloder();
                            imageFloder.setDir(dirPath);
                            imageFloder.setFirstImagePath(path);
                        }

                        int picSize = file.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename == null) {
                                    return false;
                                }
                                if (filename.endsWith(".jpg")
                                        || filename.endsWith(".gif")
                                        || filename.endsWith(".png")
                                        || filename.endsWith(".jpeg"))
                                    return true;
                                return false;
                            }
                        }).length;
                        totalCount += picSize;
                        imageFloder.setCount(picSize);
                        mImageFloders.add(imageFloder);
                    }
                }
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;
                // 通知Handler扫描图片完成
                handler.sendEmptyMessage(1);

            }
        }).start();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle=getArguments();
        if (bundle == null)return;
        chose_mode = bundle.getInt(PickConfig.EXTRA_PICK_MODE);
        max_chose_count = bundle.getInt(PickConfig.EXTRA_MAX_SIZE);
        spancount =bundle.getInt(PickConfig.EXTRA_SPAN_COUNT);
        isNeedfcamera= bundle.getBoolean(PickConfig.EXTRA_IS_NEED_CAMERA);
    }


}
