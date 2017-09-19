package com.bestpay.cn.utils;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter_QR extends PagerAdapter {

	private List<View> mViews; // View集合
	 
	 public ViewPagerAdapter_QR(Context context, List<View> Views){
	 this. mViews =Views;
	 }
	 
	 @Override
	 public int getCount() {
	  return mViews.size();
	 }
	 
	 @Override
	 public boolean isViewFromObject(View view, Object object) {
	  return view == object;
	 }
	 
	 @Override
	 public Object instantiateItem(ViewGroup container, int position) {
	  View mImageView = mViews.get(position % mViews.size());
	  container.addView(mImageView);
	  return mImageView;
	 }
	 
	 @Override
	 public void destroyItem(ViewGroup container, int position, Object object) {
	  container.removeView((View)object);
	 }

}
