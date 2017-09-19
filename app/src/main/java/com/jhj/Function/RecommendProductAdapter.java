package com.jhj.Function;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jhjpay.zyb.R;

/**
 * 推荐产品适配器
 * @author lb
 */
public class RecommendProductAdapter extends BaseAdapter {
	
	private List<Recommend> relist;
	// 用来控制CheckBox的选中状况
	private static HashMap<String, Boolean> isSelected;
	// 上下文
	private Context mcontext;
	// 用来导入布局
	private LayoutInflater inflater = null;

	// 构造器
	@SuppressLint("UseSparseArrays")
	public RecommendProductAdapter(List<Recommend> relist, Context context) {
		this.mcontext = context;
		this.relist = relist;
		inflater = LayoutInflater.from(mcontext);
		isSelected = new HashMap<String, Boolean>();
		// 初始化数据
		initDate();
	}

	// 初始化isSelected的数据
	private void initDate() {
		for (int i = 0; i < relist.size(); i++) {
			getIsSelected().put(relist.get(i).getProductId(), false);
		}
	}

	@Override
	public int getCount() {
		return relist.size();
	}

	@Override
	public Recommend getItem(int position) {
		return relist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = inflater.inflate(R.layout.activity_function_recommend_adapters, null);
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		Recommend recommend = getItem(position);
		// 设置list中TextView的显示
		holder.tv.setText(recommend.getProductName());
		// 根据isSelected来设置checkbox的选中状况
		holder.cb.setChecked(getIsSelected().get(recommend.getProductId()));
		return convertView;
	}

	public static HashMap<String, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<String, Boolean> isSelected) {
		RecommendProductAdapter.isSelected = isSelected;
	}

	public static class ViewHolder {
		TextView tv;
		CheckBox cb;
	}
}