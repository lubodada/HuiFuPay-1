package com.example.Main;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.NetWork.util.CTelephoneInfo;
import com.basewin.services.ServiceManager;
import com.bestpay.cn.utils.Networkstate;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.bestpay.cn.utils.Version_util;
import com.jhjpay.zyb.R;
import com.jhj.ViewPagerAndTabhost.MainActivity_TabHost;
import com.jhj.network.Data_zyb;
import com.jhj.network.Http_PushTask;
import com.jhj.print.Print;
import com.jhj.servrce.UpdateService;

@SuppressLint("HandlerLeak")
public class SettingActivity extends Activity {
	TextView shezhi_paykey, shezhi_shh, shezhi_datakey, guanyu, banbencc, mode,
			print_tx, on_off_tx, data_updata;
	Button datakey_qx, paykey_qd, shh_qx, shh_qd, dkey_qx, dkey_qd, UP_data_qx,
			UP_data_qd, qx_money, qd_money;
	Dialog dia_paykey, dia_shh, dia_dkey, dia_mode, dia_about, dia_update,
			dia_money, dia_money_num;
	EditText et_paykey, et_shh, et_dkey, et_money;
	// 设备终端号，收银员名称，商户号
	private String clientId, cashierName, merchantId, Str_mode, print,
			Str_money, Str_money_num;
	SharedPreferences_util su = new SharedPreferences_util();
	Data_zyb dz = new Data_zyb();
	RadioButton RB_camera, RB_Barcode, RB_fixed, RB_input;
	boolean on_off_m, merchantId_type;
	TextView et_UP_data, mode_monery, shezhi_money, line_money;
	// 下载app的名字
	public static final String appName = "下载测试";
	// 下载app的地址
	public static final String downUrl = "http://cnyssj.net/es.apk";

	/** 使用SharedPreferences 来储存与读取数据 **/
	// updata
	String result;
	boolean force_update;
	String url;
	String description;
	Version_util Vn = new Version_util();
	// 固定金额
	boolean Fixed_amount;
	String error_msg, manager_num;
	JSONObject js;
	
