package com.jhj.WalletActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.Main.MyAppLication;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.View.XListView;
import com.jhj.View.XListView.IXListViewListener;
import com.jhjpay.zyb.R;
/**
 * 公告
 * @author Administrator
 *
 */
public class NoticeActivity extends Activity implements IXListViewListener{

	private ImageButton img_back;
	private XListView xlistview_notice;
	private LinearLayout ll_notice;
	private NoticeAdapter noticeAdapter;
	private List<Notice> noticelist;
	private final static int LOAD_SUC_FINISH = -1;
	private final static int REFRESH_LOADMORE = -2;
	private YWLoadingDialog mDialog;
	private long preTime;//上一次刷新的当前系统时间毫秒值 
	private boolean isLoadMore;//是否正在加载更多数据的标记 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_wallet_notice);
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NoticeActivity.this.finish();
			}
		});
		ll_notice=(LinearLayout) findViewById(R.id.ll_notice);
		xlistview_notice=(XListView) findViewById(R.id.listview_notice);
		xlistview_notice.setXListViewListener(this);
		xlistview_notice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NoticeActivity.this,NoticeDetailsByWebView.class);
				intent.putExtra("notice_url", noticelist.get(position-1).getNotice_url());
				startActivity(intent);
			}
		});
		getList();
		onShowSuc();
		//设置可以下拉刷新，默认就是true  
		xlistview_notice.setPullRefreshEnable(true);  
		//设置可以上拉加载，默认是false  
		xlistview_notice.setPullLoadEnable(true);  
	}

	public void getList(){
		noticelist = new ArrayList<Notice>();
		//模拟数据(测试用)
		Notice notice1 = new Notice();
		notice1.setNotice_title("标题1");;
		notice1.setNotice_time("2017.08.01 14:20.06");
		notice1.setNotice_url("https://www.baidu.com/");
		noticelist.add(notice1);
		Notice notice2 = new Notice();
		notice2.setNotice_title("标题2");;
		notice2.setNotice_time("2017.08.02 14:20.06");
		notice2.setNotice_url("https://www.baidu.com/");
		noticelist.add(notice2);

		Notice notice3 = new Notice();
		notice3.setNotice_title("标题3");
		notice3.setNotice_time("2017.08.08 14:20.06");
		notice3.setNotice_url("https://www.baidu.com/");
		noticelist.add(notice3);
	}
	public void getMoreList(){
		Notice notice1 = new Notice();
		notice1.setNotice_title("标题1");;
		notice1.setNotice_time("2017.08.01 14:20.06");
		notice1.setNotice_url("https://www.baidu.com/");
		noticelist.add(notice1);
		Notice notice2 = new Notice();
		notice2.setNotice_title("标题2");;
		notice2.setNotice_time("2017.08.02 14:20.06");
		notice2.setNotice_url("https://www.baidu.com/");
		noticelist.add(notice2);

		Notice notice3 = new Notice();
		notice3.setNotice_title("标题3");;
		notice3.setNotice_time("2017.08.08 14:20.06");
		notice3.setNotice_url("https://www.baidu.com/");
		noticelist.add(notice3);
	}
	/**
	 * 加载进度条
	 * @param view
	 */
	public void onShowSuc(){
		mDialog = new YWLoadingDialog(this);
		mDialog.show();
		handler.sendEmptyMessageDelayed(LOAD_SUC_FINISH,1500);
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				if(noticelist.size()!=0){
					xlistview_notice.setVisibility(View.VISIBLE);
					noticeAdapter = new NoticeAdapter(NoticeActivity.this,noticelist);
					xlistview_notice.setAdapter(noticeAdapter);
				}else{
					ll_notice.setVisibility(View.VISIBLE);
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
					noticelist.clear();  
					getList();  
					xlistview_notice.setVisibility(View.VISIBLE);
					noticeAdapter = new NoticeAdapter(NoticeActivity.this,noticelist);
					xlistview_notice.setAdapter(noticeAdapter);
					//刷新完毕，关闭下拉刷新效果  
					xlistview_notice.stopRefresh();  
				}  
				// 刷新listview  
				noticeAdapter.notifyDataSetChanged(); 
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
