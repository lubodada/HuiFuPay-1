package com.jhj.wallet.mypayview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhjpay.zyb.R;

/**
 * Belong to the Project —— MyPayUI Created by WangJ on 2015/11/25 15:39.
 */
public class PasswordView extends RelativeLayout {
	Context context;

	private String strPassword; // 输入的密码
	private TextView[] tvList; // 用数组保存6个TextView？
								
	private GridView gridView; // 用GrideView布局键盘
	private ArrayList<Map<String, String>> valueList; 

	private int currentIndex = -1; // 用于记录当前输入密码格位置

	public PasswordView(Context context) {
		this(context, null);
	}

	public PasswordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		View view = View.inflate(context, R.layout.activity_wallet_payment_passwordview, null);

		valueList = new ArrayList<Map<String, String>>();
		tvList = new TextView[6];

		tvList[0] = (TextView) view.findViewById(R.id.tv_pass1);
		tvList[1] = (TextView) view.findViewById(R.id.tv_pass2);
		tvList[2] = (TextView) view.findViewById(R.id.tv_pass3);
		tvList[3] = (TextView) view.findViewById(R.id.tv_pass4);
		tvList[4] = (TextView) view.findViewById(R.id.tv_pass5);
		tvList[5] = (TextView) view.findViewById(R.id.tv_pass6);

		gridView = (GridView) view.findViewById(R.id.gv_keybord);

		setView();

		addView(view); //加载，显示控件
	}

	private void setView() {
		/* 初始化按钮上应该显示的数字 */
		for (int i = 1; i < 13; i++) {
			Map<String, String> map = new HashMap<String, String>();
			if (i < 10) {
				map.put("name", String.valueOf(i));
			} else if (i == 10) {
				map.put("name", "");
			} else if (i == 11) {
				map.put("name", String.valueOf(0));
			} else if (i == 12) {
				map.put("name", "");
			} 
			valueList.add(map);
		}

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < 11 && position != 9) { // 点击0~9按钮
					if (currentIndex >= -1 && currentIndex < 5) { // 判断输入位置————要小心数组越界
						tvList[++currentIndex].setText(valueList.get(position)
								.get("name"));
					}
				} else {
					if (position == 11) { // 点击退格键
						if (currentIndex - 1 >= -1) { // 判断是否删除完毕————要小心数组越界
							tvList[currentIndex--].setText("");
						}
					}
				}
			}
		});
	}

	// 设置监听方法，在第6位输入完成后触发
	public void setOnFinishInput(final OnPasswordInputFinish pass) {
		tvList[5].addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() == 1) {
					strPassword = ""; // 每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱
					for (int i = 0; i < 6; i++) {
						strPassword += tvList[i].getText().toString().trim();
					}
					pass.inputFinish(); // 接口中要实现的方法，完成密码输入完成后的响应逻辑
				}
			}
		});
	}

	/* 获取输入的密码 */
	public String getStrPassword() {
		return strPassword;
	}

	// GrideView的适配器
	BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return valueList.size();
		}

		@Override
		public Object getItem(int position) {
			return valueList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.activity_wallet_payment_item_gride, null);
				viewHolder = new ViewHolder();
				viewHolder.btnKey = (TextView) convertView.findViewById(R.id.btn_keys);
				viewHolder.imgDelete=(LinearLayout) convertView.findViewById(R.id.imgDelete);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.btnKey.setText(valueList.get(position).get("name")); 
			if (position == 9) {
				viewHolder.btnKey.setEnabled(false);
			}
			if (position == 11) {
				viewHolder.btnKey.setVisibility(View.GONE);
				viewHolder.imgDelete.setVisibility(View.VISIBLE);
			}

			return convertView;
		}
	};

	/**
	 * 存放控件
	 */
	public final class ViewHolder {
		public LinearLayout imgDelete;
		public TextView btnKey;
	}
}
