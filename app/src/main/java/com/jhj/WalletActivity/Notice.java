package com.jhj.WalletActivity;

/**
 * 公告实体类
 * @author Administrator
 *
 */
public class Notice {

	private String notice_title;//标题
	private String notice_time;//时间
	private String notice_url;//详情
	
	public Notice(){
		
	}
	public Notice(String notice_title, String notice_time, String notice_url){
		this.notice_title=notice_title;
		this.notice_time=notice_time;
		this.notice_url=notice_url;
	}
	public String getNotice_title() {
		return notice_title;
	}
	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}
	public String getNotice_time() {
		return notice_time;
	}
	public void setNotice_time(String notice_time) {
		this.notice_time = notice_time;
	}
	public String getNotice_url() {
		return notice_url;
	}
	public void setNotice_url(String notice_url) {
		this.notice_url = notice_url;
	}
	
	
	
}
