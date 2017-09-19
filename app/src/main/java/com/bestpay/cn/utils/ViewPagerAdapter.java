package com.bestpay.cn.utils;

import java.util.List;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {

	private List<View> mViews; // View集合
	 
	 public ViewPagerAdapter(Context context, List<View> Views){
	 this. mViews =Views;
//	 initViews(mViews);
	 }
	 
	 /**
	  * 初始化ImageViews集合
	  * @param imageIds
	  */
	 private void initViews(List<View> ViewIds) {
	 
	  // ImageViews集合中的图片个数在[2,3]时会存在问题，递归再次填充一遍
	  if(ViewIds.size() > 1 && ViewIds.size() < 4){
		  initViews(ViewIds);
	  }
	 }
	 
	 @Override
	 public int getCount() {
	  return mViews.size();
//	  return mViews.size() <=1 ? mViews.size() : Short.MAX_VALUE;
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
