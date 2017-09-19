package com.jhj.Function;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 交易记录统计实体类
 * @author lb
 *
 */
public class TransactionRecord_Count implements Parcelable{

	
	private String totalMoney;//总金额
	private String orderCount;//交易笔数
	private String orderType;//类型
	
	public TransactionRecord_Count(){
		
	}
	public TransactionRecord_Count(String totalMoney, String orderCount,
			String orderType) {
		// TODO Auto-generated constructor stub
		this.totalMoney = totalMoney;
		this.orderCount = orderCount;
		this.orderType = orderType;
		
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
//		dest.writeString(totalMoney);
//		dest.writeString(orderCount);
//		dest.writeString(orderType);
	}
	public TransactionRecord_Count(Parcel source){
//		totalMoney = source.readString();
//		orderCount = source.readString();
//		orderType = source.readString();
	}
	 
	
}
