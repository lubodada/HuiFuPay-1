package com.bestpay.cn.utils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.jhjpay.zyb.R;
import com.timepay.zyb.Model;

public class Pay_SumMoney_adapter extends CursorAdapter {

	private Context mContext;
	private Cursor mCursor;
	private Model mModel;
	private LayoutInflater mInflater;
	
	public Pay_SumMoney_adapter(Context context, Cursor c, Model model) {
		super(context, c);
		System.out.println("c = " + c);
		this.mContext = context;
		this.mCursor = c;
		this.mModel = model;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return  mInflater.inflate(R.layout.paysum_listview_activity, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = null;
		
		Object tag = view.getTag();
		if(tag instanceof ViewHolder) {
			holder = (ViewHolder) view.getTag();
		}
		if(holder == null) {
			holder = new ViewHolder();
			view.setTag(holder);
			//寻找控件ID
			holder.paytype = (TextView)view.findViewById(R.id.paytype);
			holder.amount = (TextView)view.findViewById(R.id.amount);
			holder.time = (TextView)view.findViewById(R.id.time);
		}
		//支付方式
		String type=cursor.getString(cursor.getColumnIndexOrThrow("paytype"));
		String paytype=getPayType(type);
		//将从数据库中查询到的title设为ListView的Item项。
		holder.paytype.setText(paytype);
		holder.amount.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("amount"))));
		holder.time.setText(cursor.getString(cursor.getColumnIndexOrThrow("time")));
		
	}
	//支付方式
	public String getPayType(String paytype){
		
		if(paytype.equals("1")){
			paytype="支付宝";
		}
		if(paytype.equals("4")){
			paytype="微信";
		}
		if(paytype.equals("13")){
			paytype="翼支付";
		}
		return paytype;
		
	}
	
	static class ViewHolder {
		TextView paytype;//支付类型
		TextView amount;//统计金额
		TextView time;//统计时间
	}
}



