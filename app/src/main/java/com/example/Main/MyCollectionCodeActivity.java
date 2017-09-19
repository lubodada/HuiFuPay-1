package com.example.Main;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestpay.cn.utils.ViewPagerAdapter_QR;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhjpay.zyb.R;

public class MyCollectionCodeActivity extends Activity {
	
	private ImageView  QR_code_one,QR_code_two,QR_code_three;
	private ImageButton img_back;
	private ViewPager mViewPager;
	private ViewPagerAdapter_QR mAdapter;
	private ArrayList<View> pageview;
	
	private Dialog dg;
	private TextView title;
	private Button determine;
	
	private YWLoadingDialog mDialog = null;
	private final int LOAD_SUC_FINISH = 0x001;
	private final int LOAD_FAIL_FINISH = 0x002;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dimissSuc();
				showDialog();//初始化弹框
				break;
			case LOAD_FAIL_FINISH:
				mDialog.dimissFail();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycollection_code);
		MyAppLication.getInstance().addActivity(this);
		initView();
		initData();//初始化数据
		onShowSuc();
	}
	/**
	 * 初始化控件
	 */
	public void initView(){
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(myonclick);
	}
	public void showDialog(){
		dg = new Dialog(MyCollectionCodeActivity.this, R.style.edit_AlertDialog_style);
		dg.setContentView(R.layout.activity_qrcode_dialog_prompt);
		title = (TextView) dg.findViewById(R.id.title);
		determine = (Button) dg.findViewById(R.id.determine);
		title.setText("左右滑动可切换二维码");
		determine.setOnClickListener(myonclick);
		dg.show();
		dg.setCanceledOnTouchOutside(false);
	}
	
	/**
	 * 初始化数据
	 */
	@SuppressLint("InflateParams")
	private void initData() {
		
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		//查找布局文件用LayoutInflater.inflate
		LayoutInflater inflater =this.getLayoutInflater();
		View view1 = inflater.inflate(R.layout.activity_qr_code_one, null);
		View view2 = inflater.inflate(R.layout.activity_qr_code_two, null);
		View view3 = inflater.inflate(R.layout.activity_qr_code_three, null);
		
		QR_code_one=(ImageView ) view1.findViewById(R.id.QR_code_one);
		QR_code_two=(ImageView ) view2.findViewById(R.id.QR_code_two);
		QR_code_three=(ImageView ) view3.findViewById(R.id.QR_code_three);
		

		//将view装入数组
		pageview =new ArrayList<View>();
		pageview.add(view1);
		pageview.add(view2);
		pageview.add(view3);
		mAdapter = new ViewPagerAdapter_QR(this, pageview);
		mViewPager.setAdapter(mAdapter);

		// 将ViewPager定位到中间页（Short.MAX_VALUE/2附近的图片资源数组第1个元素对应的页面）
		// 目的：1.图片个数 >1 才轮播 2.定位到中间页，向左向右都可滑
		if( pageview.size()> 1) {
			mViewPager.setCurrentItem(1, false);
		}
	}
	/**
	 * 加载进度条
	 * @param view
	 */
	public void onShowSuc(){
		mDialog = new YWLoadingDialog(this);
		mDialog.show();
		mHandler.sendEmptyMessageDelayed(LOAD_SUC_FINISH,1500);
	}
	public void onShowFail(View view){
		mDialog = new YWLoadingDialog(this);
		mDialog.show();
		mHandler.sendEmptyMessageDelayed(LOAD_FAIL_FINISH, 1500);
	}
	View.OnClickListener myonclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.img_back:
				MyCollectionCodeActivity.this.finish();
				break;
			case R.id.determine:
				dg.dismiss();
				break;
			default:
				break;
			}
			
		}
	};
		
	

}
