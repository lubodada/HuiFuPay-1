package com.jhj.Function;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.Main.MyAppLication;
import com.jhj.Dialog.PromptBoxDialog;
import com.jhj.info_util.OperatorUtils;
import com.jhj.info_util.Utils;
import com.jhjpay.zyb.R;

/**
 * 手机充值
 * @author Administrator
 *
 */
@SuppressLint("ClickableViewAccessibility")
public class PhoneRechargeActivity extends Activity {

	ImageButton img_back;
	TextView recharge_history;
	Button com_ok;
	TextView tx_one,tx_two, tx_tri, tx_four, tx_five, 
	tx_six, tx_sev,tx_eight, tx_nine;
	EditText ed_phone,re_ed_phone;
	// 识别运营商
	OperatorUtils OU = new OperatorUtils();
	int Operator;
	String Recharge_MONERY,phone_number,re_phone_number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_phone_recharge);
		MyAppLication.getInstance().addActivity(this);
		initView();
	}
	private void initView() {
		img_back=(ImageButton) findViewById(R.id.img_back);
		recharge_history=(TextView) findViewById(R.id.recharge_history);
		com_ok=(Button) findViewById(R.id.com_ok);
		img_back.setOnClickListener(myonclicklistener);
		recharge_history.setOnClickListener(myonclicklistener);
		com_ok.setOnClickListener(myonclicklistener);

		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_phone.setCursorVisible(false);
		ed_phone.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@SuppressWarnings("static-access")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
				} else {
					phone_number = ed_phone.getText().toString();
					Operator = OU.execute(phone_number);
				}
			}
		});
		ed_phone.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					ed_phone.setCursorVisible(true);// 再次点击显示光标
				}
				return false;
			}
		});
		re_ed_phone=(EditText) findViewById(R.id.re_ed_phone);
		re_ed_phone.setCursorVisible(false);
		re_ed_phone.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@SuppressWarnings("static-access")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
				} else {
					re_phone_number = re_ed_phone.getText().toString();
					Operator = OU.execute(re_phone_number);

				}
			}
		});
		re_ed_phone.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					re_ed_phone.setCursorVisible(true);// 再次点击显示光标
				}
				return false;
			}
		});

		tx_one = (TextView) findViewById(R.id.tx_one);
		tx_two = (TextView) findViewById(R.id.tx_two);
		tx_tri = (TextView) findViewById(R.id.tx_tri);
		tx_four = (TextView) findViewById(R.id.tx_four);
		tx_five = (TextView) findViewById(R.id.tx_five);
		tx_six = (TextView) findViewById(R.id.tx_six);
		tx_sev = (TextView) findViewById(R.id.tx_sev);
		tx_eight = (TextView) findViewById(R.id.tx_eight);
		tx_nine = (TextView) findViewById(R.id.tx_nine);

		tx_one.setOnClickListener(myonclicklistener);
		tx_two.setOnClickListener(myonclicklistener);
		tx_tri.setOnClickListener(myonclicklistener);
		tx_four.setOnClickListener(myonclicklistener);
		tx_five.setOnClickListener(myonclicklistener);
		tx_six.setOnClickListener(myonclicklistener);
		tx_sev.setOnClickListener(myonclicklistener);
		tx_eight.setOnClickListener(myonclicklistener);
		tx_nine.setOnClickListener(myonclicklistener);

	}
	View.OnClickListener myonclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				PhoneRechargeActivity.this.finish();
				break;
			case R.id.recharge_history:
				Intent intent = new Intent(PhoneRechargeActivity.this,PhoneRecharge_HistoryActivity.class);
				startActivity(intent);
				break;
			case R.id.tx_one:
				view_change(tx_one);
				Recharge_MONERY = "10";
				break;
			case R.id.tx_two:
				view_change(tx_two);
				Recharge_MONERY = "20";
				break;
			case R.id.tx_tri:
				view_change(tx_tri);
				Recharge_MONERY = "30";
				break;
			case R.id.tx_four:
				view_change(tx_four);
				Recharge_MONERY = "50";
				break;
			case R.id.tx_five:
				view_change(tx_five);
				Recharge_MONERY = "100";
				break;
			case R.id.tx_six:
				view_change(tx_six);
				Recharge_MONERY = "200";
				break;
			case R.id.tx_sev:
				view_change(tx_sev);
				Recharge_MONERY = "300";
				break;
			case R.id.tx_eight:
				view_change(tx_eight);
				Recharge_MONERY = "500";
				break;
			case R.id.tx_nine:
				view_change(tx_nine);
				Recharge_MONERY = "1000";
				break;
			case R.id.com_ok:
				ed_phone.clearFocus();
				ed_phone.setCursorVisible(false);
				re_ed_phone.clearFocus();
				re_ed_phone.setCursorVisible(false);
				if(TextUtils.isEmpty(Recharge_MONERY)){
					PromptBoxDialog promptDialog = new PromptBoxDialog(PhoneRechargeActivity.this,"请选择充值金额");  
					promptDialog.show();
					return;
				}
				if(TextUtils.isEmpty(phone_number)||TextUtils.isEmpty(re_phone_number)
						||Utils.isMobileNO(phone_number)==false
						||Utils.isMobileNO(re_phone_number)==false
						||!phone_number.equals(re_phone_number)){
					PromptBoxDialog promptDialog = new PromptBoxDialog(PhoneRechargeActivity.this,"手机号码错误");  
					promptDialog.show();
					return;
				}
				break;
			default:
				break;
			}
		}
	};
	@SuppressLint("NewApi")
	public void view_change(TextView view){
		tx_one.setTextColor(Color.BLACK);
		tx_one.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		tx_two.setTextColor(Color.BLACK);
		tx_two.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		tx_tri.setTextColor(Color.BLACK);
		tx_tri.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		tx_four.setTextColor(Color.BLACK);
		tx_four.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		tx_five.setTextColor(Color.BLACK);
		tx_five.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		tx_six.setTextColor(Color.BLACK);
		tx_six.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		tx_sev.setTextColor(Color.BLACK);
		tx_sev.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		tx_eight.setTextColor(Color.BLACK);
		tx_eight.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		tx_nine.setTextColor(Color.BLACK);
		tx_nine.setBackground(getResources().getDrawable(R.drawable.ye_button_carryunpass));
		if(view!=null){
			ed_phone.clearFocus();
			ed_phone.setCursorVisible(false);
			re_ed_phone.clearFocus();
			re_ed_phone.setCursorVisible(false);
			view.setTextColor(Color.WHITE);
			view.setBackground(getResources().getDrawable(R.drawable.ye_button_putinpass));
		}
	}
}
