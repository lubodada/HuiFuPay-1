package com.jhj.wallet.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.View.XListView;
import com.jhj.View.XListView.IXListViewListener;
import com.jhj.wallet.Fragment.Adapter.WalletDetail;
import com.jhj.wallet.Fragment.Adapter.WalletDetailAdapter;
import com.jhjpay.zyb.R;

/**
 * 转账
 * @author Administrator
 *
 */
public class FourFragment extends Fragment implements IXListViewListener{
	
	private TextView tv_empty;
	private XListView xlistview_notice;
	private WalletDetailAdapter walletdetailAdapter;
	private List<WalletDetail> walletdetail_list;
	private final static int LOAD_SUC_FINISH = -1;
	private final static int REFRESH_LOADMORE = -2;
	private YWLoadingDialog mDialog;
	private long preTime;//上一次刷新的当前系统时间毫秒值 
	private boolean isLoadMore;//是否正在加载更多数据的标记 
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_wallet_fragment_transfer, container, false);
		
		tv_empty=(TextView) view.findViewById(R.id.tv_empty);
		xlistview_notice=(XListView) view.findViewById(R.id.listview_notice);
		xlistview_notice.setXListViewListener(this);
		getList();
		onShowSuc();
		//设置可以下拉刷新，默认就是true  
		xlistview_notice.setPullRefreshEnable(true);  
		//设置可以上拉加载，默认是false  
		xlistview_notice.setPullLoadEnable(true); 
		return view;
	}
	public void getList(){
		walletdetail_list = new ArrayList<WalletDetail>();
		//模拟数据(测试用)
		WalletDetail walletDetail3 = new WalletDetail();
		walletDetail3.setTransaction_type("转账");
		walletDetail3.setMoney_flow_type("支出");
		walletDetail3.setMoney("10.34");
		walletDetail3.setTime("2017-08-11 14:20.06");
		walletDetail3.setTransaction_number("AC123456789");
		walletDetail3.setRemarks("(18765827102)转账。金额：1.00");
		walletdetail_list.add(walletDetail3);
		
	}
	public void getMoreList(){
		//模拟数据(测试用)
		WalletDetail walletDetail3 = new WalletDetail();
		walletDetail3.setTransaction_type("转账");
		walletDetail3.setMoney_flow_type("支出");
		walletDetail3.setMoney("10.34");
		walletDetail3.setTime("2017-08-11 14:20.06");
		walletDetail3.setTransaction_number("AC123456789");
		walletDetail3.setRemarks("(18765827102)转账。金额：1.00");
		walletdetail_list.add(walletDetail3);
	}
	/**
	 * 加载进度条
	 * @param view
	 */
	public void onShowSuc(){
		mDialog = new YWLoadingDialog(getActivity());
		mDialog.show();
		handler.sendEmptyMessageDelayed(LOAD_SUC_FINISH,1000);
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				if(walletdetail_list.size()!=0){
					xlistview_notice.setVisibility(View.VISIBLE);
					walletdetailAdapter = new WalletDetailAdapter(getActivity(),walletdetail_list);
					xlistview_notice.setAdapter(walletdetailAdapter);
				}else{
					tv_empty.setVisibility(View.VISIBLE);
				}
				break;
			case REFRESH_LOADMORE:
				if(isLoadMore){  
					//消息是上拉加载更多  
					getMoreList();  
					isLoadMore = false;  
					//刷新完毕，关闭上拉加载效果  
					xlistview_notice.stopLoadMore();  
				}else{  
					//消息是下拉刷新  
					walletdetail_list.clear();  
					getList();  
					xlistview_notice.setVisibility(View.VISIBLE);
					walletdetailAdapter = new WalletDetailAdapter(getActivity(),walletdetail_list);
					xlistview_notice.setAdapter(walletdetailAdapter);
					//刷新完毕，关闭下拉刷新效果  
					xlistview_notice.stopRefresh();  
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
		handler.sendEmptyMessageDelayed(REFRESH_LOADMORE, 2000);  
		onLoad();
	}

	@Override
	public void onLoadMore() {
		//标记正在加载更多，发送Handler  
		isLoadMore = true;  
		handler.sendEmptyMessageDelayed(REFRESH_LOADMORE, 2000); 
		onLoad();
	}
	@SuppressLint("SimpleDateFormat")
	private String refreshData(long preTime) {  
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(preTime));  
	}
	private void onLoad(){
		//添加上一次刷新的时间：  
		if(preTime != 0){  
			xlistview_notice.setRefreshTime(refreshData(preTime));  
		} 
		preTime = System.currentTimeMillis();
	}

}
