package com.jhj.Function;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MyAppLication;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhjpay.zyb.R;
import com.timepay.zyb.CustomDialog;
import com.timepay.zyb.DB_Manager;
import com.timepay.zyb.Model;
import com.timepay.zyb.PayUtil;

/**
 * 交易记录
 * 
 * @author Administrator
 *
 */
public class TransactionRecordActivity extends Activity implements
OnClickListener {

	private ImageButton img_back;
	private Button btn_starttime;// 选择开始时间
	private Button btn_stoptime;// 选择结束时间
	private Button query_TradingDate;// 查询交易记录
	private Button query_PayStatistical;// 查询交易金额统计信息
	private ListView lv;
	private TextView tv_statistics;// 显示统计信息
	private Button mLeftButton;// 上一页
	private Button mRightButton;// 下一页
	private LinearLayout ll_btn;// 上下页
	private TransRecordAdapter transRecordAdapter;// 交易记录适配器
	private Cursor mCursor;
	private Model mModel;
	private DB_Manager db_Manger;// 数据库操作类
	private String starttimes;// 获得起始时间 yyyy-MM-dd HH:mm:ss
	private String stoptimes;// 获得终止时间 yyyy-MM-dd HH:mm:ss
	private String starttime;// 获得起始时间 yyyyMMddHHmmss
	private String stoptime;// 获得终止时间 yyyyMMddHHmmss
	private String latelyTime;// 最近一次交易记录时间
	private String earliestTime;// 最早一次交易记录时间
	private CustomDialog dialog;// 自定义日期选择弹框
	private PayUtil payutil;
	private long length, count;
	private Select_order so;
	private Select_order_all soall;
	private YWLoadingDialog mDialog;
	SharedPreferences_util su = new SharedPreferences_util();
	private String delete_startTime, delete_stopTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_function_transrecord);
		MyAppLication.getInstance().addActivity(this);
		// 寻找控件id
		findviewbyid();
		// 方法辅助类
		payutil = new PayUtil(getApplicationContext());
		// 数据库操作类实例化
		db_Manger = new DB_Manager(getApplicationContext());
		mDialog = new YWLoadingDialog(TransactionRecordActivity.this);
		so = new Select_order();
		soall = new Select_order_all();
		mLeftButton.setEnabled(false);
		mRightButton.setEnabled(false);

		queryTransDataByTime();

	}

	private void findviewbyid() {
		// TODO Auto-generated method stub
		img_back = (ImageButton) findViewById(R.id.img_back);
		lv = (ListView) findViewById(R.id.lv);
		tv_statistics = (TextView) findViewById(R.id.tv_statistics);
		mLeftButton = (Button) findViewById(R.id.leftButton);
		mRightButton = (Button) findViewById(R.id.rightButton);
		btn_starttime = (Button) findViewById(R.id.starttime);
		btn_stoptime = (Button) findViewById(R.id.stoptime);
		query_PayStatistical = (Button) findViewById(R.id.query_PayStatistical);
		query_TradingDate = (Button) findViewById(R.id.query_TradingDate);
		ll_btn = (LinearLayout) findViewById(R.id.ll_btn);

		btn_starttime.setOnClickListener(this);
		btn_stoptime.setOnClickListener(this);
		query_TradingDate.setOnClickListener(this);
		query_PayStatistical.setOnClickListener(this);
		mLeftButton.setOnClickListener(this);
		mRightButton.setOnClickListener(this);
		img_back.setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mDialog.dismiss();
				Trading_limitData();
				// 交易记录条数
				length = db_Manger.getTransRecordCountBypayTime(starttime,stoptime);
				checkButton(length);
				break;
			case 2:
				String message = msg.getData().getString("message");
				toast(message);
				mDialog.dismiss();
				break;
			case 3:
				mDialog.dismiss();
				List<TransactionRecord_Count> trcount_list = new ArrayList<TransactionRecord_Count>();
				String ordercount = "";
				ArrayList list = msg.getData().getParcelableArrayList("orderListCount");
				trcount_list = (List<TransactionRecord_Count>) list.get(0);
				for (int i = 0; i < trcount_list.size(); i++) {
					ordercount = ordercount + "\n类型："
							+ getPayType(trcount_list.get(i).getOrderType()) + ""
							+ "  金额："+ trcount_list.get(i).getTotalMoney() + "元"+ ""
							+ "  笔数：" + trcount_list.get(i).getOrderCount()+ "笔"
							+ "\n";
				}
				tv_statistics.setText(ordercount);
				break;
			default:
				break;
			}
		};
	};

	//订单类型
	public String getPayType(String type){
		String orderType = null;
		if(type.equals("0")){
			orderType="订单总计";
		}
		if(type.equals("1")){
			orderType="支付宝";
		}
		if(type.equals("4")){
			orderType="微信支付";
		}
		if(type.equals("10")){
			orderType="易付宝";
		}
		if(type.equals("13")){
			orderType="翼支付";
		}
		if(type.equals("16")){
			orderType="京东钱包";
		}
		if(type.equals("25")){
			orderType="QQ钱包";
		}
		if(type.equals("91")){
			orderType="电信积分兑换";
		}

		return orderType;

	}

	public void toast(String str) {
		Toast.makeText(TransactionRecordActivity.this, str, Toast.LENGTH_SHORT)
		.show();
	}

	@SuppressWarnings("static-access")
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			deleteDataByTime();
			TransactionRecordActivity.this.finish();
			break;
			// 选择开始日期
		case R.id.starttime:
			Dialog(btn_starttime);
			break;
			// 选择结束日期
		case R.id.stoptime:
			Dialog(btn_stoptime);
			break;
			// 根据用户选择时间段查询交易信息
		case R.id.query_TradingDate:
			mDialog.show();
			tv_statistics.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
			ll_btn.setVisibility(View.VISIBLE);
			// 查询时间段
			getQueryTime();
			// 根据时间段查询数据库
			count = db_Manger.getTransRecordCountBypayTime(starttime, stoptime);
			// 如果数据库无数据
			if (count == 0) {
				so.runService(TransactionRecordActivity.this, starttime,
						stoptime, 20, handler, true);
			} else {
				latelyTime = db_Manger.queryLastTradRecordTime();//
				earliestTime = db_Manger.queryFirstTradRecordTime();//

				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Date dt1 = null;
				Date dt2 = null;
				Date dt3 = null;
				Date dt4 = null;
				try {
					// 数据库时间
					dt1 = df.parse(earliestTime);
					dt2 = df.parse(latelyTime);
					// 查询时间
					dt3 = df.parse(starttime);
					dt4 = df.parse(stoptime);
				} catch (ParseException e) {
					e.printStackTrace();
					toast("交易记录时间有错误！");
					mDialog.dismiss();
					return;
				}
				// 查询时间段在数据库已有时间段之间
				if (dt3.getTime() >= dt1.getTime()
						&& dt4.getTime() <= dt2.getTime()) {
					handler.sendEmptyMessage(1);
					return;
				}
				if (dt3.getTime() >= dt1.getTime()
						&& dt3.getTime() <= dt2.getTime()
						&& dt4.getTime() > dt2.getTime()) {

					so.runService(TransactionRecordActivity.this, latelyTime,
							stoptime, 20, handler, true);

					su.setPrefString(TransactionRecordActivity.this,
							"delete_startTime", latelyTime);
					su.setPrefString(TransactionRecordActivity.this,
							"delete_stopTime", stoptime);

					return;
				}
				if (dt3.getTime() < dt1.getTime()
						&& dt4.getTime() >= dt1.getTime()
						&& dt4.getTime() <= dt2.getTime()) {

					so.runService(TransactionRecordActivity.this, starttime,
							earliestTime, 20, handler, true);

					su.setPrefString(TransactionRecordActivity.this,
							"delete_startTime", starttime);
					su.setPrefString(TransactionRecordActivity.this,
							"delete_stopTime", earliestTime);
					return;
				}
				if (dt3.getTime() < dt1.getTime()
						&& dt4.getTime() > dt2.getTime()) {

					so.runService(TransactionRecordActivity.this, starttime,
							earliestTime, 20, handler, false);
					so.runService(TransactionRecordActivity.this, latelyTime,
							stoptime, 20, handler, true);
					// so.runService(TransactionRecordActivity.this, starttime,
					// stoptime, 10, handler, true);

					su.setPrefString(TransactionRecordActivity.this,
							"delete_startTime", latelyTime);
					su.setPrefString(TransactionRecordActivity.this,
							"delete_stopTime", stoptime);
					return;
				}
			}
			break;
			// 查询各支付类型的金额统计信息
		case R.id.query_PayStatistical:
			tv_statistics.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
			ll_btn.setVisibility(View.GONE);
			mDialog.show();
			getQueryTime();// 查询时间段
			soall.runService(TransactionRecordActivity.this, starttime,
					stoptime, handler);
			break;
			// 上一页
		case R.id.leftButton:
			trading_leftButton();
			// 交易记录条数
			length = db_Manger.getTradRecordCount();
			checkButton(length);
			break;
			// 下一页
		case R.id.rightButton:
			trading_rightButton();
			// 交易记录条数
			length = db_Manger.getTradRecordCount();
			checkButton(length);
			break;
		default:
			break;
		}
	}

	/**
	 * 获得查询时间段
	 */
	public void getQueryTime() {
		starttimes = btn_starttime.getText().toString();
		stoptimes = btn_stoptime.getText().toString();
		starttime = PayUtil
				.getWantDate(starttimes, PayUtil.PATTERN_STANDARD14W);
		stoptime = PayUtil.getWantDate(stoptimes, PayUtil.PATTERN_STANDARD14W);

		if (TextUtils.isEmpty(starttime)) {
			starttime = PayUtil.getCurrentData3();
		}
		if (TextUtils.isEmpty(stoptime)) {
			stoptime = PayUtil.getCurrentData4();
		}
	}

	/**
	 * 每次进入交易记录功能面时，查询数据
	 */
	public void queryTransDataByTime() {
		String StartTime;
		String StopTime;
		// 数据库记录总数
		length = db_Manger.getTradRecordCount();
		if (length == 0) {
			// 查询前三个月数据
			String befortime = PayUtil.getSubtractDay_yyyyMMddHHmmss(PayUtil
					.getCurrentData3());
			StartTime = PayUtil.getSubtractThreeMonth(befortime);
			StopTime = PayUtil.getSubtractDay_yyyyMMddHHmmss(PayUtil
					.getCurrentData4());
			so.runService(TransactionRecordActivity.this, StartTime, StopTime,
					50, handler, false);

		} else {
			StartTime = db_Manger.queryLastTradRecordTime();
			StopTime = PayUtil.getSubtractDay_yyyyMMddHHmmss(PayUtil
					.getCurrentData4());
			so.runService(TransactionRecordActivity.this, StartTime, StopTime,
					50, handler, false);
		}
	}

	/**
	 * 删除数据库多余数据
	 */
	@SuppressWarnings("static-access")
	public void deleteDataByTime() {
		delete_startTime = su.getPrefString(TransactionRecordActivity.this,
				"delete_startTime", null);
		delete_stopTime = su.getPrefString(TransactionRecordActivity.this,
				"delete_stopTime", null);
		db_Manger.deleteDataByTime(delete_startTime, delete_stopTime);
	}

	// 分页查询交易记录
	public void Trading_limitData() {

		// 创建一个Model的对象，表面这是首页，并且每页显示100个Item项
		mModel = new Model(0, 50);
		// mCursor查询到的是第0页的5个数据。
		mCursor = db_Manger.getAllTransRecordItems(
				mModel.getIndex() * mModel.getView_Count(),
				mModel.getView_Count(), starttime, stoptime);
		System.out.println("mCursor = " + mCursor);
		// 根据参数创建一个Pay_Time_adapter对象，并设给ListView。
		transRecordAdapter = new TransRecordAdapter(this, mCursor, mModel);
		lv.setAdapter(transRecordAdapter);
	}

	/**
	 * 如果页数小于或等于0，表示在第一页，向左的按钮设为不可用，向右的按钮设为可用。
	 * 如果总数目减前几页的数目，得到的是当前页的数目，如果比这一页要显示的少，则说明这是最后一页，向右的按钮不可用，向左的按钮可用。
	 * 如果不是以上两种情况，则说明页数在中间，两个按钮都设为可用。
	 */
	private void checkButton(long length) {
		if (mModel.getIndex() <= 0) {
			mLeftButton.setEnabled(false);
			mRightButton.setEnabled(true);
		} else if (length - mModel.getIndex() * mModel.getView_Count() <= mModel
				.getView_Count()) {
			mRightButton.setEnabled(false);
			mLeftButton.setEnabled(true);
		} else {
			mLeftButton.setEnabled(true);
			mRightButton.setEnabled(true);
		}
	}

	// 当查询交易记录时, 检查Button的可用性
	public void trading_leftButton() {
		// 页数向前翻一页，同时将Cursor重新查一遍，然后changeCursor，notifyDataSetChanged。
		// 检查Button的可用性。

		mModel.setIndex(mModel.getIndex() - 1);
		mCursor = db_Manger.getAllTransRecordItems(
				mModel.getIndex() * mModel.getView_Count(),
				mModel.getView_Count(), starttime, stoptime);
		transRecordAdapter.changeCursor(mCursor);
		transRecordAdapter.notifyDataSetChanged();
	}

	// 当查询交易记录时
	public void trading_rightButton() {
		// 页数向后翻一页，同时将Cursor重新查一遍，然后changeCursor，notifyDataSetChanged。
		// 检查Button的可用性。
		mModel.setIndex(mModel.getIndex() + 1);
		mCursor = db_Manger.getAllTransRecordItems(
				mModel.getIndex() * mModel.getView_Count(),
				mModel.getView_Count(), starttime, stoptime);
		transRecordAdapter.changeCursor(mCursor);
		transRecordAdapter.notifyDataSetChanged();
	}

	// 日期选择弹框
	public void Dialog(Button button) {

		CustomDialog.Builder customBuilder = new CustomDialog.Builder(
				TransactionRecordActivity.this);

		customBuilder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialog = customBuilder.create(button);
		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		deleteDataByTime();
		return super.onKeyDown(keyCode, event);
	}

}
