package com.jhj.Function;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.jhj.Dialog.MessageCenterDialog_ByTime;
import com.jhj.Dialog.PromptBoxDialog;
import com.jhjpay.zyb.R;

/**
 * 调单--条件查询
 * @author lb
 */
public class SingleTransfer_Query_Activity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private Button btn_starttime,btn_stoptime,btn_endtime,btn_state,com_ok;
	private String starttime,stoptime,endtime,state;
	private MessageCenterDialog_ByTime dialog;
	final String arr[]=new String[]{"全部","待处理","凭证已上传","凭证审核通过","正常","风险"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_function_diaodan_query);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		img_back=(ImageButton) findViewById(R.id.img_back);
		btn_starttime=(Button) findViewById(R.id.btn_starttime);
		btn_stoptime=(Button) findViewById(R.id.btn_stoptime);
		btn_endtime=(Button) findViewById(R.id.btn_endtime);
		btn_state=(Button) findViewById(R.id.btn_state);
		com_ok=(Button) findViewById(R.id.com_ok);

		img_back.setOnClickListener(this);
		btn_starttime.setOnClickListener(this);
		btn_stoptime.setOnClickListener(this);
		btn_endtime.setOnClickListener(this);
		btn_state.setOnClickListener(this);
		com_ok.setOnClickListener(this);
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			SingleTransfer_Query_Activity.this.finish();
			break;
		case R.id.btn_starttime:
			Dialog(btn_starttime);
			break;
		case R.id.btn_stoptime:
			Dialog(btn_stoptime);
			break;
		case R.id.btn_endtime:
			Dialog(btn_endtime);
			break;
		case R.id.btn_state:
			Builder jobBuilder = new Builder(SingleTransfer_Query_Activity.this,R.style.dialog);
			jobBuilder.setAdapter(new ArrayAdapter<Object>(SingleTransfer_Query_Activity.this, R.layout.choice_scope_item, arr),
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 选择经营范围
					state=arr[which].toString();
					btn_state.setText(state);
				}
			});
			jobBuilder.create().show();
			break;
		case R.id.com_ok:
			starttime=btn_starttime.getText().toString().trim();
			stoptime=btn_stoptime.getText().toString().trim();
			endtime=btn_endtime.getText().toString().trim();
			state=btn_state.getText().toString().trim();
			if(TextUtils.isEmpty(starttime)
					||TextUtils.isEmpty(stoptime)
					||TextUtils.isEmpty(endtime)
					||TextUtils.isEmpty(state)){
				PromptBoxDialog promptDialog = new PromptBoxDialog(SingleTransfer_Query_Activity.this,"查询条件不完整");  
				promptDialog.show();
				return;
			}
			int result = starttime.compareTo(stoptime);
			if(result > 0){
				PromptBoxDialog promptDialog = new PromptBoxDialog(SingleTransfer_Query_Activity.this,"开始时间不能大于结束时间");  
				promptDialog.show();
				return;
			}
			Intent intent = new Intent();
			intent.putExtra("starttime", starttime);
			intent.putExtra("stoptime", stoptime);
			intent.putExtra("endtime", endtime);
			intent.putExtra("state", state);
			setResult(RESULT_OK, intent);
			SingleTransfer_Query_Activity.this.finish();
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
		MessageCenterDialog_ByTime.Builder messageBuilder = new MessageCenterDialog_ByTime.Builder(SingleTransfer_Query_Activity.this);
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
