package com.jhj.WalletActivity;

import com.jhjpay.zyb.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 付款码
 * @author Administrator
 *
 */
public class PaymentCodeActivity extends Activity {
	
	private ImageButton img_back;
	private TextView tv_code_number;
	private ImageView img_bar_code,img_qr_code;
	private String code_number,bar_code,qr_code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet_payment_code);
		
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PaymentCodeActivity.this.finish();
			}
		});
		tv_code_number=(TextView) findViewById(R.id.tv_code_number);
		img_bar_code=(ImageView) findViewById(R.id.img_bar_code);
		img_qr_code=(ImageView) findViewById(R.id.img_qr_code);
	}

}
