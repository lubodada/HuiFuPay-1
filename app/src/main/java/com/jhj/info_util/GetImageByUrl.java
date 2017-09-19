package com.jhj.info_util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.jhjpay.zyb.R;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * 根据图片url路径获取图片
 * 
 * @author LeoLeoHan
 * 
 */
public class GetImageByUrl {

	private PicHandler pic_hdl;
	private ImageView mimgView;
	private String url;


	/**
	 * 通过图片url路径获取图片并显示到对应控件上
	 * 
	 * @param imgView
	 * @param url
	 */
	public void setImage(ImageView imgView, String url) {
		this.url = url;
		this.mimgView = imgView;
		imgView.setTag(url);
		imgView.setImageResource(R.drawable.store_loading);
		pic_hdl = new PicHandler();
		Thread t = new LoadPicThread();
		t.start();
	}


	class LoadPicThread extends Thread {
		@Override
		public void run() {
			Bitmap img = getUrlImage(url);
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
			if (mimgView.getTag() != null && mimgView.getTag().equals(url)) {
				if(myimg!=null){
					mimgView.setImageBitmap(myimg);
				}else{
					mimgView.setImageResource(R.drawable.store_loading);
				}
			}
		}

	}

	public Bitmap getUrlImage(String url) {
		Bitmap img = null;
		try {
			URL picurl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) picurl
					.openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			InputStream is = conn.getInputStream();
			img = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	
	/** 
     * 图片的质量压缩: 
     * 图片质量的压缩思想大致如下: 
     * 先将一张图片到一个字节数组输出流对象保存， 
     * 然后通过不断压缩数据，直到图片大小压缩到某个具体大小时，然后再把 
     * 字节数组输出流对象作为一个字节数组输入流参数对象传入得到一个字节数组输入流 
     * 最后再将字节数组输入流得到Bitmap对象，最终拿到图片质量压缩后的图片 
     */  
    public Bitmap getImageCompress(Bitmap bitmap) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到字节数组输出流中。  
        int options = 100;  
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩  
            baos.reset();//重置baos即清空baos  
            options -= 10;//每次都减少10  
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap2 = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap2;  
    }  

	/** 
	 * 异步下载图片的任务。 
	 *  
	 * @author guolin 
	 */  
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {  

		String imageUrl;   

		@Override  
		protected Bitmap doInBackground(String... params) {  
			imageUrl = params[0];  
			// 在后台开始下载图片  
			Bitmap bitmap = getUrlImage(imageUrl);  
			return bitmap;  
		}  

		@Override  
		protected void onPostExecute(Bitmap bitmap) {  
			if (mimgView != null && bitmap != null) {    
				mimgView.setImageBitmap(bitmap);   
			}   
		}  
	}
}