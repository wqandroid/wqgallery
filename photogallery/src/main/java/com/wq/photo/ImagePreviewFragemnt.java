package com.wq.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wq.photo.mode.Images;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ImagePreviewFragemnt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagePreviewFragemnt extends Fragment {

    private ArrayList images;
    private int pos;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ImagePreviewFragemnt.
     */
    public static ImagePreviewFragemnt newInstance(ArrayList<String> param1,int pos) {
        ImagePreviewFragemnt fragment = new ImagePreviewFragemnt();
        Bundle args = new Bundle();
        args.putStringArrayList("images",param1);
        args.putInt("pos", pos);
        fragment.setArguments(args);
        return fragment;
    }

    public ImagePreviewFragemnt() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            images= getArguments().getStringArrayList("images");
            pos = getArguments().getInt("pos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_preview_fragemnt, container, false);
    }
    public void displayImage(String url, ImageView view) {
        Glide.with(getActivity()).load(url)
                .placeholder(R.drawable.loadfaild)
                .into(view);
    }
    public String delete(){
        if(views.size()<=0){
            ((MediaChoseActivity)getActivity()).popFragment();
            return null;}
        int i=pager.getCurrentItem();
        String ima= (String) images.remove(pager.getCurrentItem());
        initpage(images);
        if(i++<images.size()){
            pos=i;
        }else{
            pos=images.size()-1;
        }
        if(views.size()==0){
            ((MediaChoseActivity)getActivity()).popFragment();
        }
        return ima;
    }


    List<View>views=new ArrayList<>();
    ViewPager pager;
    MypageAdapter adapter;
    LayoutInflater inflater;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager= (ViewPager) view.findViewById(R.id.pager);
        inflater=getLayoutInflater(savedInstanceState);
        initpage(images);
    }
    private void initpage(List<Images> list) {
        if(list==null){return;}
        views.clear();
        for(int i=0;i<list.size();i++){
            View itemview=inflater.inflate(R.layout.pageitem_view, null);
            ImageView iv_image= (ImageView) itemview.findViewById(R.id.iv_image);
            displayImage(images.get(i).toString(),iv_image);
            views.add(itemview);
        }
        pager.removeAllViews();
        adapter=new MypageAdapter(views);
        pager.setAdapter(adapter);
        if(pos>0 && pos<list.size()){
            pager.setCurrentItem(pos,false);
        }
    }


    public class MypageAdapter extends PagerAdapter {
        List<View> views;
        public MypageAdapter(List<View> views){
            this.views=views;
        }
        @Override
        public int getCount() {
            return views.size();
        }

        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
        @Override
        public int getItemPosition(Object object)   {
            if ( mChildCount > 0) {
                mChildCount --;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(views.size()>position){
                container.removeView(views.get(position));
            }
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }



}
