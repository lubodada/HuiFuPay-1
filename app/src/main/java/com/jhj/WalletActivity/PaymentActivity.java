package com.jhj.WalletActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jhj.Dialog.PromptBoxDialog;
import com.jhj.wallet.mypayview.OnPasswordInputFinish;
import com.jhj.wallet.mypayview.PasswordView;
import com.jhjpay.zyb.R;
/**
 * 付款
 * @author lb
 */
public class PaymentActivity extends Activity {

	private ImageButton img_back;
	private PasswordView pwdView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet_payment);
		
		img_back = (ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PaymentActivity.this.finish();
			}
		});
		
		pwdView = (PasswordView) findViewById(R.id.pwd_view);
		// 添加密码输入完成的响应
		pwdView.setOnFinishInput(new OnPasswordInputFinish() {
			@Override
			public void inputFinish() {
				Toast.makeText(PaymentActivity.this, pwdView.getStrPassword(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(PaymentActivity.this,PaymentCodeActivity.class);
				startActivity(intent);
				PaymentActivity.this.finish();
			}

			@Override
			public void inputFinish(String password) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * 初始化信息提示框
	 * @param info
	 */
	public void showPromptBoxDialog(String info){
		PromptBoxDialog D0Dialog = new PromptBoxDialog(PaymentActivity.this,info);  
		D0Dialog.show();
	}
}
