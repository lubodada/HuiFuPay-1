package com.jhj.WalletActivity;

import com.example.Main.MyAppLication;
import com.jhj.info_util.Utils;
import com.jhjpay.zyb.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 转账--收款人信息
 * @author Administrator
 *
 */
public class Transfer_PayeeInfoActivity extends Activity {

	private ImageButton img_back;
	private EditText payee_name,payee_phone;
	private Button btn_next_step;
	private String usname,phone_number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_wallet_transfer_payeeinfo);

		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Transfer_PayeeInfoActivity.this.finish();
			}
		});
		payee_name=(EditText) findViewById(R.id.payee_name);
		payee_name.setCursorVisible(false);
		payee_name.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					payee_name.setCursorVisible(true);// 再次点击显示光标
				}
				return false;
			}
		});
		payee_phone=(EditText) findViewById(R.id.payee_phone);
		payee_phone.setCursorVisible(false);
		payee_phone.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					payee_phone.setCursorVisible(true);// 再次点击显示光标
				}
				return false;
			}
		});
		btn_next_step=(Button) findViewById(R.id.btn_next_step);
		btn_next_step.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usname=payee_name.getText().toString().trim();
				phone_number=payee_phone.getText().toString().trim();
				if(TextUtils.isEmpty(usname) || TextUtils.isEmpty(phone_number)){
					toast("请填写收款人的完整信息");
					return;
				}
				if(Utils.isMobileNO(phone_number)==false){
					toast("手机号错误");
					return;
				}
				Intent intent = new Intent(Transfer_PayeeInfoActivity.this,Transfer_TransferAmountActivity.class);
				intent.putExtra("usname", usname);
				intent.putExtra("phone_number", phone_number);
				startActivity(intent);
			}
		});
	}

	public void toast(String str){
		Toast.makeText(Transfer_PayeeInfoActivity.this, str, Toast.LENGTH_LONG).show();
	}
}
