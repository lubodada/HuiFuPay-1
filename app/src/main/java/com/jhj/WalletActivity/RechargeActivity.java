package com.jhj.WalletActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Main.MyAppLication;
import com.jhj.info_util.CashierInputFilter;
import com.jhjpay.zyb.R;

/**
 * 充值
 * @author lb
 */
public class RechargeActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private EditText et_money;
	private Button com_ok;
	private String money;
	private Dialog recharge_dg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_wallet_recharge);
		initView();
		showSettLementDialog();
	}

	private void initView() {
		// TODO Auto-generated method stub
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		et_money=(EditText) findViewById(R.id.et_money);
		et_money.setCursorVisible(false);
		InputFilter[] is = {new CashierInputFilter()};
		et_money.setFilters(is);
		et_money.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					et_money.setCursorVisible(true);// 再次点击显示光标
				}
				return false;
			}
		});
		com_ok=(Button) findViewById(R.id.com_ok);
		com_ok.setOnClickListener(this);
	}
	/**
	 * 初始化交易密码设置提示框
	 */
	public void showSettLementDialog(){
		recharge_dg = new Dialog(RechargeActivity.this, R.style.edit_AlertDialog_style);
		recharge_dg.setContentView(R.layout.activity_dialog_signout);
		recharge_dg.setCanceledOnTouchOutside(false);
		TextView setmessage = (TextView) recharge_dg.findViewById(R.id.setmessage);
		TextView cancel = (TextView) recharge_dg.findViewById(R.id.cancel);
		TextView ensure = (TextView) recharge_dg.findViewById(R.id.ensure);
		setmessage.setText("充值手续费为  ￥0.0元");
		cancel.setOnClickListener(this);
		ensure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			RechargeActivity.this.finish();
			break;
		case R.id.cancel://
			recharge_dg.dismiss();
			break;
		case R.id.ensure://
			recharge_dg.dismiss();	
			break;	
		case R.id.com_ok:
			money=et_money.getText().toString().trim();
			if(TextUtils.isEmpty(money) || money.subSequence(money.length()-1, money.length()).equals(".")){
				toast("请正确填写充值金额");
				return;
			}
			recharge_dg.show();
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

	public void toast(String str){
		Toast.makeText(RechargeActivity.this, str, Toast.LENGTH_LONG).show();
	}
}
