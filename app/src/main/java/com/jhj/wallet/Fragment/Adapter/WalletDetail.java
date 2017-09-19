package com.jhj.wallet.Fragment.Adapter;

import java.io.Serializable;

/**
 * 钱包明细实体类
 * @author lb
 *
 */
@SuppressWarnings("serial")
public class WalletDetail implements Serializable{

	private String transaction_type;//交易类型
	private String money_flow_type;//金额流动类型
	private String money;//金额
	private String time;//时间
	private String transaction_number;//交易单号
	private String remarks;
	
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getMoney_flow_type() {
		return money_flow_type;
	}
	public void setMoney_flow_type(String money_flow_type) {
		this.money_flow_type = money_flow_type;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTransaction_number() {
		return transaction_number;
	}
	public void setTransaction_number(String transaction_number) {
		this.transaction_number = transaction_number;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
}
