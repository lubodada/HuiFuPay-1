package com.jhj.WalletActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MyAppLication;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.SetUpActivity.TradePwdActivity;
import com.jhj.info_util.CashierInputFilter;
import com.jhj.wallet.mypayview.PopEnterPassword;
import com.jhj.wallet.mypayview.getPassWord;
import com.jhjpay.zyb.R;

/**
 *    提现
 * @author Administrator
 *
 */
public class TiXianActivity extends Activity implements OnClickListener{

	private final static int BANKCARD_NUMBER = 1;
	private final static int LOAD_SUC_FINISH = -1;
	private TextView bankcard,balance,all_tixian;
	private ImageButton img_back;
	private Button com_ok;
	private EditText money;
	private String bank_number,tx_money,yue_money;
	private Dialog out_dg;
	private boolean trade_pwd_off;//是否设置了交易密码
	SharedPreferences_util su = new SharedPreferences_util();
	private YWLoadingDialog mDialog;
	private String kewword;
	
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//交易密码
		trade_pwd_off=su.getPrefboolean(TiXianActivity.this, "trade_pwd_off", false);
		bank_number=su.getPrefString(TiXianActivity.this, "bank_number", null);
		setContentView(R.layout.activity_wallet_tixian);
		MyAppLication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		bankcard=(TextView) findViewById(R.id.bankcard);//银行卡号
		balance=(TextView) findViewById(R.id.balance);//钱包余额
		money=(EditText) findViewById(R.id.money);//提现金额
		money.setCursorVisible(false);
		InputFilter[] is = {new CashierInputFilter()};
		money.setFilters(is);
		money.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					money.setCursorVisible(true);// 再次点击显示光标
				}
				return false;
			}
		});
		all_tixian=(TextView) findViewById(R.id.all_tixian);//全部提现
		img_back=(ImageButton) findViewById(R.id.img_back);
		com_ok=(Button) findViewById(R.id.com_ok);


		if(!TextUtils.isEmpty(bank_number)){
			bankcard.setText(subString(bank_number));
		}

		bankcard.setOnClickListener(this);
		all_tixian.setOnClickListener(this);
		img_back.setOnClickListener(this);
		com_ok.setOnClickListener(this);

		showSettLementDialog();
	}

	/**
	 * 初始化交易密码设置提示框
	 */
	public void showSettLementDialog(){
		out_dg = new Dialog(TiXianActivity.this, R.style.edit_AlertDialog_style);
		out_dg.setContentView(R.layout.activity_dialog_signout);
		out_dg.setCanceledOnTouchOutside(false);
		TextView setmessage = (TextView) out_dg.findViewById(R.id.setmessage);
		TextView cancel = (TextView) out_dg.findViewById(R.id.cancel);
		TextView ensure = (TextView) out_dg.findViewById(R.id.ensure);
		setmessage.setText("您还没有设置交易密码,请先设置!");
		cancel.setText("否");
		ensure.setText("是");
		cancel.setOnClickListener(this);
		ensure.setOnClickListener(this);
	}

	/**
	 * 加载进度条
	 * @param view
	 */
	public void onShowSuc(){
		mDialog = new YWLoadingDialog(TiXianActivity.this);
		mDialog.show();
		handler.sendEmptyMessageDelayed(LOAD_SUC_FINISH,1000);
	}
	/**
	 * 密码输入完成回调
	 */
	getPassWord password = new getPassWord(){

		@Override
		public void getPwd(String password) {
			// TODO Auto-generated method stub
			kewword = password;
			onShowSuc();
			
		}
	};
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				toast("提现成功，密码"+kewword);
				break;

			default:
				break;
			}
		};
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			TiXianActivity.this.finish();
			break;
		case R.id.bankcard:
			Intent bankintent = new Intent(TiXianActivity.this,JieSuan_BankCardActivity.class);
			startActivityForResult(bankintent, BANKCARD_NUMBER);
			break;
		case R.id.com_ok:
			bank_number=bankcard.getText().toString().trim();
			tx_money=money.getText().toString().trim();
			if(!TextUtils.isEmpty(tx_money) && !tx_money.subSequence(tx_money.length()-1, tx_money.length()).equals(".")){
				if(trade_pwd_off){
					PopEnterPassword popEnterPassword = new PopEnterPassword(TiXianActivity.this,"",bank_number,tx_money,password);
					// 显示窗口
					popEnterPassword.showAtLocation(com_ok,
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				}else{
					out_dg.show();
				}
			}else{
				toast("请正确填写提现金额");
			}
			break;
		case R.id.all_tixian:
			yue_money=balance.getText().toString().trim();
			money.setText(yue_money);
			break;
		case R.id.cancel://
			out_dg.dismiss();
			break;
		case R.id.ensure://
			Intent tradeintent = new Intent(TiXianActivity.this,TradePwdActivity.class);
			startActivity(tradeintent);
			out_dg.dismiss();	
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BANKCARD_NUMBER:
			if (resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				bank_number=bundle.getString("bankcard_number");
				if(bank_number!=null){
					bankcard.setText(subString(bank_number));
				}
			}
			break;
		}
	}
	public String subString(String str){
		return str.substring(0, 4)+"********"+str.substring(str.length()-4, str.length());
	}
	public void toast(String str){
		Toast.makeText(TiXianActivity.this, str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
}
