package com.jhj.WalletActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.Main.MyAppLication;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.View.XListView;
import com.jhj.View.XListView.IXListViewListener;
import com.jhj.wallet.Fragment.Adapter.WalletDetail;
import com.jhj.wallet.Fragment.Adapter.WalletDetailAdapter;
import com.jhjpay.zyb.R;
/**
 * 收益
 * @author Administrator
 *
 */
public class InterestIncomeActivity extends Activity implements IXListViewListener{

	private final static int LOAD_SUC_FINISH = -1;
	private final static int REFRESH_LOADMORE = -2;
	private ImageButton img_back;
	private TextView interest_percent,no_interest;
	private XListView listview_interest;
	private String percent;
	private WalletDetailAdapter walletdetailAdapter;
	private List<WalletDetail> walletdetail_list;
	private YWLoadingDialog mDialog;
	private long preTime;//上一次刷新的当前系统时间毫秒值 
	private boolean isLoadMore;//是否正在加载更多数据的标记 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_wallet_interest_income);
		mDialog = new YWLoadingDialog(InterestIncomeActivity.this);

		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InterestIncomeActivity.this.finish();
			}
		});
		interest_percent=(TextView) findViewById(R.id.interest_percent);
		no_interest=(TextView) findViewById(R.id.no_interest);
		listview_interest=(XListView)findViewById(R.id.listview_interest);
		listview_interest.setXListViewListener(this);
		getList();
		mDialog.show();
		//设置可以下拉刷新，默认就是true  
		listview_interest.setPullRefreshEnable(true);  
		//设置可以上拉加载，默认是false  
		listview_interest.setPullLoadEnable(true); 
		handler.sendEmptyMessage(LOAD_SUC_FINISH);
	}
	
	public void getList(){
		walletdetail_list = new ArrayList<WalletDetail>();
		//模拟数据(测试用)
		WalletDetail walletDetail1 = new WalletDetail();
		walletDetail1.setTransaction_type("充值");
		walletDetail1.setMoney_flow_type("收入");
		walletDetail1.setMoney("1.34");
		walletDetail1.setTime("2017-08-11 14:20.06");
		walletDetail1.setTransaction_number("AC123456789");
		walletDetail1.setRemarks("(18765827102)支付宝充值钱包。金额：1.00");
		walletdetail_list.add(walletDetail1);
		
		WalletDetail walletDetail2 = new WalletDetail();
		walletDetail2.setTransaction_type("提现");
		walletDetail2.setMoney_flow_type("支出");
		walletDetail2.setMoney("11.30");
		walletDetail2.setTime("2017-08-11 14:20.06");
		walletDetail2.setTransaction_number("AC123456789");
		walletDetail2.setRemarks("(18765827102)提现。金额：1.00");
		walletdetail_list.add(walletDetail2);
		
		WalletDetail walletDetail3 = new WalletDetail();
		walletDetail3.setTransaction_type("转账");
		walletDetail3.setMoney_flow_type("支出");
		walletDetail3.setMoney("10.34");
		walletDetail3.setTime("2017-08-11 14:20.06");
		walletDetail3.setTransaction_number("AC123456789");
		walletDetail3.setRemarks("(18765827102)转账。金额：1.00");
		walletdetail_list.add(walletDetail3);
		
		WalletDetail walletDetail4 = new WalletDetail();
		walletDetail4.setTransaction_type("利息");
		walletDetail4.setMoney_flow_type("收入");
		walletDetail4.setMoney("0.34");
		walletDetail4.setTime("2017-08-11 14:20.06");
		walletDetail4.setTransaction_number("AC123456789");
		walletDetail4.setRemarks("(18765827102)利息。金额：1.00");
		walletdetail_list.add(walletDetail4);
		
	}
	public void getMoreList(){
		//模拟数据(测试用)
		WalletDetail walletDetail1 = new WalletDetail();
		walletDetail1.setTransaction_type("充值");
		walletDetail1.setMoney_flow_type("收入");
		walletDetail1.setMoney("1.34");
		walletDetail1.setTime("2017-08-11 14:20.06");
		walletDetail1.setTransaction_number("AC123456789");
		walletDetail1.setRemarks("(18765827102)支付宝充值钱包。金额：1.00");
		walletdetail_list.add(walletDetail1);
		
		WalletDetail walletDetail2 = new WalletDetail();
		walletDetail2.setTransaction_type("提现");
		walletDetail2.setMoney_flow_type("支出");
		walletDetail2.setMoney("11.30");
		walletDetail2.setTime("2017-08-11 14:20.06");
		walletDetail2.setTransaction_number("AC123456789");
		walletDetail2.setRemarks("(18765827102)提现。金额：1.00");
		walletdetail_list.add(walletDetail2);
		
		WalletDetail walletDetail3 = new WalletDetail();
		walletDetail3.setTransaction_type("转账");
		walletDetail3.setMoney_flow_type("支出");
		walletDetail3.setMoney("10.34");
		walletDetail3.setTime("2017-08-11 14:20.06");
		walletDetail3.setTransaction_number("AC123456789");
		walletDetail3.setRemarks("(18765827102)转账。金额：1.00");
		walletdetail_list.add(walletDetail3);
		
		WalletDetail walletDetail4 = new WalletDetail();
		walletDetail4.setTransaction_type("利息");
		walletDetail4.setMoney_flow_type("收入");
		walletDetail4.setMoney("0.34");
		walletDetail4.setTime("2017-08-11 14:20.06");
		walletDetail4.setTransaction_number("AC123456789");
		walletDetail4.setRemarks("(18765827102)利息。金额：1.00");
		walletdetail_list.add(walletDetail4);
	}
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				if(walletdetail_list.size()!=0){
					listview_interest.setVisibility(View.VISIBLE);
					walletdetailAdapter = new WalletDetailAdapter(InterestIncomeActivity.this,walletdetail_list);
					listview_interest.setAdapter(walletdetailAdapter);
				}else{
					no_interest.setVisibility(View.VISIBLE);
				}
				break;
			case REFRESH_LOADMORE:
				if(isLoadMore){  
					//消息是上拉加载更多  
					getMoreList();  
					isLoadMore = false;  
					//刷新完毕，关闭上拉加载效果  
					listview_interest.stopLoadMore();  
				}else{  
					//消息是下拉刷新  
					walletdetail_list.clear();  
					getList();  
					listview_interest.setVisibility(View.VISIBLE);
					walletdetailAdapter = new WalletDetailAdapter(InterestIncomeActivity.this,walletdetail_list);
					listview_interest.setAdapter(walletdetailAdapter);
					//刷新完毕，关闭下拉刷新效果  
					listview_interest.stopRefresh();  
				}  
				// 刷新listview  
				walletdetailAdapter.notifyDataSetChanged(); 
				break;
			default:
				break;
			}
		};
	};
	@Override
	public void onRefresh() {
		// 下拉刷新,实际发送Handler  
		//发送一个空消息，延迟两秒后告知刷新数据  
		handler.sendEmptyMessageDelayed(REFRESH_LOADMORE, 1000);  
		onLoad();
	}

	@Override
	public void onLoadMore() {
		//标记正在加载更多，发送Handler  
		isLoadMore = true;  
		handler.sendEmptyMessageDelayed(REFRESH_LOADMORE, 1000); 
		onLoad();
	}
	@SuppressLint("SimpleDateFormat")
	private String refreshData(long preTime) {  
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(preTime));  
	}
	private void onLoad(){
		//添加上一次刷新的时间：  
		if(preTime != 0){  
			listview_interest.setRefreshTime(refreshData(preTime));  
		} 
		preTime = System.currentTimeMillis();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
}
