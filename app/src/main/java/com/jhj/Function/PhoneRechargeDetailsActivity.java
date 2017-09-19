package com.jhj.Function;

import com.jhjpay.zyb.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 手机充值详情
 * @author Administrator
 *
 */
public class PhoneRechargeDetailsActivity extends Activity {

	private ImageButton img_back;
	private TextView tv_money,tv_phone_number,tv_time,tv_trand_number,tv_wallet_balance,tv_remarks;
	private String money,phone_number,time,trand_number,wallet_balance,remarks;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_phoenrecharge_details);

		img_back = (ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhoneRechargeDetailsActivity.this.finish();
			}
		});
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_trand_number = (TextView) findViewById(R.id.tv_trand_number);
		tv_wallet_balance = (TextView) findViewById(R.id.tv_wallet_balance);
		tv_remarks = (TextView) findViewById(R.id.tv_remarks);

		money = getIntent().getStringExtra("recharge_money");
		phone_number = getIntent().getStringExtra("phone_number");
		time = getIntent().getStringExtra("time");
		trand_number = getIntent().getStringExtra("trand_number");
		remarks = getIntent().getStringExtra("remarks");
		wallet_balance = "0";

		tv_money.setText("-"+money);
		tv_phone_number.setText(phone_number);
		tv_time.setText(time);
		tv_trand_number.setText(trand_number);
		tv_remarks.setText(remarks);
		tv_wallet_balance.setText(wallet_balance);
	}
}
