package com.NetWork.util;

public class Url {
	// 交易
	public static String transaction = "https://webpaywg.bestpay.com.cn/barcode/placeOrder";
	// 查询
	public static String query = "https://webpaywg.bestpay.com.cn/query/queryOrder";
	// 退款
	public static String refund = "https://webpaywg.bestpay.com.cn/refund/commonRefund";
	// 撤销
	public static String revoke = "https://webpaywg.bestpay.com.cn/reverse/reverse";
	/**
	 * 掌优宝
	 * */
	// 交易
	public static String transaction_zyb = "http://api.zhangyoo.cn/order/create";
	// 查询
	public static String query_zyb = "http://api.zhangyoo.cn/order/query";
	// 订单取消
	public static String revoke_zyb = "http://api.zhangyoo.cn/order/cancel";
	// 退款
	public static String refund_zyb = "http://api.zhangyoo.cn/order/refund";
	// 二维码 下单
	public static String QR_code_pay = "http://api.zhangyoo.cn/order/preCreate";
	// 二维码 查询
	public static String QR_code_query = "http://api.zhangyoo.cn/order/query";

}
