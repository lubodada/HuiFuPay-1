package com.timepay.zyb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jhj.SetUpActivity.ShangHuInfo_UploadPicturesActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class PayUtil {

	Context context;
	private DB_Manager db_Manager;
	public static final String PATTERN_STANDARD08W = "yyyyMMdd";
    public static final String PATTERN_STANDARD12W = "yyyyMMddHHmm";
    public static final String PATTERN_STANDARD14W = "yyyyMMddHHmmss";
    public static final String PATTERN_STANDARD17W = "yyyyMMddHHmmssSSS";
     
    public static final String PATTERN_STANDARD10H = "yyyy-MM-dd";
    public static final String PATTERN_STANDARD16H = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_STANDARD19H = "yyyy-MM-dd HH:mm:ss";
     
    public static final String PATTERN_STANDARD10X = "yyyy/MM/dd";
    public static final String PATTERN_STANDARD16X = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_STANDARD19X = "yyyy/MM/dd HH:mm:ss";

	public PayUtil(Context context) {
		this.context = context;
		db_Manager = new DB_Manager(context);
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDataTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new java.util.Date());
	}
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDataTime1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new java.util.Date());
	}

	/**
	 * 当天0点
	 * 
	 * @return  yyyy-MM-dd 00:00:00
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentData1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return sdf.format(new java.util.Date());
	}
	/**
	 * 当天24点
	 * @return yyyy-MM-dd 24:00:00
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentData2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 24:00:00");
		return sdf.format(new java.util.Date());
	}
	/**
	 * 当天0点
	 * @return yyyyMMdd000000
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentData3() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd000000");
		return sdf.format(new java.util.Date());
	}
	/**
	 * 当天24点
	 * @return yyyyMMdd240000
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentData4() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd240000");
		return sdf.format(new java.util.Date());
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new java.util.Date());
	}

	public static String subStartTime(String starttimes){//2017-08-29 08:08:54
		String starttime = starttimes.substring(0, 4)+starttimes.substring(5, 7)+starttimes.substring(8, 10)
				+starttimes.substring(11, 13)+starttimes.substring(14, 16)+starttimes.substring(16, 18);
		return starttime;
	}
	public static String subStopTime(String stoptimes){
		String stoptime = stoptimes.substring(0, 4)+stoptimes.substring(6, 7)+stoptimes.substring(9, 10)
				+stoptimes.substring(12, 13)+stoptimes.substring(15, 16)+stoptimes.substring(18, 19);
		return stoptime;
	}
	
	/**
     * @Title: getWantDate
     * @Description: 获取想要的时间格式
     * @author YFB
     * @param dateStr
     * @param wantFormat
     * @return
     */ 
    @SuppressLint("SimpleDateFormat")
	public static String getWantDate(String dateStr,String wantFormat){
        if(!"".equals(dateStr)&&dateStr!=null){
            String pattern = PATTERN_STANDARD14W;
            int len = dateStr.length();
            switch(len){
                case 8:pattern = PATTERN_STANDARD08W;break;
                case 12:pattern = PATTERN_STANDARD12W;break;
                case 14:pattern = PATTERN_STANDARD14W;break;
                case 17:pattern = PATTERN_STANDARD17W;break;
                case 10:pattern = (dateStr.contains("-"))?PATTERN_STANDARD10H:PATTERN_STANDARD10X;break;
                case 16:pattern = (dateStr.contains("-"))?PATTERN_STANDARD16H:PATTERN_STANDARD16X;break;
                case 19:pattern = (dateStr.contains("-"))?PATTERN_STANDARD19H:PATTERN_STANDARD19X;break;
                default:pattern = PATTERN_STANDARD14W;break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(wantFormat);
            try {
                SimpleDateFormat sdfStr = new SimpleDateFormat(pattern);
                Date date = sdfStr.parse(dateStr);
                dateStr = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateStr;
    }

	/**
	 * 得到前一天时间 type:时间格式
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getSubtractDay(String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		String nowtime = sdf.format(new java.util.Date());

		Date dt;
		String subtracttime = null;
		try {
			dt = sdf.parse(nowtime);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.DAY_OF_YEAR, -1);// 日期减1天
			Date dt1 = rightNow.getTime();
			subtracttime = sdf.format(dt1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subtracttime;

	}
	/**
	 * 得到前一天时间 type:时间格式
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getSubtractDay_yyyyMMddHHmmss(String nowtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dt;
		String subtracttime = null;
		try {
			dt = sdf.parse(nowtime);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.DAY_OF_YEAR, -1);// 日期减1天
			Date dt1 = rightNow.getTime();
			subtracttime = sdf.format(dt1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subtracttime;
		
	}
	/**
	 * 加一秒
	 * @return
	 */
	public static String addASecondTime(String time){
		String de_time = time.substring(13, 14).toString();
		int second = Integer.valueOf(de_time)+1;
		time = time.substring(0, 13)+String.valueOf(second);
		return time;
		
	}
	/**
	 * 得到当前时间往前三个月时间 type:时间格式
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getSubtractThreeMonth(String nowtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dt;
		String subtracttime = null;
		try {
			dt = sdf.parse(nowtime);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.MONTH, -3);// 日期减三个月
			Date dt1 = rightNow.getTime();
			subtracttime = sdf.format(dt1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subtracttime;
		
	}
	/**
	 * 判断是否是同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private boolean isSameDate() {
		String time1 = db_Manager.queryLastTradRecordTime();//数据库中有的最近的时间
		String time2 = PayUtil.getCurrentDataTime1();//当前时间
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = df.parse(time1);
			date2 = df.parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
				.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
				.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}

	/**
	 * 计算两个日期之间的间隔天数
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static int getGapCount(String starttime, String endtime)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = sdf.parse(starttime);
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startdate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Date enddate = sdf.parse(endtime);
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(enddate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime()
				.getTime()) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 得到最后统计的交易金额的时间的后一天时间
	 * 
	 * @return time
	 */
	public static String getAddDay(String lasttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date dt;
		String time = null;
		try {
			dt = sdf.parse(lasttime);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.DAY_OF_YEAR, 1);// 日期加1天
			Date dt1 = rightNow.getTime();
			time = sdf.format(dt1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;

	}

	/**
	 * 添加交易记录信息(模拟数据) myorderid 自己生成的id orderid 掌优宝id Paytype 支付方式 monery 金額
	 * status 状态 Mnumber 支付码 Remark 备注 time 时间
	 * 
	 * @return
	 */
	public static List<TradingPay> addPayData(String myorderid, String orderid,
			int Paytype, String monery, int Status, String Mnumber,
			String Remark, String time, String Items, Double Discount,
			String Couponid, int uploadstatus) {
		List<TradingPay> tradingPays = new ArrayList<TradingPay>();
		TradingPay tradingPay = new TradingPay();

		tradingPay.setMyorderid(myorderid);
		tradingPay.setOrderid(orderid);
		// 支付方式【
		// 扫一扫{1:支付宝，2:微信，3:翼支付}】【二维码{4:支付宝，5:微信，6:翼支付}】【7:刷卡】【8:现金】【9:积分+】
		tradingPay.setPaytype(Paytype);
		tradingPay.setMoney(monery);
		// 支付状态 1:待付款 4：已取消 7：已完成 10:已退款 13:支付中
		tradingPay.setStatus(Status);
		tradingPay.setMnumber(Mnumber);
		tradingPay.setRemark("测试数据");
		tradingPay.setTime(time);// 2017-02-28 18:00:05
		tradingPay.setItems(Items);
		tradingPay.setDiscount(Discount);
		tradingPay.setCouponid(Couponid);
		tradingPay.setUploadstatus(uploadstatus);
		tradingPays.add(tradingPay);

		return tradingPays;

	}

	// 获得所需时间
	public void getTime() {
		// 最早的订单记录时间
		String firstTradingTime = db_Manager.queryFirstTradingPayTime();
		// 最近一次交易记录的时间
		String lastTradingTime = db_Manager.queryLastTradingPayTime();
		// 最后一次统计的"有金额记录的时间"
		String lastPaySumTime = db_Manager.queryLastPaySumTime();
		
		// 登录当天时间
		String todayTime = getCurrentDay();
		// 登录的前一天时间
		String yesterday = PayUtil.getSubtractDay("yyyy-MM-dd");
		// 有交易记录
		if (lastTradingTime != null) {
			if(TextUtils.isEmpty(firstTradingTime)){
				toast("最早的订单时间为空！");
				return;
			}
			// 截取最早交易时间 为"yyyy-MM-dd"格式
			String firsttime = (String) firstTradingTime.subSequence(0, 10);
			// 截取最近交易时间 为"yyyy-MM-dd"格式
			String lasttime = (String) lastTradingTime.subSequence(0, 10);
			String starttimes = null;
			String stoptimes = null;
			String sumtime = null;
			// 最后交易记录时间不是登录当天
			if (!lasttime.equals(todayTime)) {
				// 没有统计记录
				if (lastPaySumTime == null) {
					// 最早和最晚订单记录时间一样
					if (firsttime.equals(lasttime)) {
						// 要统计的记录的时间段
						starttimes = lasttime + " 00:00:00";
						stoptimes = lasttime + " 24:00:00";
						sumtime = lasttime;
					} else {
						starttimes = firsttime + " 00:00:00";
						stoptimes = firsttime + " 24:00:00";
						sumtime = firsttime;
					}
					List<PaySum> paySums = db_Manager.queryTradingPaySum(
							starttimes, stoptimes, sumtime);

					db_Manager.addPaySumData(paySums);
					getTime();
				} else {
					// 最后一次统计的"有金额记录的时间"的后一天(要统计的记录的时间)
					String tradingtime = PayUtil.getAddDay(lastPaySumTime);
					addPaySum(lastPaySumTime, yesterday, tradingtime);
				}

			} else {
				// 没有统计记录
				if (lastPaySumTime == null) {
					// 最早交易记录时间和最晚时间不是同一天
					if (!firsttime.equals(lasttime)) {
						// 要统计的记录的时间段
						starttimes = firsttime + " 00:00:00";
						stoptimes = firsttime + " 24:00:00";
						sumtime = firsttime;
						List<PaySum> paySums = db_Manager.queryTradingPaySum(
								starttimes, stoptimes, sumtime);
						db_Manager.addPaySumData(paySums);
						getTime();
					}
				} else {
					// 最后一次统计的"有金额记录的时间"的后一天(要统计的记录的时间)
					String tradingtime = PayUtil.getAddDay(lastPaySumTime);
					addPaySum(lastPaySumTime, yesterday, tradingtime);
				}
			}
		}
	}

	/**
	 * 交易金额统计及添加
	 * 
	 * @param lasttime
	 * @param nowtime
	 * @param tradingtime
	 */
	public void addPaySum(String lastPaySumTime, String yesterday,
			String tradingtime) {
		// 最后统计时间和登录当天的前一天时间不等
		if (!yesterday.equals(lastPaySumTime)) {
			// 要统计的记录的时间段
			String starttimes = tradingtime + " 00:00:00";
			String stoptimes = tradingtime + " 24:00:00";
			List<PaySum> paySums = db_Manager.queryTradingPaySum(starttimes,
					stoptimes, tradingtime);
			if (paySums.size() == 0) {
				PaySum paySum = new PaySum();
				paySum.setPaytype(0);
				paySum.setAmount(0.00);
				paySum.setTime(tradingtime);
				paySums.add(paySum);
			}
			db_Manager.addPaySumData(paySums);
			getTime();
		}
	}

	public void toast(String str) {
		Toast.makeText(context, str,
				Toast.LENGTH_LONG).show();
	}
}
