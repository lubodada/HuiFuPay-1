package com.jhj.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import com.bestpay.cn.utils.CryptTool;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.jhj.info_util.Json;
import com.jhj.network.Http_PushTask;

public class Select_order_all {

	Context context;
	Json json;
	String merchantId;
	String clientId;
	String keyString;

	String startTime;
	String endTime;

	Handler mhandler = new Handler();

	SharedPreferences_util su = new SharedPreferences_util();
	List<TransactionRecord_Count> tranRecordcount_list;

	@SuppressWarnings("static-access")
	public void runService(Context context, final String startTime,
			final String endTime, Handler handler) {
		this.context = context;
		this.startTime = startTime;
		this.endTime = endTime;
		this.mhandler = handler;

		json = new Json();

		merchantId = su.getPrefString(context, "merchantId", null);
		clientId = su.getPrefString(context, "clientId", null);
		keyString = su.getPrefString(context, "key", null);

		new Thread(new Runnable() {
			public void run() {
				select(startTime, endTime);
			}
		}).start();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressLint("NewApi")
	public void select(String startTime, String endTime) {

		// 签名
		String signature = null;
		// 掌优宝平台订单号
		// utf-8
		String inputCharset = "utf-8";
		// 加密类型
		String signType = "MD5";
		StringBuilder sb = new StringBuilder();// 组装mac加密明文串
		Map<String, String> param = new HashMap<String, String>();

		sb.append("clientId=").append(clientId);
		param.put("clientId", clientId);
		if (!TextUtils.isEmpty(endTime)) {
			sb.append("&endTime=").append(endTime);// false
			param.put("endTime", endTime);
		}

		sb.append("&inputCharset=").append(inputCharset);
		param.put("inputCharset", inputCharset);
		sb.append("&merchantId=").append(merchantId);
		param.put("merchantId", merchantId);
		// sb.append("&orderType=").append(orderType);// false
		sb.append("&orderState=").append(7);// false
		param.put("orderState", "7");

		sb.append("&signType=").append(signType);
		if (!TextUtils.isEmpty(startTime)) {
			sb.append("&startTime=").append(startTime);// false
		}

		sb.append("").append(keyString);

		try {
			signature = CryptTool.md5Digest(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}// 进行md5加密(商户自己封装MD5加密工具类，此处只提供
			// 参考)

		param.put("signature", signature);
		param.put("signType", signType);
		if (!TextUtils.isEmpty(startTime)) {
			param.put("startTime", startTime);
		}

		Http_PushTask HP = new Http_PushTask();

		try {
			String result = HP.execute(CryptTool.transMapToString(param),
					"http://api.zhangyoo.cn/order/queryStatistics").get(10000,
					TimeUnit.MILLISECONDS);

			if (!TextUtils.isEmpty(result)) {
				JSONObject js = new JSONObject(result);
				
				tranRecordcount_list = new ArrayList<TransactionRecord_Count>();
				if (js.getBoolean("result")) {
					
					tranRecordcount_list = json.parseJsonsByTransactionRecord_Count(result);
					if(tranRecordcount_list == null){
						toast(context, "没有交易数据！");
						return;
					}
					Message msg = new Message();
					msg.what=3;
					ArrayList list = new ArrayList();
					list.add(tranRecordcount_list);
					msg.getData().putParcelableArrayList("orderListCount",list);
					mhandler.sendMessage(msg);
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void toast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

}
