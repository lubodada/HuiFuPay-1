package com.jhj.Function;

import com.jhj.Dialog.YWLoadingDialog;
import com.jhjpay.zyb.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 调单
 * @author lb
 */
public class SingleTransferActivity extends Activity {

	private final static int LOAD_SUC_FINISH = -1;
	private final static int DIAODAN_BYTIME = 0;
	private ImageButton img_back;
	private TextView conditional_query;//条件查询
	private LinearLayout ll_norecord;
	private ListView listview_diaodan;
	private String starttime,stoptime,endtime,state;
	YWLoadingDialog mDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_diaodan);
		
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SingleTransferActivity.this.finish();
			}
		});
		conditional_query=(TextView) findViewById(R.id.conditional_query);
		conditional_query.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SingleTransferActivity.this,SingleTransfer_Query_Activity.class);
				startActivityForResult(intent, DIAODAN_BYTIME);
			}
		});
		ll_norecord=(LinearLayout) findViewById(R.id.ll_norecord);
		conditional_query=(TextView) findViewById(R.id.conditional_query);
		listview_diaodan=(ListView) findViewById(R.id.listview_diaodan);
		onShowSuc();
	}
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				ll_norecord.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case DIAODAN_BYTIME:
			if (resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				starttime=bundle.getString("starttime");
				stoptime=bundle.getString("stoptime");
				endtime=bundle.getString("endtime");
				state=bundle.getString("state");
				if(!TextUtils.isEmpty(starttime)
						&&!TextUtils.isEmpty(stoptime)
						&&!TextUtils.isEmpty(endtime)
						&&!TextUtils.isEmpty(state)){
					onShowSuc();
				}
			}
			break;
		}
	}
	/**
	 * 加载进度条
	 * @param view
	 */
	public void onShowSuc(){
		mDialog = new YWLoadingDialog(this);
		mDialog.show();
		handler.sendEmptyMessageDelayed(LOAD_SUC_FINISH,1500);
	}
}
