package com.example.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.NetWork.util.CTelephoneInfo;
import com.NetWork.util.SysApplication;
import com.basewin.services.ServiceManager;
import com.bestpay.cn.utils.CryptTool;
import com.bestpay.cn.utils.Manager_num;
import com.bestpay.cn.utils.Networkstate;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.bestpay.cn.utils.Version_util;
import com.bestpay.cn.utils.ViewPagerAdapter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jhj.Agreement.ZYB.Http_Pay_ZYB;
import com.jhj.Agreement.ZYB.Http_Revoke_ZYB;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.SetUpActivity.JieSuan_Information_Activity;
import com.jhj.SetUpActivity.ShangHu_Information_Activity;
import com.jhj.network.Http_PushTask;
import com.jhj.servrce.UpdateService;
import com.jhjpay.zyb.R;

public class MainActivity extends Activity{
	private final static int SCANNIN_GREQUEST_CODE = 2;
	Button mButton, query, refund, revoke, dia_Scancode_qd, dia_Scancode_qx;
	static String monery = Integer.toString(0);
	double monery_r;
	//

	TextView tx1, tx2, tx3, tx4, tx5, tx6, tx7, tx8, tx9, tx0;

	static TextView tx_amt, num_ok, tx00, tx_more, qrcode_but;
	LinearLayout num_back;
	ImageView setting, bt_QR_code;
	// 设备终端号，收银员名称，商户号
	public static String clientId, cashiername, merchantId;
	SharedPreferences_util su = new SharedPreferences_util();
	Http_Pay_ZYB hp = new Http_Pay_ZYB();
	String data_fdm, dz_mode, Str_money;
	// 弹出框
	Dialog dia_Scancode;
	Window w;
	WindowManager.LayoutParams lp;
	EditText dia_ed_Scancode;
	Http_Revoke_ZYB HR = new Http_Revoke_ZYB();
	// 升级相关
	String result;
	boolean force_update;
	String url;
	String description;
	Button UP_data_qx, UP_data_qd;
	Dialog dia_update;
	TextView et_UP_data;
	Version_util Vn = new Version_util();

	JSONObject js;
	String key;
	String error_msg;
	String Str_money_num, manager_num;
	Manager_num mn = new Manager_num();
	// 声音
	public static SoundPool soundPool;
	JSONObject printJson = new JSONObject();

	private ViewPager mViewPager;
	private ViewPagerAdapter mAdapter;
	private ArrayList<View> pageview;
	private Button quick,scancode,nfc,scanning_gun,swingcard;
	private ImageButton toleft,toright;

	private PopupWindow pop;
	//激活码
	private Dialog code_dg,data_dg,settlement_dg,qr_code_dg,login_dg;
	private ImageButton img_codesao;
	private EditText write_code;
	private Button code_determine,code_cancel;
	private String code_write;
	private Boolean write_code_off ,merchant_information_off,settlement_information_off,login_off;
	private String uid; 
	private YWLoadingDialog mDialog;
	String message;
	//银联支付链接，收款人
	String qr_code,receName;
	ImageView img_qr_code;
	Button bt_webview;
	TextView tv_receName;
	private int QR_WIDTH = 300;
	private int QR_HEIGHT = 300;
	private String merchant;//商户资料集合

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		SysApplication.getInstance().addActivity(this);
		monery = Integer.toString(0);
		data_fdm = su.getPrefString(MainActivity.this, "fdm", null);
		dz_mode = su.getPrefString(MainActivity.this, "mode", null);
		clientId = su.getPrefString(MainActivity.this, "clientId", null);
		merchantId = su.getPrefString(MainActivity.this, "merchantId", null);
		cashiername = su.getPrefString(MainActivity.this, "cashiername", null);
		Str_money = su.getPrefString(MainActivity.this, "money", null);
		//激活码
		write_code_off = su.getPrefboolean(MainActivity.this, "write_code_off", false);
		//商户资料信息
		merchant_information_off = su.getPrefboolean(MainActivity.this, "merchant_information_off", false);
		//用户id
		uid = su.getPrefString(MainActivity.this, "uid", null);
		// 商户信息集合
		merchant = su.getPrefString(MainActivity.this, "merchant", null);
		//判断是否已登录
		login_off = su.getPrefboolean(MainActivity.this, "login_off", false);

