package com.jhj.Agreement.ZYB;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.NetWork.util.Url;
import com.bestpay.cn.utils.CryptTool;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MainActivity;
import com.jhj.network.Http_PushTask;
import com.jhj.print.Print;
import com.timepay.zyb.DB_Manager;

public class Http_Query_ZYB {

	private static Timer mTimer = null;
	private static Runnable mTimerTask = null;
	private boolean isPause = false;
	private static Handler mHandler = null;
	static Context context;
	static String outOrderId;
	static String orderId;
	String monery;
	int i = 0;
	Http_Revoke_ZYB HR = new Http_Revoke_ZYB();

	String paycode;
	String clientId;
	String PaymentMethod;
	String cashierName;
	static String merchantId;
	static boolean pay_query = true;
	SharedPreferences_util su = new SharedPreferences_util();
	DB_Manager DBM;

	@SuppressWarnings({ "static-access" })
	public void startTimer(final Context context, String outOrderId,
			String orderId, String monery, String paycode, String cashierName,
			String clientId, String merchantId, String PaymentMethod) {

		stopTimer();
		isPause = false;
		this.context = context;
		this.outOrderId = outOrderId;
		this.orderId = orderId;
		this.monery = Double.toString(Double.parseDouble(monery) / 100);
		this.paycode = paycode;
		this.PaymentMethod = PaymentMethod;
		this.cashierName = cashierName;
		this.clientId = clientId;
		this.merchantId = merchantId;
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {
			mTimerTask = new Runnable() {
				@Override
				public void run() {
					do {
						try {
							if (i < 30) {
								i++;
								sendMessage(2);
								Thread.sleep(2 * 1000);
							} else {
								isPause = true;
								sendMessage(3);
							}
						} catch (InterruptedException e) {
						}
					} while (isPause == false);
				}
			};
		}

		thread(mTimerTask);

	}

	public void thread(Runnable runnable) {

		new Thread(runnable).start();

	}

	@SuppressLint("HandlerLeak")
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
			@SuppressWarnings("static-access")
			@SuppressLint("SimpleDateFormat")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					toast(context, "待付款！");
					break;
				case 2:
					if (pay_query) {
						query(context, outOrderId, orderId);
					}
					break;
				case 3:
					toast(context, "交易超时！");
					i = 0;
					stopTimer();
					break;
				case 4:
					toast(context, "已取消！");
					i = 0;
					stopTimer();
					break;
				case 7:
					stopTimer();
					// sound();
					MainActivity.soundPool.play(1, 1, 1, 0, 0, 1);
					toast(context, "支付成功！");
					// 退款
					// HR.Revoke(context, outOrderId, orderId);
					SimpleDateFormat sDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String Time_new = sDateFormat.format(new java.util.Date());
					DBM = new DB_Manager(context);
					DBM.updatePayTime(outOrderId, Time_new);
					DBM.updatePayStatus(outOrderId,7);
					if (su.getPrefboolean(context, "on_off", false)) {

						if (Build.MANUFACTURER.contains("BASEWIN")
								|| Build.MANUFACTURER.contains("basewin")) {
							Print pt = new Print();
							pt.prilay(context, orderId, monery, paycode,
									PaymentMethod, cashierName, clientId,
									Time_new);
							toast(context, "打印");
						} else {
							toast(context, "未检测到打印机！无法进行打印打印");
						}
					} else {
						toast(context, "打印机未开启");
					}
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

	@SuppressWarnings("static-access")
	@SuppressLint("HandlerLeak")
	public void query(Context context, String outOrderId, String orderId) {
		pay_query = false;
		// 签名key
		String keyString = su.getPrefString(context, "key", null);

		// 签名
		String signature = null;

		// 掌优宝平台订单号
		// utf-8
		String inputCharset = "utf-8";
		// 加密类型
		String signType = "MD5";

		StringBuilder sb = new StringBuilder();// 组装mac加密明文串
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
		param.put("inputCharset", inputCharset);
		param.put("merchantId", merchantId);
		param.put("orderId", orderId);
		param.put("outOrderId", outOrderId);
		param.put("signature", signature);
		param.put("signType", signType);

		Http_PushTask HP = new Http_PushTask();

		try {
			String rerurn = HP.execute(CryptTool.transMapToString(param),
					Url.query_zyb).get();
			JSONObject js = new JSONObject(rerurn);
			if (!TextUtils.isEmpty(rerurn)) {
				if (js.getBoolean("result")) {
					if (js.has("message")) {
						if (js.has("orderState")) {
							pay_query = true;
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

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unused")
	public void print() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy/MM/dd hh:mm:ss");
		String Time_new = sDateFormat.format(new java.util.Date());
	}

	public static void toast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

}
