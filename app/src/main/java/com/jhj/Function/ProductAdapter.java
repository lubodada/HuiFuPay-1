package com.jhj.Function;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhj.info_util.GetImageByUrl;
import com.jhjpay.zyb.R;

/**
 * 产品适配器
 * @author Administrator
 *
 */
public class ProductAdapter extends BaseAdapter {

	private List<Product> productlist;
	private Context mContext;

	public ProductAdapter(Context mContext,List<Product> productlist){
		this.mContext=mContext;
		this.productlist=productlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productlist.size();
	}

	@Override
	public Product getItem(int position) {
		// TODO Auto-generated method stub
		return productlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView==null) {
			convertView = View.inflate(mContext, R.layout.activity_function_product_adapter, null);
			holder = new ViewHolder();
			//寻找控件id
			holder.img_product_img=(ImageView) convertView.findViewById(R.id.img_product_img);
			holder.img_product_state=(ImageView) convertView.findViewById(R.id.img_product_state);
			holder.tv_product_name=(TextView) convertView.findViewById(R.id.tv_product_name);
			holder.tv_product_synopsis=(TextView) convertView.findViewById(R.id.tv_product_synopsis);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//赋值
		Product product = getItem(position);

		String url =product.getProduct_imgUrl();
		// 将图片直接显示到控件上
		GetImageByUrl getImageByUrl = new GetImageByUrl();
		getImageByUrl.setImage(holder.img_product_img, url);
		//产品名称  简介
		holder.tv_product_name.setText(product.getProduct_name());
		holder.tv_product_synopsis.setText(product.getProduct_synopsis());
		//状态
		boolean isopened = product.getProduct_state();
		if(isopened){
			holder.img_product_state.setBackgroundResource(R.drawable.product_yes_opened);
		}else{
			holder.img_product_state.setBackgroundResource(R.drawable.product_not_opened);
		}

		return convertView;
	}


	static class ViewHolder {
		private ImageView img_product_img;//产品图片
		private ImageView img_product_state;//状态
		private TextView tv_product_name;//产品名称
		private TextView tv_product_synopsis;//产品简介
	}

}