		// 弹出框
		dia_Scancode = new Dialog(MainActivity.this,
				R.style.edit_AlertDialog_style);
		dia_Scancode.setContentView(R.layout.activity_start_dialog_scancode);

		setContentView(R.layout.keyboadview2);
		MyAppLication.getInstance().addActivity(this);
		mDialog = new YWLoadingDialog(MainActivity.this);
		initData();//初始化数据

		if(login_off == true){

			if(merchant_information_off == false){
				showInformationDialog(); 
			}
		}
		tx1 = (TextView) findViewById(R.id.num1);
		tx2 = (TextView) findViewById(R.id.num2);
		tx3 = (TextView) findViewById(R.id.num3);
		tx4 = (TextView) findViewById(R.id.num4);
		tx5 = (TextView) findViewById(R.id.num5);
		tx6 = (TextView) findViewById(R.id.num6);
		tx7 = (TextView) findViewById(R.id.num7);
		tx8 = (TextView) findViewById(R.id.num8);
		tx9 = (TextView) findViewById(R.id.num9);
		tx0 = (TextView) findViewById(R.id.num0);
		tx00 = (TextView) findViewById(R.id.num00);


		dia_Scancode_qd = (Button) dia_Scancode.findViewById(R.id.Scancode_qd);
		dia_Scancode_qx = (Button) dia_Scancode.findViewById(R.id.Scancode_qx);

		dia_ed_Scancode = (EditText) dia_Scancode
				.findViewById(R.id.et_Scancode);

		num_back = (LinearLayout) findViewById(R.id.num_back);
		setting = (ImageView) findViewById(R.id.setting);
		bt_QR_code = (ImageView) findViewById(R.id.bt_QR_code);
		tx_amt = (TextView) findViewById(R.id.tx_amt);

		tx1.setOnClickListener(Onclick);
		tx2.setOnClickListener(Onclick);
		tx3.setOnClickListener(Onclick);
		tx4.setOnClickListener(Onclick);
		tx5.setOnClickListener(Onclick);
		tx6.setOnClickListener(Onclick);
		tx7.setOnClickListener(Onclick);
		tx8.setOnClickListener(Onclick);
		tx9.setOnClickListener(Onclick);
		tx0.setOnClickListener(Onclick);
		tx00.setOnClickListener(Onclick);
		num_back.setOnClickListener(Onclick);
		setting.setOnClickListener(myonclick);
		bt_QR_code.setOnClickListener(myonclick);

