package com.jhj.info_util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片工具类，对图片进行一些处理
 *
 * @author renyangyang
 */
public class ImageUtil {

    /**
     * 图片旋转
     *
     * @param bmp
     * @param degree
     * @return
     */
    public static Bitmap postRotateBitamp(Bitmap bmp, float degree) {
        // 获得Bitmap的高和宽
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        // 产生resize后的Bitmap对象
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
        return resizeBmp;
    }

    /**
     * 图片放大缩小
     *
     * @param bmp
     * @param degree
     * @return
     */
    public static Bitmap postScaleBitamp(Bitmap bmp, float sx, float sy) {
        // 获得Bitmap的高和宽
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        // System.out.println("before+w+h:::::::::::"+bmpWidth+","+bmpHeight);
        // 产生resize后的Bitmap对象
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
        // System.out.println("after+w+h:::::::::::"+resizeBmp.getWidth()+","+resizeBmp.getHeight());
        return resizeBmp;
    }

    /**
     * 图片 亮度调整
     *
     * @param bmp huevalue亮度调整黑白
     * @param sx
     * @param sy
     * @return
     */
    public static Bitmap postColorRotateBitamp(int hueValue, int lumValue, Bitmap bm) {
        // 获得Bitmap的高和宽
        // System.out.println(bm.getWidth()+","+bm.getHeight()+"------before");
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
        Canvas canvas = new Canvas(bmp); // 得到画笔对象
        Paint paint = new Paint(); // 新建paint
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理

        // 产生resize后的Bitmap对象
        ColorMatrix mAllMatrix = new ColorMatrix();
        ColorMatrix mLightnessMatrix = new ColorMatrix();
        ColorMatrix mSaturationMatrix = new ColorMatrix();
        ColorMatrix mHueMatrix = new ColorMatrix();

        float mHueValue = hueValue * 1.0F / 127; // 亮度
        mHueMatrix.reset();
        mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考

        float mSaturationValue = 127 * 1.0F / 127;// 饱和度
        mSaturationMatrix.reset();
        mSaturationMatrix.setSaturation(mSaturationValue);

        float mLumValue = (lumValue - 127) * 1.0F / 127 * 180; // 色相
        mLightnessMatrix.reset(); // 设为默认值
        mLightnessMatrix.setRotate(0, mLumValue); // 控制让红色区在色轮上旋转的角度
        mLightnessMatrix.setRotate(1, mLumValue); // 控制让绿红色区在色轮上旋转的角度
        mLightnessMatrix.setRotate(2, mLumValue); // 控制让蓝色区在色轮上旋转的角度

        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix); // 效果叠加
        mAllMatrix.postConcat(mLightnessMatrix); // 效果叠加

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
        canvas.drawBitmap(bm, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
        // System.out.println(bmp.getWidth()+","+bmp.getHeight()+"------after");
        return bmp;
    }

    /**
     * 读取资源图片
     *
     * @param context
     * @param resId
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 对图片进行处理 1，首先判断 图片的宽和高 2，如果宽和高都小于700，就放大到手机的宽度（要判断是否大于700）
     * 3，如果有一项大于700，就进行缩放，都小于700为止
     */
    public static Bitmap parseBitmap(Bitmap mBitmap, String path) {
        // 1
        int imgWidth = mBitmap.getWidth();
        int imgHeight = mBitmap.getHeight();
        // 2
        if (imgWidth > 700 || imgHeight > 700) {
            float sx = imgWidth > imgHeight ? ((float) 700) / (float) imgWidth : ((float) 700) / (float) imgHeight;

            mBitmap = postScaleBitamp(mBitmap, sx, sx);
        } else {
            /*
             * if(screenWidth<700){ float sx = imgWidth > imgHeight ?
			 * ((float)screenWidth)/(float)imgWidth
			 * :((float)screenWidth)/(float)imgHeight; mBitmap =
			 * postScaleBitamp(mBitmap, sx, sx); }else{ float sx = imgWidth >
			 * imgHeight ? ((float)700)/(float)imgWidth
			 * :((float)700)/(float)imgHeight; mBitmap =
			 * postScaleBitamp(mBitmap, sx, sx); }
			 */
            int value = imgWidth > imgHeight ? imgWidth : imgHeight;
            if (value < 100) {
                mBitmap = getBitmapByPath(path);
            } else {
                return mBitmap;
            }
        }
        return mBitmap;
    }

    @SuppressWarnings("unused")
    public static Bitmap parseBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bmp = BitmapFactory.decodeFile(path, options);

        int mWidth = 640;

