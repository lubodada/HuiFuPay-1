package com.jhj.info_util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageTask extends AsyncTask<String, ImageView, Bitmap> {  
    private ImageView iv;  
    private String imgUrl;
    public ImageTask(ImageView iv){  
        this.iv = iv;  
    }  
    @Override  
    protected Bitmap doInBackground(String... param) {  
        String imgUrl = param[0];  
        this.imgUrl = imgUrl;
        iv.setTag(imgUrl);
        try {  
            URL url = new URL(imgUrl);  
            try {  
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
                InputStream in = conn.getInputStream();  
                Bitmap bitmap = BitmapFactory.decodeStream(in);  
                if(bitmap!=null){  
                    return bitmap;  
                }  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    @Override  
    protected void onPostExecute(Bitmap result) {  
        super.onPostExecute(result);  
        if (iv.getTag() != null && iv.getTag().equals(imgUrl)) {
        	if(result != null){  
        		iv.setImageBitmap(result);  
        	}else{
        		
        	}
        }
    }  
  
    @Override  
    protected void onPreExecute() {  
        super.onPreExecute();  
    }  
}  
