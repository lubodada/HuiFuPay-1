package com.jhj.Agreement;

import java.util.HashMap;
import java.util.Map;

import com.NetWork.util.Url;
import com.bestpay.cn.utils.CryptTool;
import com.jhj.network.Http_PushTask;

public class Http_Query {
	String mac = null;

	public void query() {
		String merchantId = "02370109020131195";
		String orderNo = "20160823222021abcd";
		String orderReqNo = "2016082322202100001";
		String orderDate = "20160823";
		StringBuilder sb = new StringBuilder();// 组装mac加密明文串
		sb.append("MERCHANTID=").append(merchantId);
		sb.append("&ORDERNO=").append(orderNo);
		sb.append("&ORDERREQNO=").append(orderReqNo);
		sb.append("&ORDERDATE=").append(orderDate);
		sb.append("&KEY=").append(
				"1B48AB128017686788E1089CD16501D27F077C292A565305");// 此处是商户的key

		try {
			mac = CryptTool.md5Digest(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 进行md5加密(商户自己封装MD5加密工具类，此处只提供
			// 参考)
		Map<String, String> param = new HashMap<String, String>();// 组装请求参数，参数名大小写敏感
		param.put("merchantId", merchantId);
		param.put("orderNo", orderNo);
		param.put("orderReqNo", orderReqNo);
		param.put("orderDate", orderDate);
		param.put("mac", mac);
		Http_PushTask HP = new Http_PushTask();
		HP.execute(CryptTool.transMapToString(param), Url.query);
	}
}
