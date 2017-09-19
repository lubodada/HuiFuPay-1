package com.jhj.wallet.Fragment.Adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhj.wallet.FragmentActivity.WalletDetail_TransactionDetailActivity;
import com.jhjpay.zyb.R;

/**
 * 钱包明细适配器
 * @author Administrator
 *
 */
public class WalletDetailAdapter extends BaseAdapter {

	private List<WalletDetail> walletdetail_list;
	private Context mContext;
	
	public WalletDetailAdapter(Context mContext,List<WalletDetail> walletdetail_list){
		this.mContext=mContext;
		this.walletdetail_list=walletdetail_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return walletdetail_list.size();
	}

	@Override
	public WalletDetail getItem(int position) {
		// TODO Auto-generated method stub
		return walletdetail_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView==null) {
			convertView = View.inflate(mContext, R.layout.activity_wallet_walletdetail_adapter, null);
			holder = new ViewHolder();
			//寻找控件id
			holder.re_walletdetail=(RelativeLayout) convertView.findViewById(R.id.re_walletdetail);
			holder.image=(ImageView) convertView.findViewById(R.id.image);
			holder.tv_type=(TextView) convertView.findViewById(R.id.tv_type);
			holder.tv_money=(TextView) convertView.findViewById(R.id.tv_money);
			holder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//赋值
		WalletDetail walletDetail = getItem(position);
		
		holder.image.setBackgroundResource(getImageByType(walletDetail.getTransaction_type()));
		holder.tv_type.setText(walletDetail.getTransaction_type());
		holder.tv_money.setText(getMoneyFlowType(walletDetail.getMoney_flow_type())+walletDetail.getMoney());
		holder.tv_time.setText(walletDetail.getTime());
		
		holder.re_walletdetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,WalletDetail_TransactionDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putSerializable("wallet_list", (Serializable) walletdetail_list);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
				
			}
		});

		return convertView;
	}
	/**
	 * 选择图片
	 * @param type
	 * @return
	 */
	public int getImageByType(String type){
		int draw = R.drawable.wallet_trading;
		if(type.equals("充值") || type.equals("支付宝")){
			draw=R.drawable.wallet_trading;
		}
		if(type.equals("提现")){
			draw=R.drawable.trading_record_gatheringico_t1;
		}
		if(type.equals("转账")){
			draw=R.drawable.wallet_cash;
		}
		if(type.equals("利息")){
			draw=R.drawable.wallet_interest;
		}
		return draw;
	}
	/**
	 * 金额流动
	 * @param type
	 * @return
	 */
	public String getMoneyFlowType(String type){
		if(type.equals("收入")){
			type="+";
		}
		if(type.equals("支出")){
			type="-";
		}
		return type;
		
	}

	static class ViewHolder {
		private RelativeLayout re_walletdetail;
		private ImageView image;
		private TextView tv_type;
		private TextView tv_money;
		private TextView tv_time;
	}

}
