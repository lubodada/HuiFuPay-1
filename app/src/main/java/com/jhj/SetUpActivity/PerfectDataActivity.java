package com.jhj.SetUpActivity;

import com.example.Main.MyAppLication;
import com.jhjpay.zyb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * 完善资料
 * @author lb
 */
public class PerfectDataActivity extends Activity implements OnClickListener{
	
	private ImageButton img_back;
	private LinearLayout data_geti,data_qiye;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfect_data);
		MyAppLication.getInstance().addActivity(this);
		
		img_back=(ImageButton) findViewById(R.id.img_back);
		data_geti=(LinearLayout) findViewById(R.id.data_geti);
		data_qiye=(LinearLayout) findViewById(R.id.data_qiye);
		
		img_back.setOnClickListener(this);
		data_geti.setOnClickListener(this);
		data_qiye.setOnClickListener(this);
		
		type=getIntent().getStringExtra("type");
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			PerfectDataActivity.this.finish();
			break;
		case R.id.data_geti:
			if(type.equals("data")){
				Intent intent = new Intent(PerfectDataActivity.this,ShangHu_Information_Activity.class);
				startActivity(intent);
			}
			if(type.equals("settlement")){
				Intent intent = new Intent(PerfectDataActivity.this,JieSuan_Information_Activity.class);
				startActivity(intent);
			}
			PerfectDataActivity.this.finish();
			break;
		case R.id.data_qiye:
			
			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
}
