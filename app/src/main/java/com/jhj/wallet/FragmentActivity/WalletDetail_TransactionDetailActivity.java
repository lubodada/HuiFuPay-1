package com.jhj.wallet.FragmentActivity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jhj.wallet.Fragment.Adapter.WalletDetail;
import com.jhjpay.zyb.R;
/**
 * 钱包明细--交易详情
 * @author lb
 */
public class WalletDetail_TransactionDetailActivity extends Activity {

	private ImageButton img_back;
	private TextView tv_transaction_type,tv_money,tv_money_flow_type,
	tv_time,tv_transaction_number,tv_wallet_balance,tv_remarks;
	private String money_flow_type,balance;
	private List<WalletDetail> walletdetail_list;
	private int position;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet_walletdetail_transaction_details);

		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WalletDetail_TransactionDetailActivity.this.finish();
			}
		});
		//加载控件
		tv_transaction_type=(TextView) findViewById(R.id.tv_transaction_type);//交易类型
		tv_money=(TextView) findViewById(R.id.tv_money);//金额
		tv_money_flow_type=(TextView) findViewById(R.id.tv_money_flow_type);//金额流动
		tv_time=(TextView) findViewById(R.id.tv_time);//时间
		tv_transaction_number=(TextView) findViewById(R.id.tv_transaction_number);//交易单号
		tv_remarks=(TextView) findViewById(R.id.tv_remarks);//备注
		tv_wallet_balance=(TextView) findViewById(R.id.tv_wallet_balance);//余额
		//加载数据
		position=getIntent().getExtras().getInt("position");
		walletdetail_list=(List<WalletDetail>) getIntent().getExtras().getSerializable("wallet_list");
		if(walletdetail_list.size()!=0){
			if(walletdetail_list.get(position).getMoney_flow_type().equals("支出")){
				money_flow_type="-";
			}else{
				money_flow_type="+";
			}
			tv_transaction_type.setText(walletdetail_list.get(position).getTransaction_type());//交易类型
			tv_money.setText(money_flow_type+walletdetail_list.get(position).getMoney());//金额
			tv_money_flow_type.setText(walletdetail_list.get(position).getMoney_flow_type());//金额流动
			tv_time.setText(walletdetail_list.get(position).getTime());//时间
			tv_transaction_number.setText(walletdetail_list.get(position).getTransaction_number());//交易单号
			tv_remarks.setText(walletdetail_list.get(position).getRemarks());//备注
		}
		//余额
		if(!TextUtils.isEmpty(balance)){
			tv_wallet_balance.setText(balance);
		}
	}
}
