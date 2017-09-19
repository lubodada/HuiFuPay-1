package com.jhj.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.bestpay.cn.utils.CryptTool;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.jhj.info_util.Json;
import com.jhj.network.Http_PushTask;
import com.timepay.zyb.DB_Manager;

public class Select_order {

	Context context;
	DB_Manager db_Manger;
	List<TransactionRecord> transList;
	Json json;
	String merchantId;
	String clientId;
	String keyString;

	String startTime;
	String endTime;
	int pageNum;
	int pageSize;
	
	Handler mhandler = new Handler();

	SharedPreferences_util su = new SharedPreferences_util();

	@SuppressWarnings("static-access")
	public void runService(Context context, final String startTime,
			final String endTime, final int pageSize,Handler handler,final boolean isdisplay) {
		this.context = context;
		this.startTime = startTime;
		this.endTime = endTime;
		this.pageSize = pageSize;
		this.mhandler = handler;

		db_Manger=new DB_Manager(context);
		json = new Json();

		merchantId = su.getPrefString(context, "merchantId", null);
		clientId = su.getPrefString(context, "clientId", null);
		keyString = su.getPrefString(context, "key", null);

		new Thread(new Runnable() {
			public void run() {
				select(startTime, endTime, pageNum, pageSize,isdisplay);
			}
		}).start();
	}

	public void select(String startTime, String endTime, int pageNum,
			int pageSize, boolean isdisplay) {

		/**
		 * merchantId clientId startTime endTime pageNum pageSize orderType
		 * orderState inputCharset signType signature
		 * */
		int size = 1;
		for (int i = 1; i <= size; i++) {
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

			sb.append("&mer_id=").append(merchantId);
			param.put("mer_id", merchantId);

			sb.append("&merchantId=").append(merchantId);
			param.put("merchantId", merchantId);

			// sb.append("&orderType=").append(orderType);// false
			sb.append("&orderState=").append(7);// false
			param.put("orderState", "7");

			sb.append("&pageNum=").append(i);
			param.put("pageNum", Integer.toString(i));
			sb.append("&pageSize=").append(pageSize);
			param.put("pageSize", Integer.toString(pageSize));
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
						"http://api.zhangyoo.cn/order/queryOrderList").get(10000,
								TimeUnit.MILLISECONDS);

				if (!TextUtils.isEmpty(result)) {
					JSONObject js = new JSONObject(result);
					if (js.getBoolean("result")) {
						double ordersize = js.getInt("orderSize");
						size =  (int) Math.ceil(ordersize / (double)this.pageSize);

						transList = new ArrayList<TransactionRecord>();
						transList = json.parseJsonsByTransactionRecord(result);
						if(transList != null){
							db_Manger.addTransRecordData(transList);
						}
					}else{
						String message = js.getString("message");
						Message msg = new Message();
						msg.what=2;
						msg.getData().putString("message", message);
						mhandler.sendMessage(msg);
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(isdisplay){
			mhandler.sendEmptyMessage(1);
		}
	}

	public static void toast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

}
