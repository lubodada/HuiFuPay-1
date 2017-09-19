package com.jhj.Function;

import com.jhjpay.zyb.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 我的服务商
 * @author lb
 */
public class MyServiceProviderActivity extends Activity {

	private ImageButton img_back;
	private TextView tv_service_provider,tv_business_personnel,
		    tv_number,tv_qq,tv_weixin,tv_phone_number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_myservice_provider);
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubq
				MyServiceProviderActivity.this.finish();
			}
		});
		tv_service_provider=(TextView) findViewById(R.id.tv_service_provider);//服务商
		tv_business_personnel=(TextView) findViewById(R.id.tv_business_personnel);//业务人员
		tv_number=(TextView) findViewById(R.id.tv_number);//编号
		tv_qq=(TextView) findViewById(R.id.tv_qq);//QQ
		tv_weixin=(TextView) findViewById(R.id.tv_weixin);//微信
		tv_phone_number=(TextView) findViewById(R.id.tv_phone_number);//联系电话
	}
}
