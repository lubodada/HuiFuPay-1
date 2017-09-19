package com.example.Main;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jhj.info_util.Utils;
import com.jhjpay.zyb.R;

/**
 * 注册
 * @author 
 */
public class RegisterActivity extends Activity implements OnClickListener{

	private Button code_message,com_ok;
	private EditText phone_number,ver_code,input_pwd,reinput_pwd;
	private ImageButton img_back;
	private ImageView imgpwd,reimgpwd;
	private String phone,code_msg,pwd,repwd;
	private Boolean showPassword = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuce);
		MyAppLication.getInstance().addActivity(this);
		initView();

	}
	public void initView(){
		code_message=(Button) findViewById(R.id.code_message);
		com_ok=(Button) findViewById(R.id.com_ok);
		phone_number=(EditText) findViewById(R.id.phone_number);
		ver_code=(EditText) findViewById(R.id.ver_code);
		input_pwd=(EditText) findViewById(R.id.input_pwd);
		reinput_pwd=(EditText) findViewById(R.id.reinput_pwd);
		img_back=(ImageButton) findViewById(R.id.img_back);
		imgpwd=(ImageView) findViewById(R.id.imgpwd);
		reimgpwd=(ImageView) findViewById(R.id.reimgpwd);

		img_back.setOnClickListener(this);
		code_message.setOnClickListener(this);
		imgpwd.setOnClickListener(this);
		reimgpwd.setOnClickListener(this);
		com_ok.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//new倒计时对象,总共的时间,每隔多少秒更新一次时间  
		MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);
		switch (v.getId()) {
		case R.id.img_back:
			RegisterActivity.this.finish();
			break;
		case R.id.code_message://短信接收
			phone=phone_number.getText().toString().trim();
			if(TextUtils.isEmpty(phone)){
				Toastshow("手机号不能为空");
				return;
			}
			if(!Utils.isMobileNO(phone_number.getText().toString())){
				Toastshow("手机号错误");
				return;
			}
			myCountDownTimer.start();
			break;
		case R.id.imgpwd:
			if (!showPassword) {// 显示密码
				imgpwd.setImageDrawable(getResources().getDrawable(R.drawable.pwd_eye));
				input_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				input_pwd.setSelection(input_pwd.getText().toString().length());
				showPassword = !showPassword;
			} else {// 隐藏密码
				imgpwd.setImageDrawable(getResources().getDrawable(R.drawable.pwd_noeye));
				input_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				input_pwd.setSelection(input_pwd.getText().toString().length());
				showPassword = !showPassword;
			}
			break;
		case R.id.reimgpwd:
			if (!showPassword) {// 显示密码
				reimgpwd.setImageDrawable(getResources().getDrawable(R.drawable.pwd_eye));
				reinput_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				reinput_pwd.setSelection(reinput_pwd.getText().toString().length());
				showPassword = !showPassword;
			} else {// 隐藏密码
				reimgpwd.setImageDrawable(getResources().getDrawable(R.drawable.pwd_noeye));
				reinput_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				reinput_pwd.setSelection(reinput_pwd.getText().toString().length());
				showPassword = !showPassword;
			}
			break;
		case R.id.com_ok:
			phone=phone_number.getText().toString().trim();
			code_msg=ver_code.getText().toString().trim();
			pwd=input_pwd.getText().toString().trim();
			repwd=reinput_pwd.getText().toString().trim();
			if(TextUtils.isEmpty(phone)){
				Toastshow("手机号不能为空");
				return;
			}
			if(TextUtils.isEmpty(code_msg)){
				Toastshow("验证码不能为空");
				return;
			}
			if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(repwd)){
				Toastshow("密码不能为空");
				return;
			}
			if(pwd.length()<6){
				Toastshow("密码不能少于6位");
				return;
			}
			if(!pwd.equals(repwd)){
				Toastshow("密码不一致");
				return;
			}
			break;

		default:
			break;
		}
	}
	public void Toastshow(String str) {
		Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
	}
	//复写倒计时  
	private class MyCountDownTimer extends CountDownTimer {  

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {  
			super(millisInFuture, countDownInterval);  
		}  
		//计时过程  
		@Override  
		public void onTick(long l) {  
			//防止计时过程中重复点击  
			code_message.setClickable(false);  
			code_message.setText(l/1000+"s");  
		}  

		//计时完毕的方法  
		@Override  
		public void onFinish() {  
			//重新给Button设置文字  
			code_message.setText("重新获取");  
			//设置可点击  
			code_message.setClickable(true);  
		}  
	}  
}
