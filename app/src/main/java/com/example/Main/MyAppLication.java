package com.example.Main;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Application;

public class MyAppLication extends Application {

	// 保存所有的Activity
	private List<Activity> activityList;
	private static MyAppLication instance;

	private MyAppLication(){

	}

	//单例模式中获取唯一的MyApplication实例 
	public static MyAppLication getInstance(){ 
		if(null == instance)
		{
			instance = new MyAppLication();
		}
		return instance;             
	}

	/**
	 * 添加activity到activityList集合中
	 */
	public void addActivity(Activity activity) {
		if (activityList == null) {
			activityList = new ArrayList<Activity>();
		}
		activityList.add(activity);
	}

	public int getListSize() {
		if (activityList != null) {
			return activityList.size();
		}
		return 0;
	}

	public void removeActivity(Activity activity) {
		if (activityList != null) {

			if (activityList.contains(activity)) {
				activityList.remove(activity);
			}
		}

	}

	/** 
	 * 清空列表，取消引用 
	 */
	public void clearActivity() {
		activityList.clear();
	}

	/**
	 * 退出
	 */
	public void exit() {
		for (Activity activity : activityList) {
			if (!activity.isFinishing() && activity != null) {
				activity.finish();
			}
		}
		clearActivity();
//		System.exit(0);
	}

	/**
	 * 结束指定类名的Activity
	 * @param cls
	 */
	public void finishActivity(Activity activity) {

		if (activity != null) {
			activityList.remove(activity);
			activity.finish();
			activity = null; 
		}
	}

	/**
	 * 结束指定类名的Activity
	 * 
	 * @param cls
	 */
	public void finishActivity(Class<?> cls) {
		for (int i = 0; i < activityList.size(); i++) {

			if (activityList.get(i).getClass().equals(cls)) {
				finishActivity(activityList.get(i));
			}
		}

	}
}
