package com.bestpay.cn.utils;

import com.jhjpay.zyb.R;
import com.timepay.zyb.Model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class Pay_Time_adapter extends CursorAdapter{

	private Context mContext;
	private Cursor mCursor;
	private Model mModel;
	private LayoutInflater mInflater;
	
	public Pay_Time_adapter(Context context, Cursor c, Model model) {
		super(context, c);
		System.out.println("c = " + c);
		this.mContext = context;
		this.mCursor = c;
		this.mModel = model;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return  mInflater.inflate(R.layout.paylist_activity, parent, false);
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
			//寻找控件id
			holder.myorderid=(TextView) view.findViewById(R.id.myorderid);
			holder.orderid=(TextView) view.findViewById(R.id.orderid);
			holder.paytype = (TextView)view.findViewById(R.id.paytype);
			holder.money = (TextView)view.findViewById(R.id.money);
			holder.status = (TextView)view.findViewById(R.id.status);
			holder.mnumber = (TextView)view.findViewById(R.id.mnumber);
			holder.remark = (TextView)view.findViewById(R.id.remark);
			holder.time = (TextView)view.findViewById(R.id.time);
		}
		//支付方式
		String type=cursor.getString(cursor.getColumnIndexOrThrow("paytype"));
		String paytype=getPayType(type);
		//支付状态
		String statu=cursor.getString(cursor.getColumnIndexOrThrow("status"));
		String status=getStatus(statu);
		//将从数据库中查询到的title设为ListView的Item项。
		holder.myorderid.setText(cursor.getString(cursor.getColumnIndexOrThrow("myorderid")));
		holder.orderid.setText(cursor.getString(cursor.getColumnIndexOrThrow("orderid")));
		holder.paytype.setText(paytype);
		holder.money.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("money"))));
		holder.status.setText(status);
		holder.mnumber.setText(cursor.getString(cursor.getColumnIndexOrThrow("mnumber")));
		holder.remark.setText(cursor.getString(cursor.getColumnIndexOrThrow("remark")));
		holder.time.setText(cursor.getString(cursor.getColumnIndexOrThrow("time")));
		
	}
	//支付状态
	public String getStatus(String status){
		if(status.equals("1")){
			status="待付款";
		}
		if(status.equals("4")){
			status="已取消";
		}
		if(status.equals("7")){
			status="已完成";
		}
		if(status.equals("10")){
			status="已退款";
		}
		if(status.equals("13")){
			status="支付中";
		}
		
		return status;
		
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
//		if(paytype.equals("4")){
//			paytype="支付宝(二维码)";
//		}
//		if(paytype.equals("5")){
//			paytype="微信(二维码)";
//		}
//		if(paytype.equals("6")){
//			paytype="翼支付(二维码)";
//		}
//		if(paytype.equals("7")){
//			paytype="刷卡";
//		}
//		if(paytype.equals("8")){
//			paytype="现金";
//		}
//		if(paytype.equals("9")){
//			paytype="积分+";
//		}
		
		return paytype;
		
	}
	
	static class ViewHolder {
		TextView myorderid;//订单ID（自己）
		TextView orderid;//订单id(掌游宝)
		TextView paytype;//支付类型
		TextView money;//交易金额
		TextView status;//支付状态
		TextView mnumber;//客户付款码
		TextView remark;//描述
		TextView time;//时间
	}
}
