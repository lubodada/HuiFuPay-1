package com.jhj.WalletActivity;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhjpay.zyb.R;

public class JieSuan_BankCardAdapter extends BaseAdapter {

	private List<BankCard> banklist;
	private Context mContext;
	
	public JieSuan_BankCardAdapter(Context mContext,List<BankCard> banklist){
		this.mContext=mContext;
		this.banklist=banklist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return banklist.size();
	}

	@Override
	public BankCard getItem(int position) {
		// TODO Auto-generated method stub
		return banklist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView==null) {
			convertView = View.inflate(mContext, R.layout.activity_wallet_jiesuan_bankcard_adapter, null);
			holder = new ViewHolder();
			//寻找控件id
			holder.re_bank=(RelativeLayout) convertView.findViewById(R.id.re_bank);
			holder.img_setdefault_jiecard=(ImageView) convertView.findViewById(R.id.img_setdefault_jiecard);
			holder.bank=(TextView) convertView.findViewById(R.id.bank);
			holder.bankcard_number=(TextView) convertView.findViewById(R.id.bankcard_number);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//赋值
		BankCard bankCard = getItem(position);
		
		holder.re_bank.setBackgroundResource(getImageByBank(bankCard.getBankcard()));
		holder.bank.setText(bankCard.getBankcard());
		holder.bankcard_number.setText(subString(bankCard.getBankcard_number()));

		return convertView;
	}
	/**
	 * 选择背景图片
	 * @param bank
	 * @return
	 */
	public int getImageByBank(String bank){
		int draw = R.drawable.bank_jianshe;
		if(bank.equals("中国工商银行")){
			draw=R.drawable.bank_gongshang;
		}
		if(bank.equals("中国农业银行")){
			draw=R.drawable.bank_nongye;
		}
		if(bank.equals("中国银行")){
			draw=R.drawable.bank_zhongguo;
		}
		if(bank.equals("中国建设银行")){
			draw=R.drawable.bank_jianshe;
		}
		if(bank.equals("中国光大银行")){
			draw=R.drawable.bank_guangda;
		}
		if(bank.equals("中国民生银行")){
			draw=R.drawable.bank_minsheng;
		}
		if(bank.equals("广东发展银行")){
			draw=R.drawable.bank_guangfa;
		}
		if(bank.equals("平安银行(深圳发展银行)")){
			draw=R.drawable.bank_pingan;
		}
		if(bank.equals("招商银行")){
			draw=R.drawable.bank_zhaoshang;
		}
		if(bank.equals("交通银行")){
			draw=R.drawable.bank_jiaotong;
		}
		if(bank.equals("华夏银行")){
			draw=R.drawable.bank_huaxia;
		}
		if(bank.equals("中信银行")){
			draw=R.drawable.bank_zhogxin;
		}
		if(bank.equals("兴业银行")){
			draw=R.drawable.bank_xingye;
		}
		if(bank.equals("邮政储蓄")){
			draw=R.drawable.bank_youzheng;
		}
		if(bank.equals("上海浦东发展银行")){
			draw=R.drawable.bank_pufa;
		}
		return draw;
	}
	public String subString(String str){
		return str.substring(0, 4)+"********"+str.substring(str.length()-4, str.length());
	}

	static class ViewHolder {
		private RelativeLayout re_bank;//银行图片
		private ImageView img_setdefault_jiecard;
		private TextView bank;//银行
		private TextView bankcard_number;//银行卡号
	}

}
