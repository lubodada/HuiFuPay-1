package com.jhj.Agreement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.NetWork.util.Url;
import com.bestpay.cn.utils.CryptTool;
import com.jhj.network.Http_PushTask;

public class Http_Pay {
	static String mac;

	public static void pay(String barcode, String monery, Context context,
			String fdm) {

		String merchantId = "02370109020131195";
		String subMerchantId = "02370109020132210";
		String orderNo = CryptTool.getCurrentDate() + "abcd";
		String orderReqNo = CryptTool.getCurrentDate() + "00001";// 年月日时分秒+收银员用户名+四位随机数
		String orderDate = CryptTool.getCurrentDate();
		// 条形码
		String orderAmt = monery;
		String keyString = "1B48AB128017686788E1089CD16501D27F077C292A565305";

		StringBuilder sb = new StringBuilder();// 组装mac加密明文串
		sb.append("MERCHANTID=").append(merchantId);
		sb.append("&ORDERNO=").append(orderNo);
		sb.append("&ORDERREQNO=").append(orderReqNo);
		sb.append("&ORDERDATE=").append(orderDate);
		sb.append("&BARCODE=").append(barcode);
		sb.append("&ORDERAMT=").append(orderAmt);
		sb.append("&KEY=").append(keyString);// 此处是商户的key

		try {
			mac = CryptTool.md5Digest(sb.toString());
		} catch (Exception e) {

			e.printStackTrace();
		}// 进行md5加密(商户自己封装MD5加密工具类，此处只提供参考)

		Map<String, String> param = new HashMap<String, String>();// 组装请求参数

		param.put("merchantId", merchantId);
		param.put("subMerchantId", subMerchantId);
		param.put("barcode", barcode);
		param.put("orderNo", orderNo);
		param.put("orderReqNo", orderReqNo);
		param.put("orderDate", orderDate);
		param.put("channel", "05");
		param.put("busiType", "0000001");
		param.put("TransType", "B");
		param.put("orderAmt", orderAmt);
		param.put("productAmt", monery);
		param.put("attachAmt", "0");
		param.put("goodsName", "条码支付");
		param.put("storeId", "201231");
		param.put("backUrl", "http://121.41.113.42:8080/PaymentCallback");
		param.put("ledgerDetail", "02370109020132210:1|02370109020131195:1");
		param.put("attach", fdm);
		param.put("mac", mac);
		Http_PushTask HP = new Http_PushTask();
		try {
			String rerurn = HP.execute(CryptTool.transMapToString(param),
					Url.transaction).get();

			if (!TextUtils.isEmpty(rerurn)) {
				Toast.makeText(context, "交易成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "交易失败", Toast.LENGTH_SHORT).show();
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
