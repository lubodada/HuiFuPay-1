package com.jhj.Agreement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.NetWork.util.Url;
import com.bestpay.cn.utils.CryptTool;
import com.jhj.network.Http_PushTask;

public class Http_Revoke {
	String mac = null;

	/**
	 * 撤销
	 * */
	public void revoke() {
		String merchantId = "02370109020131195";
		String merchantPwd = "663913";
		String oldOrderNo = "1533734609531";
		String oldOrderReqNo = "2016082400000638519455";
		String refundReqNo = "20150608113649";
		String refundReqDate = "20160823";
		String transAmt = "1";
		String channel = "05";
		StringBuilder sb = new StringBuilder();// 组装mac加密明文串
		sb.append("MERCHANTID=").append(merchantId);
		sb.append("&MERCHANTPWD=").append(merchantPwd);
		sb.append("&OLDORDERNO=").append(oldOrderNo);
		sb.append("&OLDORDERREQNO=").append(oldOrderReqNo);
		sb.append("&REFUNDREQNO=").append(refundReqNo);
		sb.append("&REFUNDREQDATE=").append(refundReqDate);
		sb.append("&TRANSAMT=").append(transAmt);
		sb.append("&KEY=").append(
				"1B48AB128017686788E1089CD16501D27F077C292A565305");// 此处是商户的key
		try {
			mac = CryptTool.md5Digest(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 进行md5加密(商户自己封装MD5加密工具类，此处只提供
			// 参考)
		Map<String, String> param = new HashMap<String, String>();// 组装请求参数
		param.put("merchantId", merchantId);
		param.put("merchantPwd", merchantPwd);
		param.put("oldOrderNo", oldOrderNo);
		param.put("oldOrderReqNo", oldOrderReqNo);
		param.put("refundReqNo", refundReqNo);
		param.put("refundReqDate", refundReqDate);
		param.put("transAmt", transAmt);
		param.put("channel", channel);
		// param.put("mac", mac);
		Http_PushTask HP = new Http_PushTask();
		try {
			String reselt = (HP.execute(CryptTool.transMapToString(param),
					Url.revoke).get()).toString();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
