package com.jhj.info_util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jhj.Function.Product;
import com.jhj.Function.Recommend;
import com.jhj.Function.TransactionRecord;
import com.jhj.Function.TransactionRecord_Count;
import com.jhj.WalletActivity.BankCard;
import com.jhj.WalletActivity.Notice;

public class Json {

	/**
	 * 结算银行卡信息
	 * @param result
	 * @return
	 */
	public List<BankCard> parseJsonsByBank(String result) {
		List<BankCard> bank_list = null;

		try {
			JSONArray jsonObjs = new JSONArray(result);
			JSONObject jsonObj = null;
			if (jsonObjs.length() != 0) {
				BankCard bank = null;
				bank_list = new ArrayList<BankCard>();
				for (int i = 0; i < jsonObjs.length(); i++) {
					jsonObj = ((JSONObject) jsonObjs.opt(i));

					String bankcard = jsonObj.getString("bankcard");
					String bankcard_number = jsonObj.getString("bankcard_number");
					String remarks = jsonObj.getString("remarks");
					bank = new BankCard(bankcard,bankcard_number,remarks);
					bank_list.add(bank);
				}
			}
		} catch (JSONException e) {
			System.out.println("Jsons转换错误_结算银行卡");
			e.printStackTrace();
		}

		return bank_list;
	}
	/**
	 * 公告信息
	 * @param result
	 * @return
	 */
	public List<Notice> parseJsonsByNotice(String result) {
		List<Notice> notice_list = null;

		try {
			JSONArray jsonObjs = new JSONArray(result);
			JSONObject jsonObj = null;
			if (jsonObjs.length() != 0) {
				Notice notice = null;
				notice_list = new ArrayList<Notice>();
				for (int i = 0; i < jsonObjs.length(); i++) {
					jsonObj = ((JSONObject) jsonObjs.opt(i));

					String notice_title = jsonObj.getString("notice_title");
					String notice_time = jsonObj.getString("notice_time");
					String notice_url = jsonObj.getString("notice_url");
					notice = new Notice(notice_title,notice_time,notice_url);
					notice_list.add(notice);
				}
			}
		} catch (JSONException e) {
			System.out.println("Jsons转换错误_公告信息");
			e.printStackTrace();
		}

		return notice_list;
	}
	/**
	 * 产品信息
	 * @param result
	 * @return
	 */
	public List<Product> parseJsonsByProduct(String result) {
		List<Product> product_list = null;

		try {
			JSONArray jsonObjs = new JSONArray(result);
			JSONObject jsonObj = null;
			if (jsonObjs.length() != 0) {
				Product product = null;
				product_list = new ArrayList<Product>();
				for (int i = 0; i < jsonObjs.length(); i++) {
					jsonObj = ((JSONObject) jsonObjs.opt(i));

					String product_imgUrl = jsonObj.getString("product_imgUrl");
					String product_name = jsonObj.getString("product_name");
					String product_synopsis = jsonObj.getString("product_synopsis");
					Boolean product_state = jsonObj.getBoolean("product_state");
					product = new Product(product_imgUrl,product_name,product_synopsis,product_state);
					product_list.add(product);
				}
			}
		} catch (JSONException e) {
			System.out.println("Jsons转换错误_产品信息");
			e.printStackTrace();
		}

		return product_list;
	}
	/**
	 * 交易记录
	 * @param result
	 * @return
	 */
	public List<TransactionRecord> parseJsonsByTransactionRecord(String result) {
		List<TransactionRecord> tranRecord_list = null;

		try {
			JSONArray jsonObjs = (new JSONObject(result)).getJSONArray("orderList");
			JSONObject jsonObj = null;
			if (jsonObjs.length() != 0) {
				TransactionRecord transactionRecord = null;
				tranRecord_list = new ArrayList<TransactionRecord>();
				for (int i = 0; i < jsonObjs.length(); i++) {
					jsonObj = ((JSONObject) jsonObjs.opt(i));

					double totalFee = jsonObj.getDouble("totalFee")/100;
					String orderId = jsonObj.getString("orderId");
					int orderState = jsonObj.getInt("orderState");
					int orderType = jsonObj.getInt("orderType");
					String type = jsonObj.getString("type");
					String payTime = jsonObj.getString("payTime");
					String buyerLoginId = jsonObj.getString("buyerLoginId");

					transactionRecord = new TransactionRecord(totalFee,orderId,orderState,orderType,type,payTime,buyerLoginId);
					tranRecord_list.add(transactionRecord);
				} 
			}
		} catch (JSONException e) {
			System.out.println("Jsons转换错误_交易记录");
			e.printStackTrace();
		}

		return tranRecord_list;
	}
	/**
	 * 交易记录统计
	 * @param result
	 * @return
	 */
	public List<TransactionRecord_Count> parseJsonsByTransactionRecord_Count(String result) {
		List<TransactionRecord_Count> tranRecordcount_list = null;
		
		try {
			JSONArray jsonObjs = (new JSONObject(result)).getJSONArray("orderList");
			JSONObject jsonObj = null;
			if (jsonObjs.length() != 0) {
				TransactionRecord_Count tr_count = null;
				tranRecordcount_list = new ArrayList<TransactionRecord_Count>();
				for (int i = 0; i < jsonObjs.length(); i++) {
					jsonObj = ((JSONObject) jsonObjs.opt(i));
					
					String totalMoney = String.valueOf(Double.valueOf(jsonObj.getString("totalMoney"))/100);
					String orderCount = jsonObj.getString("orderCount");
					String orderType = jsonObj.getString("orderType");
					
					tr_count = new TransactionRecord_Count(totalMoney,orderCount,orderType);
					tranRecordcount_list.add(tr_count);
				} 
			}
		} catch (JSONException e) {
			System.out.println("Jsons转换错误_交易记录统计");
			e.printStackTrace();
		}
		
		return tranRecordcount_list;
	}

	/**
	 * 获取推荐产品
	 * @param result
	 * @return
	 */
	public List<Recommend> parseJsonsByReProduct(String result) {
		List<Recommend> relist = null;

		try {
			JSONArray jsonObjs = (new JSONObject(result)).getJSONArray("data");
			JSONObject jsonObj = null;
			if (jsonObjs.length() != 0) {
				Recommend recommend = null;
				relist = new ArrayList<Recommend>();
				for (int i = 0; i < jsonObjs.length(); i++) {
					jsonObj = ((JSONObject) jsonObjs.opt(i));

					String productName = jsonObj.getString("title");
					String productId = jsonObj.getString("id");
					recommend = new Recommend(productName,productId);
					relist.add(recommend);
				}
			}
		} catch (JSONException e) {
			System.out.println("Jsons转换错误_获取推荐产品");
			e.printStackTrace();
		}

		return relist;
	}
}
