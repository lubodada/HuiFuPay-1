package com.jhj.Agreement.ZYB;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.NetWork.util.Url;
import com.bestpay.cn.utils.CryptTool;
import com.jhj.network.Http_PushTask;

public class Http_Revoke_ZYB {

	private static Timer mTimer = null;
	private static Runnable mTimerTask = null;
	private boolean isPause = false;
	private static Handler mHandler = null;
	static Context context;
	static String outOrderId;
	static String orderId;

	/**
	 * 退款
	 * */
	@SuppressWarnings({ "static-access" })
	public void startTimer(final Context context, String outOrderId,
			String orderId) {
		stopTimer();
		isPause = false;
		this.context = context;
		this.outOrderId = outOrderId;
		this.orderId = orderId;

		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {
			mTimerTask = new Runnable() {
				@Override
				public void run() {
					do {
						try {
							sendMessage(2);
							Thread.sleep(2 * 1000);
						} catch (InterruptedException e) {
						}
					} while (isPause == false);
				}
			};
		}

		new Thread(mTimerTask).start();

	}

	public void stopTimer() {
		isPause = true;
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask = null;
		}
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					toast(context, "待付款！");
					break;
				case 2:
					Revoke(context, outOrderId, orderId);
					break;
				case 3:
					toast(context, "交易超时！");
					break;
				case 4:
					toast(context, "已经取消！");
					stopTimer();
					break;
				case 7:
					toast(context, "支付成功！");
					stopTimer();
					break;
				case 10:
					toast(context, "已退款！");
					stopTimer();
					break;
				case 13:
					toast(context, "支付中！");
					break;
				default:
					break;
				}
			}
		};
	}

	public static void sendMessage(int id) {
		if (mHandler != null) {
			Message message = Message.obtain(mHandler, id);
			mHandler.sendMessage(message);
		}
	}

	@SuppressLint("HandlerLeak")
	public static void Revoke(Context context, String outOrderId, String orderId) {

		// 签名key
		String keyString = "75ea87fd1a71421a9b74d0ec29035639";
		// 签名
		String signature = null;

		String merchantId = "918";
		// 掌优宝平台订单号
		// utf-8
		String inputCharset = "utf-8";
		// 加密类型
		String signType = "MD5";
		String clientId = null;

		StringBuilder sb = new StringBuilder();// 组装mac加密明文串
		// sb.append("clientId=").append(clientId);
		sb.append("inputCharset=").append(inputCharset);
		sb.append("&merchantId=").append(merchantId);
		sb.append("&orderId=").append(orderId);
		sb.append("&outOrderId=").append(outOrderId);
		sb.append("&signType=").append(signType);
		sb.append("").append(keyString);
		try {
			signature = CryptTool.md5Digest(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}// 进行md5加密(商户自己封装MD5加密工具类，此处只提供
			// 参考)
		Map<String, String> param = new HashMap<String, String>();// 组装请求参数，参数名大小写敏感
		// param.put("clientId", clientId);
		param.put("inputCharset", inputCharset);
		param.put("merchantId", merchantId);
		param.put("orderId", orderId);
		param.put("outOrderId", outOrderId);
		param.put("signature", signature);
		param.put("signType", signType);

		Http_PushTask HP = new Http_PushTask();
		try {
			String rerurn = HP.execute(CryptTool.transMapToString(param),
					Url.refund_zyb).get();
			JSONObject js = new JSONObject(rerurn);
			if (!TextUtils.isEmpty(rerurn)) {
				if (js.getBoolean("result")) {
					if (js.has("message")) {
						if (js.has("orderState")) {
							sendMessage(js.getInt("orderState"));
						}
					}
				} else {

					toast(context, js.get("message").toString());
				}

			} else {
				toast(context, "交易失败");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void toast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
}
