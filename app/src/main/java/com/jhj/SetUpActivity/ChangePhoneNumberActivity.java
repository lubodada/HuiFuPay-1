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
import com.jhj.Dialog.PromptBoxDialog;
import com.jhj.info_util.Utils;
import com.jhjpay.zyb.R;

public class ChangePhoneNumberActivity extends Activity implements OnClickListener{
	
	private ImageButton img_back;
	private Button code_message,com_ok;
	private EditText phone_number,ver_code;
	private String phnumber,codenumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_change_phonenumber);
		MyAppLication.getInstance().addActivity(this);
		initView();
	}
	
	public void initView(){
		img_back = (ImageButton) findViewById(R.id.img_back);
		code_message = (Button) findViewById(R.id.code_message);
		com_ok = (Button) findViewById(R.id.com_ok);
		phone_number = (EditText) findViewById(R.id.phone_number);
		ver_code = (EditText) findViewById(R.id.ver_code);
		
		img_back.setOnClickListener(this);
		code_message.setOnClickListener(this);
		com_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			ChangePhoneNumberActivity.this.finish();
			break;
		case R.id.code_message:
			
			break;
		case R.id.com_ok:
			phnumber=phone_number.getText().toString().trim();
			codenumber=ver_code.getText().toString().trim();
			if(TextUtils.isEmpty(phnumber)){
				showDialog("请输入新手机号");
				return;
			}
			if(Utils.isMobileNO(phnumber)==false){
				showDialog("手机号格式错误");
				return;
			}
			if(TextUtils.isEmpty(codenumber)){
				toast("请输入短信验证码");
				return;
			}
			
			break;

		default:
			break;
		}
	}
	
	public void showDialog(String title){
		PromptBoxDialog promptDialog = new PromptBoxDialog(ChangePhoneNumberActivity.this,title);  
		promptDialog.show();
	}
	public void toast(String str){
		Toast.makeText(ChangePhoneNumberActivity.this, str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
	
}