        int max = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        if (max > mWidth) {
            options.inSampleSize = max / mWidth;
            int height = options.outHeight * mWidth / max;
            int width = options.outWidth * mWidth / max;
            options.outWidth = width;
            options.outHeight = height;

        } else {
            options.inSampleSize = 1;
            options.outWidth = options.outWidth;
            options.outHeight = options.outHeight;
        }
		/* 这样才能真正的返回一个Bitmap给你 */
        options.inJustDecodeBounds = false;
        return getBitmapByPath(path, options);
    }

    @SuppressWarnings("unused")
    public static Bitmap parseBitmapToLittle(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);

        int mWidth = 320;

        int max = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        if (max > mWidth) {
            options.inSampleSize = max / mWidth;
            // int height = options.outHeight * mWidth / max;
            // int width = options.outWidth *mWidth / max;
            options.outWidth = 320;
            options.outHeight = 320;

        } else {
            options.inSampleSize = max / mWidth;
            options.outWidth = 320;
            options.outHeight = 320;
        }
		/* 这样才能真正的返回一个Bitmap给你 */
        options.inJustDecodeBounds = false;
        return getBitmapByPath(path, options);
    }

    @SuppressWarnings("unused")
    public static Bitmap parseHeadBitmapToLittle(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);

        int mWidth = 120;

        int max = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        if (max > mWidth) {
            options.inSampleSize = max / mWidth;
            options.outWidth = 120;
            options.outHeight = 120;

        } else {
            options.inSampleSize = max / mWidth;
            options.outWidth = 120;
            options.outHeight = 120;
        }
		/* 这样才能真正的返回一个Bitmap给你 */
        options.inJustDecodeBounds = false;
        return getBitmapByPath(path, options);
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }

    public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 根据Uri获取文件的路径
     *
     * @param uri
     * @return String
     * @Title: getUriString
     * @date 2012-11-28 下午1:19:31
     */
    public static String getUriString(Uri uri, ContentResolver cr) {
        String imgPath = null;
        if (uri != null) {
            String uriString = uri.toString();
            // 小米手机的适配问题，小米手机的uri以file开头，其他的手机都以content开头
            // 以content开头的uri表明图片插入数据库中了，而以file开头表示没有插入数据库
            // 所以就不能通过query来查询，否则获取的cursor会为null。
            if (uriString.startsWith("file")) {
                // uri的格式为file:///mnt....,将前七个过滤掉获取路径
                imgPath = uriString.substring(7, uriString.length());
                return imgPath;
            }
            Cursor cursor = cr.query(uri, null, null, null, null);
            cursor.moveToFirst();
            imgPath = cursor.getString(1); // 图片文件路径

        }
        return imgPath;
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(String filePath, Bitmap bitmap) {
        try {
            if (bitmap != null) {
                FileOutputStream fos = new FileOutputStream(filePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                fos.write(bytes);
                fos.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 先进行大小压缩压缩到一定比例之后进行质量压缩并处理某些手机拍照角度旋转的问题
     *
     * @throws FileNotFoundException
     */
    @SuppressLint("NewApi")
    public static Bitmap compressImage(Context context, String filePath, String fileName) throws FileNotFoundException {
        Bitmap bitmap = getSmallBitmap(filePath);

        int degree = readPictureDegree(filePath);
        if (degree != 0) {// 选择照片角度
            bitmap = rotateBitmap(bitmap, degree);
        }

        bitmap = compressImage(bitmap);

        return bitmap;
    }

    /**
     * 根据图片路径获取图片并压缩，返回bitmap用于显示
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

    /**
     * 判断照片的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
     * 旋转图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degress);
            Bitmap bitmap01 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap01 != bitmap) {
                bitmap.recycle();
            }
            return bitmap01;
        }
        return bitmap;
    }

    /**
     * 进行质量压缩(质量压缩后图片显示模糊)
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(CompressFormat.JPEG, 100, baos);
        int options = 80;
        int size = baos.toByteArray().length / 1024;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (size > 50 && options > 0) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(CompressFormat.JPEG, options, baos);
            size = baos.toByteArray().length / 1024;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        return BitmapFactory.decodeStream(bais);
    }

    /**
     * 压缩图片并保存到sd卡
     *
     * @param path 传过来的图片路径
     * @return 压缩后图片的路径
     */
    @SuppressWarnings("unused")
    public static String parseBitmapToSD(String path, int mWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        int max = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        if (max > mWidth) {
            float f = ((float) max / (float) mWidth);
            // LogUtil.log("----------- ((float) max / (float) mWidth)-------->"+f);
            options.inSampleSize = Math.round(f);
            // LogUtil.log("------------options.inSampleSize)------->"+options.inSampleSize);
            // int height = options.outHeight * mWidth / max;
            // int width = options.outWidth * mWidth / max;
            // options.outWidth = width;
            // options.outHeight = height;
        } else {
            options.inSampleSize = 1;
        }
		/* 这样才能真正的返回一个Bitmap给你 */
        options.inJustDecodeBounds = false;
        Bitmap parseBitmap = getBitmapByPath(path, options);
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (parseBitmap.compress(CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return path;
        }
        return path;
    }


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     */
    @SuppressLint("NewApi")
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
