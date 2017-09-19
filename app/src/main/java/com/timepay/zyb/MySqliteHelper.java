package com.timepay.zyb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {

	// 数据库版本号
	private static final int DATABASE_VERSION = 1;
	// 数据库名
	private static final String DATABASE_NAME = "trading.db";

	// 数据表名，一个数据库中可以有多个表
	// 交易信息表名
	public static final String TradingPay_TABLE_NAME = "tradingpay";
	// 各支付类型的金额统计表名
	public static final String PaySum_TABLE_NAME = "paysum";
	// 交易记录
	public static final String TransactionRecord_TABLE_NAME = "transactionRecord";

	public MySqliteHelper(Context context, String name, CursorFactory factory,
			int version) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// context：上下文环境
		// name：数据库名字
		// factory：游标工厂（可选）
		// version：数据库模型版本号
	}

	// 首次使用软件时生成数据库表
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		// 交易信息记录表
		// myorderid:自己生成订单编号，orderid：掌游宝订单id，paytype：支付方式，money：金额，status：支付状态，mnumber：付款码，remark：备注,time:时间
		// items：消费物品（json类型），discount：优惠金额，couponid：优惠券id,uploadstatus:上传状态
		db.execSQL(" CREATE TABLE IF NOT EXISTS tradingpay( _id INTEGER PRIMARY KEY AUTOINCREMENT,myorderid VARCHAR(30),orderid VARCHAR(30),paytype INTEGER, money DOUBLE(20), "
				+ " status INTEGER, mnumber VARCHAR(20), remark VARCHAR(100), time VARCHAR(30), items VARCHAR(1000), "
				+ " discount DOUBLE(20), couponid VARCHAR(20), uploadstatus INTEGER ) ");
		// 金额统计表
		// paytype：支付方式，amount：总金额，time:时间
		db.execSQL(" CREATE TABLE IF NOT EXISTS paysum( _id INTEGER PRIMARY KEY AUTOINCREMENT,paytype INTEGER, amount DOUBLE(20), time VARCHAR(30) ) ");

		// 交易记录
		// totalFee：交易金额，payCode：订单支付码，orderId:订单号， orderState:订单状态, orderType:订单类型, type:交易类型
		// payTime:交易时间, buyerLoginId:第三方平台这登录id
		db.execSQL(" CREATE TABLE IF NOT EXISTS transactionRecord( _id INTEGER PRIMARY KEY AUTOINCREMENT,totalFee DOUBLE(20), payCode VARCHAR(30),"
				+ " orderId VARCHAR(30) UNIQUE, orderState INTEGER, orderType INTEGER, type VARCHAR(30), payTime VARCHAR(30), buyerLoginId VARCHAR(30) ) ");

	}

	// 数据库升级时调用(当前为测试方法)
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// 交易记录
		// totalFee：交易金额，payCode：订单支付码，orderId:订单号， orderState:订单状态,orderType:订单类型, type:交易类型
		// payTime:交易时间, buyerLoginId:第三方平台这登录id
		db.execSQL(" CREATE TABLE IF NOT EXISTS transactionRecord( _id INTEGER PRIMARY KEY AUTOINCREMENT,totalFee DOUBLE(20), payCode VARCHAR(30),"
				+ " orderId VARCHAR(30), orderState INTEGER, orderType INTEGER, type VARCHAR(30), payTime VARCHAR(30), buyerLoginId VARCHAR(30) ) ");
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onDowngrade(db, oldVersion, newVersion);
		Log.d("数据库", "降级");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// 每次打开数据库之后首先被执行

		Log.d("MySqliteHelper", "DatabaseHelper 打开");
	}

	public MySqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
		Log.d("Mysqlitedbhelp", "DatabaseHelper Constructor");

	}

	public void deleteDatabase(Context context) {

		context.deleteDatabase(DATABASE_NAME);

	}

}
