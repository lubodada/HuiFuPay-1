package com.jhj.Function;

/**
 * 手机充值实体类
 * @author Administrator
 *
 */
public class PhoneRecharge {

	private String phone_number;//手机号
	private String recharge_money;//充值金额
	private String trading_state;//交易状态
	private String time;//交易时间
	private String trading_number;//交易单号
	private String remarks;//备注
	
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getRecharge_money() {
		return recharge_money;
	}
	public void setRecharge_money(String recharge_money) {
		this.recharge_money = recharge_money;
	}
	public String getTrading_state() {
		return trading_state;
	}
	public void setTrading_state(String trading_state) {
		this.trading_state = trading_state;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTrading_number() {
		return trading_number;
	}
	public void setTrading_number(String trading_number) {
		this.trading_number = trading_number;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
