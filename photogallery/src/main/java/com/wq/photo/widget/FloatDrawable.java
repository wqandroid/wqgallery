package com.wq.photo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.wq.photo.R;


/**
 * 头像图片选择框的浮层
 * @author Administrator
 *
 */
public class FloatDrawable extends Drawable {
	
	private Context mContext;
	private Drawable mCropPointDrawable;
	private Paint mLinePaint=new Paint();
	{
	    mLinePaint.setARGB(200, 50, 50, 50);
	    mLinePaint.setStrokeWidth(1F);
	    mLinePaint.setStyle(Paint.Style.STROKE);
	    mLinePaint.setAntiAlias(true);
	    mLinePaint.setColor(Color.WHITE);
	}
	
	public FloatDrawable(Context context) {
		super();
		this.mContext=context;
		init();
	}
	
	private void init()
	{
		mCropPointDrawable=mContext.getResources().getDrawable(R.drawable.point);
	}
	
	public int getCirleWidth()
	{
		return mCropPointDrawable.getIntrinsicWidth();
	}
	
	public int getCirleHeight()
	{
		return mCropPointDrawable.getIntrinsicHeight();
	}

	@Override
	public void draw(Canvas canvas) {
		
		int left=getBounds().left;
		int top=getBounds().top;
		int right=getBounds().right;
		int bottom=getBounds().bottom;
		
		Rect mRect=new Rect(
				left+mCropPointDrawable.getIntrinsicWidth()/2, 
				top+mCropPointDrawable.getIntrinsicHeight()/2, 
				right-mCropPointDrawable.getIntrinsicWidth()/2, 
				bottom-mCropPointDrawable.getIntrinsicHeight()/2);
		//方框
		canvas.drawRect(mRect, mLinePaint);
		
		//左上
		mCropPointDrawable.setBounds(left, top, left+mCropPointDrawable.getIntrinsicWidth(), top+mCropPointDrawable.getIntrinsicHeight());
		mCropPointDrawable.draw(canvas);
		
		//右上
		mCropPointDrawable.setBounds(right-mCropPointDrawable.getIntrinsicWidth(), top, right, top+mCropPointDrawable.getIntrinsicHeight());
		mCropPointDrawable.draw(canvas);
		
		//左下
		mCropPointDrawable.setBounds(left, bottom-mCropPointDrawable.getIntrinsicHeight(), left+mCropPointDrawable.getIntrinsicWidth(), bottom);
		mCropPointDrawable.draw(canvas);
		
		//右下
		mCropPointDrawable.setBounds(right-mCropPointDrawable.getIntrinsicWidth(), bottom-mCropPointDrawable.getIntrinsicHeight(), right, bottom);
		mCropPointDrawable.draw(canvas);
		
	}

	@Override
	public void setBounds(Rect bounds) {
		super.setBounds(new Rect(
				bounds.left-mCropPointDrawable.getIntrinsicWidth()/2, 
				bounds.top-mCropPointDrawable.getIntrinsicHeight()/2, 
				bounds.right+mCropPointDrawable.getIntrinsicWidth()/2, 
				bounds.bottom+mCropPointDrawable.getIntrinsicHeight()/2));
	}

	@Override
	public void setAlpha(int alpha) {}

	@Override
	public void setColorFilter(ColorFilter cf) {}

	@Override
	public int getOpacity() {
		return 0;
	}

}
