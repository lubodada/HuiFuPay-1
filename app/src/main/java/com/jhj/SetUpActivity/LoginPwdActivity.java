package com.jhj.SetUpActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.Main.MyAppLication;
import com.jhjpay.zyb.R;

public class LoginPwdActivity extends Activity {
	
	private ImageButton img_back;
	private Button com_ok;
	private EditText old_pwd,new_pwd,confirm_new_pwd;
	private String old_word,new_word,confirm_word;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_login_pwd);
		MyAppLication.getInstance().addActivity(this);
		initView();
	}
	
	public void initView(){
		img_back = (ImageButton) findViewById(R.id.img_back);
		com_ok = (Button) findViewById(R.id.com_ok);
		old_pwd=(EditText) findViewById(R.id.old_pwd);
		new_pwd=(EditText) findViewById(R.id.new_pwd);
		confirm_new_pwd=(EditText) findViewById(R.id.confirm_new_pwd);
		
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginPwdActivity.this.finish();
			}
		});
		com_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				old_word=old_pwd.getText().toString().trim();
				new_word=new_pwd.getText().toString().trim();
				confirm_word=confirm_new_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(old_word)){
					toast("旧密码不能为空");
					return;
				}
				if(TextUtils.isEmpty(new_word)){
					toast("新密码不能为空");
					return;
				}
				if(TextUtils.isEmpty(confirm_word)){
					toast("请确认新密码");
					return;
				}
				if(!new_word.equals(confirm_word)){
					toast("密码不一致");
					return;
				}
			}
		});
	}
	public void toast(String str){
		Toast.makeText(LoginPwdActivity.this, str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
}
