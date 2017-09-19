package com.jhj.Function;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jhjpay.zyb.R;
import com.timepay.zyb.Model;
/**
 * 交易记录适配器
 * @author Administrator
 *
 */
public class TransRecordAdapter extends CursorAdapter {

	Context mContext;
	Cursor mCursor;
	Model mModel;
	LayoutInflater mInflater;

	@SuppressWarnings("deprecation")
	public TransRecordAdapter(Context context, Cursor c, Model model) {
		super(context, c);
		System.out.println("c = " + c);
		this.mContext = context;
		this.mCursor = c;
		this.mModel = model;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return  mInflater.inflate(R.layout.activity_function_transrecord_adapter, parent, false);
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
			holder.totalFee=(TextView) view.findViewById(R.id.totalFee);
			holder.orderId=(TextView) view.findViewById(R.id.orderId);
			holder.orderState = (TextView)view.findViewById(R.id.orderState);
			holder.orderType = (TextView)view.findViewById(R.id.orderType);
			holder.payTime = (TextView)view.findViewById(R.id.payTime);
		}
		
		//订单类型
		int type=cursor.getInt(cursor.getColumnIndexOrThrow("orderType"));
		String orderType=getPayType(type);

		//订单状态
		int statu=cursor.getInt(cursor.getColumnIndexOrThrow("orderState"));
		String orderState=getStatus(statu);

		//赋值
		holder.totalFee.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("totalFee"))));
		holder.orderId.setText(cursor.getString(cursor.getColumnIndexOrThrow("orderId")));
		holder.orderState.setText(orderState);
		holder.orderType.setText(cursor.getString(cursor.getColumnIndexOrThrow("type")));
		holder.payTime.setText(cursor.getString(cursor.getColumnIndexOrThrow("payTime")));
	}
	//订单状态
	public String getStatus(int status){
		String orderState = null;
		if(status==1){
			orderState="待付款";
		}
		if(status==4){
			orderState="已取消";
		}
		if(status==7){
			orderState="已完成";
		}
		if(status==10){
			orderState="已退款";
		}
		if(status==13){
			orderState="支付中";
		}

		return orderState;

	}
	//订单类型
	public String getPayType(int type){
		String orderType = null;
		if(type==0){
			orderType="订单总计";
		}
		if(type==1){
			orderType="支付宝";
		}
		if(type==4){
			orderType="微信支付";
		}
		if(type==10){
			orderType="易付宝";
		}
		if(type==13){
			orderType="翼支付";
		}
		if(type==16){
			orderType="京东钱包";
		}
		if(type==25){
			orderType="QQ钱包";
		}
		if(type==91){
			orderType="电信积分兑换";
		}

		return orderType;

	}

	static class ViewHolder {
		TextView totalFee;//金额
		TextView orderId;//订单编号
		TextView orderState;//订单状态
		TextView orderType;//订单类型
		TextView payTime;//交易时间
	}

}
