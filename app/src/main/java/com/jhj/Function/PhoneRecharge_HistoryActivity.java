package com.jhj.Function;

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

import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.View.XListView;
import com.jhj.View.XListView.IXListViewListener;
import com.jhjpay.zyb.R;
/**
 * 手机充值——历史记录
 * @author Administrator
 *
 */
public class PhoneRecharge_HistoryActivity extends Activity implements IXListViewListener{

	private final static int LOAD_SUC_FINISH = -1;
	private final static int REFRESH_LOADMORE = -2;
	private ImageButton img_back;
	private TextView tv_total_money,tv_total_number,no_recharge;
	private XListView xlistview_phonerecharge;
	private String total_money,total_number;
	private PhoneRechargeApapter phoneRechargeApapter;
	private List<PhoneRecharge> phList;
	private YWLoadingDialog mDialog;
	private long preTime;//上一次刷新的当前系统时间毫秒值 
	private boolean isLoadMore;//是否正在加载更多数据的标记 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_phonerecharge_history);
		mDialog = new YWLoadingDialog(PhoneRecharge_HistoryActivity.this);
		img_back = (ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhoneRecharge_HistoryActivity.this.finish();
			}
		});
		tv_total_money = (TextView) findViewById(R.id.tv_total_money);
		tv_total_number = (TextView) findViewById(R.id.tv_total_number);
		no_recharge = (TextView) findViewById(R.id.no_recharge);
		xlistview_phonerecharge = (XListView) findViewById(R.id.xlistview_phonerecharge);
		xlistview_phonerecharge.setXListViewListener(this);
		//设置可以下拉刷新，默认就是true  
		xlistview_phonerecharge.setPullRefreshEnable(true);  
		//设置可以上拉加载，默认是false  
		xlistview_phonerecharge.setPullLoadEnable(true); 
		getList();
		mDialog.show();
		handler.sendEmptyMessage(LOAD_SUC_FINISH);
	}

	public void getList(){
		phList = new ArrayList<PhoneRecharge>();
		PhoneRecharge phRecharge = new PhoneRecharge();
		phRecharge.setPhone_number("18765827102");
		phRecharge.setRecharge_money("50.0");
		phRecharge.setTime("2017-08-24 20:20:20");
		phRecharge.setTrading_number("54545641584");
		phRecharge.setRemarks("向18765827102充值50元");
		phList.add(phRecharge);
	}
	public void getMoreList(){
		PhoneRecharge phRecharge = new PhoneRecharge();
		phRecharge.setPhone_number("18765827102");
		phRecharge.setRecharge_money("50.0");
		phRecharge.setTime("2017-08-24 20:20:20");
		phRecharge.setTrading_number("54545641584");
		phRecharge.setRemarks("向18765827102充值50元");
		phList.add(phRecharge);
	}
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				if(phList.size()!=0){
					xlistview_phonerecharge.setVisibility(View.VISIBLE);
					phoneRechargeApapter = new PhoneRechargeApapter(PhoneRecharge_HistoryActivity.this,phList);
					xlistview_phonerecharge.setAdapter(phoneRechargeApapter);
				}else{
					no_recharge.setVisibility(View.VISIBLE);
				}
				break;
			case REFRESH_LOADMORE:
				if(isLoadMore){  
					//消息是上拉加载更多  
					getMoreList();
					isLoadMore = false;  
					//刷新完毕，关闭上拉加载效果  
					xlistview_phonerecharge.stopLoadMore();  
				}else{  
					//消息是下拉刷新  
					phList.clear();  
					getList();
					xlistview_phonerecharge.setVisibility(View.VISIBLE);
					phoneRechargeApapter = new PhoneRechargeApapter(PhoneRecharge_HistoryActivity.this,phList);
					xlistview_phonerecharge.setAdapter(phoneRechargeApapter);
					//刷新完毕，关闭下拉刷新效果  
					xlistview_phonerecharge.stopRefresh();  
				}  
				// 刷新listview  
				phoneRechargeApapter.notifyDataSetChanged(); 
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
			xlistview_phonerecharge.setRefreshTime(refreshData(preTime));  
		} 
		preTime = System.currentTimeMillis();
	}
}
