package com.timepay.zyb;

import java.util.ArrayList;
import java.util.List;

import com.jhj.Function.TransactionRecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库操作类
 * @author 
 */
public class DB_Manager {

	private MySqliteHelper helper;
	private SQLiteDatabase db;
	private String tag = "DB_Manager";

	public DB_Manager(Context context)
	{
		Log.d(tag, "DB_Manager开始");
		helper = new MySqliteHelper(context);	
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
//		helper.deleteDatabase(context);
		db = helper.getWritableDatabase();

	}

	/**
	 * 添加交易支付信息
	 * @param goos
	 */
	public String addPayData(List<TradingPay> tradingPays)
	{	
		db = helper.getWritableDatabase();
		
		//添加数据时，若数据库中交易记录超过50000条，则先删除最早的一条，再添加最新一条
		if(getTradingCount()>=50000){
			deleteFirstData();
		}
		//设置标签 "0"为添加数据失败
		String  flag = "0";
		Log.d(tag, "正在添加交易支付信息....");
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			for (TradingPay tradingPay : tradingPays)
			{
				db.execSQL(" INSERT INTO " + MySqliteHelper.TradingPay_TABLE_NAME
						+ " VALUES(null,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[] { tradingPay.getMyorderid(), tradingPay.getOrderid(),
								tradingPay.getPaytype(), tradingPay.getMoney(), tradingPay.getStatus(), 
								tradingPay.getMnumber(), tradingPay.getRemark(), tradingPay.getTime(),
								tradingPay.getItems(), tradingPay.getDiscount(), tradingPay.getCouponid(),tradingPay.getUploadstatus() });
			}

