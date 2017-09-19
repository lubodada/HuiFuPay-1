package com.example.Main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.CryptTool;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.ViewPagerAndTabhost.MainActivity_TabHost;
import com.jhj.info_util.Iinformation;
import com.jhj.network.Http_PushTask;
import com.jhjpay.zyb.R;
import com.timepay.zyb.DB_Manager;

public class LoginActivity extends Activity {
	EditText login_edtId, login_edtPwd;
	Button login;
	ImageView img_head,img_choice;
	TextView tv_forgetpwd;
	Button login_zhuce;
	SharedPreferences_util su = new SharedPreferences_util();
	String userName, password;
	String error_msg;
	String data_result;
	String uid;
	String idCardName;// 商户姓名
	String token;
	YWLoadingDialog yDialog;
	private Boolean merchant_information_off,login_off;
	private Boolean choice_off;//记住密码
	private DB_Manager db_Manger;// 数据库操作类

	@SuppressWarnings("static-access")
	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示q标题栏
		// 全屏
		if (su.getPrefString(LoginActivity.this, "clientId", null) == null) {
			su.setPrefString(LoginActivity.this, "clientId", "API000000001");
		}
		login_off = su.getPrefboolean(LoginActivity.this, "login_off", false);
		choice_off = su.getPrefboolean(LoginActivity.this, "choice_off", false);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_xitongdl);
		//		setImageView();// 加载头像
		db_Manger = new DB_Manager(getApplicationContext());
		yDialog = new YWLoadingDialog(LoginActivity.this);
		login_edtId = (EditText) findViewById(R.id.login_edtId);
		login_edtPwd = (EditText) findViewById(R.id.login_edtPwd);
		//记住密码
		img_choice = (ImageView) findViewById(R.id.img_choice);
		img_choice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!choice_off){
					choice_off = true;
					su.setPrefboolean(LoginActivity.this, "choice_off", choice_off);
					img_choice.setBackgroundResource(R.drawable.huifu_choice_check);
				}else if(choice_off){
					choice_off = false;
					su.setPrefboolean(LoginActivity.this, "choice_off", choice_off);
					img_choice.setBackgroundResource(R.drawable.huifu_choice_uncheck);
				}
			}
		});
		tv_forgetpwd = (TextView) findViewById(R.id.tv_forgetpwd);
		login = (Button) findViewById(R.id.login_btnLogin);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// showmessage(1);
				if (!TextUtils.isEmpty(login_edtId.getText())) {
					if (!TextUtils.isEmpty(login_edtPwd.getText())) {
						yDialog.show();
						userName = login_edtId.getText().toString().trim();
						password = login_edtPwd.getText().toString().trim();

						(new Loading_postdata()).start();

					} else {
						Toastshow("用户密码不能为空");
					}
				} else {
					Toastshow("用户账号不能为空");
				}
			}
		});
		login_zhuce = (Button) findViewById(R.id.login_zhuce);
		login_zhuce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						ZhuCeActivity.class);
				startActivity(intent);
			}
		});
		initControl();
	}

	/**
	 * 加载头像
	 */
	@SuppressWarnings("static-access")
	public void setImageView() {
		img_head = (ImageView) findViewById(R.id.img_head);
		String mPhotoPath = su.getPrefString(LoginActivity.this, "imgPath",
				null);
		if (mPhotoPath != null) {
			File file = new File(mPhotoPath);
			if (file.exists()) {
				Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
				img_head.setImageBitmap(bitmap);
			}
		} else {
			img_head.setImageResource(R.drawable.ic_launcher);
		}

	}
	private void toast(){
		Toast.makeText(LoginActivity.this,"",Toast.LENGTH_LONG).show();
	}
	// 接受异步通知的URL地址
	@SuppressWarnings("static-access")
	private void initControl() {

		if(choice_off){
			choice_off = true;
			su.setPrefboolean(LoginActivity.this, "choice_off", choice_off);
			img_choice.setBackgroundResource(R.drawable.huifu_choice_check);
		}else if(!choice_off){
			choice_off = false;
			su.setPrefboolean(LoginActivity.this, "choice_off", choice_off);
			img_choice.setBackgroundResource(R.drawable.huifu_choice_uncheck);
		}
		userName = su.getPrefString(LoginActivity.this, "userName", null);
		password = su.getPrefString(LoginActivity.this, "password", null);
		if (userName != null) {
			login_edtId.setText(userName);
		}
		if(choice_off){
			if (password != null) {
				login_edtPwd.setText(password);
			}
			login_edtId.setSelection(login_edtId.getText().toString().length());
		}
	}

	class Loading_postdata extends Thread {
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			try {
				String Url = "http://cnyssj.com/dfweb/sys/sysuser/login";

				Map<String, String> param = new HashMap<String, String>();// 组装请求参数
				param.put("userName", userName);
				param.put("password", password);
				Http_PushTask HP = new Http_PushTask();
				String result = HP.execute(CryptTool.transMapToString(param),
						Url).get();
				if (!TextUtils.isEmpty(result)) {
					JSONObject js = new JSONObject(result);
					if (js.getString("result").equals("1")) {

						if(!userName.equals(su.getPrefString(LoginActivity.this, "userName", null))){
							db_Manger.deleteAllTransRecord();
						}
						showmessage(2);
						su.setPrefString(LoginActivity.this, "userName",
								userName);
						su.setPrefString(LoginActivity.this, "password",
								password);
						su.setPrefString(LoginActivity.this, "uid",
								js.getString("uid"));
						su.setPrefString(LoginActivity.this, "token",
								js.getString("token"));
						showmessage(3);
					} else {
						data_result = js.getString("result");
						showmessage(10);
					}
				} else {
					showmessage(11);
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

	// 发送handle
	public void showmessage(int message) {
		Message localMessage = new Message();
		localMessage.what = message;
		this.handler.sendMessage(localMessage);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@SuppressWarnings("static-access")
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				yDialog.dismiss();
				// Toastshow("登录成功！");
				Intent intent = new Intent();
				intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity_TabHost.class);
				startActivity(intent);
				login_off = true;
				su.setPrefboolean(LoginActivity.this, "login_off", login_off);
				finish();
				break;
			case 2:
				(new downLoad()).start();
				break;
			case 3:
				(new download_userInfo()).start();
				break;
			case 6:
				yDialog.dismiss();
				Toastshow(error_msg);
				break;
			case 10:
				yDialog.dismiss();
				if (data_result.equals("-1")) {
					Toastshow("用户名有误或被禁用");
				} else if (data_result.equals("-2")) {
					Toastshow("密码错误");
				}
				break;
			case 11:
				Toastshow("登录请求失败！");
				yDialog.dismiss();
				break;
			}
		}

	};

	class downLoad extends Thread {
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			Looper.prepare();

			Http_PushTask HP = new Http_PushTask();
			JSONObject js;
			try {
				String result = HP.execute(
						"",
						"http://cnyssj.net/ykdconfig/?" + "username="
								+ userName + "&" + "password=" + password).get(
										10000, TimeUnit.MILLISECONDS);
				// onToast(result);
				if (!TextUtils.isEmpty(result)) {
					js = new JSONObject(result);
					if (js.getBoolean("success")) {
						su.setPrefString(LoginActivity.this, "merchantId",
								js.getString("merchantId"));
						su.setPrefString(LoginActivity.this, "key",
								js.getString("key"));
						// 商户名称
						su.setPrefString(LoginActivity.this, "cashiername",
								js.getString("merchantName"));
						su.setPrefString(LoginActivity.this, "clientId",
								js.getString("deviceNumber"));

					} else {
						su.setPrefString(LoginActivity.this, "merchantId", "");
						su.setPrefString(LoginActivity.this, "key", "");
						// 商户名称
						su.setPrefString(LoginActivity.this, "cashiername", "");
						su.setPrefString(LoginActivity.this, "clientId", "");
						error_msg = js.getString("error_msg");
						showmessage(6);
					}
				} else {
					yDialog.dismiss();
					Toastshow("商户信息获取失败！");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				Toastshow("获取商户号失败，请检查网络连接后重试!");
				e.printStackTrace();
				return;
			}

		}
	}

	class download_userInfo extends Thread {
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Looper.prepare();

			Http_PushTask HP = new Http_PushTask();
			JSONObject js;
			try {
				Map<String, String> param = new HashMap<String, String>();// 组装请求参数
				param.put("uid",
						su.getPrefString(LoginActivity.this, "uid", null));
				String result = HP.execute(CryptTool.transMapToString(param),
						"http://cnyssj.com/dfweb/sys/user/getUserInfo").get(
								10000, TimeUnit.MILLISECONDS);

				if (!TextUtils.isEmpty(result)) {
					js = new JSONObject(result);
					js.getInt("status");
					// 完善资料状态
					su.setPrefString(LoginActivity.this,
							"registeredUnionpayMerchant", String.valueOf(js
									.getInt("registeredUnionpayMerchant")));

					if (js.getInt("registeredUnionpayMerchant") == 1
							|| js.getInt("registeredUnionpayMerchant") == 2) {
						merchant_information_off = true;
						su.setPrefboolean(LoginActivity.this,
								"merchant_information_off",
								merchant_information_off);
					} else {
						merchant_information_off = false;
						su.setPrefboolean(LoginActivity.this,
								"merchant_information_off",
								merchant_information_off);
					}
					su.setPrefString(LoginActivity.this, "balance",
							String.valueOf(js.getDouble("balance")));

					// 商户资料信息
					Iinformation info = new Iinformation();
					info.Merchant(LoginActivity.this,
							js.getString("idCardName"),// 商户姓名
							js.getString("idCardNumber"),// 身份证号
							js.getString("bankCardNumber"), // 银行卡号
							"", js.getString("companyName"),// 公司名称
							"", "", "", js.getString("mobileNumber"));// 手机号
					// 商户资质图片信息
					Map<String, String> fileMap = new HashMap<String, String>();
					fileMap.put("holding_id_card_photo",
							js.getString("idCardImgUrl"));// 手持身份证正面
					fileMap.put("id_card_front_img",
							js.getString("idCardFrontImgUrl"));// 身份证正面
					fileMap.put("id_card_back_img",
							js.getString("idCardBackImgUrl"));// 身份证反面
					fileMap.put("bank_card_front_img",
							js.getString("bankFrontImgUrl"));// 银行卡正面
					fileMap.put("bank_card_back_img",
							js.getString("bankBackImgUrl"));// 银行卡反面
					fileMap.put("driver_license_img",
							js.getString("driverLicenseImgUrl"));// 驾驶证
					info.Merchant_Img_String(LoginActivity.this, fileMap);
					// 登录
					showmessage(1);
				} else {
					yDialog.dismiss();
					Toastshow("商户资料信息获取失败！");
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				Toastshow("获取商户资料信息失败，请检查网络连接后重试!");
				e.printStackTrace();
				return;
			} finally {

			}
		}
	}

	public void Toastshow(String str) {
		Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
	}

}
