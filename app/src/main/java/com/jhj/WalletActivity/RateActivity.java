package com.jhj.WalletActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MyAppLication;
import com.jhj.Dialog.PromptBoxDialog;
import com.jhjpay.zyb.R;
/**
 * 费率
 * @author lb
 */
public class RateActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private RelativeLayout re_settlement_type;
	private TextView tv_settlement_type;
	private TextView tv_rate_nfc,tv_rate_weixin,tv_rate_quick,tv_rate_zfb;
	private String rate_nfc,rate_weixin,rate_quick,rate_zfb;
	SharedPreferences_util su = new SharedPreferences_util();
	private Dialog sty_dg;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//费率信息
		rate_nfc=su.getPrefString(RateActivity.this, "rate_nfc", null);
		rate_weixin=su.getPrefString(RateActivity.this, "rate_weixin", null);
		rate_quick=su.getPrefString(RateActivity.this, "rate_quick", null);
		rate_zfb=su.getPrefString(RateActivity.this, "rate_zfb", null);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_wallet_rate);

		img_back=(ImageButton) findViewById(R.id.img_back);
		tv_rate_nfc=(TextView) findViewById(R.id.tv_rate_nfc);
		tv_rate_weixin=(TextView) findViewById(R.id.tv_rate_weixin);
		tv_rate_quick=(TextView) findViewById(R.id.tv_rate_quick);
		tv_rate_zfb=(TextView) findViewById(R.id.tv_rate_zfb);
		
		if(!TextUtils.isEmpty(rate_nfc)){
			tv_rate_nfc.setText(rate_nfc);
		}
		if(!TextUtils.isEmpty(rate_weixin)){
			tv_rate_weixin.setText(rate_weixin);
		}
		if(!TextUtils.isEmpty(rate_quick)){
			tv_rate_quick.setText(rate_quick);
		}
		if(!TextUtils.isEmpty(rate_zfb)){
			tv_rate_zfb.setText(rate_zfb);
		}
		
		re_settlement_type=(RelativeLayout) findViewById(R.id.re_settlement_type);
		tv_settlement_type=(TextView) findViewById(R.id.tv_settlement_type);
		img_back.setOnClickListener(this);
		re_settlement_type.setOnClickListener(this);
		showCodeDialog();
	}
	/**
	 * 初始化结算类型选择框
	 */
	public void showCodeDialog(){
		sty_dg = new Dialog(RateActivity.this, R.style.edit_AlertDialog_style);
		sty_dg.setContentView(R.layout.activity_dialog_rate_settlement_type);
		
		TextView tv_D0 = (TextView) sty_dg.findViewById(R.id.tv_D0);
		TextView tv_T1 = (TextView) sty_dg.findViewById(R.id.tv_T1);
		tv_D0.setOnClickListener(this);
		tv_T1.setOnClickListener(this);
	}
	/**
	 * 初始化信息提示框
	 * @param info
	 */
	public void showPromptBoxDialog(String info){
		PromptBoxDialog D0Dialog = new PromptBoxDialog(RateActivity.this,info);  
		D0Dialog.show();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			RateActivity.this.finish();
			break;
		case R.id.re_settlement_type:
			sty_dg.setCanceledOnTouchOutside(true);
			sty_dg.show();
			break;
		case R.id.tv_D0:
			tv_settlement_type.setText("D0实时结算");
			showPromptBoxDialog("扫码交易每笔交易实时结算");
			sty_dg.dismiss();
			break;
		case R.id.tv_T1:
			tv_settlement_type.setText("T+1结算");
			showPromptBoxDialog("当天的交易在下一个工作日结算");
			sty_dg.dismiss();
			break;
		default:
			break;
		}
	}
}
