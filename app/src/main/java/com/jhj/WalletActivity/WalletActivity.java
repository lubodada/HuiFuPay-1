package com.jhj.WalletActivity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.LoginActivity;
import com.example.Main.MyAppLication;
import com.jhj.Dialog.BigHeadImage_Dialog;
import com.jhj.SetUpActivity.JieSuan_Information_Activity;
import com.jhj.SetUpActivity.SettingUpActivity;
import com.jhj.SetUpActivity.ShangHu_Information_Activity;
import com.jhj.info_util.ImageUtil;
import com.jhjpay.zyb.R;

/**
 *  底部导航——钱包页面
 * @author Administrator
 *
 */
public class WalletActivity extends Activity {

	private ImageView img_head_portrait;
	private TextView tv_name,tv_phone_number,tv_balance,profit_yesterday,profit_cumulative;
	private LinearLayout payment,balance,transfer_accounts;
	private RelativeLayout re_wallet_details,re_banknote,re_chongzhi,re_interest,re_jiesuan,
	re_bubble,re_gonggao,re_rate,re_setting;
	SharedPreferences_util su = new SharedPreferences_util();
	//激活码,完善信息
	private Dialog code_dg,settlement_dg,data_dg,login_dg;
	private ImageButton img_codesao;
	private EditText write_code;
	private Button determine,cancel;
	private String code_write;
	private Boolean write_code_off,settlement_information_off,merchant_information_off,login_off;

	protected static final int TAKE_PICTURE = 0;
	protected static final int CHOOSE_PICTURE = 1;		
	protected static final int VIEW_BIG_PICTURE = 2;
	protected static Uri tempUri;

