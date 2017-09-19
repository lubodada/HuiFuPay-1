package com.jhj.Function;

import com.jhjpay.zyb.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Cashier_Activity extends Activity {
	
	private TextView total_money,count,tv_nothing;
	private ImageButton img_back;
	private ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_cashier);
		initView();
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cashier_Activity.this.finish();
			}
		});
	}
	/**
	 * 初始化控件
	 */
	public void initView(){
		total_money=(TextView) findViewById(R.id.total_money);
		count=(TextView) findViewById(R.id.count);
		tv_nothing=(TextView) findViewById(R.id.tv_nothing);
		img_back=(ImageButton) findViewById(R.id.img_back);
		listview=(ListView) findViewById(R.id.listview);
	}
}
