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

public class Http_Refund {
	String mac;

	/**
	 * 退款
	 * */
	public void Refund(Context context) {
		String merchantId = "02370109020131195";
		String merchantPwd = "663913";
		String oldOrderNo = "20160902144440abcd";
		String oldOrderReqNo = "2016090214444000001";
		String refundReqNo = "20150608113649";
		String refundReqDate = "20160824";
		String transAmt = "2";
		String channel = "05";
		String ledgerDetail = "02370109020132210:1|02370109020131195:1";
		String bgUrl = "http://121.41.113.42:8080/PaymentCallback";
		StringBuilder sb = new StringBuilder();// 组装mac加密明文串
		// sb.append("BGURL").append(bgUrl);
		sb.append("MERCHANTID=").append(merchantId);
		sb.append("&MERCHANTPWD=").append(merchantPwd);
		sb.append("&OLDORDERNO=").append(oldOrderNo);
		sb.append("&OLDORDERREQNO=").append(oldOrderReqNo);
		sb.append("&REFUNDREQNO=").append(refundReqNo);
		sb.append("&REFUNDREQDATE=").append(refundReqDate);
		sb.append("&TRANSAMT=").append(transAmt);
		sb.append("&LEDGERDETAIL=").append(ledgerDetail);
		sb.append("&KEY=").append(
				"1B48AB128017686788E1089CD16501D27F077C292A565305");// 此处是商户的key
		String mac = null;
		try {
			mac = CryptTool.md5Digest(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 进行md5加密(商户自己封装MD5加密工具类，此处只提供 参考)
		Map<String, String> param = new HashMap<String, String>();// 组装请求参数
		param.put("merchantId", merchantId);
		param.put("merchantPwd", merchantPwd);
		param.put("oldOrderNo", oldOrderNo);
		param.put("oldOrderReqNo", oldOrderReqNo);
		param.put("refundReqNo", refundReqNo);
		param.put("refundReqDate", refundReqDate);
		param.put("transAmt", transAmt);
		param.put("channel", channel);
		param.put("ledgerDetail", ledgerDetail);
		param.put("mac", mac);
		param.put("bgUrl", bgUrl);
		Http_PushTask HP = new Http_PushTask();

		try {
			String rerurn = HP.execute(CryptTool.transMapToString(param),
					Url.refund).get();

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
