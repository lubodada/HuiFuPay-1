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
 * 订单
 * @author lb
 *
 */
public class ShopMall_OrderActivity extends Activity implements IXListViewListener{

	private final static int LOAD_SUC_FINISH = -1;
	private final static int REFRESH_LOADMORE = -2;
	private ImageButton img_back;
	private XListView xlistview_order;
	private TextView no_order;
	private ShopMall_OrderAdapter shopMall_OrderAdapter;
	private List<ShopMall> shopmall_list;
	private YWLoadingDialog mDialog;
	private long preTime;//上一次刷新的当前系统时间毫秒值 
	private boolean isLoadMore;//是否正在加载更多数据的标记 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_shopmall_order);
		mDialog = new YWLoadingDialog(ShopMall_OrderActivity.this);
		img_back = (ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShopMall_OrderActivity.this.finish();
			}
		});
		no_order = (TextView) findViewById(R.id.no_order);
		xlistview_order = (XListView) findViewById(R.id.xlistview_order);
		xlistview_order.setXListViewListener(this);
		//设置可以下拉刷新，默认就是true  
		xlistview_order.setPullRefreshEnable(true);  
		//设置可以上拉加载，默认是false  
		xlistview_order.setPullLoadEnable(true); 
		getList();
		mDialog.show();
		handler.sendEmptyMessage(LOAD_SUC_FINISH);

	}
	public void getList(){
		shopmall_list = new ArrayList<ShopMall>();
		
		ShopMall shopMall = new ShopMall();
		shopMall.setOrder_number("1234546");
		shopMall.setOrder_state("2");
		shopMall.setCard_type("翼开店专用版");
		shopMall.setCard_number("3");
		shopMall.setOrder_money("900.0");
		shopMall.setName("卢波");
		shopMall.setLocation("济南市天桥区时代总部基地4期");
		shopMall.setPhone("135416132564");
		shopMall.setTime("2017-08-23");
		
		shopmall_list.add(shopMall);
	}
	public void getMoreList(){
		
		ShopMall shopMall = new ShopMall();
		shopMall.setOrder_number("12427");
		shopMall.setOrder_state("3");
		shopMall.setCard_type("翼开店专用版");
		shopMall.setCard_number("1");
		shopMall.setOrder_money("300.0");
		shopMall.setName("卢波");
		shopMall.setLocation("济南市天桥区时代总部基地4期");
		shopMall.setPhone("135416132564");
		shopMall.setTime("2017-08-23");
		shopmall_list.add(shopMall);
	}
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				if(shopmall_list.size()!=0){
					xlistview_order.setVisibility(View.VISIBLE);
					shopMall_OrderAdapter = new ShopMall_OrderAdapter(ShopMall_OrderActivity.this,shopmall_list);
					xlistview_order.setAdapter(shopMall_OrderAdapter);
				}else{
					no_order.setVisibility(View.VISIBLE);
				}
				break;
			case REFRESH_LOADMORE:
				if(isLoadMore){  
					//消息是上拉加载更多  
					getMoreList();
					isLoadMore = false;  
					//刷新完毕，关闭上拉加载效果  
					xlistview_order.stopLoadMore();  
				}else{  
					//消息是下拉刷新  
					shopmall_list.clear();  
					getList();
					xlistview_order.setVisibility(View.VISIBLE);
					shopMall_OrderAdapter = new ShopMall_OrderAdapter(ShopMall_OrderActivity.this,shopmall_list);
					xlistview_order.setAdapter(shopMall_OrderAdapter);
					//刷新完毕，关闭下拉刷新效果  
					xlistview_order.stopRefresh();  
				}  
				// 刷新listview  
				shopMall_OrderAdapter.notifyDataSetChanged(); 
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
			xlistview_order.setRefreshTime(refreshData(preTime));  
		} 
		preTime = System.currentTimeMillis();
	}
}
