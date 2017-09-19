package com.jhj.info_util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.jhjpay.zyb.R;

/**
 * 加载服务器图片工具类
 * @author Administrator
 *
 */
public class FileDownLoadUtils {

	private ImageView imgView;
	private String url;
	private PicHandler pic_hdl;
	public void getImg(ImageView imgView, String url){
		this.url = url;
		this.imgView = imgView;
		imgView.setTag(url);
		imgView.setImageResource(R.drawable.store_loading);
		pic_hdl = new PicHandler();
		(new downloadImg()).start();
	}
	
	class downloadImg extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Bitmap img = getImage2(url);
			System.out.println(img + "---");
			Message msg = pic_hdl.obtainMessage();
			msg.what = 0;
			msg.obj = img;
			pic_hdl.sendMessage(msg);
		}
	}
	
	@SuppressLint("HandlerLeak")
	class PicHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			Bitmap myimg = (Bitmap) msg.obj;
			// 通过 tag 来防止图片错位
			if (imgView.getTag() != null && imgView.getTag().equals(url)) {
				if(myimg!=null){
					imgView.setImageBitmap(myimg);
				}else{
					imgView.setImageResource(R.drawable.store_loading);
				}
			}
		}

	}
	/**
	 * 从conn的输入流中获取数据将其转化为Bitmap型数据
	 * @param path
	 * @return
	 */
	public static Bitmap getImage(String path){ 

		try { 
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection(); 
			conn.setConnectTimeout(10000); 
			conn.setRequestMethod("GET"); 
			if(conn.getResponseCode() == 200){ 
				InputStream inputStream = conn.getInputStream(); 
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);   
				return bitmap; 
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return null; 
	} 

	/**
	 * 从conn的输入流中获取数据将其转化为Bitmap型数据
	 * @param path
	 * @return
	 */
	public static Bitmap getImage1(String path){ 

		HttpGet get = new HttpGet(path); 
		HttpClient client = new DefaultHttpClient(); 
		Bitmap pic = null; 
		try { 
			HttpResponse response = client.execute(get); 
			HttpEntity entity = response.getEntity(); 
			InputStream is = entity.getContent(); 

			pic = BitmapFactory.decodeStream(is);  // 关键是这句代 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return pic; 
	} 

	public static Bitmap getImage2(final String path){

		Bitmap bm = null ;
		try {
			//2:把网址封装为一个URL对象
			URL url = new URL(path);

			//3:获取客户端和服务器的连接对象，此时还没有建立连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//4:初始化连接对象
			conn.setRequestMethod("GET");
			//设置连接超时
			conn.setConnectTimeout(10000);
			//设置读取超时
			conn.setReadTimeout(10000);
			//5:发生请求，与服务器建立连接
			conn.connect();
			//如果响应码为200，说明请求成功
			if(conn.getResponseCode() == 200)
			{
				//获取服务器响应头中的流
				InputStream is = conn.getInputStream();

				//读取流里的数据，构建成bitmap位图
				bm = BitmapFactory.decodeStream(is);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;

	}


}