	@SuppressWarnings({ "static-access" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 商户号
		merchantId = su.getPrefString(SettingActivity.this, "merchantId", null);
		// 商户名称
		cashierName = su.getPrefString(SettingActivity.this, "cashiername",
				null);
		// 设备终端号
		clientId = su.getPrefString(SettingActivity.this, "clientId", null);

		Str_mode = su.getPrefString(SettingActivity.this, "mode", null);
		Str_money = su.getPrefString(SettingActivity.this, "money", null);
		print = su.getPrefString(SettingActivity.this, "print", null);
		on_off_m = su.getPrefboolean(SettingActivity.this, "on_off", false);
		Str_money_num = su.getPrefString(SettingActivity.this, "Str_money_num",
				"1000");

		setContentView(R.layout.activity_setting);
		MyAppLication.getInstance().addActivity(this);
		Context context = SettingActivity.this;
		// 打印最后一次
		print_tx = (TextView) findViewById(R.id.print);
		print_tx.setOnClickListener(mylistener);
		// 收银员名称
		shezhi_datakey = (TextView) findViewById(R.id.shezhi_datakey);
		shezhi_datakey.setOnClickListener(mylistener);
		dia_dkey = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_dkey.setContentView(R.layout.activity_start_dialog_cashiername);
		dkey_qx = (Button) dia_dkey.findViewById(R.id.Dkey_qx);
		dkey_qd = (Button) dia_dkey.findViewById(R.id.Dkey_qd);
		et_dkey = (EditText) dia_dkey.findViewById(R.id.et_dkey);
		dkey_qx.setOnClickListener(mylistener);
		dkey_qd.setOnClickListener(mylistener);

		// 选择扫码模式
		dia_mode = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_mode.setContentView(R.layout.activity_start_dialog_mode);
		RB_camera = (RadioButton) dia_mode.findViewById(R.id.RB_camera);
		RB_Barcode = (RadioButton) dia_mode.findViewById(R.id.RB_Barcode);
		RB_camera.setOnClickListener(mylistener);
		RB_Barcode.setOnClickListener(mylistener);
		mode = (TextView) findViewById(R.id.mode);
		mode.setOnClickListener(mylistener);
		if (!TextUtils.isEmpty(Str_mode)) {
			RB_Barcode.setChecked(true);
		} else {
			RB_camera.setChecked(true);
		}
		// 设备终端号
		shezhi_paykey = (TextView) findViewById(R.id.shezhi_paykey);
		shezhi_paykey.setOnClickListener(mylistener);
		dia_paykey = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_paykey.setContentView(R.layout.activity_start_dialog_dtn);
		et_paykey = (EditText) dia_paykey.findViewById(R.id.et_paykey);
		datakey_qx = (Button) dia_paykey.findViewById(R.id.datakey_qx);
		paykey_qd = (Button) dia_paykey.findViewById(R.id.paykey_qd);
		datakey_qx.setOnClickListener(mylistener);
		paykey_qd.setOnClickListener(mylistener);

		/**
		 * 商户号
		 * */
		shezhi_shh = (TextView) findViewById(R.id.shezhi_shh);
		shezhi_shh.setOnClickListener(mylistener);
		dia_shh = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_shh.setContentView(R.layout.activity_start_dialog_merchantid);
		shh_qx = (Button) dia_shh.findViewById(R.id.shh_qx);
		shh_qd = (Button) dia_shh.findViewById(R.id.shh_qd);
		et_shh = (EditText) dia_shh.findViewById(R.id.et_email);
		shh_qx.setOnClickListener(mylistener);
		shh_qd.setOnClickListener(mylistener);
		// SpannableStringBuilder builder = new
		// SpannableStringBuilder(shezhi_shh
		// .getText().toString());
		// ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.BLACK);
		// ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.GRAY);
		// builder.setSpan(redSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// builder.setSpan(whiteSpan, 4, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		// shezhi_shh.setText(builder);

		/**
		 * 关于
		 * */
		dia_about = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_about.setContentView(R.layout.activity_start_dialog_about);

		guanyu = (TextView) findViewById(R.id.about);
		guanyu.setOnClickListener(mylistener);

		/**
		 * 版本检测
		 * */
		banbencc = (TextView) findViewById(R.id.xinbanben);
		banbencc.setOnClickListener(mylistener);
		dia_update = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_update.setContentView(R.layout.activity_start_dialog_update);
		et_UP_data = (TextView) dia_update.findViewById(R.id.et_UP_data);
		UP_data_qx = (Button) dia_update.findViewById(R.id.UP_data_qx);
		UP_data_qd = (Button) dia_update.findViewById(R.id.UP_data_qd);
		UP_data_qx.setOnClickListener(mylistener);
		UP_data_qd.setOnClickListener(mylistener);
		// 更新参数
		data_updata = (TextView) findViewById(R.id.data_updata);
		data_updata.setOnClickListener(mylistener);

		/**
		 * 设定余额相关
		 * 
		 * */
		// 模式
		mode_monery = (TextView) findViewById(R.id.mode_monery);
		mode_monery.setOnClickListener(mylistener);
		dia_money = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_money.setContentView(R.layout.activity_start_dialog_money_mode);
		RB_fixed = (RadioButton) dia_money.findViewById(R.id.RB_fixed);
		RB_input = (RadioButton) dia_money.findViewById(R.id.RB_input);
		RB_fixed.setOnClickListener(mylistener);
		RB_input.setOnClickListener(mylistener);
		line_money = (TextView) findViewById(R.id.line_money);
		// 输入金额相关
		shezhi_money = (TextView) findViewById(R.id.shezhi_money);
		shezhi_money.setOnClickListener(mylistener);
		dia_money_num = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_money_num.setContentView(R.layout.activity_start_dialog_monrynum);
		qx_money = (Button) dia_money_num.findViewById(R.id.qx_money);
		qd_money = (Button) dia_money_num.findViewById(R.id.qd_money);
		et_money = (EditText) dia_money_num.findViewById(R.id.et_money);
		qx_money.setOnClickListener(mylistener);
		qd_money.setOnClickListener(mylistener);

		if (TextUtils.isEmpty(Str_money)) {
			shezhi_money.setVisibility(View.GONE);
			line_money.setVisibility(View.GONE);
			RB_input.setChecked(true);

		} else {
			if (!TextUtils.isEmpty(Str_money_num)) {
				shezhi_money.setText("金额：" + settextnum(Str_money_num));
			}

			RB_fixed.setChecked(true);
		}

		/**
		 * 打印机状态
		 * */
		on_off_tx = (TextView) findViewById(R.id.on_off);
		on_off_tx.setOnClickListener(mylistener);
		if (on_off_m) {
			on_off_tx.setText(col_tx("打印机状态：已开启"));
		} else {
			on_off_tx.setText(col_tx("打印机状态：已关闭"));
		}

		if (!TextUtils.isEmpty(clientId)) {
			shezhi_paykey.setText("设备终端号: " + clientId);
			// shezhi_shh.setText("商户号:  ");
		} else {
			shezhi_paykey.setText("设备终端号 ");
			// shezhi_shh.setText("商户号 ");
		}
		if (!TextUtils.isEmpty(cashierName)) {
			shezhi_datakey.setText("商户名称: " + cashierName);
		} else {
			shezhi_datakey.setText("商户名称 ");
		}

		if (!TextUtils.isEmpty(merchantId)) {
			merchantId_type = true;
			shezhi_shh.setText("商户号: " + merchantId);
		} else {
			merchantId_type = false;
			shezhi_shh.setText("商户号 ");
		}
		String imeiSIM1 = null;
		TextView ms_ID = (TextView) findViewById(R.id.ms_ID);
		if (Build.MANUFACTURER.contains("BASEWIN")
				|| Build.MANUFACTURER.contains("basewin")) {
			try {
				imeiSIM1 = ServiceManager.getInstence().getDeviceinfo().getSN();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} else {
			CTelephoneInfo telephonyInfo = CTelephoneInfo.getInstance(SettingActivity.this);
			telephonyInfo.setCTelephoneInfo();
			imeiSIM1 = telephonyInfo.getImeiSIM1();
			ms_ID.setText("设备识别号:" + imeiSIM1);
		}
		ms_ID.setText("设备识别号:" + imeiSIM1);

	}

	View.OnClickListener mylistener = new View.OnClickListener() {
		Window w;
		WindowManager.LayoutParams lp;

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.shezhi_datakey:

				dia_dkey.show();
				dia_dkey.setCanceledOnTouchOutside(false);
				w = dia_dkey.getWindow();
				lp = w.getAttributes();
				lp.x = 0;
				lp.y = 40;
				dia_dkey.onWindowAttributesChanged(lp);

				break;
			case R.id.shezhi_paykey:
				// 设置
				stoi("暂不支持更改！");
				// dia_paykey.show();
				// dia_paykey.setCanceledOnTouchOutside(false);
				// w = dia_paykey.getWindow();
				// lp = w.getAttributes();
				// lp.x = 0;
				// lp.y = 40;
				// dia_paykey.onWindowAttributesChanged(lp);

				break;
			case R.id.shezhi_shh:
				// 商户号
				stoi("暂不支持更改！");
				// dia_shh.show();
				// dia_shh.setCanceledOnTouchOutside(false);
				// w = dia_shh.getWindow();
				// lp = w.getAttributes();
				// lp.x = 0;
				// lp.y = 40;
				// dia_shh.onWindowAttributesChanged(lp);

				break;

			case R.id.xinbanben:
				// 跳转service下载程序
				showmessage(1);
				// stoi("您已是最新版本，暂时无更新！");

				break;
			case R.id.UP_data_qx:
				if (!force_update) {
					dia_update.cancel();
				} else {
					stoi("大哥，新版本可美了，你得升级啊！");
				}

				break;
			case R.id.UP_data_qd:
				dia_update.cancel();
				// 跳转service下载程序
				Intent intent = new Intent(SettingActivity.this,
						UpdateService.class);
				intent.putExtra("Key_App_Name", "翼开店超市版");
				intent.putExtra("Key_Down_Url", url);
				startService(intent);
				break;
			case R.id.datakey_qx:
				dia_paykey.cancel();// 关闭弹出框
				et_paykey.setText("");
				break;
			case R.id.data_updata:
				// 更新参数
				showmessage(5);
				break;
			case R.id.paykey_qd:
				clientId = String.valueOf(et_paykey.getText());

				su.setPrefString(SettingActivity.this, "clientId", clientId);
				shezhi_paykey.setText("设备终端号：" + clientId);
				dia_paykey.cancel();// 关闭弹出框

				break;
			case R.id.shh_qx:
				dia_shh.cancel();// 关闭弹出框
				et_shh.setText("");
				break;
			case R.id.shh_qd:
				stoi("暂不支持更改！");
				// merchantId = String.valueOf(et_shh.getText());
				// if (!TextUtils.isEmpty(merchantId) && merchantId != "") {
				// if (merchantId_type) {
				// String num = et_shh.getText().toString();
				// if (num.substring(0, 4).equals("1027")) {
				// shezhi_shh.setText("商户号："
				// + num.substring(4, num.length()));
				// su.setPrefString(SettingActivity.this,
				// "merchantId",
				// num.substring(4, num.length()));
				// dia_shh.cancel();
				// et_shh.setText("");
				// } else {
				// stoi("请输入正确的商户号编码");
				// }
				// } else {
				// shezhi_shh
				// .setText("商户号：" + et_shh.getText().toString());
				// su.setPrefString(SettingActivity.this, "merchantId",
				// et_shh.getText().toString());
				// et_shh.setText("");
				// dia_shh.cancel();
				// }
				//
				// } else {
				// stoi("请输入正确的商户号");
				// }

				break;

			case R.id.Dkey_qx:
				dia_dkey.cancel();// 关闭弹出框
				et_dkey.setText("");
				break;
			case R.id.Dkey_qd:

				String cn = String.valueOf(et_dkey.getText());
				shezhi_datakey.setText("商户名称：" + cn);
				su.setPrefString(SettingActivity.this, "cashiername", cn);
				et_dkey.setText("");
				dia_dkey.cancel();// 关闭弹出框

				break;
			case R.id.mode:
				// 模式弹出框
				dia_mode.show();
				dia_mode.setCanceledOnTouchOutside(false);
				w = dia_mode.getWindow();
				lp = w.getAttributes();
				lp.x = 0;
				lp.y = 40;
				dia_mode.onWindowAttributesChanged(lp);
				break;
			case R.id.RB_camera:
				// 相机扫码
				su.setPrefString(SettingActivity.this, "mode", null);
				RB_camera.setChecked(true);
				dia_mode.cancel();
				break;
			case R.id.RB_Barcode:
				// 扫码枪
				su.setPrefString(SettingActivity.this, "mode", "mode");
				dia_mode.cancel();
				break;
			case R.id.print:
				if (on_off_m) {
					if (Build.MANUFACTURER.contains("BASEWIN")
							|| Build.MANUFACTURER.contains("basewin")) {
						try {

							if (print != null) {

								JSONObject jsonObject = new JSONObject(print);
								String orderId = jsonObject
										.getString("orderId").toString();
								String monery = jsonObject.getString("monery")
										.toString();
								String paycode = jsonObject
										.getString("paycode").toString();
								String PaymentMethod = jsonObject.getString(
										"PaymentMethod").toString();
								String clientId = jsonObject.getString(
										"clientId").toString();
								String Time = jsonObject.getString("Time")
										.toString();
								String shanghm = jsonObject
										.getString("shanghm").toString();
								Print pt = new Print();
								pt.prilay(SettingActivity.this, orderId,
										monery, paycode, PaymentMethod,
										shanghm, clientId, Time);

							} else {
								stoi("没有有效的交易数据");
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						stoi("未检测到打印机！无法进行打印打印");
					}
				} else {
					stoi("打印机未开启");
				}
				break;
			case R.id.about:
				dia_about.setCancelable(true);
				dia_about.show();
				dia_about.setCanceledOnTouchOutside(true);
				w = dia_about.getWindow();
				lp = w.getAttributes();
				lp.x = 0;
				lp.y = 40;
				dia_about.onWindowAttributesChanged(lp);
				break;
			case R.id.on_off:
				if (on_off_m) {
					on_off_m = false;
					su.setPrefboolean(SettingActivity.this, "on_off", on_off_m);
					on_off_tx.setText(col_tx("打印机状态：已关闭"));

				} else {
					on_off_m = true;
					su.setPrefboolean(SettingActivity.this, "on_off", on_off_m);
					on_off_tx.setText(col_tx("打印机状态：已开启"));
				}
				break;
			case R.id.mode_monery:
				dia_money.show();
				dia_money.setCanceledOnTouchOutside(false);
				w = dia_money.getWindow();
				lp = w.getAttributes();
				lp.x = 0;
				lp.y = 40;
				dia_money.onWindowAttributesChanged(lp);
				break;
			case R.id.RB_fixed:
				shezhi_money.setVisibility(View.VISIBLE);
				line_money.setVisibility(View.VISIBLE);
				su.setPrefString(SettingActivity.this, "money", "money");
				shezhi_money.setText("金额：" + settextnum(Str_money_num));
				RB_fixed.setChecked(true);
				dia_money.cancel();
				break;
			case R.id.RB_input:
				shezhi_money.setVisibility(View.GONE);
				line_money.setVisibility(View.GONE);
				su.setPrefString(SettingActivity.this, "money", null);
				RB_input.setChecked(true);
				dia_money.cancel();
				break;
			case R.id.shezhi_money:
				dia_money_num.show();
				dia_money_num.setCanceledOnTouchOutside(false);
				w = dia_money_num.getWindow();
				lp = w.getAttributes();
				lp.x = 0;
				lp.y = 40;
				dia_money_num.onWindowAttributesChanged(lp);
				break;
			case R.id.qx_money:
				et_money.setText("");
				dia_money_num.cancel();
				break;
			case R.id.qd_money:
				if (!TextUtils.isEmpty(et_money.getText().toString())) {
					shezhi_money.setText("金额：" + et_money.getText().toString());
					su.setPrefString(SettingActivity.this, "Str_money_num",
							Double.toString(Double.parseDouble(et_money
									.getText().toString()) * 100));
					et_money.setText("");
					dia_money_num.cancel();
				} else {
					stoi("请输入正确的金额");
				}

				break;
			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			Intent intent = new Intent();
			intent.setClass(this, MainActivity_TabHost.class);
			startActivity(intent);
			finish();
		}
		return true; // 不允许返回
	}

	public void stoi(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	public SpannableStringBuilder col_tx(String str) {
		SpannableStringBuilder builder = new SpannableStringBuilder(str);
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.BLACK);
		ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.BLUE);
		builder.setSpan(redSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(whiteSpan, 6, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return builder;

	}

	// 发送handle
	public void showmessage(int message) {
		Message localMessage = new Message();
		localMessage.what = message;
		this.handler.sendMessage(localMessage);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				(new UpdateVersion()).start();
				break;
			case 2:
				stoi("您已是最新版本，无需升级！");
				break;
			case 3:
				if (force_update) {
					UP_data_qx
							.setBackgroundResource(R.drawable.button_oncleck_item_num);
				}
				dia_update.setCancelable(false);
				Window w;
				WindowManager.LayoutParams lp;
				dia_update.show();
				dia_update.setCanceledOnTouchOutside(false);
				w = dia_update.getWindow();
				lp = w.getAttributes();
				lp.x = 0;
				lp.y = 40;
				dia_update.onWindowAttributesChanged(lp);
				et_UP_data.setText(description);
				break;
			case 4:
				stoi(error_msg + "，请联系服务商！");
				break;
			case 5:
				data_up();
				break;
			case 6:
				(new downLoad()).start();
				break;
			case 7:
				stoi("数据获取失败！");
				break;
			case 8:
				stoi("获取商户号失败，请检查网络连接后重试!");
				break;
			case 9:
				try {
					shezhi_shh.setText("商户号: " + js.getString("merchantId"));
					shezhi_datakey.setText("商户名称: "
							+ js.getString("merchantName"));
					shezhi_paykey.setText("设备终端号: "
							+ js.getString("deviceNumber"));
					stoi("参数更新成功");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	class UpdateVersion extends Thread {
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			try {
				Http_PushTask HP = new Http_PushTask();
				result = HP.execute(
						"",
						"http://cnyssj.net/auto_update/?" + "app_name="
								+ Vn.getAppInfo(SettingActivity.this)).get();
				JSONObject js = new JSONObject(result);
				if (!TextUtils.isEmpty(result)) {
					js.getString("version");
					String version = Vn.getVersionName(SettingActivity.this);
					if (version.equals(js.getString("version"))
							|| "false".equals(js.getString("version"))) {
						showmessage(2);
						return;
					} else {
						force_update = js.getBoolean("force_update");
						url = js.getString("url");
						description = js.getString("description");
						showmessage(3);
						return;
					}
				} else {
					showmessage(5);
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class downLoad extends Thread {
		@SuppressWarnings("static-access")
		@Override
		public void run() {

			Http_PushTask HP = new Http_PushTask();
			try {
				String result = HP.execute(
						"",
						"http://121.41.113.42/ykdconfig/?" + "app_key="
								+ manager_num)
						.get(10000, TimeUnit.MILLISECONDS);
				js = new JSONObject(result);
				// onToast(result);
				if (!TextUtils.isEmpty(result)) {
					if (js.getBoolean("success")) {

						su.setPrefString(SettingActivity.this, "merchantId",
								js.getString("merchantId"));

						su.setPrefString(SettingActivity.this, "key",
								js.getString("key"));
						// 商户名称

						su.setPrefString(SettingActivity.this, "cashiername",
								js.getString("merchantName"));

						su.setPrefString(SettingActivity.this, "clientId",
								js.getString("deviceNumber"));
						showmessage(9);
					} else {
						error_msg = js.getString("error_msg");
						showmessage(4);
					}
				} else {
					showmessage(7);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				showmessage(8);
				e.printStackTrace();
				return;
			}

		}
	}

	private void data_up() {
		if (Build.MANUFACTURER.contains("BASEWIN")
				|| Build.MANUFACTURER.contains("basewin")) {
			if (Networkstate.isNetworkAvailable(SettingActivity.this)) {
				// manager_num = mn.manager();
				try {
					manager_num = ServiceManager.getInstence().getDeviceinfo()
							.getSN();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				showmessage(6);
			} else {
				stoi("无可用网络！请联网后重试！");
			}

		} else {
			stoi("未能识别该设备！");
		}
	}

	public String settextnum(String num) {
		if (!TextUtils.isEmpty(num)) {
			Double money = Double.parseDouble(num) / 100;
			return Double.toString(money);
		} else {
			return null;
		}

	}
}
