package com.jhj.Function;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhjpay.zyb.R;

/**
 * 手机充值历史记录适配器
 * @author Administrator
 *
 */
public class PhoneRechargeApapter extends BaseAdapter {

	private List<PhoneRecharge> phList;
	private Context mContext;

	public PhoneRechargeApapter(Context mContext,List<PhoneRecharge> phList){
		this.mContext=mContext;
		this.phList=phList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return phList.size();
	}

	@Override
	public PhoneRecharge getItem(int position) {
		// TODO Auto-generated method stub
		return phList.get(position);
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
			convertView = View.inflate(mContext, R.layout.activity_function_phone_recharge_adapter, null);
			holder = new ViewHolder();
			//寻找控件id
			holder.re_rechargedetail=(RelativeLayout) convertView.findViewById(R.id.re_rechargedetail);
			holder.tv_phone_number=(TextView) convertView.findViewById(R.id.tv_phone_number);
			holder.tv_recharge_money=(TextView) convertView.findViewById(R.id.tv_recharge_money);
			holder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final PhoneRecharge phRecharge = getItem(position);
		holder.tv_phone_number.setText(phRecharge.getPhone_number());
		holder.tv_recharge_money.setText("-"+phRecharge.getRecharge_money());
		holder.tv_time.setText(phRecharge.getTime());

		holder.re_rechargedetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,PhoneRechargeDetailsActivity.class);
				intent.putExtra("phone_number", phRecharge.getPhone_number());
				intent.putExtra("recharge_money", phRecharge.getRecharge_money());
				intent.putExtra("time", phRecharge.getTime());
				intent.putExtra("trand_number", phRecharge.getTrading_number());
				intent.putExtra("remarks", phRecharge.getRemarks());
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		private RelativeLayout re_rechargedetail;
		private TextView tv_phone_number;//
		private TextView tv_recharge_money;
		private TextView tv_time;
	}

}
