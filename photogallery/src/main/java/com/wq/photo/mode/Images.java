package com.wq.photo.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangqiong on 15/3/27.
 */
public class Images implements Parcelable {
    public  String photopath;
    public  String imageid;//图片id
    public  boolean ishaveorigin;//是否有缩略图
    public  String photoThumbpath;
    public boolean isChecked=false;





    @Override
    public int hashCode() {
        return super.hashCode();
    }
    @Override
    public String toString() {
        return "photopath:"+photopath+"photoThumbpath:"+photoThumbpath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photopath);
        dest.writeString(this.imageid);
        dest.writeByte(ishaveorigin ? (byte) 1 : (byte) 0);
        dest.writeString(this.photoThumbpath);
        dest.writeByte(isChecked ? (byte) 1 : (byte) 0);
    }

    public Images() {
    }

    private Images(Parcel in) {
        this.photopath = in.readString();
        this.imageid = in.readString();
        this.ishaveorigin = in.readByte() != 0;
        this.photoThumbpath = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Creator<Images> CREATOR = new Creator<Images>() {
        public Images createFromParcel(Parcel source) {
            return new Images(source);
        }

        public Images[] newArray(int size) {
            return new Images[size];
        }
    };
}