	private	PopupWindow pw;
	private static final String TAG = "WalletActivity";
	private int param = 1;
	BigHeadImage_Dialog myImageDialog;//查看头像大图
	View loadingview;
	ImageView imageview_head_big;
	private String userName,str_balance,str_profit_yesterday,str_profit_cumulative;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 商户信息集合
		userName = su.getPrefString(WalletActivity.this, "userName", null);
		str_balance = su.getPrefString(WalletActivity.this, "balance", null);
		login_off = su.getPrefboolean(WalletActivity.this, "login_off", false);
		setContentView(R.layout.activity_wallet);
		MyAppLication.getInstance().addActivity(this);
		initView();
		setImg();
	}
	/**
	 * 初始化控件
	 */
	public void initView(){
		img_head_portrait = (ImageView) findViewById(R.id.img_head_portrait);//头像
		tv_name = (TextView) findViewById(R.id.tv_name);//姓名
		tv_phone_number=(TextView) findViewById(R.id.tv_phone_number);//手机号
		tv_balance = (TextView) findViewById(R.id.tv_balance);//余额
		profit_yesterday = (TextView) findViewById(R.id.profit_yesterday);//昨日收益
		profit_cumulative = (TextView) findViewById(R.id.profit_cumulative);//累计收益
		payment = (LinearLayout) findViewById(R.id.payment);//付款
		balance = (LinearLayout) findViewById(R.id.balance);//余额
		transfer_accounts = (LinearLayout) findViewById(R.id.transfer_accounts);//转账
		re_wallet_details = (RelativeLayout) findViewById(R.id.re_wallet_details);//钱包明细
		re_banknote = (RelativeLayout) findViewById(R.id.re_banknote);//提现
		re_chongzhi = (RelativeLayout) findViewById(R.id.re_chongzhi);//充值
		re_interest = (RelativeLayout) findViewById(R.id.re_interest);//利息收益
		re_jiesuan = (RelativeLayout) findViewById(R.id.re_jiesuan);//结算
		re_bubble = (RelativeLayout) findViewById(R.id.re_bubble);//消息中心
		re_gonggao = (RelativeLayout) findViewById(R.id.re_gonggao);//公告
		re_rate = (RelativeLayout) findViewById(R.id.re_rate);//费率
		re_setting = (RelativeLayout) findViewById(R.id.re_setting);//设置

		img_head_portrait.setOnClickListener(walletListener);
		re_wallet_details.setOnClickListener(walletListener);
		profit_yesterday.setOnClickListener(walletListener);
		profit_cumulative.setOnClickListener(walletListener);
		payment.setOnClickListener(walletListener);
		balance.setOnClickListener(walletListener);
		transfer_accounts.setOnClickListener(walletListener);
		re_banknote.setOnClickListener(walletListener);
		re_chongzhi.setOnClickListener(walletListener);
		re_interest.setOnClickListener(walletListener);
		re_jiesuan.setOnClickListener(walletListener);
		re_bubble.setOnClickListener(walletListener);
		re_gonggao.setOnClickListener(walletListener);
		re_rate.setOnClickListener(walletListener);
		re_setting.setOnClickListener(walletListener);

		if(login_off){
			tv_phone_number.setText("欢迎您："+userName);
		}else{
			tv_phone_number.setText("欢迎您");
		}
		if(!TextUtils.isEmpty(str_balance)){
			tv_balance.setText(str_balance);
		}
		if(!TextUtils.isEmpty(str_profit_yesterday)){
			profit_yesterday.setText(str_profit_yesterday);
		}
		if(!TextUtils.isEmpty(str_profit_cumulative)){
			profit_cumulative.setText(str_profit_cumulative);
		}
	}
	/**
	 * 初始化激活码输入框
	 */
	public void showCodeDialog(){
		code_dg = new Dialog(WalletActivity.this, R.style.edit_AlertDialog_style);
		code_dg.setContentView(R.layout.activity_dialog_activation_code);

		img_codesao=(ImageButton) code_dg.findViewById(R.id.img_codesao);
		write_code=(EditText) code_dg.findViewById(R.id.write_code);
		determine = (Button) code_dg.findViewById(R.id.code_determine);
		cancel = (Button) code_dg.findViewById(R.id.code_cancel);

		img_codesao.setOnClickListener(Listener);
		determine.setOnClickListener(Listener);
		cancel.setOnClickListener(Listener);

		code_dg.setCanceledOnTouchOutside(false);
		code_dg.show();
	}
	/**
	 * 初始化结算信息提示框
	 */
	public void showSettLementDialog(){
		settlement_dg = new Dialog(WalletActivity.this, R.style.edit_AlertDialog_style);
		settlement_dg.setContentView(R.layout.activity_dialog_settlementinformation);

		TextView cancel = (TextView) settlement_dg.findViewById(R.id.settlement_cancel);
		TextView perfect = (TextView) settlement_dg.findViewById(R.id.settlement_perfect);
		cancel.setOnClickListener(Listener);
		perfect.setOnClickListener(Listener);

		settlement_dg.setCanceledOnTouchOutside(false);
		settlement_dg.show();
	}
	/**
	 *  初始化商户信息提示框
	 */
	public void showInformationDialog(){
		data_dg = new Dialog(WalletActivity.this, R.style.edit_AlertDialog_style);
		data_dg.setContentView(R.layout.activity_dialog_data_information);

		TextView cancel = (TextView) data_dg.findViewById(R.id.datacancel);
		TextView perfect = (TextView) data_dg.findViewById(R.id.dataperfect);
		cancel.setOnClickListener(Listener);
		perfect.setOnClickListener(Listener);

		data_dg.setCanceledOnTouchOutside(false);
		data_dg.show();
	}
	/**
	 * 初始化登录提示框
	 */
	public void showLoginDialog(){
		login_dg = new Dialog(WalletActivity.this, R.style.edit_AlertDialog_style);
		login_dg.setContentView(R.layout.activity_dialog_login);

		TextView logincancel = (TextView) login_dg.findViewById(R.id.logincancel);
		TextView loginperfect = (TextView) login_dg.findViewById(R.id.loginperfect);

		logincancel.setOnClickListener(Listener);
		loginperfect.setOnClickListener(Listener);

		login_dg.setCanceledOnTouchOutside(false);
		login_dg.show();
	}
	/**
	 * 上传头像弹出框
	 * @param view
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showEditPhotoWindow(View view) {
		View contentView=getLayoutInflater().inflate(R.layout.popup_window_title_image, null);
		pw=new PopupWindow(contentView, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight(), true);
		//处理popupwindow
		popupwindowselectphoto(contentView);
		//设置点击弹窗外隐藏自身
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		//设置pipupwindow弹出动画
		pw.setAnimationStyle(R.style.popupwindow_anim_style);
		//设置popupwindow背景
		pw.setBackgroundDrawable(new ColorDrawable());
		pw.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);

	}
	//初始化控件和控件的点击事件
	private void popupwindowselectphoto(View contentView) {
		TextView tv_select_pic=(TextView) contentView.findViewById(R.id.tv_photo);
		TextView tv_pai_pic=(TextView) contentView.findViewById(R.id.tv_photograph);
		TextView tv_big_pic=(TextView) contentView.findViewById(R.id.tv_bigphoto);
		TextView tv_cancl=(TextView) contentView.findViewById(R.id.tv_cancle);
		LinearLayout layout=(LinearLayout) contentView.findViewById(R.id.dialog_ll);
		tv_select_pic.setOnClickListener(walletListener);
		tv_pai_pic.setOnClickListener(walletListener);
		tv_big_pic.setOnClickListener(walletListener);
		tv_cancl.setOnClickListener(walletListener);
		layout.setOnClickListener(walletListener);
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

	View.OnClickListener Listener = new View.OnClickListener() {

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.img_codesao:
				toast("暂不支持使用！");
				break;
			case R.id.code_determine://激活码-确定
				if(!TextUtils.isEmpty(write_code.getText())){
					code_write=write_code.getText().toString().trim();
					if(code_write.equals("001")){
						write_code_off=true;
						su.setPrefboolean(WalletActivity.this, "write_code_off", write_code_off);
						code_dg.cancel();
					}else{
						toast("激活码错误");
						write_code_off=false;
						su.setPrefboolean(WalletActivity.this, "write_code_off", write_code_off);
					}
				}else{
					toast("请输入激活码");
				}
				break;	
			case R.id.code_cancel://激活码-取消
				code_dg.cancel();
				break;		
			case R.id.settlement_cancel://结算信息-取消
				settlement_dg.cancel();
				break;
			case R.id.settlement_perfect://完善结算信息
				Intent setlintent = new Intent(WalletActivity.this,JieSuan_Information_Activity.class);
				setlintent.putExtra("type", "settlement");
				startActivity(setlintent);
				settlement_dg.cancel();
				break;	
			case R.id.datacancel://资料信息-取消
				data_dg.cancel();
				break;
			case R.id.dataperfect://完善商户资料信息
				Intent dataintent = new Intent(WalletActivity.this,ShangHu_Information_Activity.class);
				startActivity(dataintent);
				data_dg.cancel();
				break;	
			case R.id.logincancel:
				login_dg.cancel();
				break;
			case R.id.loginperfect:
				Intent loginintent = new Intent(WalletActivity.this,LoginActivity.class);
				startActivity(loginintent);
				login_dg.cancel();
				WalletActivity.this.finish();
				break;		

			default:
				break;
			}
		}

	};

	View.OnClickListener walletListener = new View.OnClickListener() {

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//激活码
			write_code_off = su.getPrefboolean(WalletActivity.this, "write_code_off", false);
			//结算信息
			settlement_information_off = su.getPrefboolean(WalletActivity.this, "settlement_information_off", false);
			//商户资料信息
			merchant_information_off = su.getPrefboolean(WalletActivity.this, "merchant_information_off", false);
			//判断是否已登录
			login_off = su.getPrefboolean(WalletActivity.this, "login_off", false);

			if(!isPerfect()){
				return;
			}
			switch (v.getId()) {
			case R.id.img_head_portrait://头像
				showEditPhotoWindow(v);
				break;
			case R.id.tv_photo://本地相册
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT    
				intent.addCategory(Intent.CATEGORY_OPENABLE);    
				intent.setType("image/*");    
				if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){                    
					getParent().startActivityForResult(intent, CHOOSE_PICTURE);      
				}else{                  
					getParent().startActivityForResult(intent, CHOOSE_PICTURE);     
				} 
				pw.dismiss();
				break;
			case R.id.tv_photograph://拍照
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				tempUri = Uri.fromFile(new File(
						Environment.getExternalStorageDirectory(), "img_head_portrait.jpg"));

				// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
				getParent().startActivityForResult(openCameraIntent, TAKE_PICTURE);
				pw.dismiss();
				break;
			case R.id.tv_bigphoto://查看大图
				String bigimgpath=su.getPrefString(WalletActivity.this, "bigimgPath", null);
				if(bigimgpath==null){
					toast("请上传头像！");
					pw.dismiss();
					return;
				}
				File file = new File(bigimgpath);
				if(!file.exists()){
					bigimgpath=su.getPrefString(WalletActivity.this, "imgPath", null);
				}
				Bitmap bitmap = ImageUtil.getBitmapByPath(bigimgpath);
				myImageDialog = new BigHeadImage_Dialog(WalletActivity.this,R.style.popupwindow_anim_style,0,-300,bitmap);  
				myImageDialog.show();  
				pw.dismiss();
				break;
			case R.id.tv_cancle://取消
				if(pw!=null){
					pw.dismiss();
				}
				break;
			case R.id.dialog_ll://点击提示框以外的地方关闭
				if(pw!=null){
					pw.dismiss();
				}
				break;	
			case R.id.re_wallet_details://钱包明细
				toast("暂不支持使用");
				//				Intent detintent = new Intent(WalletActivity.this,WalletDetailsActivity.class);
				//				startActivity(detintent);
				break;
			case R.id.payment://付款
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent payintent = new Intent(WalletActivity.this,PaymentActivity.class);
				//				startActivity(payintent);
				break;
			case R.id.balance://余额
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent balintent = new Intent(WalletActivity.this,BalanceActivity.class);
				//				startActivity(balintent); 
				break;
			case R.id.transfer_accounts://转账
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent traintent = new Intent(WalletActivity.this,Transfer_PayeeInfoActivity.class);
				//				startActivity(traintent);
				break;
			case R.id.profit_yesterday://昨日收益
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent yesintent = new Intent(WalletActivity.this,InterestIncomeActivity.class);
				//				startActivity(yesintent);
				break;
			case R.id.profit_cumulative://累计收益
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent cumintent = new Intent(WalletActivity.this,InterestIncomeActivity.class);
				//				startActivity(cumintent);
				break;
			case R.id.re_banknote://提现
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent txintent = new Intent(WalletActivity.this,TiXianActivity.class);
				//				startActivity(txintent);
				break;
			case R.id.re_chongzhi://充值
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent reintent = new Intent(WalletActivity.this,RechargeActivity.class);
				//				startActivity(reintent);
				break;
			case R.id.re_interest://收益
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
//								Intent restintent = new Intent(WalletActivity.this,InterestIncomeActivity.class);
//								startActivity(restintent);
				break;
			case R.id.re_jiesuan://结算银行卡
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent jsintent = new Intent(WalletActivity.this,JieSuan_BankCardActivity.class);
				//				startActivity(jsintent);
				break;
			case R.id.re_bubble://消息中心
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent msgintent = new Intent(WalletActivity.this,MessageCenterActivity.class);
				//				startActivity(msgintent);
				break;
			case R.id.re_gonggao://公告
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent notintent = new Intent(WalletActivity.this,NoticeActivity.class);
				//				startActivity(notintent);
				break;
			case R.id.re_rate://费率
				if(!isPerfect()){
					return;
				}
				toast("暂不支持使用");
				//				Intent rateintent = new Intent(WalletActivity.this,RateActivity.class);
				//				startActivity(rateintent);
				break;
			case R.id.re_setting://设置
				if(!isPerfect()){
					return;
				}
				Intent setintent = new Intent(WalletActivity.this,SettingUpActivity.class);
				startActivity(setintent);
				break;

			default:
				break;
			}
		}
	};

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals( android.os.Environment.MEDIA_MOUNTED );   //判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		}
		return sdDir.toString();

	}

	@SuppressWarnings("static-access")
	public void setImg(){
		String mPhotoPath =su.getPrefString(WalletActivity.this, "imgPath", null);
		if(mPhotoPath!=null){
			File file = new File(mPhotoPath);
			if(file.exists()){
				Bitmap bitmap = BitmapFactory.decodeFile( mPhotoPath, null );
				img_head_portrait.setImageBitmap(bitmap);
			}
		}else{
			img_head_portrait.setImageResource(R.drawable.ic_launcher);
		}
	}

	public void toast(String str){
		Toast.makeText(WalletActivity.this, str, Toast.LENGTH_SHORT).show();
	}
	public String subString(String str){
		return str.substring(0, 4)+"********"+str.substring(str.length()-4, str.length());
	}

	//Activity创建或者从后台重新回到前台时被调用  
	@Override  
	protected void onStart() {  
		super.onStart();  
		setImg();
		Log.i(TAG, "onStart called.");  
	}  

	//Activity从后台重新回到前台时被调用  
	@Override  
	protected void onRestart() {  
		super.onRestart();  
		setImg();
		Log.i(TAG, "onRestart called.");  
	}  

	//Activity创建或者从被覆盖、后台重新回到前台时被调用  
	@Override  
	protected void onResume() {  
		super.onResume();  
		Log.i(TAG, "onResume called.");  
	}  

	//Activity窗口获得或失去焦点时被调用,在onResume之后或onPause之后  
	@SuppressWarnings("static-access")
	@Override 
	public void onWindowFocusChanged(boolean hasFocus) { 
		super.onWindowFocusChanged(hasFocus); 
		setImg();
		Log.i(TAG, "onWindowFocusChanged called."); 
	}  

	//Activity被覆盖到下面或者锁屏时被调用  
	@Override  
	protected void onPause() {  
		super.onPause();  
		Log.i(TAG, "onPause called.");  
		//有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据  
	}  

	//退出当前Activity或者跳转到新Activity时被调用  
	@Override  
	protected void onStop() {  
		super.onStop();  
		Log.i(TAG, "onStop called.");     
	}  

	//退出当前Activity时被调用,调用之后Activity就结束了  
	@Override  
	protected void onDestroy() {  
		super.onDestroy();  
		Log.i(TAG, "onDestory called.");  
	}  

	/** 
	 * Activity被系统杀死时被调用. 
	 * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死. 
	 * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态. 
	 * 在onPause之前被调用. 
	 */  
	@Override  
	protected void onSaveInstanceState(Bundle outState) {  
		outState.putInt("param", param);  

		Log.i(TAG, "onSaveInstanceState called. put param: " + param);  
		super.onSaveInstanceState(outState);  
	}  

	/** 
	 * Activity被系统杀死后再重建时被调用. 
	 * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity. 
	 * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后. 
	 */  
	@Override  
	protected void onRestoreInstanceState(Bundle savedInstanceState) {  
		param = savedInstanceState.getInt("param");  

		Log.i(TAG, "onRestoreInstanceState called. get param: " + param);  
		super.onRestoreInstanceState(savedInstanceState);  
	}  
	
	
}
