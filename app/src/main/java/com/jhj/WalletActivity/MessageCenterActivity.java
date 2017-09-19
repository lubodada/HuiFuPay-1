package com.jhj.WalletActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.Main.MyAppLication;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhjpay.zyb.R;
/**
 * 消息中心
 * @author Administrator
 */
public class MessageCenterActivity extends Activity {

	private final static int LOAD_SUC_FINISH = -1;
	private final static int LOAD_FAIL_FINISH = -2;
	private final static int MWSSAGE_BYTIME = 0;
	private String starttime,stoptime;
	ImageButton img_back,img_search;
	ImageView no_message;
	ListView listview_message;
	YWLoadingDialog mDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet_message_center);
		MyAppLication.getInstance().addActivity(this);
		
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MessageCenterActivity.this.finish();
			}
		});
		img_search=(ImageButton) findViewById(R.id.img_search);
		img_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MessageCenterActivity.this,MessageCenterActivity_ByTime.class);
				startActivityForResult(intent, MWSSAGE_BYTIME);
			}
		});
		no_message=(ImageView) findViewById(R.id.no_message);
		listview_message=(ListView) findViewById(R.id.listview_message);
		onShowSuc();
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
	public void onShowFail(){
		mDialog = new YWLoadingDialog(this);
		mDialog.show();
		handler.sendEmptyMessageDelayed(LOAD_FAIL_FINISH, 1500);
	}
	
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				no_message.setVisibility(View.VISIBLE);
				break;
			case LOAD_FAIL_FINISH:
				mDialog.dimissFail();
				no_message.setVisibility(View.VISIBLE);
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
		case MWSSAGE_BYTIME:
			if (resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				starttime=bundle.getString("starttime");
				stoptime=bundle.getString("stoptime");
				if(!TextUtils.isEmpty(starttime)&&!TextUtils.isEmpty(stoptime)){
					onShowSuc();
				}
			}
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
