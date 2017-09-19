package com.jhj.SetUpActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MyAppLication;
import com.jhjpay.zyb.R;

public class TradePwdActivity extends Activity implements OnClickListener{
	
	private TextView phone_number;
	private EditText ver_code,pwd,confirm_pwd;
	private Button code_message,com_ok;
	private ImageButton img_back;
	private String ph_number,code,word,con_word;
	private boolean trade_pwd_off;//是否设置了交易密码
	SharedPreferences_util su = new SharedPreferences_util();

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//交易密码
		trade_pwd_off=su.getPrefboolean(TradePwdActivity.this, "trade_pwd_off", false);
		//手机号(登录账号)
		ph_number=su.getPrefString(TradePwdActivity.this, "userName", null);
		setContentView(R.layout.activity_setup_trade_pwd);
		MyAppLication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		phone_number=(TextView) findViewById(R.id.phone_number);
		phone_number.setText(ph_number);
		ver_code=(EditText) findViewById(R.id.ver_code);
		pwd=(EditText) findViewById(R.id.pwd);
		confirm_pwd=(EditText) findViewById(R.id.confirm_pwd);
		code_message=(Button) findViewById(R.id.code_message);
		com_ok=(Button) findViewById(R.id.com_ok);
		img_back=(ImageButton) findViewById(R.id.img_back);
		
		code_message.setOnClickListener(this);
		com_ok.setOnClickListener(this);
		img_back.setOnClickListener(this);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			TradePwdActivity.this.finish();
			break;
		case R.id.code_message:
			
			break;
		case R.id.com_ok:
			code=ver_code.getText().toString().trim();
			word=pwd.getText().toString().trim();
			con_word=confirm_pwd.getText().toString().trim();
			if(TextUtils.isEmpty(code)||TextUtils.isEmpty(word)||TextUtils.isEmpty(con_word)){
				trade_pwd_off=false;
				su.setPrefboolean(TradePwdActivity.this, "trade_pwd_off", trade_pwd_off);
				toast("请填写完整信息");
				return;
			}
			
			trade_pwd_off=true;
			su.setPrefboolean(TradePwdActivity.this, "trade_pwd_off", trade_pwd_off);
			
			break;

		default:
			break;
		}
	}
	public void toast(String str){
		Toast.makeText(TradePwdActivity.this, str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
}
