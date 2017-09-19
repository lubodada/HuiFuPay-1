package com.timepay.zyb;

/**
 * 交易信息记录实体类
 * @author lb
 */
public class TradingPay {
	
	private String myorderid;//订单id(自己生成)
	private String orderid;//订单id(获取“掌游宝”的)
	private int paytype;//支付方式【 扫一扫{1:支付宝，2:微信，3:翼支付}】【二维码{4:支付宝，5:微信，6:翼支付}】【7:刷卡】【8:现金】【9:积分+】
	private String money;//金额
	private int status;//支付状态 1:待付款 4：已取消 7：已完成 10:已退款 13:支付中
	private String mnumber;//客户付款码
	private String remark;//备注
	private String time;//时间 2017-02-28 18:00:39
	private String items;//消费商品详情
	private double discount;//优惠金额
	private String couponid;//优惠券id
	private int uploadstatus;//上传状态
	
	public String getMyorderid() {
		return myorderid;
	}
	public void setMyorderid(String myorderid) {
		this.myorderid = myorderid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getPaytype() {
		return paytype;
	}
	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMnumber() {
		return mnumber;
	}
	public void setMnumber(String mnumber) {
		this.mnumber = mnumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getCouponid() {
		return couponid;
	}
	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}
	public int getUploadstatus() {
		return uploadstatus;
	}
	public void setUploadstatus(int uploadstatus) {
		this.uploadstatus = uploadstatus;
	}
	
	
}