			db.setTransactionSuccessful(); // 设置事务成功完成
			//"1"为发货成功
			flag = "1";
			Log.d(tag, "交易支付信息添加成功");
		}catch (Exception e) {
			// TODO: handle exception
			Log.d(tag, "交易支付信息添加失败");
			e.printStackTrace();
		}
		finally
		{
			db.endTransaction(); //结束事务
			db.close();
		}

		return flag;
	}


	/**
	 * 查询某个时间段的交易信息
	 * @return
	 */
	public List<TradingPay> queryOneDayPay(String starttime,String stoptime)
	{
		db = helper.getReadableDatabase();
		List<TradingPay> tradingPays = new ArrayList<TradingPay>();

		Cursor c = queryonedaypay(starttime,stoptime);

		if( c == null || "".equals(c)){
			Log.d(tag, "查询无数据");
			return tradingPays;
		}

		while (c.moveToNext())
		{
			TradingPay tradingPay = new TradingPay();

			tradingPay.setMyorderid(c.getString(c.getColumnIndex("myorderid")));
			tradingPay.setOrderid(c.getString(c.getColumnIndex("orderid")));
			tradingPay.setPaytype(c.getInt(c.getColumnIndex("paytype")));
			tradingPay.setMoney(c.getString(c.getColumnIndex("money")));
			tradingPay.setStatus(c.getInt(c.getColumnIndex("status")));
			tradingPay.setMnumber(c.getString(c.getColumnIndex("mnumber")));
			tradingPay.setRemark(c.getString(c.getColumnIndex("remark")));
			tradingPay.setTime(c.getString(c.getColumnIndex("time")));
			tradingPay.setItems(c.getString(c.getColumnIndex("items")));
			tradingPay.setDiscount(c.getDouble(c.getColumnIndex("discount")));
			tradingPay.setCouponid(c.getString(c.getColumnIndex("couponid")));
			tradingPay.setUploadstatus(c.getInt(c.getColumnIndex("uploadstatus")));
			tradingPays.add(tradingPay);
		}
		c.close();
		db.close();

		return tradingPays;
	}
	//select * from tblName where rDate Between '2008-06-10' and  '2008-06-12' 
	public Cursor queryonedaypay(String starttime,String stoptime)
	{
		Cursor c = db.rawQuery(" SELECT * FROM " 
				+ MySqliteHelper.TradingPay_TABLE_NAME+" where time >= ? and time <= ? ", 
				new String[]{starttime,stoptime});

		return c;
	}

	/**
	 * 查询全部交易信息，按时间从最近开始显示
	 * @return
	 */
	public List<TradingPay> queryAllPayData()
	{
		db = helper.getReadableDatabase();
		List<TradingPay> tradingPays = new ArrayList<TradingPay>();

		Cursor c = queryallpaydata();

		if( c == null || "".equals(c)){
			Log.d(tag, "查询无数据");
			return tradingPays;
		}

		while (c.moveToNext())
		{
			TradingPay tradingPay = new TradingPay();

			tradingPay.setMyorderid(c.getString(c.getColumnIndex("myorderid")));
			tradingPay.setOrderid(c.getString(c.getColumnIndex("orderid")));
			tradingPay.setPaytype(c.getInt(c.getColumnIndex("paytype")));
			tradingPay.setMoney(c.getString(c.getColumnIndex("money")));
			tradingPay.setStatus(c.getInt(c.getColumnIndex("status")));
			tradingPay.setMnumber(c.getString(c.getColumnIndex("mnumber")));
			tradingPay.setRemark(c.getString(c.getColumnIndex("remark")));
			tradingPay.setTime(c.getString(c.getColumnIndex("time")));
			tradingPay.setItems(c.getString(c.getColumnIndex("items")));
			tradingPay.setDiscount(c.getDouble(c.getColumnIndex("discount")));
			tradingPay.setCouponid(c.getString(c.getColumnIndex("couponid")));
			tradingPay.setUploadstatus(c.getInt(c.getColumnIndex("uploadstatus")));
			
			tradingPays.add(tradingPay);
		}
		c.close();
		db.close();

		return tradingPays;
	}
	//SELECT Company, OrderNumber FROM Orders ORDER BY Company DESC 
	public Cursor queryallpaydata(){
		Cursor c = db.rawQuery(" SELECT * FROM " 
				+ MySqliteHelper.TradingPay_TABLE_NAME+" ORDER BY time DESC ", null);

		return c; 
	}

	/**
	 * 查询最近一次交易记录的时间
	 * @return SELECT LAST(OrderPrice) AS LastOrderPrice FROM Orders
	 */
	public String queryLastTradingPayTime(){
		String lastTradingTime = null;
		db = helper.getReadableDatabase();
		String sql = " SELECT time FROM "
				+MySqliteHelper.TradingPay_TABLE_NAME+" ORDER BY time DESC limit ?,?";
		Cursor mCursor = db.rawQuery(sql,new String[]{"0","1"});
		while (mCursor.moveToNext()) {
			
			lastTradingTime=mCursor.getString(mCursor.getColumnIndex("time"));
		}
		
		return lastTradingTime;
	}
	/**
	 * 查询最早一次交易记录的时间
	 * @return SELECT LAST(OrderPrice) AS LastOrderPrice FROM Orders
	 */
	public String queryFirstTradingPayTime(){
		String lastTradingTime = null;
		db = helper.getReadableDatabase();
		String sql = " SELECT time FROM "
				+MySqliteHelper.TradingPay_TABLE_NAME+" ORDER BY time ASC limit ?,?";
		Cursor mCursor = db.rawQuery(sql,new String[]{"0","1"});
		while (mCursor.moveToNext()) {
			
			lastTradingTime=mCursor.getString(mCursor.getColumnIndex("time"));
		}
		
		return lastTradingTime;
	}
	/**
	 * 查询上一次金额统计的时间
	 * @return SELECT LAST(OrderPrice) AS LastOrderPrice FROM Orders
	 */
	public String queryLastPaySumTime(){
		String lastPaySumTime = null;
		db = helper.getReadableDatabase();
		String sql = " SELECT time FROM "
				+MySqliteHelper.PaySum_TABLE_NAME+" ORDER BY time DESC limit ?,?";
		Cursor mCursor = db.rawQuery(sql,new String[]{"0","1"});
		while (mCursor.moveToNext()) {

			lastPaySumTime=mCursor.getString(mCursor.getColumnIndex("time"));
		}

		return lastPaySumTime;
	}

	/**
	 * 查询交易记录表，分别统计各支付类型的交易金额总和
	 * 支付方式【 0:无交易  1:支付宝，2:现金，3:余额，4:微信,5:银联，13:翼支付}】
	 * 支付状态  支付状态 1:待付款 4：已取消 7：已完成 10:已退款 13:支付中
	 * @return SELECT Customer,SUM(OrderPrice) FROM Orders GROUP BY Customer
	 */
	public List<PaySum> queryTradingPaySum(String starttime,String stoptime,String tradingtime){
		List<PaySum> paySums=new ArrayList<PaySum>();
		db = helper.getReadableDatabase();
		String sql = " SELECT status,time,paytype,SUM(money) AS amount FROM "
				+MySqliteHelper.TradingPay_TABLE_NAME
				+" where time >= ? and time <= ? and status = ? GROUP BY paytype ";
		Cursor mCursor = db.rawQuery(sql,new String[]{starttime,stoptime,String.valueOf(7)});
		while (mCursor.moveToNext()) {
			PaySum paySum=new PaySum();

			paySum.setPaytype(mCursor.getInt(mCursor.getColumnIndex("paytype")));
			paySum.setAmount(mCursor.getDouble(mCursor.getColumnIndex("amount")));
			paySum.setTime(tradingtime);
			paySums.add(paySum);
		}

		return paySums;
	}
	/**
	 * 查询交易统计表，分别统计各支付类型的交易金额总和
	 * 支付方式【 0:无交易  1:支付宝，2:现金，3:余额，4:微信,5:银联，13:翼支付}】
	 * @return SELECT Customer,SUM(OrderPrice) FROM Orders GROUP BY Customer
	 */
	public List<PaySum> queryTradingSum(String starttime,String stoptime,String tradingtime){
		List<PaySum> paySums=new ArrayList<PaySum>();
		db = helper.getReadableDatabase();
		String sql = " SELECT paytype,time,SUM(amount) AS amount FROM "
				+MySqliteHelper.PaySum_TABLE_NAME
				+" where time >= ? and time <= ? GROUP BY paytype ";
		Cursor mCursor = db.rawQuery(sql,new String[]{starttime,stoptime});
		while (mCursor.moveToNext()) {
			PaySum paySum=new PaySum();
			
			paySum.setPaytype(mCursor.getInt(mCursor.getColumnIndex("paytype")));
			paySum.setAmount(mCursor.getDouble(mCursor.getColumnIndex("amount")));
			paySum.setTime(tradingtime);
			paySums.add(paySum);
		}
		
		return paySums;
	}

	/**
	 * 添加金额统计信息到金额统计表(PaySum_TABLE_NAME)
	 * @param paySums
	 */
	public String addPaySumData(List<PaySum> paySums)
	{	
		db = helper.getWritableDatabase();
		//设置标签 "0"为添加数据失败
		String  flag = "0";
		Log.d(tag, "正在添加金额统计信息....");
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			for (PaySum paySum : paySums)
			{
				db.execSQL(" INSERT INTO " + MySqliteHelper.PaySum_TABLE_NAME+ " VALUES(null,?, ?, ?) ", 
						new Object[] { paySum.getPaytype(), paySum.getAmount(),paySum.getTime() });
			}

			db.setTransactionSuccessful(); // 设置事务成功完成
			//"1"为发货成功
			flag = "1";
			Log.d(tag, "交易金额统计信息添加成功");
		}catch (Exception e) {
			// TODO: handle exception
			Log.d(tag, "交易金额统计信息添加失败");
			e.printStackTrace();
		}
		finally
		{
			db.endTransaction(); //结束事务
			db.close();
		}

		return flag;
	}

	/**
	 * 查询支付统计表中各支付类型的"日/月/年统计信息"
	 * @param starttime 开始时间  例：2017-03-03 17:18:19
	 * @param stoptime 结束时间
	 * @return
	 */
	public List<PaySum> queryMonthPaySum(String starttime,String stoptime){
		
		if("".trim().equals(starttime)||null==starttime&&
				"".trim().equals(stoptime)||null==stoptime){
			starttime=PayUtil.getCurrentData1();
			stoptime=PayUtil.getCurrentData2();
			
		}
		db = helper.getReadableDatabase();
		//截取时间 例：2017-03-03
		String Starttime=(String) starttime.subSequence(0, 10);
		String Endtime=(String) stoptime.subSequence(0, 10);
		
		List<PaySum> paySums = new ArrayList<PaySum>();

		Cursor c = queryMonthpaysum(Starttime,Endtime);

		if( c == null || "".equals(c)){
			Log.d(tag, "查询无数据");
			return paySums;
		}

		while (c.moveToNext())
		{
			PaySum paySum = new PaySum();

			paySum.setPaytype(c.getInt(c.getColumnIndex("paytype")));
			paySum.setAmount(c.getDouble(c.getColumnIndex("amount")));
			paySum.setTime(c.getString(c.getColumnIndex("time")));
			paySums.add(paySum);
		}
		c.close();
		db.close();

		return paySums;
	}
	//select * from tblName where rDate Between '2008-06-10' and  '2008-06-12' 
	public Cursor queryMonthpaysum(String Starttime,String Endtime)
	{
		Cursor c = db.rawQuery(" SELECT * FROM " 
				+ MySqliteHelper.PaySum_TABLE_NAME+" where time >= ? and time <= ? ", 
				new String[]{Starttime,Endtime});

		return c;
	}
	/**
	 * 查询支付统计表中各支付类型的"日统计信息"
	 * @return
	 */
	public List<PaySum> queryDayPaySum(String daytime)
	{
		db = helper.getReadableDatabase();
		List<PaySum> paySums = new ArrayList<PaySum>();

		Cursor c = queryDaypaysum(daytime);

		if( c == null || "".equals(c)){
			Log.d(tag, "查询无数据");
			return paySums;
		}

		while (c.moveToNext())
		{
			PaySum paySum = new PaySum();

			paySum.setPaytype(c.getInt(c.getColumnIndex("paytype")));
			paySum.setAmount(c.getDouble(c.getColumnIndex("amount")));
			paySum.setTime(c.getString(c.getColumnIndex("time")));

			paySums.add(paySum);
		}
		c.close();
		db.close();

		return paySums;
	}
	//select * from tblName where rDate Between '2008-06-10' and  '2008-06-12' 
	public Cursor queryDaypaysum(String daytime)
	{
		Cursor c = db.rawQuery(" SELECT * FROM " 
				+ MySqliteHelper.PaySum_TABLE_NAME+" where time = ? ", 
				new String[]{daytime});

		return c;
	}

	/**
	 * 退款时根据订单ID(orderid)查询对应的交易信息
	 * @return
	 */
	public List<TradingPay> queryRefundDate(String orderid){
		db = helper.getReadableDatabase();
		List<TradingPay> tradingPays = new ArrayList<TradingPay>();

		Cursor c = queryperiodtimePay(orderid);

		if( c == null || "".equals(c)){
			Log.d(tag, "查询无数据");
			return tradingPays;
		}

		while (c.moveToNext()){
			TradingPay tradingPay = new TradingPay();

			tradingPay.setMyorderid(c.getString(c.getColumnIndex("myorderid")));
			tradingPay.setOrderid(c.getString(c.getColumnIndex("orderid")));
			tradingPay.setPaytype(c.getInt(c.getColumnIndex("paytype")));
			tradingPay.setMoney(c.getString(c.getColumnIndex("money")));
			tradingPay.setStatus(c.getInt(c.getColumnIndex("status")));
			tradingPay.setMnumber(c.getString(c.getColumnIndex("mnumber")));
			tradingPay.setRemark(c.getString(c.getColumnIndex("remark")));
			tradingPay.setTime(c.getString(c.getColumnIndex("time")));
			tradingPay.setItems(c.getString(c.getColumnIndex("items")));
			tradingPay.setDiscount(c.getDouble(c.getColumnIndex("discount")));
			tradingPay.setCouponid(c.getString(c.getColumnIndex("couponid")));
			tradingPay.setUploadstatus(c.getInt(c.getColumnIndex("uploadstatus")));
			
			tradingPays.add(tradingPay);
		}
		c.close();
		db.close();

		return tradingPays;
	}
	//select * from tblName where rDate Between '2008-06-10' and  '2008-06-12' 
	public Cursor queryperiodtimePay(String orderid)
	{
		Cursor c = db.rawQuery(" SELECT * FROM " 
				+ MySqliteHelper.TradingPay_TABLE_NAME+" where orderid = ? ", 
				new String[]{orderid});

		return c;
	}
	/**
	 * 退款时根据退款订单的支付类型(paytype)和时间(time)查询统计表中对应的统计金额(amount)
	 * @return
	 */
	public double querySumAmount(List<TradingPay> tradingPays){
		//退款订单的支付类型
		int paytype=tradingPays.get(0).getPaytype();
		//退款订单的时间 2017-02-02 10:10:10
		String time=tradingPays.get(0).getTime();
		//退款订单的金额
		double money=Double.valueOf(tradingPays.get(0).getMoney());
		//截取时间 2017-02-02
		String daytime =(String) time.subSequence(0,10);

		db = helper.getReadableDatabase();
		double amount = 0;
		Cursor c = queryAmount(paytype,daytime);

		if( c == null || "".equals(c)){
			Log.d(tag, "查询无数据");
			return amount;
		}

		while (c.moveToNext()){
			amount=Double.valueOf(c.getString(c.getColumnIndex("amount")));
		}
		amount=money-amount;
		c.close();
		db.close();

		return amount;
	}
	//select * from tblName where rDate Between '2008-06-10' and  '2008-06-12' 
	public Cursor queryAmount(int paytype,String daytime)
	{
		Cursor c = db.rawQuery(" SELECT amount FROM " 
				+ MySqliteHelper.PaySum_TABLE_NAME+" where paytype = ? and time = ?", 
				new String[]{String.valueOf(paytype),daytime});

		return c;
	}

	/**
	 * 更新金额
	 * 退款后更新统计表中对应金额
	 * @param amount 退款后的金额
	 * @return flag
	 */
	public boolean updatePaySumAmount(double amount,List<TradingPay> tradingPays){
		
		int paytype=tradingPays.get(0).getPaytype();
		String time=tradingPays.get(0).getTime();

		String daytime =(String) time.subSequence(0,10);
		boolean flag=false;
		db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("amount",amount);
		int c=db.update(MySqliteHelper.PaySum_TABLE_NAME, cv, " paytype = ? and time = ? ",
				new String[]{String.valueOf(paytype),daytime});
		
		if(c==0){
			Log.d(tag, "退款后金额更新失败");
			return flag;
		}else{
			flag=true;
			Log.d(tag, "退款后金额更新成功");
		}
		return flag;

	}

	/**
	 * 退款删除
	 * 用户退款后，删除对应的交易记录
	 */
	public void deleteRefundData(String orderid){
		db = helper.getWritableDatabase();
		try {
			db.delete(MySqliteHelper.TradingPay_TABLE_NAME, " orderid = ? ", 
					new String[]{orderid});
			Log.d(tag, "退款后成功删除交易记录");
		} catch (Exception e) {
			Log.d(tag, "删除退款的交易记录出错了");
			// TODO: handle exception
		}

	}

	/**
	 * 支付状态更新
	 * @param myorderid 订单ID(自己的)  1:待付款 4：已取消 7：已完成 10:已退款 13:支付中
	 * @return flag
	 */
	public boolean updatePayStatus(String myorderid,int status)
	{	boolean flag=false;
	ContentValues cv = new ContentValues();
	cv.put("status",status);
	int c = db.update(MySqliteHelper.TradingPay_TABLE_NAME, cv, " myorderid = ? ",
			new String[]{myorderid});
	if(c==0){
		Log.d(tag, "支付状态更新失败");
		return flag;
	}else{
		flag=true;
		Log.d(tag, "支付状态更新成功");
	}
	return flag;

	}
	/**
	 * 上传状态更新
	 * @param myorderid 订单ID(自己的)  
	 * @return flag
	 */
	public boolean updateUploadStatus(String myorderid,int uploadstatus)
	{	boolean flag=false;
	ContentValues cv = new ContentValues();
	cv.put("uploadstatus",uploadstatus);
	int c = db.update(MySqliteHelper.TradingPay_TABLE_NAME, cv, " myorderid = ? ",
			new String[]{myorderid});
	if(c==0){
		Log.d(tag, "上传状态更新失败");
		return flag;
	}else{
		flag=true;
		Log.d(tag, "上传状态更新成功");
	}
	return flag;
	
	}
	/**
	 * 时间更新
	 * @param myorderid 订单ID(自己的)
	 * @param time  时间
	 * @return
	 */
	public boolean updatePayTime(String myorderid,String time)
	{	boolean flag=false;
	ContentValues cv = new ContentValues();
	cv.put("time",time);
	int c = db.update(MySqliteHelper.TradingPay_TABLE_NAME, cv, " myorderid = ? ",
			new String[]{myorderid});
	if(c==0){
		Log.d(tag, "时间更新失败");
		return flag;
	}else{
		flag=true;
		Log.d(tag, "时间更新成功");
	}
	return flag;
	
	}
	/**
	 * 订单ID(掌游宝)更新
	 * @param myorderid 订单ID(自己的)
	 * @param orderid  订单ID(掌游宝)
	 * @return
	 */
	public boolean updatePayOrderid(String myorderid,String orderid)
	{	boolean flag=false;
	ContentValues cv = new ContentValues();
	cv.put("orderid",orderid);
	int c = db.update(MySqliteHelper.TradingPay_TABLE_NAME, cv, " myorderid = ? ",
			new String[]{myorderid});
	if(c==0){
		Log.d(tag, "订单ID(掌游宝)更新失败");
		return flag;
	}else{
		flag=true;
		Log.d(tag, "订单ID(掌游宝)更新成功");
	}
	return flag;
	
	}

	/**
	 * 删除所有交易数据
	 */
	public void deleteAllData(){
		db = helper.getWritableDatabase();
		try {
			db.delete(MySqliteHelper.TradingPay_TABLE_NAME, null, null);
			Log.d(tag, "成功删除所有的交易数据");
		} catch (Exception e) {
			Log.d(tag, "删除所有的交易数据出错了");
			// TODO: handle exception
		}

	}
	/**
	 * 删除统计表中所有数据
	 */
	public void deleteAllPaySumData(){
		db = helper.getWritableDatabase();
		try {
			db.delete(MySqliteHelper.PaySum_TABLE_NAME, null, null);
			Log.d(tag, "成功删除所有统计的数据");
		} catch (Exception e) {
			Log.d(tag, "删除所有的统计数据出错了");
			// TODO: handle exception
		}
		
	}



	/**
	 * 删除记录的第一条
	 * 当表中数据条数大于某一指定值时，每插入一条新数据，则根据时间删除表中最早时间的一条数据
	 * delete from timedata where time in (select min(time) from timedata)
	 */
	public void deleteFirstData(){
		db = helper.getWritableDatabase();
		try {
			db.delete(MySqliteHelper.TradingPay_TABLE_NAME, " time in (select min(time) from "
					+MySqliteHelper.TradingPay_TABLE_NAME+")" , null);
			Log.d(tag, "成功记录的第一条数据数据");
		} catch (Exception e) {
			Log.d(tag, "删除记录的第一条数据出错了");
			// TODO: handle exception
		}
	}


	/**
	 * 查询交易记录的总数
	 * @return length
	 */
	public long getTradingCount() {
		db = helper.getWritableDatabase();
		String sql = "select count(*) from "+ MySqliteHelper.TradingPay_TABLE_NAME;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		long length = c.getLong(0);
		c.close();
		return length;
	}
	
	/**
	 * 根据时间段查询交易记录的总数
	 * @return length
	 */
	public long getPayOrderCountByTime(String starttimes, String stoptimes) {
		db = helper.getWritableDatabase();
		String sql = "select count(*) from "+ MySqliteHelper.TradingPay_TABLE_NAME
				+ " where time >= ? and time <= ? ";
		Cursor c = db.rawQuery(sql, new String[]{starttimes,stoptimes});
		c.moveToFirst();
		long length = c.getLong(0);
		c.close();
		return length;
	}

	/**
	 * 拿到所有的交易记录条数,分页查询
	 * @param firstResult 从第几条数据开始查询。
	 * @param maxResult   每页显示多少条记录。
	 * @param starttimes 开始时间
	 * @param stoptimes 结束时间
	 * @return 当前页的记录
	 */
	public Cursor getAll_TradingItems(int firstResult, int maxResult, String starttimes, String stoptimes) {
		db = helper.getWritableDatabase();
		String sql = "select * from "
				+MySqliteHelper.TradingPay_TABLE_NAME 
				+ " where time >= ? and time <= ? ORDER BY time DESC limit ?,? ";
		Cursor mCursor = db.rawQuery(sql, new String[]{starttimes,stoptimes,String.valueOf(firstResult), String.valueOf(maxResult)});
		return mCursor;
	}
	
	/**
	 * 查询金额统计记录的总数
	 * @return length
	 */
	public long getPaySumCount() {
		db = helper.getWritableDatabase();
		String sql = "select count(*) from "+ MySqliteHelper.PaySum_TABLE_NAME;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		long length = c.getLong(0);
		c.close();
		return length;
	}
	
	/**
	 * 根据时间段查询交易金额统计记录的总数
	 * @return length
	 */
	public long getPaySumCountByTime(String starttimes, String stoptimes) {
		db = helper.getWritableDatabase();
		String sql = "select count(*) from "+ MySqliteHelper.PaySum_TABLE_NAME
				+ " where time >= ? and time <= ? ";
		Cursor c = db.rawQuery(sql, new String[]{starttimes,stoptimes});
		c.moveToFirst();
		long length = c.getLong(0);
		c.close();
		return length;
	}
	
	/**
	 * 拿到所有的金额统计条数,分页查询
	 * @param firstResult 从第几条数据开始查询。
	 * @param maxResult   每页显示多少条记录。
	 * @param starttimes 开始时间
	 * @param stoptimes 结束时间
	 * @return 当前页的记录
	 */
	public Cursor getAll_PayStatisticalItems(int firstResult, int maxResult, String starttimes, String stoptimes) {
		db = helper.getWritableDatabase();
		String sql = "select * from "
				+MySqliteHelper.PaySum_TABLE_NAME 
				+ " where time >= ? and time <= ? ORDER BY time DESC limit ?,? ";
		Cursor mCursor = db.rawQuery(sql, new String[]{starttimes,stoptimes,String.valueOf(firstResult), String.valueOf(maxResult)});
		return mCursor;
	}

	
	//****************************以下是--交易记录*******************************//
	
	/**
	 * 添加交易记录
	 * @param goos
	 */
	public String addTransRecordData(List<TransactionRecord> trRecords)
	{	
		db = helper.getWritableDatabase();
		
		//添加数据时，若数据库中交易记录超过50000条，则先删除最早的一条，再添加最新一条
		if(getTradRecordCount()>=50000){
			deleteFirstTransData();
		}
		//设置标签 "0"为添加数据失败
		String  flag = "0";
		Log.d(tag, "正在添加交易记录....");
		// 采用事务处理，确保数据完整性
		db.beginTransaction(); // 开始事务
		try
		{
			for (TransactionRecord trRecord : trRecords)
			{
				db.execSQL(" INSERT OR IGNORE INTO " + MySqliteHelper.TransactionRecord_TABLE_NAME
						+ " VALUES(null,?, ?, ?, ?, ?, ?, ?, ?)", new Object[] { 
						trRecord.getTotalFee(), trRecord.getPayCode(),
						trRecord.getOrderId(), trRecord.getOrderState(),
						trRecord.getOrderType(), trRecord.getType(),
						trRecord.getPayTime(), trRecord.getBuyerLoginId() });
			}

			db.setTransactionSuccessful(); // 设置事务成功完成
			//"1"为发货成功
			flag = "1";
			Log.d(tag, "交易记录添加成功");
		}catch (Exception e) {
			// TODO: handle exception
			Log.d(tag, "交易记录添加失败");
			e.printStackTrace();
		}
		finally
		{
			db.endTransaction(); //结束事务
			db.close();
		}

		return flag;
	}
	
	/**
	 * 查询交易记录的总数
	 * @return length
	 */
	public long getTradRecordCount() {
		db = helper.getWritableDatabase();
		String sql = "select count(*) from "+ MySqliteHelper.TransactionRecord_TABLE_NAME;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		long length = c.getLong(0);
		c.close();
		return length;
	}
	
	/**
	 * 删除交易记录的第一条
	 * 当表中数据条数大于某一指定值时，每插入一条新数据，则根据时间删除表中最早时间的一条数据
	 * delete from timedata where time in (select min(time) from timedata)
	 */
	public void deleteFirstTransData(){
		db = helper.getWritableDatabase();
		try {
			db.delete(MySqliteHelper.TransactionRecord_TABLE_NAME, " payTime in (select min(payTime) from "
					+MySqliteHelper.TransactionRecord_TABLE_NAME+")" , null);
			Log.d(tag, "成功删除交易记录的第一条数据数据");
		} catch (Exception e) {
			Log.d(tag, "删除记录的第一条数据出错了");
			// TODO: handle exception
		}
	}
	
	/**
	 * 根据时间段查询交易记录的总数
	 * @return length
	 */
	public long getTransRecordCountBypayTime(String starttimes, String stoptimes) {
		db = helper.getWritableDatabase();
		String sql = "select count(*) from "+ MySqliteHelper.TransactionRecord_TABLE_NAME
				+ " where payTime >= ? and payTime <= ? ";
		Cursor c = db.rawQuery(sql, new String[]{starttimes,stoptimes});
		c.moveToFirst();
		long length = c.getLong(0);
		c.close();
		return length;
	}

	/**
	 * 拿到所有的交易记录条数,分页查询
	 * @param firstResult 从第几条数据开始查询。
	 * @param maxResult   每页显示多少条记录。
	 * @param starttimes 开始时间
	 * @param stoptimes 结束时间
	 * @return 当前页的记录
	 */
	public Cursor getAllTransRecordItems(int firstResult, int maxResult, String starttimes, String stoptimes) {
		db = helper.getWritableDatabase();
		String sql = "select * from "
				+MySqliteHelper.TransactionRecord_TABLE_NAME 
				+ " where payTime >= ? and payTime <= ? ORDER BY payTime DESC limit ?,? ";
		Cursor mCursor = db.rawQuery(sql, new String[]{starttimes,stoptimes,String.valueOf(firstResult), String.valueOf(maxResult)});
		return mCursor;
	}

	/**
	 * 查询最近一次交易记录的时间
	 * @return SELECT LAST(OrderPrice) AS LastOrderPrice FROM Orders
	 */
	public String queryLastTradRecordTime(){
		String lastTradingTime = null;
		db = helper.getReadableDatabase();
		String sql = " SELECT payTime FROM "
				+MySqliteHelper.TransactionRecord_TABLE_NAME+" ORDER BY payTime DESC limit ?,?";
		Cursor mCursor = db.rawQuery(sql,new String[]{"0","1"});
		while (mCursor.moveToNext()) {
			
			lastTradingTime=mCursor.getString(mCursor.getColumnIndex("payTime"));
		}
		
		return lastTradingTime;
	}
	/**
	 * 查询最早一次交易记录的时间
	 * @return SELECT LAST(OrderPrice) AS LastOrderPrice FROM Orders
	 */
	public String queryFirstTradRecordTime(){
		String lastTradingTime = null;
		db = helper.getReadableDatabase();
		String sql = " SELECT payTime FROM "
				+MySqliteHelper.TransactionRecord_TABLE_NAME+" ORDER BY payTime ASC limit ?,?";
		Cursor mCursor = db.rawQuery(sql,new String[]{"0","1"});
		while (mCursor.moveToNext()) {
			
			lastTradingTime=mCursor.getString(mCursor.getColumnIndex("payTime"));
		}
		
		return lastTradingTime;
	}
	
	/**
	 * 用户退出交易记录页面时删除数据库多余数据
	 */
	public void deleteDataByTime(String delete_startTime,String delete_stopTime){
		db = helper.getWritableDatabase();
		try {
			db.delete(MySqliteHelper.TransactionRecord_TABLE_NAME, " payTime > ? and payTime <= ? ", 
					new String[]{delete_startTime,delete_stopTime});
			Log.d(tag, "成功删除多余交易记录");
		} catch (Exception e) {
			Log.d(tag, "删除多余交易记录出错了");
			// TODO: handle exception
		}

	}
	
	/**
	 * 删除所有交易记录数据
	 */
	public void deleteAllTransRecord(){
		db = helper.getWritableDatabase();
		try {
			db.delete(MySqliteHelper.TransactionRecord_TABLE_NAME, null, null);
			Log.d(tag, "成功删除所有的交易记录数据");
		} catch (Exception e) {
			Log.d(tag, "删除所有的交易记录数据出错了");
			// TODO: handle exception
		}

	}
}
