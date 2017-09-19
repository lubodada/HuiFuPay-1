package com.jhj.Function;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhjpay.zyb.R;

/**
 * 商城——订单适配器
 * @author lb
 *
 */
public class ShopMall_OrderAdapter extends BaseAdapter {

	private List<ShopMall> shoplist;
	private Context mContext;

	public ShopMall_OrderAdapter(Context mContext,List<ShopMall> shoplist){
		this.mContext=mContext;
		this.shoplist=shoplist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shoplist.size();
	}

	@Override
	public ShopMall getItem(int position) {
		// TODO Auto-generated method stub
		return shoplist.get(position);
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
			convertView = View.inflate(mContext, R.layout.activity_function_shopmall_orderadapter, null);
			holder = new ViewHolder();
			//寻找控件id
			holder.ll_orderlist=(LinearLayout) convertView.findViewById(R.id.ll_orderlist);
			holder.tv_order_number=(TextView) convertView.findViewById(R.id.tv_order_number);
			holder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_order_money=(TextView) convertView.findViewById(R.id.tv_order_money);
			holder.tv_order_state=(TextView) convertView.findViewById(R.id.tv_order_state);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final ShopMall shopMall = getItem(position);
		holder.tv_order_number.setText(shopMall.getOrder_number());
		holder.tv_time.setText(shopMall.getTime());
		holder.tv_order_money.setText(shopMall.getOrder_money()+"元");
		holder.tv_order_state.setText(getState(shopMall.getOrder_state()));

		holder.ll_orderlist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,ShopMall_OrderDetailActivity.class);
				intent.putExtra("order_number", shopMall.getOrder_number());//订单编号
				intent.putExtra("order_state", shopMall.getOrder_state());//订单状态
				intent.putExtra("card_type", shopMall.getCard_type());//卡片类型
				intent.putExtra("number", shopMall.getCard_number());//数量
				intent.putExtra("total_money", shopMall.getOrder_money());//金额
				intent.putExtra("name", shopMall.getName());
				intent.putExtra("location", shopMall.getLocation());
				intent.putExtra("phone", shopMall.getPhone());
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	/**
	 * 订单状态
	 * @param state
	 * @return
	 */
	public String getState(String state){
		if(state.equals("1")){
			state = "待付款";
		}
		if(state.equals("2")){
			state = "已付款";
		}
		if(state.equals("3")){
			state = "已取消";
		}
		return state;
		
	}

	static class ViewHolder {
		private LinearLayout ll_orderlist;
		private TextView tv_order_number;//
		private TextView tv_time;
		private TextView tv_order_money;
		private TextView tv_order_state;
	}


}
