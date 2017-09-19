package com.jhj.WalletActivity;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhjpay.zyb.R;
/**
 * 公告适配器
 * @author Administrator
 *
 */
public class NoticeAdapter extends BaseAdapter {

	private List<Notice> noticelist;
	private Context mContext;
	
	public NoticeAdapter(Context mContext,List<Notice> noticelist){
		this.mContext=mContext;
		this.noticelist=noticelist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return noticelist.size();
	}

	@Override
	public Notice getItem(int position) {
		// TODO Auto-generated method stub
		return noticelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView==null) {
			convertView = View.inflate(mContext, R.layout.activity_wallet_notice_adapter, null);
			holder = new ViewHolder();
			//寻找控件id
			holder.notice_title=(TextView) convertView.findViewById(R.id.notice_title);
			holder.notice_time=(TextView) convertView.findViewById(R.id.notice_time);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//赋值
		Notice notice = getItem(position);
		
		holder.notice_title.setText(notice.getNotice_title());
		holder.notice_time.setText(notice.getNotice_time());

		return convertView;
	}

	static class ViewHolder {
		private TextView notice_title;//标题
		private TextView notice_time;//时间
	}


}
