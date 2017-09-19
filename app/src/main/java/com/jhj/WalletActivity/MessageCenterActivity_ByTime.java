package com.jhj.WalletActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.Main.MyAppLication;
import com.jhj.Dialog.MessageCenterDialog_ByTime;
import com.jhj.Dialog.PromptBoxDialog;
import com.jhjpay.zyb.R;

/**
 * 消息中心--时间选择
 * @author Administrator
 *
 */
public class MessageCenterActivity_ByTime extends Activity implements OnClickListener{

	private ImageButton img_back;
	private Button btn_starttime,btn_stoptime,com_ok;
	private String starttime,stoptime;
	private MessageCenterDialog_ByTime dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wallet_massage_bytime);
		MyAppLication.getInstance().addActivity(this);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		img_back=(ImageButton) findViewById(R.id.img_back);
		btn_starttime=(Button) findViewById(R.id.btn_starttime);
		btn_stoptime=(Button) findViewById(R.id.btn_stoptime);
		com_ok=(Button) findViewById(R.id.com_ok);

		img_back.setOnClickListener(this);
		btn_starttime.setOnClickListener(this);
		btn_stoptime.setOnClickListener(this);
		com_ok.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			MessageCenterActivity_ByTime.this.finish();
			break;
		case R.id.btn_starttime:
			Dialog(btn_starttime);
			break;
		case R.id.btn_stoptime:
			Dialog(btn_stoptime);
			break;
		case R.id.com_ok:
			starttime=btn_starttime.getText().toString().trim();
			stoptime=btn_stoptime.getText().toString().trim();
			if(TextUtils.isEmpty(starttime)||TextUtils.isEmpty(stoptime)){
				PromptBoxDialog promptDialog = new PromptBoxDialog(MessageCenterActivity_ByTime.this,"请选择开始&结束时间");  
				promptDialog.show();
				return;
			}
			int result = starttime.compareTo(stoptime);
			if(result > 0){
				PromptBoxDialog promptDialog = new PromptBoxDialog(MessageCenterActivity_ByTime.this,"开始时间不能大于结束时间");  
				promptDialog.show();
				return;
			}
			Intent intent = new Intent();
			intent.putExtra("starttime", starttime);
			intent.putExtra("stoptime", stoptime);
			setResult(RESULT_OK, intent);
			MessageCenterActivity_ByTime.this.finish();
			break;

		default: 
			break;
		}
	}
	/**
	 * 日期选择弹框
	 * @param button
	 */
	public void Dialog(Button button){
		MessageCenterDialog_ByTime.Builder messageBuilder = new MessageCenterDialog_ByTime.Builder(MessageCenterActivity_ByTime.this);
		messageBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialog = messageBuilder.create(button);
		dialog.show();
	}
}
