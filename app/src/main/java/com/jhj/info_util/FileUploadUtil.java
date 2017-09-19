package com.jhj.info_util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

/**
 * 上传图片工具类
 * @author Administrator
 *
 */
public class FileUploadUtil {
	public static String fileUploadBreak = "fileUploadBreak";
	private HttpURLConnection conn;

	public void stopConnection() {
		if (conn != null) {
			conn.disconnect();
		}
	}
	Context mContext;
	public FileUploadUtil(Context context){
		this.mContext=context;
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param url Service net address
	 * @param params text content
	 * @param files pictures
	 * @return String result of Service response
	 * @throws IOException
	 */
	public static String uploadPost(String url, Map<String, String> params, Map<String, File> files)
			throws IOException {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(30000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (files != null)
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\""+file.getKey()+"\"; filename=\""
						+ file.getValue().getName() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();

		// 得到响应码?
		int res = conn.getResponseCode();
		StringBuilder sb2 = new StringBuilder();
		if (res == 200) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null){
				sb2.append(line);
				System.out.println(line);
			}
		}else if (res == 400) {
			return "400";
		}
		outStream.close();
		conn.disconnect();

		return sb2.toString();
	}


	/**
	 * 上传图片(png)+参数
	 * @param urlString  //地址
	 * @param params  //参数集合
	 * @param bmMap  //图片集合
	 * @return
	 * @throws Exception
	 */
	public String uploadImage(String urlString, Map<String, String> params,
			Map<String, Bitmap> bmMap) throws Exception {
		DataOutputStream dos;
		String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
		String twoLine = "--";
		String end = "\r\n";
		URL url = new URL(urlString);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(50000);
		conn.setReadTimeout(30000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Charset", "utf-8");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		dos = new DataOutputStream(conn.getOutputStream());
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (String key : params.keySet()) {
				sb.append(end + twoLine + boundary + end);
				sb.append("Content-Disposition:form-data;name=\"").append(key)
				.append("\"" + end);
				sb.append(end);
				sb.append(params.get(key));
			}
		}
		sb.append(end);
		dos.write(sb.toString().getBytes());
		/*
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("version")
		 * .append("\"" + end); sb.append(end); sb.append("1");
		 * 
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("phone")
		 * .append("\"" + end); sb.append(end); sb.append("1");
		 * 
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("action")
		 * .append("\"" + end); sb.append(end); sb.append("2");
		 * 
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("str")
		 * .append("\"" + end); sb.append(end); sb.append("M");
		 * 
		 * sb.append(end + twoLine + boundary + end);
		 * sb.append("Content-Disposition:form-data;name=\"").append("token")
		 * .append("\"" + end); sb.append(end);
		 * sb.append("5D1EF6E5-AECC-2FCF-1228-4817BBCFC5DA"); sb.append(end);
		 */
		if (bmMap != null) {
			for (String key : bmMap.keySet()) {

				StringBuffer fileBuffer = new StringBuffer();
				fileBuffer.append(twoLine + boundary + end);
				fileBuffer.append("Content-Disposition:form-data;name=\"")
				.append(key).append("\";").append("filename=\"")
				.append("aa.png").append("\"" + end);
				fileBuffer.append("Content-Type: ").append("image/png")
				.append(end);
				fileBuffer.append(end);
				dos.write(fileBuffer.toString().getBytes());

				/*
				 * dis = new DataInputStream(new FileInputStream(new
				 * File(path))); int lenth = 0; byte[] bytes = new byte[1024];
				 * while ((lenth = dis.read(bytes)) != -1) { } dis.close();
				 */

				dos.write(bmToByte(bmMap.get(key)));

				dos.write(end.getBytes());
			}
		}
		String string = twoLine + boundary + twoLine + end;
		dos.write(string.getBytes());
		dos.flush();
		dos.close();

		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			if (conn != null) {
				conn.disconnect();
			}
			return fileUploadBreak;
		}
		/* 读取服务器信息 */
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String getString = null;
		StringBuffer getBuffer = new StringBuffer();
		while ((getString = br.readLine()) != null) {
			getBuffer.append(getString);

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				if (conn != null) {
					conn.disconnect();
				}
				return fileUploadBreak;
			}
		}
		br.close();
		if (conn != null) {
			conn.disconnect();
			conn = null;
		}
		if (getBuffer.length() == 0) {
			return "";
		}
		return getBuffer.toString();
	}

	/**
	 * 上传图片(jpeg)+参数
	 * @param urlString  //地址
	 * @param params //参数集合
	 * @param bmMap  //图片资源
	 * @return
	 * @throws Exception
	 */
	public String uploadImageJpg(String urlString, Map<String, String> params,
			Map<String, Bitmap> bmMap) throws Exception {
		DataOutputStream dos;
		String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
		String twoLine = "--";
		String end = "\r\n";
		URL url = new URL(urlString);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(30000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Charset", "utf-8");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);

		dos = new DataOutputStream(conn.getOutputStream());
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (String key : params.keySet()) {
				sb.append(end + twoLine + boundary + end);
				sb.append("Content-Disposition:form-data;name=\"").append(key)
				.append("\"" + end);
				sb.append(end);
				sb.append(params.get(key));
			}
		}
		sb.append(end);
		dos.write(sb.toString().getBytes());
		if (bmMap != null) {
			for (String key : bmMap.keySet()) {

				StringBuffer fileBuffer = new StringBuffer();
				fileBuffer.append(twoLine + boundary + end);
				fileBuffer.append("Content-Disposition:form-data;name=\"")
				.append(key).append("\";").append("filename=\"")
				.append(bmMap.get(key)+".jpg").append("\"" + end); 
				fileBuffer.append("Content-Type: ").append("image/jpeg")
				.append(end);
				fileBuffer.append(end);
				dos.write(fileBuffer.toString().getBytes());

				dos.write(bmToByteForJpg(bmMap.get(key)));

				dos.write(end.getBytes());
			}
		}
		String string = twoLine + boundary + twoLine + end;
		dos.write(string.getBytes());
		dos.flush();
		dos.close();

		/* 读取服务器信息 */
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String getString = null;
		StringBuffer getBuffer = new StringBuffer();
		while ((getString = br.readLine()) != null) {
			getBuffer.append(getString);
		}
		br.close();
		if (conn != null) {
			conn.disconnect();
			conn = null;
		}
		if (getBuffer.length() == 0) {
			return "";
		}
		return getBuffer.toString();
	}
	/**
	 * 上传图片(jpeg)+参数+文件名
	 * @param urlString  //地址
	 * @param params  //参数
	 * @param bmMap  //图片
	 * @param fileName  //文件名
	 * @return
	 * @throws Exception
	 */
	public String uploadImageJpg(String urlString, Map<String, String> params,
			Map<String, Bitmap> bmMap,String fileName) throws Exception {
		DataOutputStream dos;
		String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
		String twoLine = "--";
		String end = "\r\n";
		URL url = new URL(urlString);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(30000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Charset", "utf-8");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);

		dos = new DataOutputStream(conn.getOutputStream());
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (String key : params.keySet()) {
				sb.append(end + twoLine + boundary + end);
				sb.append("Content-Disposition:form-data;name=\"").append(key)
				.append("\"" + end);
				sb.append(end);
				sb.append(params.get(key));
			}
		}
		sb.append(end);
		dos.write(sb.toString().getBytes());
		if (bmMap != null) {
			for (String key : bmMap.keySet()) {

				StringBuffer fileBuffer = new StringBuffer();
				fileBuffer.append(twoLine + boundary + end);
				fileBuffer.append("Content-Disposition:form-data;name=\"")
				.append(key).append("\";").append("filename=\"")
				.append(fileName).append("\"" + end);
				fileBuffer.append("Content-Type: ").append("image/jpeg")
				.append(end);
				fileBuffer.append(end);
				dos.write(fileBuffer.toString().getBytes());

				dos.write(bmToByteForJpg(bmMap.get(key)));

				dos.write(end.getBytes());
			}
		}
		String string = twoLine + boundary + twoLine + end;
		dos.write(string.getBytes());
		dos.flush();
		dos.close();

		/* 读取服务器信息 */
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String getString = null;
		StringBuffer getBuffer = new StringBuffer();
		while ((getString = br.readLine()) != null) {
			getBuffer.append(getString);
		}
		br.close();
		if (conn != null) {
			conn.disconnect();
			conn = null;
		}
		if (getBuffer.length() == 0) {
			return "";
		}
		return getBuffer.toString();
	}

	public static byte[] bmToByte(Bitmap bm) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}

	public static byte[] bmToByteForJpg(Bitmap bm) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 60, bos);
		return bos.toByteArray();
	}

	//	public  String fileUploadA(final String urlStr, final Map<String, String> textMap,
	//			final Map<String, File> fileMap) throws ClientProtocolException, IOException {
	//		HttpClient httpclient = new DefaultHttpClient();
	//	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	//
	//	    HttpPost httppost = new HttpPost(urlStr);
	//	    for(Entry<String, String> textEntry:textMap.entrySet()){
	//	    	MultipartEntity mpEntity = new MultipartEntity();
	//	    	ContentBody cb = new StringBody(textEntry.getValue());
	//	    	mpEntity.addPart(textEntry.getKey(), cb);
	//	    	httppost.setEntity(mpEntity);
	//	    }
	//	    for(Entry<String, File> fileEntry:fileMap.entrySet()){
	//	    	MultipartEntity mpEntity = new MultipartEntity();
	//		    ContentBody cbFile = new FileBody(fileEntry.getValue(), "image/jpeg");
	//		    mpEntity.addPart(fileEntry.getKey(), cbFile);
	//		    httppost.setEntity(mpEntity);
	//	    }
	//
	////	    HttpEntity httpEntity = MultipartEntityBuilder.create()
	////	    	    .addBinaryBody("file", file, ContentType.create("image/jpeg"), file.getName())
	////	    	    .build();
	//	    
	//	    System.out.println("executing request " + httppost.getRequestLine());
	//	    HttpResponse response = httpclient.execute(httppost);
	//	    HttpEntity resEntity = response.getEntity();
	//
	//	    System.out.println(response.getStatusLine());
	//	    if (resEntity != null) {
	//	      System.out.println(EntityUtils.toString(resEntity));
	//	    }
	//	    if (resEntity != null) {
	//	      resEntity.consumeContent();
	//	    }
	//
	//	    httpclient.getConnectionManager().shutdown();
	//		
	//		return null;
	//	}

	/**
	 * 上传文件
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public  String fileUpload(final String urlStr, final Map<String, String> textMap,
			final Map<String, File> fileMap) {
		String res = "";
		final String BOUNDARY = UUID.randomUUID().toString(); // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				for (String inputName : textMap.keySet()) {
					String inputValue = textMap.get(inputName);
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
					.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				Log.e("http", strBuf.toString());
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
				for (String inputName : fileMap.keySet()) {
					File file = fileMap.get(inputName);
					if (!file.exists()) {
						continue;
					}
					String filename = file.getName();
					String contentType = null;
					// new MimeTypeMap().getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (filename.endsWith(".jpg")) {
						contentType = "image/jpg";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}

					StringBuffer strBuff = new StringBuffer();
					strBuff.append("\r\n").append("--").append(BOUNDARY)
					.append("\r\n");
					strBuff.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuff.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuff.toString().getBytes());

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
					Log.e("http", strBuff.toString());
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
	/**
	 * 上传文件
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public  String formUpload(String urlStr, Map<String, String> textMap,
			Map<String, String> fileMap) {
		String res = "";
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				for (String inputName : textMap.keySet()) {
					String inputValue = textMap.get(inputName);
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
					.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
				for (String inputName : fileMap.keySet()) {
					String inputValue = fileMap.get(inputName);
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = null;
					// new MimeTypeMap().getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (filename.endsWith(".jpeg")) {
						contentType = "image/jpeg";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
					.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
					Log.e("http", strBuf.toString());
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
	/**
	 * 压缩图片方法
	 * @param path
	 * @param bitmap
	 * @param maxW
	 * @param maxH
	 * @return
	 */
	public static Bitmap cropBitmap(String path, Bitmap bitmap, int maxW,
			int maxH) {

		if (path != null && bitmap != null) {
			return null;
		}
		if (path == null && bitmap == null) {
			return null;
		}
		if (maxW == 0 || maxH == 0) {
			return null;
		}
		Bitmap bm;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if (path != null) {
			bm = BitmapFactory.decodeFile(path, options);
		}
		options.inJustDecodeBounds = false;
		int width = options.outWidth;
		int height = options.outHeight;

		if (width <= maxW && height <= maxH) {
			return BitmapFactory.decodeFile(path);
		} else if (width > maxW || height > maxH) {
			// 图片过大进行缩放
			int beW = 0;
			int beH = 0;
			int be = 1;
			if (maxH != 0 && maxW != 0) {
				beW = width / maxW;
				beH = height / maxH;
			}
			be = Math.max(beH, beW);
			if (be < 1) {
				be = 1;
			}
			options.inSampleSize = be;
			bm = BitmapFactory.decodeFile(path, options);
			// 重新测量长度，修改绝对的长和宽
			int scaleW = bm.getWidth();
			int scaleH = bm.getHeight();

			Matrix matrix = new Matrix();
			if (scaleH <= maxH && scaleW <= maxW) {
				return bm;
			}
			if (scaleH > maxH || scaleW > maxW) {
				float x = (float) maxW / (float) scaleW;
				float y = (float) maxH / (float) scaleH;
				if (x < y) {
					matrix.postScale(x, x);
				} else {
					matrix.postScale(y, y);
				}
				bm = Bitmap
						.createBitmap(bm, 0, 0, scaleW, scaleH, matrix, true);
				return bm;
			}

		}
		return null;
	}
	/**
	 * 压缩图片
	 * @param filePath
	 * @param targetPath
	 * @param quality
	 * @return
	 */
	public static String compressImage(String filePath, String targetPath, int quality)  {
		Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
		int degree = readPictureDegree(filePath);//获取相片拍摄角度
		if(degree!=0){//旋转照片角度，防止头像横着显示
			bm=rotateBitmap(bm,degree);
		}
		File outputFile=new File(targetPath);
		try {
			if (!outputFile.exists()) {
				outputFile.getParentFile().mkdirs();
				//outputFile.createNewFile();
			}else{
				outputFile.delete();
			}
			FileOutputStream out = new FileOutputStream(outputFile);
			bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
		}catch (Exception e){}
		return outputFile.getPath();
	}

	/**
	 * 根据路径获得图片信息并按比例压缩，返回bitmap
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
		BitmapFactory.decodeFile(filePath, options);
		// 计算缩放比
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// 完整解析图片返回bitmap
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}


	/**
	 * 获取照片角度
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转照片
	 * @param bitmap
	 * @param degress
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}
