package com.jhj.info_util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{
	private ImageView mImageView;//图片显示控件

	public MyAsyncTask(ImageView iv){
		mImageView = iv;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		String urlParams = params[0];//拿到execute()传过来的图片url
		Bitmap bitmap = null;
		URLConnection conn = null;
		InputStream is = null;
		try {
			URL url = new URL(urlParams);
			conn = url.openConnection();
			is = conn.getInputStream();
			//将获取到的输入流转成Bitmap
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			is.close();
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		super.onPostExecute(bitmap);
		mImageView.setImageBitmap(bitmap);
	}
}
