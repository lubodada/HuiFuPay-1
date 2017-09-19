package com.jhj.WalletActivity;

import com.example.Main.MyAppLication;
import com.jhjpay.zyb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 余额
 * @author lb
 */
public class BalanceActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private Button bt_recharge,bt_tixian;
	private TextView tv_balance;
	private String money;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_wallet_balance);

		img_back=(ImageButton) findViewById(R.id.img_back);
		bt_recharge=(Button) findViewById(R.id.bt_recharge);
		bt_tixian=(Button) findViewById(R.id.bt_tixian);
		tv_balance=(TextView) findViewById(R.id.tv_balance);
		img_back.setOnClickListener(this);
		bt_recharge.setOnClickListener(this);
		bt_tixian.setOnClickListener(this);

		if(!TextUtils.isEmpty(money)){
			tv_balance.setText(money);
		}
	}
	@Override
	public void onClick(View v) { 
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			BalanceActivity.this.finish();
			break;
		case R.id.bt_recharge://充值
			Intent reintent = new Intent(BalanceActivity.this,RechargeActivity.class);
			startActivity(reintent);
			break;
		case R.id.bt_tixian://提现
			Intent txintent = new Intent(BalanceActivity.this,TiXianActivity.class);
			startActivity(txintent);
			break;

		default:
			break;
		}
	}
}
