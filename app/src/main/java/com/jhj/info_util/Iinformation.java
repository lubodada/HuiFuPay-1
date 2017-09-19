package com.jhj.info_util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bestpay.cn.utils.SharedPreferences_util;

public class Iinformation {

	SharedPreferences_util su = new SharedPreferences_util();

	/**
	 * 结算信息
	 * 
	 * @param context
	 * @param name
	 *            姓名
	 * @param bankcard
	 *            银行卡号
	 * @param bank
	 *            开户行
	 * @param phone
	 *            手机号
	 */
	@SuppressWarnings("static-access")
	public void Settlement(Context context, String name, String bankcard,
			String bank, String phone) {
		try {
			JSONObject bodyjson = new JSONObject();
			bodyjson.put("name", name);
			bodyjson.put("bankcard", bankcard);
			bodyjson.put("bank", bank);
			bodyjson.put("phone", phone);

			// 保存数据
			su.setPrefString(context, "settlement", bodyjson.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 商户信息
	 * 
	 * @param context
	 * @param usname
	 *            商户姓名
	 * @param id_number
	 *            身份证号
	 * @param bankcard_number
	 *            银行卡号
	 * @param bankcard
	 *            开户行
	 * @param shanghu_name
	 *            商户名称
	 * @param business_range
	 *            经营范围
	 * @param business_address
	 *            经营地址
	 * @param detail_address
	 *            详细地址
	 * @param service_phone
	 *            服务电话
	 */
	@SuppressWarnings("static-access")
	public void Merchant(Context context, String idCardName, String idCardNumber,
			String bankCardNumber, String bankcard, String companyName,
			String business_range, String business_address,
			String detail_address, String mobileNumber) {

		try {
			JSONObject bodyjson = new JSONObject();
			bodyjson.put("idCardName", idCardName);
			bodyjson.put("idCardNumber", idCardNumber);
			bodyjson.put("bankCardNumber", bankCardNumber);
			bodyjson.put("bankcard", bankcard);
			bodyjson.put("companyName", companyName);
			bodyjson.put("business_range", business_range);
			bodyjson.put("business_address", business_address);
			bodyjson.put("detail_address", detail_address);
			bodyjson.put("mobileNumber", mobileNumber);

			// 保存数据
			su.setPrefString(context, "merchant", bodyjson.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	Map<String, File> fileMap = new HashMap<String, File>();
	/**
	 * 存放银联注册所需图片
	 * @param context
	 * @param fileMap
	 */
	@SuppressWarnings("static-access")
	public void Merchant_Img(Context context,Map<String, File> fileMap) {
		
		try {
			JSONObject bodyjson = new JSONObject();
			for (String key : fileMap.keySet()) {
				bodyjson.put(key, fileMap.get(key).toString());
			}
			// 保存数据
			su.setPrefString(context, "merchant_img", bodyjson.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 存放银联注册所需图片
	 * @param context
	 * @param fileMap
	 */
	@SuppressWarnings("static-access")
	public void Merchant_Img_String(Context context,Map<String, String> strMap) {
		
		try {
			JSONObject bodyjson = new JSONObject();
			for (String key : strMap.keySet()) {
				bodyjson.put(key, strMap.get(key).toString());
			}
			// 保存数据
			su.setPrefString(context, "merchant_img", bodyjson.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
