package com.jhj.WalletActivity;

/**
 * 结算银行卡实体类
 * @author Administrator
 *
 */
public class BankCard {

	private String bankcard;
	private String bankcard_number;
	private String remarks;
	
	public BankCard(){
		
	}
	public BankCard(String bankcard, String bankcard_number, String remarks){
		this.bankcard=bankcard;
		this.bankcard_number=bankcard_number;
		this.remarks=remarks;
		
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}
	public String getBankcard_number() {
		return bankcard_number;
	}
	public void setBankcard_number(String bankcard_number) {
		this.bankcard_number = bankcard_number;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
