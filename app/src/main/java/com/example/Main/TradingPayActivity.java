package com.example.Main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bestpay.cn.utils.Pay_Time_adapter;
import com.jhjpay.zyb.R;
import com.timepay.zyb.CustomDialog;
import com.timepay.zyb.DB_Manager;
import com.timepay.zyb.Model;
import com.timepay.zyb.PaySum;
import com.timepay.zyb.PayUtil;

public class TradingPayActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private Button starttime;//选择开始时间
	private Button stoptime;//选择结束时间
	private Button query_TradingDate;//查询交易记录
	private Button query_PayStatistical;//查询交易金额统计信息
	private ListView lv;
	private Button mLeftButton;//上一页
	private Button mRightButton;//下一页
	private Pay_Time_adapter pay_Time_adapter;//交易记录适配器
	private Cursor mCursor;
	private Model mModel;
	private DB_Manager db_Manger;//数据库操作类
	private String starttimes;//获得起始时间
	private String stoptimes;//获得终止时间
	private String nowtime;//当天时间
	private CustomDialog dialog;//自定义日期选择弹框
	private PayUtil payutil;
	private long length;
	private long length1;

	private TextView xj_amount,xj_time;//现金
	private TextView ye_amount,ye_time;//余额
	private TextView zfb_amount,zfb_time;//支付宝
	private TextView wx_amount,wx_time;//微信
	private TextView yl_amount,yl_time;//银联
	private TextView yzf_amount,yzf_time;//翼支付
	private TextView sum_amount;//总计
	private LinearLayout ll_tradingsum;//统计表格
	private LinearLayout ll_btn;//上下页
	private String paytype;//支付类型
	private double amount;//统计金额
	private double amount1;//现金
	private double amount2;//余额
	private double amount3;//支付宝
	private double amount4;//微信
	private double amount5;//银联
	private double amount6;//翼支付
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tradingpay_activity);
		MyAppLication.getInstance().addActivity(this);
		//寻找控件id
		findviewbyid();
		//方法辅助类
		payutil=new PayUtil(getApplicationContext());
		//数据库操作类实例化
		db_Manger=new DB_Manager(getApplicationContext());
		mLeftButton.setEnabled(false);
		mRightButton.setEnabled(false);
	}

	public void findviewbyid(){
		lv=(ListView)findViewById(R.id.lv);
		mLeftButton = (Button) findViewById(R.id.leftButton);
		mRightButton = (Button) findViewById(R.id.rightButton);
		starttime=(Button) findViewById(R.id.starttime);
		stoptime=(Button) findViewById(R.id.stoptime);
		query_PayStatistical=(Button) findViewById(R.id.query_PayStatistical);
		query_TradingDate=(Button) findViewById(R.id.query_TradingDate);

		xj_amount=(TextView) findViewById(R.id.xj_amount);
		xj_time=(TextView) findViewById(R.id.xj_time);
		ye_amount=(TextView) findViewById(R.id.ye_amount);
		ye_time=(TextView) findViewById(R.id.ye_time);
		zfb_amount=(TextView) findViewById(R.id.zfb_amount);
		zfb_time=(TextView) findViewById(R.id.zfb_time);
		wx_amount=(TextView) findViewById(R.id.wx_amount);
		wx_time=(TextView) findViewById(R.id.wx_time);
		yl_amount=(TextView) findViewById(R.id.yl_amount);
		yl_time=(TextView) findViewById(R.id.yl_time);
		yzf_amount=(TextView) findViewById(R.id.yzf_amount);
		yzf_time=(TextView) findViewById(R.id.yzf_time);
		sum_amount=(TextView) findViewById(R.id.sum_amount);
		ll_tradingsum=(LinearLayout) findViewById(R.id.ll_tradingsum);
		ll_btn=(LinearLayout) findViewById(R.id.ll_btn);
		img_back=(ImageButton) findViewById(R.id.img_back);

		starttime.setOnClickListener(this);
		stoptime.setOnClickListener(this);
		query_TradingDate.setOnClickListener(this);
		query_PayStatistical.setOnClickListener(this);
		mLeftButton.setOnClickListener(this);
		mRightButton.setOnClickListener(this);
		img_back.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.img_back:
			TradingPayActivity.this.finish();
			break;
			//选择开始日期
		case R.id.starttime:
			Dialog(starttime);
			break;
			//选择结束日期	
		case R.id.stoptime:
			Dialog(stoptime);
			break;
			//根据用户选择时间段查询交易信息
		case R.id.query_TradingDate:
			ll_tradingsum.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
			ll_btn.setVisibility(View.VISIBLE);
			Trading_limitData();
			//交易记录条数
			length=db_Manger.getTradingCount();
			checkButton(length);
			break;
			//查询各支付类型的金额统计信息	
		case R.id.query_PayStatistical:
			payutil.getTime();
			ll_tradingsum.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
			ll_btn.setVisibility(View.GONE);
			mLeftButton.setEnabled(false);
			mRightButton.setEnabled(false);
			clearAmount();
			getTradingSumTime();
			setAmountByPayType(0.0,"-1");
			break;
			//上一页
		case R.id.leftButton:
			trading_leftButton();
			//交易记录条数
			length=db_Manger.getTradingCount();
			checkButton(length);
			break;
			//下一页	
		case R.id.rightButton:
			trading_rightButton();
			//交易记录条数
			length=db_Manger.getTradingCount();
			checkButton(length);
			break;
		default:
			break;
		}

	}

	//分页查询交易记录
	public void Trading_limitData(){
		setTime();
		//创建一个Model的对象，表面这是首页，并且每页显示100个Item项
		mModel = new Model(0, 100);
		//mCursor查询到的是第0页的5个数据。
		mCursor = db_Manger.getAll_TradingItems(mModel.getIndex()*mModel.getView_Count(), mModel.getView_Count(),starttimes,stoptimes);
		System.out.println("mCursor = " + mCursor);
		//根据参数创建一个Pay_Time_adapter对象，并设给ListView。
		pay_Time_adapter = new Pay_Time_adapter(this, mCursor, mModel);
		lv.setAdapter(pay_Time_adapter);
	}

	//选择的时间段,查询交易记录
	public void setTime(){
		starttimes=starttime.getText().toString();
		stoptimes=stoptime.getText().toString();
		if("".trim().equals(starttimes)||null==starttimes&&
				"".trim().equals(stoptimes)||null==stoptimes){
			starttimes=PayUtil.getCurrentData1();
			stoptimes=PayUtil.getCurrentData2();

		}
	}

	/**
	 * 查询交易记录表，根据时间段统计金额
	 */
	public void getOrderDate(){
		//订单记录条数
		length1=db_Manger.getPayOrderCountByTime(starttimes, stoptimes);
		if(length1!=0){
			List<PaySum> paySums=db_Manger.queryTradingPaySum(starttimes, stoptimes, nowtime);
			for(int i=0;i<paySums.size();i++){
				paytype=String.valueOf(paySums.get(i).getPaytype());
				amount=paySums.get(i).getAmount();
				setAmountByPayType(amount,paytype);
			}
		}
	}

	//选择查询交易统计信息的时间段
	public void getTradingSumTime(){
		starttimes=starttime.getText().toString();
		stoptimes=stoptime.getText().toString();
		//当天时间 yyyy-MM-dd
		nowtime=PayUtil.getCurrentDay();
		//如果没有选择时间段，默认时间为当天
		if("".trim().equals(starttimes)||null==starttimes&&
				"".trim().equals(stoptimes)||null==stoptimes){
			starttimes=PayUtil.getCurrentData1();
			stoptimes=PayUtil.getCurrentData2();
			//查询订单表，统计金额
			getOrderDate();
			getVilible(nowtime);

		}else{
			String time1=(String) starttimes.subSequence(0, 10);
			String time2=(String) stoptimes.subSequence(0, 10);
			//开始时间是当天
			if(time1.equals(nowtime)){
				//查询订单表，统计金额
				getOrderDate();
				getVilible(nowtime);
			}else{
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dt1 = null;
				Date dt2 = null;
				try {
					dt1 = df.parse(time2);
					dt2 = df.parse(nowtime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//结束时间是当天
				if(dt1.getTime()>=dt2.getTime()){
					long length2=db_Manger.getPaySumCountByTime(time1, time2);
					//统计表中没有数据
					if(length2==0){
						//统计记录
						starttimes=PayUtil.getCurrentData1();
						stoptimes=PayUtil.getCurrentData2();
						//查询订单表，统计金额
						getOrderDate();
						getVilible(nowtime);
					}else{
						//根据时间段查询统计表
						List<PaySum> paySums1=db_Manger.queryTradingSum(time1,time2,time2);
						for(int i=0;i<paySums1.size();i++){
							paytype=String.valueOf(paySums1.get(i).getPaytype());
							amount=paySums1.get(i).getAmount();
							setAmountByPayType(amount,paytype);
						}
						starttimes=PayUtil.getCurrentData1();
						//根据时间段查询订单表
						List<PaySum> paySums2=db_Manger.queryTradingPaySum(starttimes, stoptimes,nowtime);
						for(int j=0;j<paySums2.size();j++){
							String type=String.valueOf(paySums2.get(j).getPaytype());
							double money=paySums2.get(j).getAmount();
							//支付宝
							if(type.equals("1")){
								money=money+amount1;
							}
							//现金
							if(type.equals("2")){
								money=money+amount2;
							}
							//余额
							if(type.equals("3")){
								money=money+amount3;
							}
							//微信
							if(type.equals("4")){
								money=money+amount4;
							}
							//银联
							if(type.equals("5")){
								money=money+amount5;
							}
							//翼支付
							if(type.equals("13")){
								money=money+amount6;
							}

							setAmountByPayType(money,type);
						}

						getVilible(time2);
					}
				}else{
					//根据时间段查询统计表,再统计
					List<PaySum> orderPaySums=db_Manger.queryTradingSum(time1, time2,time2);
					for(int i=0;i<orderPaySums.size();i++){
						paytype=String.valueOf(orderPaySums.get(i).getPaytype());
						amount=orderPaySums.get(i).getAmount();
						setAmountByPayType(amount,paytype);
					}
					getVilible(time2);
				}
			}
		}

	}

	/**
	 * 获得各支付类型统计金额
	 * @param amount 金额
	 * @param paytype 支付类型
	 */
	public void setAmountByPayType(double amount,String paytype){

		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");  
		//总计
		if(paytype.equals("-1")){
			double money1=Double.parseDouble(xj_amount.getText().toString());
			double money2=Double.parseDouble(ye_amount.getText().toString());
			double money3=Double.parseDouble(zfb_amount.getText().toString());
			double money4=Double.parseDouble(wx_amount.getText().toString());
			double money5=Double.parseDouble(yl_amount.getText().toString());
			double money6=Double.parseDouble(yzf_amount.getText().toString());
			amount=money1+money2+money3+money4+money5+money6;
			sum_amount.setText(df.format(amount));
		}
		//支付宝
		if(paytype.equals("1")){
			amount1=amount;
			zfb_amount.setText(df.format(amount1));
		}
		//现金
		if(paytype.equals("2")){
			amount2=amount;
			xj_amount.setText(df.format(amount2));
		}
		//余额
		if(paytype.equals("3")){
			amount3=amount;
			ye_amount.setText(df.format(amount3));
		}

		//微信
		if(paytype.equals("4")){
			amount4=amount;
			wx_amount.setText(df.format(amount4));
		}
		//银联
		if(paytype.equals("5")){
			amount5=amount;
			yl_amount.setText(df.format(amount5));
		}
		//翼支付
		if(paytype.equals("13")){
			amount6=amount;
			yzf_amount.setText(df.format(amount6));
		}
	}
	/**
	 * 获得统计时间
	 * @param time
	 */
	public void getVilible(String time){

		xj_time.setText(time);
		ye_time.setText(time);
		zfb_time.setText(time);
		wx_time.setText(time);
		yl_time.setText(time);
		yzf_time.setText(time);
	}
	/**
	 * 清空数据
	 */
	public void clearAmount(){
		xj_amount.setText("0.0");
		ye_amount.setText("0.0");
		zfb_amount.setText("0.0");
		wx_amount.setText("0.0");
		yl_amount.setText("0.0");
		yzf_amount.setText("0.0");
		amount1=0;
		amount2=0;
		amount3=0;
		amount4=0;
		amount5=0;
		amount6=0;
	}

	/**
	 * 如果页数小于或等于0，表示在第一页，向左的按钮设为不可用，向右的按钮设为可用。
	 * 如果总数目减前几页的数目，得到的是当前页的数目，如果比这一页要显示的少，则说明这是最后一页，向右的按钮不可用，向左的按钮可用。
	 * 如果不是以上两种情况，则说明页数在中间，两个按钮都设为可用。  
	 */
	private void checkButton(long length) {
		if(mModel.getIndex() <= 0) {
			mLeftButton.setEnabled(false);
			mRightButton.setEnabled(true);
		} else if(length - mModel.getIndex()*mModel.getView_Count() <= mModel.getView_Count()) {
			mRightButton.setEnabled(false);
			mLeftButton.setEnabled(true);
		} else {
			mLeftButton.setEnabled(true);
			mRightButton.setEnabled(true);
		}
	}
	//当查询交易记录时, 检查Button的可用性
	public void trading_leftButton(){
		//页数向前翻一页，同时将Cursor重新查一遍，然后changeCursor，notifyDataSetChanged。
		//检查Button的可用性。

		mModel.setIndex(mModel.getIndex() - 1);
		mCursor = db_Manger.getAll_TradingItems(mModel.getIndex()*mModel.getView_Count(), mModel.getView_Count(),starttimes,stoptimes);
		pay_Time_adapter.changeCursor(mCursor);
		pay_Time_adapter.notifyDataSetChanged();
	}
	//当查询交易记录时
	public void trading_rightButton(){
		//页数向后翻一页，同时将Cursor重新查一遍，然后changeCursor，notifyDataSetChanged。
		//检查Button的可用性。
		mModel.setIndex(mModel.getIndex() + 1);
		mCursor = db_Manger.getAll_TradingItems(mModel.getIndex()*mModel.getView_Count(), mModel.getView_Count(),starttimes,stoptimes);
		pay_Time_adapter.changeCursor(mCursor);
		pay_Time_adapter.notifyDataSetChanged();
	}

	//日期选择弹框
	public void Dialog(Button button){

		CustomDialog.Builder customBuilder = new CustomDialog.Builder(TradingPayActivity.this);

		customBuilder.setPositiveButton("确定", 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialog = customBuilder.create(button);
		dialog.show();
	}



}
