package com.jhj.WalletActivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MyAppLication;
import com.jhj.Dialog.PromptBoxDialog;
import com.jhjpay.zyb.R;

/**
 * 结算银行卡
 * @author Administrator
 */
public class JieSuan_BankCardActivity extends Activity implements OnClickListener{

	private ImageButton img_back,img_bankcard_add;
	private ListView listview_bangkard;
	private List<BankCard> banklist = new ArrayList<BankCard>();
	private Dialog bank_dg;
	private String bankcard_number;
	private LinearLayout ll_nobankcard;
	private JieSuan_BankCardAdapter bankcard_adapter;
	private Boolean update_bankcard_off;
	SharedPreferences_util su = new SharedPreferences_util();
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//判断是否允许切换银行卡
		update_bankcard_off=su.getPrefboolean(JieSuan_BankCardActivity.this, "update_bankcard_off", false);
		setContentView(R.layout.activity_wallet_jiesuan_bankcard);
		MyAppLication.getInstance().addActivity(this);

		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		img_bankcard_add=(ImageButton) findViewById(R.id.img_bankcard_add);
		img_bankcard_add.setOnClickListener(this);
		ll_nobankcard=(LinearLayout) findViewById(R.id.ll_nobankcard);

		listview_bangkard=(ListView) findViewById(R.id.listview_bangkard);
		listview_bangkard.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				bankcard_number=banklist.get(position).getBankcard_number();
				bank_dg.setCanceledOnTouchOutside(false);
				bank_dg.show();
			}
		});
		showSettLementDialog();
		//模拟数据(测试用)
		BankCard bank1 = new BankCard();
		bank1.setBankcard("中国建设银行");
		bank1.setBankcard_number("6227002271530099694");
		banklist.add(bank1);

		BankCard bank2 = new BankCard();
		bank2.setBankcard("中国工商银行");
		bank2.setBankcard_number("6212261612004194974");
		banklist.add(bank2);

		BankCard bank3 = new BankCard();
		bank3.setBankcard("中国农业银行");
		bank3.setBankcard_number("6216261612004191586");
		banklist.add(bank3);

		if(banklist.size()!=0){
			ll_nobankcard.setVisibility(View.GONE);
			listview_bangkard.setVisibility(View.VISIBLE);
			bankcard_adapter = new JieSuan_BankCardAdapter(JieSuan_BankCardActivity.this,banklist);
			listview_bangkard.setAdapter(bankcard_adapter);
		}
	}
	/**
	 * 初始化交易密码设置提示框
	 */
	public void showSettLementDialog(){
		bank_dg = new Dialog(JieSuan_BankCardActivity.this, R.style.edit_AlertDialog_style);
		bank_dg.setContentView(R.layout.activity_dialog_signout);

		TextView setmessage = (TextView) bank_dg.findViewById(R.id.setmessage);
		TextView cancel = (TextView) bank_dg.findViewById(R.id.cancel);
		TextView ensure = (TextView) bank_dg.findViewById(R.id.ensure);
		setmessage.setText("是否选择此卡为默认结算卡");

		cancel.setOnClickListener(this);
		ensure.setOnClickListener(this);
	}
	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			JieSuan_BankCardActivity.this.finish();
			break;
		case R.id.img_bankcard_add:
			Intent addintent = new Intent(JieSuan_BankCardActivity.this,JieSuan_AddBankCardActivity.class);
			startActivity(addintent); 
			break;
		case R.id.cancel:
			bank_dg.dismiss();
			break;
		case R.id.ensure:
//			bank_dg.dismiss();
//			if(!update_bankcard_off){
//				PromptBoxDialog pb_dialog = new PromptBoxDialog(JieSuan_BankCardActivity.this,"您好，切换银行卡功能暂停使用！");
//				pb_dialog.show();
//				return;
//			}
//			bankcard_adapter.notifyDataSetChanged();
			su.setPrefString(JieSuan_BankCardActivity.this, "bank_number", bankcard_number);
			Intent bankintent = new Intent();
			bankintent.putExtra("bankcard_number", bankcard_number);
			setResult(RESULT_OK,bankintent);
			JieSuan_BankCardActivity.this.finish();
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
}
