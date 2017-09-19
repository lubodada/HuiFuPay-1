package com.timepay.zyb;

/**
 * 支付金额统计实体类	
 * @author 
 */
public class PaySum {

	//支付方式  【 扫一扫{1:支付宝，2:微信，3:翼支付}】
	//【二维码{4:支付宝，5:微信，6:翼支付}】【7:刷卡】【8:现金】【9:积分+】
	private int paytype;
	//总金额
	private double amount;
	//日期
	private String time;
	
	
	public int getPaytype() {
		return paytype;
	}
	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
