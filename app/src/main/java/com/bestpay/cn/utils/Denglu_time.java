package com.bestpay.cn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class Denglu_time {

	@SuppressLint("SimpleDateFormat")
	public int getTime() {

		SimpleDateFormat formatter = new SimpleDateFormat("mmss");
		Date curDate = new Date(System.currentTimeMillis());
		return Integer.parseInt(formatter.format(curDate));
	}
}