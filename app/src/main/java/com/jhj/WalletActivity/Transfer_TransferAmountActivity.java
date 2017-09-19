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
import com.jhj.Dialog.PromptBoxDialog;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.SetUpActivity.TradePwdActivity;
import com.jhj.info_util.CashierInputFilter;
import com.jhj.wallet.mypayview.PopEnterPassword;
import com.jhj.wallet.mypayview.getPassWord;
import com.jhjpay.zyb.R;
/***
 * 转账-输入转账金额
 * @author Administrator
 *
 */
public class Transfer_TransferAmountActivity extends Activity{

	private final static int LOAD_SUC_FINISH = -1;
	private String name,phone_number,money,balance;
	private ImageButton img_back;
	private EditText et_money;//转账金额
	private TextView tv_name,tv_phone,tv_balance;//余额
	private Button btn_confirm_transfer;
	private boolean trade_pwd_off;//是否设置了交易密码
	SharedPreferences_util su = new SharedPreferences_util();
	private Dialog out_dg;
	private YWLoadingDialog mDialog;
	private String kewword;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		//交易密码
		trade_pwd_off=su.getPrefboolean(Transfer_TransferAmountActivity.this, "trade_pwd_off", false);
		setContentView(R.layout.activity_wallet_transfer_transfer_amount);

		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Transfer_TransferAmountActivity.this.finish();
			}
		});

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

		tv_name=(TextView) findViewById(R.id.tv_name);
		tv_phone=(TextView) findViewById(R.id.tv_phone);
		name=getIntent().getStringExtra("usname");
		phone_number=getIntent().getStringExtra("phone_number");
		tv_name.setText(name);
		tv_phone.setText(phone_number);

		tv_balance=(TextView) findViewById(R.id.tv_balance);
		if(!TextUtils.isEmpty(balance)){
			tv_balance.setText(balance);
		}
		showSettLementDialog();
		btn_confirm_transfer=(Button) findViewById(R.id.btn_confirm_transfer);
		btn_confirm_transfer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				money=et_money.getText().toString().trim();
				if(!TextUtils.isEmpty(money) && !money.subSequence(money.length()-1, money.length()).equals(".")){
					if(trade_pwd_off){
						PopEnterPassword popEnterPassword = new PopEnterPassword(Transfer_TransferAmountActivity.this,name,phone_number,money,password);
						// 显示窗口
						popEnterPassword.showAtLocation(btn_confirm_transfer,
								Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
					}else{
						out_dg.show();
					}
				}else{ 
					toast("请正确填写转账金额");
				}
			}
		});

	}
	/**
	 * 加载进度条
	 * @param view
	 */
	public void onShowSuc(){
		mDialog = new YWLoadingDialog(Transfer_TransferAmountActivity.this);
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
				toast("转账成功，密码"+kewword);
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 初始化信息提示框
	 * @param info
	 */
	public void showPromptBoxDialog(String info){
		PromptBoxDialog D0Dialog = new PromptBoxDialog(Transfer_TransferAmountActivity.this,info);  
		D0Dialog.show();
	}
	/**
	 * 初始化交易密码设置提示框
	 */
	public void showSettLementDialog(){
		out_dg = new Dialog(Transfer_TransferAmountActivity.this, R.style.edit_AlertDialog_style);
		out_dg.setContentView(R.layout.activity_dialog_signout);
		out_dg.setCanceledOnTouchOutside(false);
		TextView setmessage = (TextView) out_dg.findViewById(R.id.setmessage);
		TextView cancel = (TextView) out_dg.findViewById(R.id.cancel);
		TextView ensure = (TextView) out_dg.findViewById(R.id.ensure);
		setmessage.setText("您还没有设置交易密码,请先设置!");
		cancel.setText("否");
		ensure.setText("是");
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				out_dg.dismiss();
			}
		});
		ensure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tradeintent = new Intent(Transfer_TransferAmountActivity.this,TradePwdActivity.class);
				startActivity(tradeintent);
				out_dg.dismiss();
			}
		});
	}


	public void toast(String str){
		Toast.makeText(Transfer_TransferAmountActivity.this, str, Toast.LENGTH_LONG).show();
	}

}