		dia_Scancode_qd.setOnClickListener(myonclick);
		dia_Scancode_qx.setOnClickListener(myonclick);
		dia_ed_Scancode.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					result();
					return true;
				}
				return false;
			}
		});
		dia_ed_Scancode.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

				}
				return true;
			}
		});
		dia_update = new Dialog(MainActivity.this,
				R.style.edit_AlertDialog_style);
		dia_update.setContentView(R.layout.activity_start_dialog_update);
		et_UP_data = (TextView) dia_update.findViewById(R.id.et_UP_data);
		UP_data_qx = (Button) dia_update.findViewById(R.id.UP_data_qx);
		UP_data_qd = (Button) dia_update.findViewById(R.id.UP_data_qd);
		UP_data_qx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!force_update) {
					dia_update.cancel();
				} else {
					toast("大哥，新版本可美了，你得升级啊！");
				}
			}
		});
		UP_data_qd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dia_update.cancel();
				// 跳转service下载程序
				Intent intent = new Intent(MainActivity.this,
						UpdateService.class);
				intent.putExtra("Key_App_Name", "翼开店收银台");
				intent.putExtra("Key_Down_Url", url);
				startService(intent);
			}
		});
		showmessage(1);
		//
		// 商户号
		if (Networkstate.isNetworkAvailable(MainActivity.this)) {
			if (su.getPrefString(MainActivity.this, "merchantId", null) == null
					|| su.getPrefString(MainActivity.this, "key", null) == null
					|| su.getPrefString(MainActivity.this, "clientId", null) == null) {
				// 获取配置文档

				if (Build.MANUFACTURER.contains("BASEWIN")
						|| Build.MANUFACTURER.contains("basewin")) {

					try {
						manager_num = ServiceManager.getInstence()
								.getDeviceinfo().getSN();
						showmessage(4);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					CTelephoneInfo telephonyInfo = CTelephoneInfo
							.getInstance(this);
					telephonyInfo.setCTelephoneInfo();
					manager_num = telephonyInfo.getImeiSIM1();
					if (manager_num == null) {
						toast("未能识别设备!");
					} else {
						showmessage(4);
					}
				}
			}
		} else {
			toast("无可用网络！无法获取配置信息！");
		}

		soundPool = new SoundPool(100, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.paymentsuccess, 1);
		// 赋值
	}
	/**
	 * 初始化激活码输入框
	 */
	public void showCodeDialog(){
		code_dg = new Dialog(MainActivity.this, R.style.edit_AlertDialog_style);
		code_dg.setContentView(R.layout.activity_dialog_activation_code);

		img_codesao=(ImageButton) code_dg.findViewById(R.id.img_codesao);
		write_code=(EditText) code_dg.findViewById(R.id.write_code);
		code_determine = (Button) code_dg.findViewById(R.id.code_determine);
		code_cancel = (Button) code_dg.findViewById(R.id.code_cancel);

		img_codesao.setOnClickListener(Onclick);
		code_determine.setOnClickListener(Onclick);
		code_cancel.setOnClickListener(Onclick);

		code_dg.setCanceledOnTouchOutside(false);
		code_dg.show();
	}

	/**
	 * 初始化商户信息提示框
	 */
	public void showInformationDialog(){
		data_dg = new Dialog(MainActivity.this, R.style.edit_AlertDialog_style);
		data_dg.setContentView(R.layout.activity_dialog_data_information);

		TextView cancel = (TextView) data_dg.findViewById(R.id.datacancel);
		TextView perfect = (TextView) data_dg.findViewById(R.id.dataperfect);
		cancel.setOnClickListener(Onclick);
		perfect.setOnClickListener(Onclick);

		data_dg.setCanceledOnTouchOutside(false);
		data_dg.show();
	}
	/**
	 * 初始化登录提示框
	 */
	public void showLoginDialog(){
		login_dg = new Dialog(MainActivity.this, R.style.edit_AlertDialog_style);
		login_dg.setContentView(R.layout.activity_dialog_login);

		TextView logincancel = (TextView) login_dg.findViewById(R.id.logincancel);
		TextView loginperfect = (TextView) login_dg.findViewById(R.id.loginperfect);

		logincancel.setOnClickListener(Onclick);
		loginperfect.setOnClickListener(Onclick);

		login_dg.setCanceledOnTouchOutside(false);
		login_dg.show();
	}
	/**
	 * 初始化结算信息提示框
	 */
	public void showSettLementDialog(){
		settlement_dg = new Dialog(MainActivity.this, R.style.edit_AlertDialog_style);
		settlement_dg.setContentView(R.layout.activity_dialog_settlementinformation);

		TextView cancel = (TextView) settlement_dg.findViewById(R.id.settlement_cancel);
		TextView perfect = (TextView) settlement_dg.findViewById(R.id.settlement_perfect);
		cancel.setOnClickListener(Onclick);
		perfect.setOnClickListener(Onclick);

		settlement_dg.setCanceledOnTouchOutside(false);
		settlement_dg.show();
	}
	/**
	 * 初始化快捷支付二维码提示框
	 */
	public void showQuickQr_CodeDialog(){
		qr_code_dg = new Dialog(MainActivity.this, R.style.edit_AlertDialog_style);
		qr_code_dg.setContentView(R.layout.activity_quickpay_qr_code);

		img_qr_code = (ImageView) qr_code_dg.findViewById(R.id.img_qr_code);
		bt_webview = (Button) qr_code_dg.findViewById(R.id.bt_webview);
		tv_receName = (TextView) qr_code_dg.findViewById(R.id.tv_receName);
		bt_webview.setOnClickListener(myonclick);
		qr_code_dg.setCanceledOnTouchOutside(true);
	}

	/**
	 * 初始化PoPupWindow弹框
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showPoPupWindow(){
		LayoutInflater inflater = LayoutInflater.from(this);  
		// 引入窗口配置文件  
		View view = inflater.inflate(R.layout.activity_pay_popupwindow, null);  
		// 创建PopupWindow对象  
		pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);  
		// 需要设置一下此参数，点击外边可消失  
		pop.setBackgroundDrawable(new BitmapDrawable());  
		//设置点击窗口外边窗口消失  
		pop.setOutsideTouchable(true);  
		// 设置此参数获得焦点，否则无法点击  
		pop.setFocusable(true);

		LinearLayout cashier = (LinearLayout) view.findViewById(R.id.cashier);
		LinearLayout collection_code = (LinearLayout) view.findViewById(R.id.collection_code);

		cashier.setOnClickListener(myonclick);
		collection_code.setOnClickListener(myonclick);

		// 显示窗口  
		pop.showAsDropDown(bt_QR_code);
	}

	/**
	 * 初始化数据
	 */
	@SuppressLint("InflateParams")
	private void initData() {
		toleft=(ImageButton) findViewById(R.id.toleft);
		toright=(ImageButton) findViewById(R.id.toright);
		toleft.setOnClickListener(Onclick);
		toright.setOnClickListener(Onclick);

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		//查找布局文件用LayoutInflater.inflate
		LayoutInflater inflater =this.getLayoutInflater();
		View view1 = inflater.inflate(R.layout.activity_receivables_quick, null);
		View view2 = inflater.inflate(R.layout.activity_receivables_scancode, null);
		//		View view3 = inflater.inflate(R.layout.activity_receivables_nfc, null);
		//		View view4 = inflater.inflate(R.layout.activity_receivables_scanninggun, null);
		//		View view5 = inflater.inflate(R.layout.activity_receivables_swingcard, null);

		quick=(Button) view1.findViewById(R.id.quick);//快捷收款
		scancode=(Button) view2.findViewById(R.id.scancode);//扫码收款
		//		nfc=(Button) view3.findViewById(R.id.nfc);//nfc收款
		//		scanning_gun=(Button) view4.findViewById(R.id.scanning_gun);//扫码枪收款
		//		swingcard=(Button) view5.findViewById(R.id.swingcard);//刷卡收款

		quick.setOnClickListener(myonclick);
		scancode.setOnClickListener(myonclick);
		//		nfc.setOnClickListener(myonclick);
		//		scanning_gun.setOnClickListener(myonclick);
		//		swingcard.setOnClickListener(myonclick);


		//将view装入数组
		pageview =new ArrayList<View>();
		pageview.add(view1);
		pageview.add(view2);
		//		pageview.add(view3);
		//		pageview.add(view4);
		//		pageview.add(view5);
		mAdapter = new ViewPagerAdapter(this, pageview);
		mViewPager.setAdapter(mAdapter);

		// 将ViewPager定位到中间页（Short.MAX_VALUE/2附近的图片资源数组第1个元素对应的页面）
		// 目的：1.图片个数 >1 才轮播 2.定位到中间页，向左向右都可滑
		if( pageview.size()> 1) {
			mViewPager.setCurrentItem(0, false);
			//			mViewPager.setCurrentItem(((Short.MAX_VALUE / 2) / pageview.size()) * pageview.size(), false);
		}
	}

	@Override
	protected void onStart() {
		money_nm();
		super.onStart();
	}
	/**
	 * 判断是否输入激活码及完善商户信息
	 */
	public boolean isPerfect(){
		boolean isPerfect = true;
		if(login_off == false){
			showLoginDialog();
			isPerfect=false;
		}else if(merchant_information_off == false){
			showInformationDialog(); 
			isPerfect=false;
		}
		return isPerfect;
	}
	/**
	 * 获取商户姓名
	 */
	public void getUsetInfo(){
		try {
			if (!TextUtils.isEmpty(merchant)) {
				JSONObject jsonObject = new JSONObject(merchant);
				receName = jsonObject.getString("idCardName");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	View.OnClickListener Onclick = new OnClickListener() {

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.toleft:	
				mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1, true);
				break;
			case R.id.toright:	
				mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
				break;
			case R.id.num00:
				settextnum("00");
				break;
			case R.id.num0:
				settextnum("0");
				break;
			case R.id.num1:
				settextnum("1");
				break;
			case R.id.num2:
				settextnum("2");
				break;
			case R.id.num3:
				settextnum("3");
				break;
			case R.id.num4:
				settextnum("4");
				break;
			case R.id.num5:
				settextnum("5");
				break;
			case R.id.num6:
				settextnum("6");
				break;
			case R.id.num7:
				settextnum("7");
				break;
			case R.id.num8:
				settextnum("8");
				break;
			case R.id.num9:
				settextnum("9");
				break;
			case R.id.num_back:
				backTextNum();
				break;
			case R.id.img_codesao://激活码-扫一扫
				toast("暂不支持使用！");
				break;
			case R.id.code_determine://激活码-确定
				if(!TextUtils.isEmpty(write_code.getText())){
					code_write=write_code.getText().toString().trim();
					if(code_write.equals("001")){
						write_code_off=true;
						su.setPrefboolean(MainActivity.this, "write_code_off", write_code_off);
						code_dg.cancel();
					}else{
						toast("激活码错误");
						write_code_off=false;
						su.setPrefboolean(MainActivity.this, "write_code_off", write_code_off);
					}
				}else{
					toast("请输入激活码");
				}
				break;	
			case R.id.code_cancel://激活码-取消
				code_dg.cancel();
				break;
			case R.id.datacancel://资料信息-取消
				data_dg.cancel();
				break;
			case R.id.dataperfect://完善商户资料信息
				Intent dataintent = new Intent(MainActivity.this,ShangHu_Information_Activity.class);
				startActivity(dataintent);
				data_dg.cancel();
				break;
			case R.id.settlement_cancel://结算信息-取消
				settlement_dg.cancel();
				break;
			case R.id.settlement_perfect://完善结算信息
				Intent setintent = new Intent(MainActivity.this,JieSuan_Information_Activity.class);
				startActivity(setintent);
				settlement_dg.cancel();
				break;
			case R.id.logincancel://
				login_dg.cancel();
				break;
			case R.id.loginperfect://登录
				Intent loginintent = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(loginintent);
				login_dg.cancel();
				MainActivity.this.finish();
				break;	
			default:
				break;
			}
		}

	};
	View.OnClickListener myonclick = new OnClickListener() {
		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			// 商户信息集合
			merchant = su.getPrefString(MainActivity.this, "merchant", null);
			//商户资料信息
			merchant_information_off = su.getPrefboolean(MainActivity.this, "merchant_information_off", false);
			//激活码
			write_code_off = su.getPrefboolean(MainActivity.this, "write_code_off", false);
			//结算信息
			settlement_information_off = su.getPrefboolean(MainActivity.this, "settlement_information_off", false);
			//判断是否已登录
			login_off = su.getPrefboolean(MainActivity.this, "login_off", false);
			
			if(!isPerfect()){
				return;
			}
			switch (v.getId()) {
			case R.id.scancode://扫码收款
				
				if (Networkstate.isNetworkAvailable(MainActivity.this)) {
					if (!TextUtils.isEmpty(monery)
							&& Integer.parseInt(monery) > 0) {

						if (!TextUtils.isEmpty(clientId)) {

							if (!TextUtils.isEmpty(dz_mode)) {
								dia_Scancode.show();
								dia_Scancode.setCanceledOnTouchOutside(false);
								w = dia_Scancode.getWindow();
								lp = w.getAttributes();
								lp.x = 0;
								lp.y = 40;
								dia_Scancode.onWindowAttributesChanged(lp);
							} else {
								Intent intent = new Intent();
								intent.setClass(MainActivity.this,
										MipcaActivityCapture.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								getParent().startActivityForResult(intent,
										SCANNIN_GREQUEST_CODE);
								su.setPrefString(getApplicationContext(),
										"monery", monery);
							}

						} else {
							toast("请在设置中设置正确的设备终端号！");
						}
					} else {
						Toast.makeText(MainActivity.this, "请输入正确的金额！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					toast("无可用网络！请连接网络后重试");
				}
				break;
			case R.id.quick://快捷收款
				getUsetInfo();
				if(!TextUtils.isEmpty(monery)
						&& Integer.parseInt(monery) > 0){
					if(Integer.parseInt(monery) < 300){
						Toast.makeText(MainActivity.this, "金额不得低于300元！",
								Toast.LENGTH_SHORT).show();
						return;
					}
					mDialog.show();
					mDialog.setCanceledOnTouchOutside(false);
					(new quickPay()).start();

				}else {
					Toast.makeText(MainActivity.this, "请输入正确的金额！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.bt_webview://打开快捷支付网页
				Intent intent = new Intent(MainActivity.this,QuickPayActivity.class);
				intent.putExtra("qr_code", qr_code);
				startActivity(intent);
				qr_code_dg.dismiss();
				QcTextNum();
				break;
			case R.id.nfc:
				toast("暂不支持使用");
				break;
			case R.id.scanning_gun://扫码枪收款
				toast("暂不支持使用");
				break;
			case R.id.swingcard: //刷卡收款
				// print();
				break;
			case R.id.setting:
				QcTextNum();
				Intent intent1 = new Intent();
				intent1.setClass(MainActivity.this, SettingActivity.class);
				startActivity(intent1);
				finish();
				break;
			case R.id.Scancode_qd:
				result();
				break;
			case R.id.Scancode_qx:
				dia_Scancode.cancel();// 关闭弹出框
				dia_ed_Scancode.setText("");
				break;
			case R.id.bt_QR_code:
				QcTextNum();
				// 显示窗口  
				showPoPupWindow(); 
				break;
			case R.id.cashier://收银台
				toast("暂不支持使用");
				//Intent caintent = new Intent(MainActivity.this,Cashier_Activity.class);
				//startActivity(caintent);
				pop.dismiss();
				break;
			case R.id.collection_code://收款码	
				Intent inten = new Intent();
				inten.setClass(MainActivity.this, QRcode_Activity.class);
				startActivity(inten);
				pop.dismiss();
				break;
			
			default:
				break;
			}
		}
	};

	public void settextnum(String num) {
		if (Integer.parseInt(num) != 0) {
			monery = Integer.toString((int) Double.parseDouble(monery + num));
			monery_r = Double.parseDouble(monery);
			tx_amt.setText(Double.toString(Double.parseDouble(monery) / 100));
		} else {
			if (Integer.parseInt(monery) != 0) {
				monery = Integer.toString((int) Double
						.parseDouble(monery + num));
				tx_amt.setText(Double.toString(Double.parseDouble(monery) / 100));
			} else {
				tx_amt.setText("0");
			}
		}

	}

	public void backTextNum() {
		try {
			if (Integer.parseInt(monery) != 0) {
				monery = monery.substring(0, monery.length() - 1);

				if (Integer.parseInt(monery) != 0) {
					tx_amt.setText(Double.toString(Double.parseDouble(monery) / 100));
				} else {
					tx_amt.setText("0");
				}

			} else {
				tx_amt.setText("0");
			}
		} catch (NumberFormatException e) {
			tx_amt.setText("0");
			monery = "0";
		}

	}

	public static void QcTextNum() {
		tx_amt.setText("0");
		monery = "0";
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			SysApplication.getInstance().exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void result() {
		String result_r = dia_ed_Scancode.getText().toString();
		if(TextUtils.isEmpty(result_r)){
			return;
		}
		int result = Integer.parseInt(result_r.substring(0, 2));
		if (result == 13) {
			// 微信
			hp.runService(Integer.parseInt(monery), MainActivity.this,
					result_r, cashiername, clientId, merchantId, 4, "微信");

		} else if (result == 51) {
			// 翼支付
			hp.runService(Integer.parseInt(monery), MainActivity.this,
					result_r, cashiername, clientId, merchantId, 13, "翼支付");

		} else if (result == 28) {
			toast("请使用翼支付付款！");
			// 支付宝
			hp.runService(Integer.parseInt(monery), MainActivity.this,
					result_r, cashiername, clientId, merchantId, 1, "支付宝");

		} else {
			toast("无效二维码数据！");
		}
		dia_ed_Scancode.setText("");
		dia_Scancode.cancel();
		money_nm();
	}

	// 发送handle
	public void showmessage(int message) {
		Message localMessage = new Message();
		localMessage.what = message;
		this.handler.sendMessage(localMessage);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				if (Networkstate.isNetworkAvailable(MainActivity.this)) {
					(new UpdateVersion()).start();
				} else {
					toast("无可用网络！无法获取版本信息");
				}
				break;
			case 2:
				// toast("版本一致，无需升级！");
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
				(new downLoad()).start();
				break;
			case 5:
				toast("数据获取失败！");
				break;
			case 6:
				toast(error_msg);
				break;
			case 7:
				mDialog.dismiss();
				//快捷支付弹框
				showQuickQr_CodeDialog();
				final Bitmap scanbitmap = createQRImage(qr_code);
				img_qr_code.setImageBitmap(GetRoundedCornerBitmap(scanbitmap));
				tv_receName.setText("收款人:"+receName);
				qr_code_dg.show();
				break;
			case 8:
				mDialog.dismiss();
				toast(error_msg);
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
								+ Vn.getAppInfo(MainActivity.this)).get();
				JSONObject js = new JSONObject(result);
				if (!TextUtils.isEmpty(result)) {
					js.getString("version");
					String version = Vn.getVersionName(MainActivity.this);

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
						su.setPrefString(MainActivity.this, "merchantId",
								js.getString("merchantId"));
						su.setPrefString(MainActivity.this, "key",
								js.getString("key"));
						// 商户名称
						su.setPrefString(MainActivity.this, "cashiername",
								js.getString("merchantName"));
						su.setPrefString(MainActivity.this, "clientId",
								js.getString("deviceNumber"));

					} else {
						error_msg = js.getString("error_msg");
						showmessage(6);
					}
				} else {
					toast("数据获取失败！");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				toast("获取商户号失败，请检查网络连接后重试!");
				e.printStackTrace();
				return;
			}

		}
	}

	class quickPay extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Looper.prepare();
			try {
				Map<String, String> quickMap = new HashMap<String, String>();
				quickMap.put("uid", uid);
				quickMap.put("totalFee", monery);
				Http_PushTask HP = new Http_PushTask();
				String result = HP.execute(CryptTool.transMapToString(quickMap),
						"http://cnyssj.com/dfweb/sys/payment/qrCodePay").get(10000,TimeUnit.MILLISECONDS);

				if (!TextUtils.isEmpty(result)) {
					JSONObject js = new JSONObject(result);
					if (js.getBoolean("success")) {
						qr_code = js.getJSONObject("value").getJSONObject("data").getString("qr_code");
						showmessage(7);
					} else {
						error_msg = js.getString("messageText");
						showmessage(8);
					}
				} else {
					toast("获取数据失败！");
					mDialog.dismiss();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (TimeoutException e) {
				toast("获取支付链接超时，请检查网络！");
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("static-access")
	public void money_nm() {
		monery = "";
		Str_money_num = su.getPrefString(MainActivity.this, "Str_money_num",
				"1000.0");
		if (!TextUtils.isEmpty(Str_money)) {
			if (!TextUtils.isEmpty(Str_money_num)) {
				// String money_z = Double.toString((int) Double
				// .parseDouble(Str_money_num) * 100);
				settextnum(Str_money_num.substring(0,
						Str_money_num.length() - 2));
			}
		} else {
			QcTextNum();
		}
	}

	// 生成二维码图片
	public Bitmap createQRImage(String url) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];

			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// 生成圆角图片
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			final float roundPx = 15;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}
	public void toast(String str) {
		Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
	}

}
