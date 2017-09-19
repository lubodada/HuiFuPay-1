package com.jhj.Function;

/**
 * 交易记录实体类
 * @author lb
 *
 */
public class TransactionRecord {

	private double totalFee;//交易金额
	private String payCode;//订单支付码
	private String orderId;//订单编号
	private int orderState;//订单状态
	private int orderType;//订单类型
	private String type;//订单类型
	private String payTime;//交易时间
	private String buyerLoginId;//第三方登录平台购买者id
	
	public TransactionRecord(){
		
	}
	
	public TransactionRecord(double totalFee, String orderId,
			int orderState, int orderType, String type, 
			String payTime, String buyerLoginId) {
		// TODO Auto-generated constructor stub
		this.totalFee = totalFee;
		this.orderId = orderId;
		this.orderState = orderState;
		this.orderType = orderType;
		this.type = type;
		this.payTime = payTime;
		this.buyerLoginId = buyerLoginId;
	}
	public double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getOrderState() {
		return orderState;
	}
	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}
	
	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getBuyerLoginId() {
		return buyerLoginId;
	}
	public void setBuyerLoginId(String buyerLoginId) {
		this.buyerLoginId = buyerLoginId;
	}
	
	
}
