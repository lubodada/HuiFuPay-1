package com.jhj.SetUpActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Main.LoginActivity;
import com.example.Main.MyAppLication;
import com.jhjpay.zyb.R;

/**
 * 设置
 * @author lb
 *
 */
public class SettingUpActivity extends Activity implements OnClickListener{

	private RelativeLayout re_shanghu_info,re_jiesuan_info,re_change_phonenumber,
	        			   re_login_pwd,re_trade_pwd,re_gesture_pwd,re_remove_shebei,
	        			   re_contact_us,re_about_us;
	private ImageButton img_back;
	private Button sign_out;
	private Dialog out_dg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_up);
		MyAppLication.getInstance().addActivity(this);
		
		re_shanghu_info=(RelativeLayout) findViewById(R.id.re_shanghu_info);
		re_jiesuan_info=(RelativeLayout) findViewById(R.id.re_jiesuan_info);
		re_change_phonenumber=(RelativeLayout) findViewById(R.id.re_change_phonenumber);
		re_login_pwd=(RelativeLayout) findViewById(R.id.re_login_pwd);
		re_trade_pwd=(RelativeLayout) findViewById(R.id.re_trade_pwd);
		re_gesture_pwd=(RelativeLayout) findViewById(R.id.re_gesture_pwd);
		re_remove_shebei=(RelativeLayout) findViewById(R.id.re_remove_shebei);
		re_contact_us=(RelativeLayout) findViewById(R.id.re_contact_us);
		re_about_us=(RelativeLayout) findViewById(R.id.re_about_us);
		img_back=(ImageButton) findViewById(R.id.img_back);
		sign_out=(Button) findViewById(R.id.sign_out);
		
		re_shanghu_info.setOnClickListener(this);
		re_jiesuan_info.setOnClickListener(this);
		re_change_phonenumber.setOnClickListener(this);
		re_login_pwd.setOnClickListener(this);
		re_trade_pwd.setOnClickListener(this);
		re_gesture_pwd.setOnClickListener(this);
		re_remove_shebei.setOnClickListener(this);
		re_contact_us.setOnClickListener(this);
		re_about_us.setOnClickListener(this);
		img_back.setOnClickListener(this);
		sign_out.setOnClickListener(this);
		
		showSettLementDialog();
	}
	
	/**
	 * 初始化退出提示框
	 */
	public void showSettLementDialog(){
		out_dg = new Dialog(SettingUpActivity.this, R.style.edit_AlertDialog_style);
		out_dg.setContentView(R.layout.activity_dialog_signout);

		TextView cancel = (TextView) out_dg.findViewById(R.id.cancel);
		TextView ensure = (TextView) out_dg.findViewById(R.id.ensure);
		cancel.setOnClickListener(this);
		ensure.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.re_shanghu_info://商户信息
			intent.setClass(SettingUpActivity.this, ShangHu_Information_Activity.class);
			startActivity(intent);
			break;
		case R.id.re_jiesuan_info://结算信息
			toast("暂不支持使用");
//			intent.setClass(SettingUpActivity.this, JieSuan_Information_Activity.class);
//			startActivity(intent);
			break;
		case R.id.re_change_phonenumber://更换手机号
			toast("暂不支持使用");
//			intent.setClass(SettingUpActivity.this, ChangePhoneNumberActivity.class);
//			startActivity(intent);
			break;
		case R.id.re_login_pwd://登录密码
			toast("暂不支持使用");
//			intent.setClass(SettingUpActivity.this, LoginPwdActivity.class);
//			startActivity(intent);
			break;
		case R.id.re_trade_pwd://交易密码
			toast("暂不支持使用");
//			intent.setClass(SettingUpActivity.this, TradePwdActivity.class);
//			startActivity(intent);
			break;
		case R.id.re_gesture_pwd://手势密码
			toast("暂不支持使用");
			break;
		case R.id.re_remove_shebei://解绑设备
			toast("暂不支持使用");
			break; 
		case R.id.re_contact_us://联系我们
			intent.setClass(SettingUpActivity.this, ContactUsActivity.class);
			startActivity(intent);
			break;
		case R.id.re_about_us://关于翼开店
			intent.setClass(SettingUpActivity.this, AboutUsActivity.class);
			startActivity(intent);
			break;
		case R.id.img_back://返回
			SettingUpActivity.this.finish();
			break;
		case R.id.sign_out://安全退出
			out_dg.setCanceledOnTouchOutside(false);
			out_dg.show();
			break;
		case R.id.cancel://
			out_dg.dismiss();
			break;
		case R.id.ensure://
			intent.setClass(SettingUpActivity.this, LoginActivity.class);
			startActivity(intent);
			MyAppLication.getInstance().exit();
			out_dg.dismiss();
			break;

		default:
			break;
		}
	}
	public void toast(String str){
		Toast.makeText(SettingUpActivity.this, str, Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
}
